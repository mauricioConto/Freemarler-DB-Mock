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

    public TemplateStored(UUID pmid, UUID adc) {
        this.pmid = pmid;
        this.adc = adc;
    }

    public TemplateStored(UUID pmid, UUID adc, String template) {
        this.pmid = pmid;
        this.adc = adc;
        this.template = template;
    }
}
