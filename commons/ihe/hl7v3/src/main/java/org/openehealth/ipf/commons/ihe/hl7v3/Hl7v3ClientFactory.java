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
package org.openehealth.ipf.commons.ihe.hl7v3;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.cxf.databinding.plainxml.PlainXmlDataBinding;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InNamespaceMergeInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadInjectorInterceptor;

/**
 * Factory for HL7 v3 Web Service clients.
 * @author Dmytro Rud
 */
public class Hl7v3ClientFactory extends ItiClientFactory {

    /**
     * Constructs the factory.
     * @param serviceInfo
     *          the info about the web-service.
     * @param serviceUrl
     *          the URL of the web-service.
     * @param customInterceptors
     *          user-defined custom CXF interceptors.          
     */
    public Hl7v3ClientFactory(
            Hl7v3ServiceInfo serviceInfo,
            String serviceUrl, 
            InterceptorProvider customInterceptors) 
    {
        super(serviceInfo, serviceUrl, customInterceptors);
    }

    
    @Override
    protected void configureInterceptors(Client client) {
        super.configureInterceptors(client);
        client.getInInterceptors().add(new InPayloadExtractorInterceptor());
        client.getInInterceptors().add(new InNamespaceMergeInterceptor());
        client.getInInterceptors().add(new InPayloadInjectorInterceptor(0));
        client.getEndpoint().getService().setDataBinding(new PlainXmlDataBinding());
    }
}
