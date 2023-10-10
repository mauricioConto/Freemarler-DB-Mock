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
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Controller
public class TemplateStoredController {

    @Autowired
    TemplateStoredService templateStoredService;

    @QueryMapping()
    public Mono<List<TemplateStored>> obtainTemplateStored(){
        return templateStoredService.obtainTemplateStored();
    }

    @MutationMapping()
    public TemplateStored saveTemplateFreemarker(@Argument TemplateStored templateStored) throws IOException{
        return templateStoredService.saveTemplate(templateStored);
    }

    @MutationMapping()
    public Mono<MsgResponse> requestPersonWithTemplate(@Argument String pmid, @Argument Person person)  throws IOException, TemplateException, Exception {
        return templateStoredService.requestPerson(person, pmid);
    }

    @MutationMapping()
    public TemplateStored updateTemplateFreemarker(@Argument String pmid, @Argument String template) throws  IOException{
        return templateStoredService.updateTemplateFreemarker(pmid, template);
    }

}
