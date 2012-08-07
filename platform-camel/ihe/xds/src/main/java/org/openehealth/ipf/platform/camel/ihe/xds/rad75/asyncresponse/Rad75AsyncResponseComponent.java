/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.rad75.asyncresponse;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rad75.Rad75ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rad75.asyncresponse.Rad75AsyncResponsePortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsAsyncResponseEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xds.rad75.asyncresponse.Rad75AsyncResponseService;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the RAD-75 (XCA-I) async response.
 */
public class Rad75AsyncResponseComponent extends AbstractWsComponent<WsTransactionConfiguration> {
    private final static WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:rad:xdsi-b:2009", "InitiatingGateway_Service", "iherad"),
            Rad75AsyncResponsePortType.class,
            new QName("urn:ihe:rad:xdsi-b:2009", "InitiatingGateway_Binding", "iherad"),
            false,
            "wsdl/rad75-asyncresponse.wsdl",
            true,
            false,
            false,
            false);

    @SuppressWarnings("unchecked") // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new XdsAsyncResponseEndpoint(uri, remaining, this, getCustomInterceptors(parameters), null);
    }

    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public WsAuditStrategy getClientAuditStrategy(boolean allowIncompleteAudit) {
        return null;   // no producer support
    }

    @Override
    public WsAuditStrategy getServerAuditStrategy(boolean allowIncompleteAudit) {
        return new Rad75ClientAuditStrategy(allowIncompleteAudit);
    }

    @Override
    public Rad75AsyncResponseService getServiceInstance(AbstractWsEndpoint<?> endpoint) {
        return new Rad75AsyncResponseService();
    }

    @Override
    public AbstractWsProducer<?,?> getProducer(
            AbstractWsEndpoint<?> endpoint,
            JaxWsClientFactory clientFactory)
    {
        throw new IllegalStateException("No producer support for asynchronous response endpoints");
    }
}
