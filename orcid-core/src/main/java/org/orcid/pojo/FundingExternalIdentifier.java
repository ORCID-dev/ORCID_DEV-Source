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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.13 at 04:58:08 PM BST 
//

package org.orcid.pojo;

import java.io.Serializable;

import org.orcid.jaxb.model.message.FundingExternalIdentifierType;
import org.orcid.jaxb.model.message.Url;
import org.orcid.jaxb.model.record_rc2.Relationship;
import org.orcid.pojo.ajaxForm.PojoUtil;

public class FundingExternalIdentifier implements Serializable {
    private static final long serialVersionUID = -7384268121070514399L;
    protected FundingExternalIdentifierType type;
    protected String value;
    protected Url url;
    protected Relationship relationship;

    public FundingExternalIdentifierType getType() {
        return type;
    }

    public void setType(FundingExternalIdentifierType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }
    
    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + ((relationship == null) ? 0 : relationship.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FundingExternalIdentifier other = (FundingExternalIdentifier) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;                
        if (relationship == null) {
            if (other.relationship != null)
                return false;
        } else if (!relationship.equals(other.relationship))
            return false;                
        return true;
    }

    public org.orcid.jaxb.model.message.FundingExternalIdentifier toMessagePojo() {
        org.orcid.jaxb.model.message.FundingExternalIdentifier messagePojo = new org.orcid.jaxb.model.message.FundingExternalIdentifier();
        messagePojo.setType(this.getType());
        messagePojo.setUrl(this.getUrl());
        messagePojo.setValue(this.getValue());
        return messagePojo;
    }

    public org.orcid.jaxb.model.record_rc2.FundingExternalIdentifier toRecordPojo() {
        org.orcid.jaxb.model.record_rc2.FundingExternalIdentifier recordPojo = new org.orcid.jaxb.model.record_rc2.FundingExternalIdentifier();

        if (this.getType() != null) {
            org.orcid.jaxb.model.record_rc2.FundingExternalIdentifierType type = org.orcid.jaxb.model.record_rc2.FundingExternalIdentifierType.fromValue(this.getType().value());
            recordPojo.setType(type);
        }

        if (this.getUrl() != null && !PojoUtil.isEmpty(this.getUrl().getValue())) {
            org.orcid.jaxb.model.common_rc2.Url url = new org.orcid.jaxb.model.common_rc2.Url(this.getUrl().getValue());
            recordPojo.setUrl(url);
        }

        if (!PojoUtil.isEmpty(this.getValue())) {
            recordPojo.setValue(this.getValue());
        }
        
        if(this.getRelationship() != null) {
            recordPojo.setRelationship(this.getRelationship());
        }

        return recordPojo;
    }

    public static FundingExternalIdentifier fromMessagePojo(org.orcid.jaxb.model.message.FundingExternalIdentifier messagePojo) {
        FundingExternalIdentifier result = new FundingExternalIdentifier();
        if (messagePojo.getType() != null) {
            result.setType(FundingExternalIdentifierType.fromValue(messagePojo.getType().value()));
        }

        if (messagePojo.getUrl() != null) {
            result.setUrl(new Url(messagePojo.getUrl().getValue()));
        }

        if (!PojoUtil.isEmpty(messagePojo.getValue())) {
            result.setValue(messagePojo.getValue());
        }

        return result;
    }

    public static FundingExternalIdentifier fromRecordPojo(org.orcid.jaxb.model.record_rc2.FundingExternalIdentifier recordPojo) {
        FundingExternalIdentifier result = new FundingExternalIdentifier();
        if (recordPojo.getType() != null) {
            result.setType(FundingExternalIdentifierType.fromValue(recordPojo.getType().value()));
        }
        
        if (recordPojo.getUrl() != null) {
            result.setUrl(new Url(recordPojo.getUrl().getValue()));
        }

        if (!PojoUtil.isEmpty(recordPojo.getValue())) {
            result.setValue(recordPojo.getValue());
        }
        
        if(recordPojo.getRelationship() != null) {
            result.setRelationship(recordPojo.getRelationship());
        }
        return result;
    }
}