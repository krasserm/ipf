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
package org.openehealth.ipf.modules.hl7dsl

import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adapt
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adaptStructure
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adaptStructures
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.selector
import static org.openehealth.ipf.modules.hl7dsl.util.Messages.copyMessage

import org.codehaus.groovy.runtime.InvokerHelper

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.Group

/**
 * @author Martin Krasser
 * @author Christian Ohr
 */
class GroupAdapter extends StructureAdapter {
    
    Group group
    
    private Set cachedNames
    
    GroupAdapter(Group group) {
        this.group = group
        this.path = ''
        this.cachedNames = group.names as HashSet
    }
    
    def getTarget() {
        group
    }
    
    void wrapTarget(Group group) {
        this.group = group
    }
    
    int count(String s) {
        group.getAll(s).length
    }
    
    StructureAdapter nrp(String s) {
        // create new structure within repeating group
        adaptStructure(group.get(s, count(s))) 
    }
    
    public Object invokeMethod(String name, Object args) {
        if (cachedNames.contains(name)) {
            return getAt(name).call(args)
        } else if (name.startsWith('findLastIndexOf')) {
            return findLastIndexOf { it.name == name.substring(15) }
        } else if (name.startsWith('findIndexOf')) {
            return findIndexOf { it.name == name.substring(11) }
        } else if (name.startsWith('findAll')) {
            return findAll { it.name == name.substring(7) }
        } else if (name.startsWith('find')) {
            return find { it.name == name.substring(4) }
        } else {
            return adapt(InvokerHelper.invokeMethod(group, name, args))
        }
    }
    
    def get(String s) {
        if (cachedNames.contains(s)) {
            return getAt(s)
        } else {
            return adapt(InvokerHelper.getProperty(group, s))
        }
    }
    
    void set(String s, Object value) {
        def group = getAt(s)
        if (value instanceof Closure) {
            throw new AdapterException('cannot assign to a repetition')
        }
        group.from(value)
    }
    
    def getAt(String s) {
        if (group.isRepeating(s)) {
            return selector(adaptStructures(group.getAll(s)), this, s)
        } else {
            return adaptStructure(group.get(s))
        }
    }
    
    void from(def value) {
        throw new UnsupportedOperationException('group copying not implemented yet')
    }
    
    Iterator iterator() {
        GroupAdapterIterator.iterator(this)
    }
    
    def call(object) {
        throw new AdapterException("The group ${group.class.simpleName} is not repeatable in this group or message")
    }
    
    /**
     * @return true if the group has only empty substructures
     */
    boolean isEmpty() {
        group.getNames().every { 
            get(it).isEmpty()
        }
    }
    
    Object eachWithIndex(Closure closure) {
        for (Iterator iter = iterator(); iter.hasNext();) {
            def next = iter.next()
            closure.call(next, next.path)
        }
        this
    }
    
    String findIndexOf(Closure closure) {
        String result = ''
        for (Iterator iter = iterator(); iter.hasNext();) {
            def next = iter.next()
            if (closure.call(next)) {
                result = next.path
                break
            }
        }
        result
    }
    
    String findLastIndexOf(Closure closure) {
        String result = ''
        for (Iterator iter = iterator(); iter.hasNext();) {
            def next = iter.next()
            if (closure.call(next)) {
                result = next.path
            }
        }
        result
    }
    
    List<String> findIndexValues(Closure closure) {
        List<String> result = []
        for (Iterator iter = iterator(); iter.hasNext();) {
            def next = iter.next()
            if (closure.call(next)) {
                result << next.path
            }
        }
        result
    }
}