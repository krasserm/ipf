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
package org.openehealth.ipf.commons.ihe.xds.iti51;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.xds.iti51.Iti51AuditDataset;
import org.openehealth.ipf.commons.ihe.xds.iti51.Iti51AuditStrategy;

/**
 * Client audit strategy for ITI-51.
 * @author Dmytro Rud
 * @author Michael Ottati
 */
public class Iti51ClientAuditStrategy extends Iti51AuditStrategy {

    private static final String[] NECESSARY_AUDIT_FIELDS = new String[] {
        "EventOutcomeCode",
        "ServiceEndpointUrl",
        "QueryUuid",
        "RequestPayload"
        /*"PatientId"*/};


    /**
     * Constructs the audit strategy.
     * @param allowIncompleteAudit
     *      whether this strategy should allow incomplete audit records
     *      (parameter initially configurable via endpoint URL).
     */
    public Iti51ClientAuditStrategy(boolean allowIncompleteAudit) {
        super(false, allowIncompleteAudit);
    }

    //TODO Check if this needs its own event type or this one can be used.
    @Override
    public void doAudit(Iti51AuditDataset auditDataset) {
        AuditorManager.getCustomXdsAuditor().auditIti51(
                auditDataset.getEventOutcomeCode(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getUserName(),
                auditDataset.getQueryUuid(),
                auditDataset.getRequestPayload(),
                auditDataset.getHomeCommunityId(),
                auditDataset.getPatientId());
    }

    @Override
    public String[] getNecessaryAuditFieldNames() {
        return NECESSARY_AUDIT_FIELDS;
    }
}
