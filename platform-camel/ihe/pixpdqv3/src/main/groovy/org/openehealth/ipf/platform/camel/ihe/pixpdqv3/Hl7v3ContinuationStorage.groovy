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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3

/**
 * @author Dmytro Rud
 */
interface Hl7v3ContinuationStorage {
    void storeMessage(String key, String message)
    String getMessage(String key)

    void storeLastResultNumber(String key, int lastResultNumber)
    int getLastResultNumber(String key)

    void storeContinuationQuantity(String key, int continuationQuantity)
    int getContinuationQuantity(String key)

    boolean remove(String key)
}
