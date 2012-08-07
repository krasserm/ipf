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
package org.openehealth.ipf.platform.camel.ihe.xds.rad75;


import org.apache.camel.impl.DefaultExchange
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveImagingDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * Tests for RAD-75.
 * @author Clay Sebourn
 */
class TestRad75 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'rad-75.xml'
    final String SERVICE1_URI =
            "xcai-rad75://localhost:${port}/rad75service" +
            '?correlator=#correlator' +
            '&inInterceptors=#clientSyncInLogger' +
            '&inFaultInterceptors=#clientSyncInLogger' +
            '&outInterceptors=#clientSyncOutLogger' +
            '&outFaultInterceptors=#clientSyncOutLogger'

    final String SERVICE1_RESPONSE_URI = "http://localhost:${port}/rad75service-response"
    final String SERVICE2_URI = "xcai-rad75://localhost:${port}/rad75service2"
    
    static final RetrieveImagingDocumentSet REQUEST = SampleData.createRetrieveImagingDocumentSet()
    
    static void main(args) {
        PayloadLoggerBase.setGloballyEnabled(false)
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void setUpClass() {
        PayloadLoggerBase.setGloballyEnabled(false)
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    /**
     * Test whether:
     * <ol>
     *   <li> sync and async requests are possible...
     *   <li> ...and not influence each other (they shouldn't),
     *   <li> async requests are really async (exchanges are InOnly and delays do not matter),
     *   <li> SOAP headers (WSA ReplyTo) can be set and read,
     *   <li> XSD and Schematron validations work...
     *   <li> ...and the messages are valid either,
     *   <li> ATNA auditing works.
     * </ol>
     */
    @Test
    void testRad75() {
        final int N = 5
        int i = 0
        
        N.times {
            send(SERVICE1_URI, i++, SERVICE1_RESPONSE_URI)
            send(SERVICE1_URI, i++)
        }

        // wait for completion of asynchronous routes
        Thread.currentThread().sleep(1000 + Rad75TestRouteBuilder.ASYNC_DELAY)

        assert Rad75TestRouteBuilder.responseCount.get() == N * 2
        assert Rad75TestRouteBuilder.asyncResponseCount.get() == N
        
        assert auditSender.messages.size() == N * 4
        
        assert ! Rad75TestRouteBuilder.errorOccurred
    }
    
    
    private void send(
            String endpointUri,
            int n,
            String responseEndpointUri = null)
    {
        def requestExchange = new DefaultExchange(camelContext)
        requestExchange.in.body = REQUEST
        
        // set WSA ReplyTo header, when necessary
        if (responseEndpointUri) {
            requestExchange.in.headers[AbstractWsEndpoint.WSA_REPLYTO_HEADER_NAME] = responseEndpointUri
        }
        
        // set correlation key
        requestExchange.in.headers[AbstractWsEndpoint.CORRELATION_KEY_HEADER_NAME] = "corr ${n}"
        
        // set request HTTP headers
        requestExchange.in.headers[AbstractWsEndpoint.OUTGOING_HTTP_HEADERS] =
                ['MyRequestHeader': "Number ${n}".toString()]
        
        // send and check timing
        long startTimestamp = System.currentTimeMillis()
        def resultMessage = Exchanges.resultMessage(producerTemplate.send(endpointUri, requestExchange))
        // TODO: reactivate test
        //assert (System.currentTimeMillis() - startTimestamp < Rad75TestRouteBuilder.ASYNC_DELAY)
        
        // for sync messages -- check acknowledgement code and incoming TTL header
        if (!responseEndpointUri) {
            assert resultMessage.getBody(RetrievedDocumentSet.class).status == Status.SUCCESS
            
            def inHttpHeaders = resultMessage.headers[AbstractWsEndpoint.INCOMING_HTTP_HEADERS]
            assert inHttpHeaders['MyResponseHeader'].startsWith('Re: Number')
        }
    }
    
}
