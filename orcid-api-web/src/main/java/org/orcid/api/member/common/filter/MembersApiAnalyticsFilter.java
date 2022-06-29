package org.orcid.api.member.common.filter;

import javax.ws.rs.ext.Provider;

import org.orcid.api.common.filter.AnalyticsFilter;

@Provider
public class MembersApiAnalyticsFilter extends AnalyticsFilter {

    public MembersApiAnalyticsFilter() {
        this.setIsPublicApi();
    }
    
    @Override
    public void setIsPublicApi() {
        this.isPublicApi = true;      
    }

}
