/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.pixpdqv3.pcc1;

import org.openehealth.ipf.commons.ihe.pixpdqv3.Hl7v3ContinuationsPortType;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.Action;
 
/**
 * SEI for PCC QED -- Query for Existing Data.
 * @author Dmytro Rud
 */
@WebService(targetNamespace = "urn:ihe:pcc:qed:2007", name = "ClinicalDataSource_PortType")
@SOAPBinding(style = Style.DOCUMENT, parameterStyle = ParameterStyle.BARE)
public interface Pcc1PortType extends Hl7v3ContinuationsPortType {
    
    @Action(input = "urn:hl7-org:v3:QUPC_IN043100UV01",
            output = "urn:hl7-org:v3:QUPC_IN043200UV01")
    @WebMethod(operationName = "ClinicalDataSource_QUPC_IN043100UV01",
               action = "urn:hl7-org:v3:QUPC_IN043100UV01")
    String operation(
        @WebParam(partName = "Body", targetNamespace = "urn:ihe:pcc:qed:2007")
        String request
    );

    @Action(input = "urn:hl7-org:v3:QUQI_IN000003UV01_Continue",
            output = "urn:hl7-org:v3:QUPC_IN043200UV01")
    @WebMethod(operationName = "ClinicalDataSource_QUQI_IN000003UV01_Continue",
               action = "urn:hl7-org:v3:QUQI_IN000003UV01_Continue")
    String continuation(
        @WebParam(partName = "Body", targetNamespace = "urn:ihe:pcc:qed:2007")
        String request
    );

    @Action(input = "urn:hl7-org:v3:QUQI_IN000003UV01_Cancel",
            output = "urn:hl7-org:v3:MCCI_IN000002UV01")
    @WebMethod(operationName = "ClinicalDataSource_QUQI_IN000003UV01_Cancel",
               action = "urn:hl7-org:v3:QUQI_IN000003UV01_Cancel")
    String cancel(
        @WebParam(partName = "Body", targetNamespace = "urn:ihe:pcc:qed:2007")
        String request
    );
}
