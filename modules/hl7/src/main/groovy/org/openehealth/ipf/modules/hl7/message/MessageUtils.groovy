/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.modules.hl7.message

import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7.HL7v2Exception
import org.openehealth.ipf.modules.hl7.AckTypeCode
import ca.uhn.hl7v2.parser.*
import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.util.Terser
import ca.uhn.hl7v2.util.MessageIDGenerator
import ca.uhn.hl7v2.sourcegen.SourceGenerator
import ca.uhn.hl7v2.app.DefaultApplication
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.DateTime

/**
 * This is a utility class that offers convenience methods for
 * accessing and creating HL7 messages. It's primarily used by
 * the HapiModelExtension to dynamically add the convenience
 * methods directly to the affected classes.
 * 
 * @author Christian Ohr
 * @author Marek V�clav�k
 */
class MessageUtils {

	private static DateTimeFormatter FMT = ISODateTimeFormat.basicDateTimeNoMillis()
	private static int INDENT_SIZE = 3

	
	/**
	 * @return Returns current time in HL7 format
	 */
	static def String hl7Now() {
		FMT.print(new DateTime())[0..7,9..14]
	}
	
	/**
	 * @return Encodes a string using HL7 encoding definition taken
	 * 	from the passed message
	 */
	static def encodeHL7String(String s, Message msg) {
		Escape.escape(s, encodingCharacters(msg))
	}
	
	/**
	 * @return ER7-formatted representation of the type
	 */
	static def pipeEncode(Type t) {
		PipeParser.encode(t, encodingCharacters(t.message))
	}

	/**
	 * @return ER7-formatted representation of the segment
	 */
	static def pipeEncode(Segment s) {
		PipeParser.encode(s, encodingCharacters(s.message))
	}

	/**
	 * @return event type of the message, e.g. 'ADT'
	 */
	static def eventType(Message msg) {
		Terser.get(msg.MSH, 9, 0, 1, 1)
	}

	/**
	 * @return trigger event of the message, e.g. 'A01'
	 */
	static def triggerEvent(Message msg) {
		Terser.get(msg.MSH, 9, 0, 2, 1)
	}
	
	/** 
	 *  @return a positive ACK response message
	 */
	static def ack(Message msg) {
		def ack = response(msg, 'ACK', triggerEvent(msg))
   	 	Terser terser = new Terser(ack)
        terser.set("MSA-1", "AA");
        ack
	}
	
	/** 
	 *  @return a negative ACK response message using a String cause
	 */
	static def nak(Message msg, String cause, AckTypeCode ackType) {
		HL7v2Exception e = new HL7v2Exception(cause)
		nak(msg, e, ackType)
	}
	
	/** 
	 *  @return a negative ACK response message using an Exception cause
	 */
	static def nak(Message msg, AbstractHL7v2Exception e, AckTypeCode ackType) {
		def ack = ack(msg)
		e.populateMessage(ack, ackType)
		ack
	}
	
	/** 
	 *  @return a negative ACK response message from scratch using a String cause
	 */
    static def defaultNak = { AbstractHL7v2Exception e, AckTypeCode ackType, String version ->
    	def cause = encodeHL7String(e.message, null)
    	def now = hl7Now()
    	
		def cannedNak = "MSH|^~\\&|unknown|unknown|unknown|unknown|$now||ACK|unknown|T|$version|\r" +
						"MSA|AE|MsgIdUnknown|$cause|\r"
		def nak = new GenericParser().parse(cannedNak)
		e.populateMessage(nak, ackType)
		nak
    }
	
