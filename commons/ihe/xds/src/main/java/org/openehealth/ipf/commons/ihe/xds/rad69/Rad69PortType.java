/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.rad69;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveImagingDocumentSetRequestType;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * Provides the RAD-69 web-service interface.
 */
@WebService(targetNamespace = "urn:ihe:rad:xdsi-b:2009", name = "DocumentRepository_PortType")
@XmlSeeAlso({
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Rad69PortType
{

    /**
     * Retrieves a set of imaging documents according to the RAD-69 specification.
     *
     * @param body
     *          the request.
     * @return the response.
     */
    @WebResult(name = "RetrieveDocumentSetResponse", targetNamespace = "urn:ihe:iti:xds-b:2007", partName = "body")
    @Action(input = "urn:ihe:rad:2011:CrossGatewayRetrieveImagingDocumentSet", output = "urn:ihe:rad:2011:CrossGatewayRetrieveImagingDocumentSetResponse")
    @WebMethod(operationName = "DocumentRepository_RetrieveImagingDocumentSet")
    public RetrieveDocumentSetResponseType documentRepositoryRetrieveImagingDocumentSet(
        @WebParam(partName = "body", name = "RetrieveImagingDocumentSetRequest", targetNamespace = "urn:ihe:rad:xdsi-b:2009")
        RetrieveImagingDocumentSetRequestType body
    );
}
