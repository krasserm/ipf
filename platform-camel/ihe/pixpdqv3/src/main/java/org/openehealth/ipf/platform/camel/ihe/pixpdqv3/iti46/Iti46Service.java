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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti46;

import org.openehealth.ipf.commons.ihe.pixpdqv3.iti46.Iti46PortType;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.DefaultHl7v3WebService;

/**
 * Service implementation for the IHE ITI-46 transaction (PIX Update Notification v3).
 * @author Dmytro Rud
 */
public class Iti46Service extends DefaultHl7v3WebService implements Iti46PortType {

    public Iti46Service() {
        super(Iti46Endpoint.ITI_46);
    }

    @Override
    public String recordRevised(String body) {
        return doProcess(body);
    }
}
