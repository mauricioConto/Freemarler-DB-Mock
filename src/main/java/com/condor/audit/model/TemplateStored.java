package com.condor.audit.model;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
@Table(name = "template")
public class TemplateStored {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer pmid;
    private String adc;

    private String template;

    public Integer getPmid() {
        return pmid;
    }

    public String getTemplate() {
        return template;
    }

    public String getAdc() {
        return adc;
    }

    public TemplateStored() {
    }

    public void setPmid(Integer pmid) {
        this.pmid = pmid;
    }

    public void setAdc(String adc) {
        this.adc = adc;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public TemplateStored(Integer pmid, String adc) {
        this.pmid = pmid;
        this.adc = adc;
    }

    public TemplateStored(Integer pmid, String adc, String template) {
        this.pmid = pmid;
        this.adc = adc;
        this.template = template;
    }
}
