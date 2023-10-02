package com.condor.audit.service;

import com.condor.audit.model.MsgResponse;
import com.condor.audit.model.Person;
import com.condor.audit.model.TemplateStored;
import freemarker.template.TemplateException;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

public interface TemplateStoredI {

    List<TemplateStored> obtainTemplateStored();

    MsgResponse requestPerson(Person person, Integer id)  throws IOException, TemplateException, Exception;

    TemplateStored saveTemplate(TemplateStored templateStored) throws IOException;
}
