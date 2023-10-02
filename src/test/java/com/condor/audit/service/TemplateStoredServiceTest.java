package com.condor.audit.service;

import com.condor.audit.model.MsgResponse;
import com.condor.audit.model.Person;
import com.condor.audit.model.TemplateStored;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TemplateStoredServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TemplateStoredRepository templateStoredRepository;

    @Mock
    private Configuration freemarkerConfiguration;

    @InjectMocks
    private TemplateStoredService templateStoredService;

    Template temp =

    private String TEMPLATE_BASE64 = "ezwjYXNzaWduIGNyZWF0ZWRBdD1qYi5maWVsZENoZWNrKCJjcmVhdGVkQXQiKQ0KaW5zdGl0dXRpb25faWQ9amIuZmllbGRDaGVjaygiZXZlbnQuaW5zdGl0dXRpb25faWQiKSBwZXJzb25OYW1lVG9rZW5JZD1qYi5maWVsZENoZWNrKCJldmVudC5wZXJzb25OYW1lVG9rZW5JZCIpIHBlcnNvbkFkZHJlc3NUb2tlbklkPWpiLmZpZWxkQ2hlY2soImV2ZW50LnBlcnNvbkFkZHJlc3NUb2tlbklkIikgc3NuVG9rZW5JZD1qYi5maWVsZENoZWNrKCJldmVudC5zc25Ub2tlbklkIikgcHJvZ3JhbV9tYW5hZ2VyX2lkPWpiLmZpZWxkQ2hlY2soImV2ZW50LnByb2dyYW1fbWFuYWdlcl9pZCIpIGluc3RhbmNlSWQ9amIuZmllbGRDaGVjaygiZXZlbnQuaW5zdGFuY2VJZCIpIGV4dGVybmFsUGVyc29uSWQ9amIuZmllbGRDaGVjaygiZXZlbnQuZXh0ZXJuYWxQZXJzb25JZCIpID4NCjwjZnVuY3Rpb24gc2V0TnVsbCB2YXI+DQogIDwjaWYgdmFyLmlzTnVsbCgpPg0KICAgIDwjbG9jYWwgZGVyaXZlZFZhcj0ibnVsbCI+DQogIDwjZWxzZT4NCiAgICA8I2xvY2FsIGRlcml2ZWRWYXI9IlwiJHt2YXIuc3RyaW5nVmFsdWUoKX1cIiI+DQogIDwvI2lmPg0KICA8I3JldHVybiBkZXJpdmVkVmFyPg0KPC8jZnVuY3Rpb24+DQogICJJbnN0aXR1dGlvbklkIjogIiR7aW5zdGl0dXRpb25faWR9IiwNCiAgIlBlcnNvbk5hbWVUb2tlbklkIjogJHtwZXJzb25OYW1lVG9rZW5JZH0sDQogICJQZXJzb25BZGRyZXNzVG9rZW5JZCI6ICR7cGVyc29uQWRkcmVzc1Rva2VuSWR9LA0KICAiU3NuVG9rZW5JZCI6ICR7c3NuVG9rZW5JZH0sDQogICJQcm9ncmFtTWFuYWdlcklkIjogIiR7cHJvZ3JhbV9tYW5hZ2VyX2lkfSIsDQogICJFeHRlcm5hbFBlcnNvbklkIjogIiR7aW5zdGFuY2VJZH06JHtleHRlcm5hbFBlcnNvbklkfSIsDQogICJBY3RpdmUiOiB0cnVlLA0KICAiQ3JlYXRlZEF0IjogIjwjaWYgY3JlYXRlZEF0LmlzTnVsbCgpPiR7Lm5vdz9pc29fdXRjX256fTwjZWxzZT48I2Fzc2lnbiBjcmVhdGVkQXRUWj0iJHtjcmVhdGVkQXQuZ2V0KCl9IEVTVCI+JHtjcmVhdGVkQXRUWj9kYXRldGltZSgieXl5eS1NTS1kZCBISDptbTpzcyBaIik/aXNvX3V0Y19uen08LyNpZj4iDQp9";

    @Test
    public void testRequestPerson() throws Exception {

        TemplateStored templateStored = new TemplateStored(1, "1", TEMPLATE_BASE64);
        // Decodificar la cadena de base64 a una matriz de bytes
        byte[] decodedTemplate = Base64.getDecoder().decode(templateStored.getTemplate());
        // Convertir la matriz de bytes en una cadena
        String decodedString = new String(decodedTemplate);
        // Crea un objeto Template de FreeMarker desde el contenido
        Template t = new Template(null, new StringReader(decodedString), freemarkerConfiguration);

        when(templateStoredService.obtainTemplateFromDB(1)).thenReturn(t);

        String body="";

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> root = new HashMap<>();
        root.put("jb", jb);
        t.process(root, stringWriter);
        body = stringWriter.toString();

        // Configura el comportamiento del restTemplate.exchange
        //when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(), eq(MsgResponse.class)))
          //      .thenReturn(new ResponseEntity<>(new MsgResponse(), HttpStatus.OK));

        // Simula un objeto Person y un ID
        Person person = new Person(/* proporciona los valores necesarios */);
        Integer id = 1;

        // Llama al método requestPerson
        MsgResponse response = templateStoredService.requestPerson(person, id);

        // Verifica que la respuesta no sea nula y tiene el comportamiento esperado
        assertNotNull(response);
        // Agrega más afirmaciones según el comportamiento esperado
    }

    @Test
    public void testObtainTemplateFromDB() throws Exception {
        // Simula el comportamiento del templateStoredRepository
        TemplateStored templateStored = new TemplateStored(1, "1", TEMPLATE_BASE64);
        when(templateStoredRepository.findByPmid(anyInt())).thenReturn(Optional.of(templateStored));

        // Llama al método obtainTemplateFromDB
        Template template = templateStoredService.obtainTemplateFromDB(1);

        // Verifica que el template no sea nulo y tiene el comportamiento esperado
        assertNotNull(template);
        // Agrega más afirmaciones según el comportamiento esperado
    }
}