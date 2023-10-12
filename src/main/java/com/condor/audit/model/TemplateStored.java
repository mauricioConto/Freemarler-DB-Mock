package com.condor.audit.model;


import org.hibernate.annotations.NaturalId;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Table(name = "template")
public class TemplateStored {

    @Id
    private UUID pmid;

    @NaturalId
    private UUID adc;

    private String template;

    private String httpMethod;

    private String url;

    private String headerTemplate;

    public UUID getPmid() {
        return pmid;
    }

    public String getTemplate() {
        return template;
    }

    public UUID getAdc() {
        return adc;
    }

    public TemplateStored() {
    }

    public void setPmid(UUID pmid) {
        this.pmid = pmid;
    }

    public void setAdc(UUID adc) {
        this.adc = adc;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeaderTemplate() {
        return headerTemplate;
    }

    public void setHeaderTemplate(String headerTemplate) {
        this.headerTemplate = headerTemplate;
    }

    public TemplateStored(UUID pmid, UUID adc) {
        this.pmid = pmid;
        this.adc = adc;
    }

    public TemplateStored(UUID pmid, UUID adc, String template, String httpMethod, String url, String headerTemplate) {
        this.pmid = pmid;
        this.adc = adc;
        this.template = template;
        this.httpMethod = httpMethod;
        this.url = url;
        this.headerTemplate = headerTemplate;
    }
}
