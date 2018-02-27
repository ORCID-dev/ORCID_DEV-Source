/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.integration.blackbox.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jettison.json.JSONException;
import org.orcid.integration.api.helper.APIRequestType;
import org.orcid.integration.blackbox.api.v3.dev1.MemberV3Dev1ApiClientImpl;
import org.orcid.jaxb.model.message.ScopePathType;
import org.orcid.jaxb.model.v3.dev1.groupid.GroupIdRecord;

import com.sun.jersey.api.client.ClientResponse;

public class BlackBoxBaseV3 extends BlackBoxBase {
    @Resource(name = "memberV3_0_dev1ApiClient")
    protected MemberV3Dev1ApiClientImpl memberV3Dev1ApiClient;
    
    protected static List<GroupIdRecord> groupRecords = null;
    
    /**
     * Create group ids
     * */
    public List<GroupIdRecord> createGroupIdsV3() throws JSONException {
        //Use the existing ones
        if(groupRecords != null && !groupRecords.isEmpty()) 
            return groupRecords;
        
        groupRecords = new ArrayList<GroupIdRecord>();
        
        String token = getClientCredentialsAccessToken(ScopePathType.GROUP_ID_RECORD_UPDATE, getClient1ClientId(), getClient1ClientSecret(), APIRequestType.MEMBER);
        GroupIdRecord g1 = new GroupIdRecord();
        g1.setDescription("Description");
        g1.setGroupId("orcid-generated:01" + System.currentTimeMillis());
        g1.setName("Group # 1");
        g1.setType("publisher");
        
        GroupIdRecord g2 = new GroupIdRecord();
        g2.setDescription("Description");
        g2.setGroupId("orcid-generated:02" + System.currentTimeMillis());
        g2.setName("Group # 2");
        g2.setType("publisher");                
        
        ClientResponse r1 = memberV3Dev1ApiClient.createGroupIdRecord(g1, token); 
        
        String r1LocationPutCode = r1.getLocation().getPath().replace("/orcid-api-web/v3.0_dev1/group-id-record/", "");
        g1.setPutCode(Long.valueOf(r1LocationPutCode));
        groupRecords.add(g1);
        
        ClientResponse r2 = memberV3Dev1ApiClient.createGroupIdRecord(g2, token);
        String r2LocationPutCode = r2.getLocation().getPath().replace("/orcid-api-web/v3.0_dev1/group-id-record/", "");
        g2.setPutCode(Long.valueOf(r2LocationPutCode));
        groupRecords.add(g2);
        
        return groupRecords;
    }
    
}