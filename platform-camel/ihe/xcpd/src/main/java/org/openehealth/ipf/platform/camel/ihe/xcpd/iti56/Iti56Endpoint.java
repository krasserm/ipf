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
package org.openehealth.ipf.platform.camel.ihe.xcpd.iti56;

import java.net.URISyntaxException;

import javax.xml.namespace.QName;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.xcpd.XcpdClientFactory;
import org.openehealth.ipf.commons.ihe.xcpd.XcpdServiceFactory;
import org.openehealth.ipf.commons.ihe.xcpd.iti56.Iti56ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xcpd.iti56.Iti56PortType;
import org.openehealth.ipf.commons.ihe.xcpd.iti56.Iti56ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * The Camel endpoint for the ITI-56 transaction.
 */
public class Iti56Endpoint extends DefaultItiEndpoint {
    private final static String NS_URI = "urn:ihe:iti:xcpd:2009";
    public final static Hl7v3ServiceInfo ITI_56 = new Hl7v3ServiceInfo(
            new QName(NS_URI, "RespondingGateway_Service", "xcpd"),
            Iti56PortType.class,
            new QName(NS_URI, "RespondingGateway_Binding_Soap12", "xcpd"),
            false,
            "wsdl/iti56/iti56-raw.wsdl",
            new String[][] {new String[] {"PatientLocationQueryRequest", null, "IHE/XCPD_PLQ"}},
            new String[][] {new String[] {"PatientLocationQueryResponse", null, "IHE/XCPD_PLQ"}}
    );

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti56Component
     *          the component creating this endpoint.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    public Iti56Endpoint(
            String endpointUri, 
            String address, 
            Iti56Component iti56Component,
            InterceptorProvider customInterceptors) throws URISyntaxException 
    {
        super(endpointUri, address, iti56Component, customInterceptors);
    }

    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new XcpdClientFactory(
                ITI_56,
                isAudit() ? new Iti56ClientAuditStrategy(isAllowIncompleteAudit()) : null,
                getServiceUrl(),
                getCorrelator(),
                getCustomInterceptors());
        return new Iti56Producer(this, clientFactory);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new XcpdServiceFactory(
                ITI_56,
                isAudit() ? new Iti56ServerAuditStrategy(isAllowIncompleteAudit()) : null,
                getServiceAddress(),
                getCustomInterceptors());
        ServerFactoryBean serverFactory =
            serviceFactory.createServerFactory(Iti56Service.class);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
