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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindFoldersForMultiplePatientsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindFoldersForMultiplePatientsQueryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindFoldersQueryTransformer;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindFoldersQueryTransformer}.
 * @author Jens Riemschneider
 */
public class FindFoldersForMultiplePatientsQueryTransformerTest {
    private FindFoldersForMultiplePatientsQueryTransformer transformer;
    private FindFoldersForMultiplePatientsQuery query;
    private EbXMLAdhocQueryRequest ebXML;
    
    @Before
    public void setUp() {
        transformer = new FindFoldersForMultiplePatientsQueryTransformer();
        query = new FindFoldersForMultiplePatientsQuery();

        query.setPatientIds(Arrays.asList(new Identifiable("id1", new AssigningAuthority("name1", "uni1", "uniType1")), new Identifiable("id2", new AssigningAuthority("name2", "uni2", "uniType2"))));
        query.getLastUpdateTime().setFrom("1");
        query.getLastUpdateTime().setTo("2");
        QueryList<Code> codes = new QueryList<Code>();
        codes.getOuterList().add(
                Arrays.asList(new Code("code7", null, "scheme7"), new Code("code8", null, "scheme8")));
        codes.getOuterList().add(
                Arrays.asList(new Code("code9", null, "scheme9")));
        query.setCodes(codes);
        query.setStatus(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));
        query.setHomeCommunityId("12.21.41");

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }
    
    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.FIND_FOLDERS_MPQ.getId(), ebXML.getId());
        assertEquals("12.21.41", ebXML.getHome());

        assertEquals(Arrays.asList("('id1^^^name1&uni1&uniType1')","('id2^^^name2&uni2&uniType2')"),
                ebXML.getSlotValues(QueryParameter.FOLDER_PATIENT_ID.getSlotName()));
        
        assertEquals(Arrays.asList("1"),
                ebXML.getSlotValues(QueryParameter.FOLDER_LAST_UPDATE_TIME_FROM.getSlotName()));
        assertEquals(Arrays.asList("2"),
                ebXML.getSlotValues(QueryParameter.FOLDER_LAST_UPDATE_TIME_TO.getSlotName()));

        List<EbXMLSlot> slots = ebXML.getSlots(QueryParameter.FOLDER_CODES.getSlotName());
        assertEquals(2, slots.size());
        assertEquals(Arrays.asList("('code7^^scheme7')", "('code8^^scheme8')"), slots.get(0).getValueList());
        assertEquals(Arrays.asList("('code9^^scheme9')"), slots.get(1).getValueList());
        
        assertEquals(Arrays.asList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')", "('urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted')"),
                ebXML.getSlotValues(QueryParameter.FOLDER_STATUS.getSlotName()));
        
        assertEquals(6, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new FindFoldersForMultiplePatientsQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }



    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        FindFoldersForMultiplePatientsQuery result = new FindFoldersForMultiplePatientsQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query.toString(), result.toString());
    }
    
    @Test
    public void testFromEbXMLLineBreakInAValueList() {
        transformer.toEbXML(query, ebXML);
        ebXML.getSlots().get(5).getValueList().clear();
        ebXML.getSlots().get(5).getValueList().add("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved',\n'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted')");
        FindFoldersForMultiplePatientsQuery result = new FindFoldersForMultiplePatientsQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query.toString(), result.toString());
    }
    
    @Test
    public void testFromEbXMLNull() {
        FindFoldersForMultiplePatientsQuery result = new FindFoldersForMultiplePatientsQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new FindFoldersForMultiplePatientsQuery(), result);
    }
        
    @Test
    public void testFromEbXMLEmpty() {
        FindFoldersForMultiplePatientsQuery result = new FindFoldersForMultiplePatientsQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new FindFoldersForMultiplePatientsQuery().toString(), result.toString());
    }
}
