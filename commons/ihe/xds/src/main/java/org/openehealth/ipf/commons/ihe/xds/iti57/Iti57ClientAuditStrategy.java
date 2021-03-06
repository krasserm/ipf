/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.iti57;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomXdsAuditor;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditStrategy30;

/**
 * Client audit strategy for ITI-57.
 * @author Boris Stanojevic
 */
public class Iti57ClientAuditStrategy extends XdsSubmitAuditStrategy30 {

    private static final String[] NECESSARY_AUDIT_FIELDS = new String[] {
        "EventOutcomeCode",
        "ServiceEndpointUrl",
        "SubmissionSetUuid",
        "PatientId"};


    /**
     * Constructs the audit strategy.
     * @param allowIncompleteAudit
     *      whether this strategy should allow incomplete audit records
     *      (parameter initially configurable via endpoint URL).
     */
    public Iti57ClientAuditStrategy(boolean allowIncompleteAudit) {
        super(false, allowIncompleteAudit);
    }

    @Override
    public void doAudit(XdsSubmitAuditDataset auditDataset) {
        AuditorManager.getCustomXdsAuditor().auditClientIti57(
                auditDataset.getEventOutcomeCode(),
                auditDataset.getUserId(),
                auditDataset.getUserName(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getSubmissionSetUuid(),
                auditDataset.getPatientId());
    }

    @Override
    public String[] getNecessaryAuditFieldNames() {
        return NECESSARY_AUDIT_FIELDS;
    }
}
