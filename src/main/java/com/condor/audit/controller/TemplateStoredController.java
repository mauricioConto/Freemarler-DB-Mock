package com.condor.audit.controller;

import com.condor.audit.model.MsgResponse;
import com.condor.audit.model.Person;
import com.condor.audit.model.TemplateStored;
import com.condor.audit.service.TemplateStoredService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

@Controller
public class TemplateStoredController {

    @Autowired
    TemplateStoredService templateStoredService;

    @QueryMapping()
    public List<TemplateStored> obtainTemplateStored(){
        return templateStoredService.obtainTemplateStored();
    }

    @MutationMapping()
    public TemplateStored saveTemplateFreemarker(@Argument TemplateStored templateStored) throws IOException{
        return templateStoredService.saveTemplate(templateStored);
    }

    @MutationMapping()
    public MsgResponse requestPersonWithTemplate(@Argument Integer id, @Argument Person person)  throws IOException, TemplateException, Exception {
        return templateStoredService.requestPerson(person, id);
    }

}
