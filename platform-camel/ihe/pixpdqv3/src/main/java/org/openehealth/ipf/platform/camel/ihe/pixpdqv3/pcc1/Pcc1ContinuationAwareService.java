/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.pcc1;

import org.openehealth.ipf.commons.ihe.pixpdqv3.pcc1.Pcc1PortType;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.Hl7v3ContinuationAwareWebService;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.Hl7v3ContinuationStorage;

// The main purpose of this class is to add "implements Pcc1PortType"
// to the class Hl7v3ContinuationAwareWebService, as required by CXF.

/**
 * Continuation-Aware service implementation for the IHE PCC-1 transaction (QED).
 * @author Dmytro Rud
 */
public class Pcc1ContinuationAwareService
        extends Hl7v3ContinuationAwareWebService
        implements Pcc1PortType
{
    public Pcc1ContinuationAwareService(
            Hl7v3ContinuationStorage storage,
            int defaultThreshold,
            boolean validationOnContinuation)
    {
        super(Pcc1Endpoint.PCC_1, storage, defaultThreshold, "QUPC_IN043200UV01", validationOnContinuation);
    }
}