	/** 
	 *  @return a response message with the basic MSH fields already populated
	 */
	static def response(Message msg, String eventType, String triggerEvent) {

        // make message of correct version
        def version = msg.version

        def name = eventType == 'ACK' ? 'ACK' : "${eventType}_${triggerEvent}"
        String className = 'ca.uhn.hl7v2.model.v' + version.replace('.','') + ".message.$name"

        Message out
        try {
            Class c = Class.forName(className)
            out = c.newInstance()
        }
        catch (Exception e) {
            throw new HL7v2Exception("Can't instantiate message of class $className")
        }
        
        //populate outbound MSH using data from inbound message ...                     
        Terser outTerser = new Terser(out)
        def msh = msg.MSH
        outTerser.set('MSH-1', Terser.get(msh, 1, 0, 1, 1))
        outTerser.set('MSH-2', Terser.get(msh, 2, 0, 1, 1))
        
        outTerser.set('MSH-3-1', Terser.get(msh, 5, 0, 1, 1))
        outTerser.set('MSH-3-2', Terser.get(msh, 5, 0, 2, 1))
        outTerser.set('MSH-3-3', Terser.get(msh, 5, 0, 3, 1))
        
        outTerser.set('MSH-4-1', Terser.get(msh, 6, 0, 1, 1))
        outTerser.set('MSH-4-2', Terser.get(msh, 6, 0, 2, 1))
        outTerser.set('MSH-4-3', Terser.get(msh, 6, 0, 3, 1))       
        
        outTerser.set('MSH-5-1', Terser.get(msh, 3, 0, 1, 1))
        outTerser.set('MSH-5-2', Terser.get(msh, 3, 0, 2, 1))
        outTerser.set('MSH-5-3', Terser.get(msh, 3, 0, 3, 1))
        
        outTerser.set('MSH-6-1', Terser.get(msh, 4, 0, 1, 1))
        outTerser.set('MSH-6-2', Terser.get(msh, 4, 0, 2, 1))
        outTerser.set('MSH-6-3', Terser.get(msh, 4, 0, 3, 1))

        outTerser.set('MSH-7', hl7Now())
        outTerser.set("MSH-9-1", eventType)
        outTerser.set("MSH-9-2", triggerEvent ?: Terser.get(msh, 9, 0, 2, 1))
        if (['2.3.1', '2.4', '2.5'].contains(version))
        	outTerser.set("MSH-9-3", Parser.getMessageStructureForEvent(name, version))
       
        outTerser.set('MSH-10', MessageIDGenerator.getInstance().getNewID())
        outTerser.set('MSH-11', Terser.get(msh, 11, 0, 1, 1))       
        outTerser.set('MSH-12', version)
        
        if (out.get("MSA") != null) {
          outTerser.set('MSA-2', Terser.get(msh, 10, 0, 1, 1))
        }
        
        out
    }
	

	
	/** 
	 *  @return a hierarchical dump of the message
	 */
	 static def dump(Message msg) {
		 def version = msg.version
         StringBuffer buf = new StringBuffer("${msg.class.simpleName} Version $version\n")
         dumpStructure(msg, buf, INDENT_SIZE).toString()
     }

	 private static def dumpStructure(Group group, StringBuffer buf, int indent) {
		 group.names.each { name ->
			 if (group.isRepeating(name))  {
				 group.getAll(name).eachWithIndex { structure, i ->
                    buf << ' ' * indent << "${structure.class.simpleName}($i)\n"
                    dumpStructure(structure, buf, indent + INDENT_SIZE)
				 }
			 } else {
				 def structure = group.get(name)
				 buf << ' ' * indent << "${structure.class.simpleName}\n"
				 dumpStructure(structure, buf, indent + INDENT_SIZE)
			 }
		 }
		 buf
	 }

	 private static def dumpStructure(Segment segment, StringBuffer buf, int indent) {
		 if (segment.numFields() > 0) {
		 (1..segment.numFields()).each { i ->
		 	if (segment.getMaxCardinality(i) > 1) {
		 		segment.getField(i).eachWithIndex { type, j ->
		 			String encoded = pipeEncode(type)
		 			buf << ' ' * indent << "${segment.name}[$i]($j) ${type.class.simpleName} \"$encoded\"\n"
		 			dumpType(type, true, buf, indent + INDENT_SIZE)
		 		}
		 	} else {
		 		Type type = segment.getField(i, 0)
	 			String encoded = pipeEncode(type)
		 		buf << ' ' * indent << "${segment.name}[$i] ${type.class.simpleName} \"$encoded\"\n"
		 		dumpType(type, false, buf, indent + INDENT_SIZE)                    
		 	}
		 }
		 }
		 buf
	 }


	 private static def dumpType(Type type, boolean isRepeating, StringBuffer buf, int indent) {
		 if (type instanceof Varies) {
			 buf << ' ' * indent << "${type.data.class.simpleName}\n"
			 dumpType(type.data, isRepeating, buf, indent + INDENT_SIZE);
		 } else if (type instanceof Composite) {
			 type.components.eachWithIndex { it, i ->
			 buf << ' ' * indent << "${type.name}[$i] (${it.class.simpleName})\n"
			 	dumpType(it, isRepeating, buf, indent + INDENT_SIZE)
			 }
		 } else {
			 buf << ' ' * indent << "${type.value}\n"
		 }

/*            if (type.extraComponents.numComponents() > 0) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("ExtraComponents");
                treeParent.insert(newNode, treeParent.getChildCount());
                for (int i = 0; i < messParent.getExtraComponents().numComponents(); i++) {
                    DefaultMutableTreeNode variesNode = new DefaultMutableTreeNode("Varies");
                    newNode.insert(variesNode, i);
                    addChildren(messParent.getExtraComponents().getComponent(i), variesNode);
                }
            }
*/        
		buf
	 }	
	
    
    private static EncodingCharacters encodingCharacters(Message msg) {
    	if (msg != null) {
    		String fieldSepString = msg.MSH.fieldSeparator     
    		char fieldSep = fieldSepString ? fieldSepString as char : '|'      
        	String encCharString = msg.MSH.encodingCharacters
        	return new EncodingCharacters(fieldSep, encCharString);
    	} else {
    		return new EncodingCharacters('|' as char, '^~\\&')
    	}
    }
}
	
