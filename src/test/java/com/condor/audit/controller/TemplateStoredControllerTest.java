package com.condor.audit.controller;

import com.condor.audit.model.MsgResponse;
import com.condor.audit.model.Person;
import com.condor.audit.model.TemplateStored;
import com.condor.audit.service.TemplateStoredService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateStoredControllerTest {

    @InjectMocks
    TemplateStoredController templateStoredController;

    @Mock
    TemplateStoredService templateStoredService;

    private String id = "5465-46546-54654-6546-541";

    UUID uuid = UUID.fromString(id);
    List<TemplateStored> templateStoredList = Arrays.asList(
            new TemplateStored(uuid,uuid, "1", "post", "localhost", "user:user"),
            new TemplateStored(uuid,uuid, "1", "post", "localhost", "user:user"));

    TemplateStored templateStored = new TemplateStored(uuid,uuid, "1", "post", "localhost", "user:user");

    Person person = new Person();

    MsgResponse msgResponse = new MsgResponse("ok");

    @Test
    void obtainTemplateStored() {
        when(templateStoredService.obtainTemplateStored()).thenReturn(templateStoredList);
        List<TemplateStored>  templateStored = templateStoredController.obtainTemplateStored();
        Assertions.assertEquals(templateStored.get(0).getTemplate(), templateStoredList.get(0).getTemplate());
    }

    @Test
    void saveTemplateFreemarker() throws IOException {
        when(templateStoredService.saveTemplate(any())).thenReturn(templateStored);
        TemplateStored templateStoredOutput = templateStoredController.saveTemplateFreemarker(templateStored);
        Assertions.assertEquals(templateStoredOutput.getTemplate(), templateStored.getTemplate());
    }

    @Test
    void requestPersonWithTemplate() throws Exception {
        when(templateStoredService.requestPerson(any(), any())).thenReturn(new MsgResponse("ok"));
        MsgResponse msgResponseOutput = templateStoredController.requestPersonWithTemplate("1", person);
        Assertions.assertEquals(msgResponseOutput.getMessage(), msgResponse.getMessage());
    }

    @Test
    void updateTemplateFreemarker() throws Exception {
        when(templateStoredService.updateTemplateFreemarker(any())).thenReturn(templateStored);
        TemplateStored templateStoredOutput = templateStoredController.updateTemplateFreemarker(templateStored);
        Assertions.assertEquals(templateStored.getTemplate(), templateStoredOutput.getTemplate());
    }
}