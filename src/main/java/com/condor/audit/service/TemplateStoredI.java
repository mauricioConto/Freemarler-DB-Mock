package com.condor.audit.service;

import com.condor.audit.model.MsgResponse;
import com.condor.audit.model.Person;
import com.condor.audit.model.TemplateStored;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface TemplateStoredI {

    List<TemplateStored> obtainTemplateStored();

    MsgResponse requestPerson(Person person, String id) throws Exception;

    TemplateStored saveTemplate(TemplateStored templateStored) throws IOException;

    TemplateStored updateTemplateFreemarker(TemplateStored templateStored) throws IOException;
}
