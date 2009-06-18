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
package org.openehealth.ipf.platform.camel.ihe.xds.iti18.audit;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditDataset;

/**
 * ITI-18 specific Audit Dataset.
 * 
 * @author Dmytro Rud
 */
public class Iti18AuditDataset extends AuditDataset {

    private String queryUuid;

    public Iti18AuditDataset(boolean isServerSide) {
        super(isServerSide);
    }

    public void setQueryUuid(String queryUuid) {
        this.queryUuid = queryUuid;
    }

    public String getQueryUuid() {
        return queryUuid;
    }
}