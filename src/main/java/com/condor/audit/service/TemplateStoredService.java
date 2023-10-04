package com.condor.audit.service;

import com.condor.audit.model.MsgResponse;
import com.condor.audit.model.Person;
import com.condor.audit.model.TemplateStored;
import com.condor.audit.model.utilities.JsonBase;
import com.condor.audit.model.utilities.json.DSLJsoner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TemplateStoredService implements TemplateStoredI, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateStoredService.class);
    private final RestTemplate restTemplate;
    private static final String URL = "http://localhost:8080/validate";
    private static String USER = "user";
    private static String PASSWORD = "password";

    private static final DSLJsoner dslJsoner = new DSLJsoner();

    public static Configuration config = new Configuration(Configuration.VERSION_2_3_31);
    @Autowired
    public TemplateStoredService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Autowired
    private TemplateStoredRepository templateStoredRepository;


    @Autowired
    private Configuration freemarkerConfiguration;

    @Autowired
    private TemplateStoredRepository templateRepository;

    @Override
    public MsgResponse requestPerson(Person person, Integer id) throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(person);
        JsonBase jb = dslJsoner.deserializeJsonPayload(json);
        String body = "";

        try {

            String databaseTemplate = obtainTemplateFromDB(id);
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate("person", databaseTemplate);
            config.setTemplateLoader(stringLoader);
            Template freemarkerTemplate = config.getTemplate("person");
            StringWriter stringWriter = new StringWriter();
            Map<String, Object> root = new HashMap<>();
            root.put("jb", jb);
            freemarkerTemplate.process(root, stringWriter);
            body = stringWriter.toString();
        }
        catch (TemplateException | IOException e) {
            jb.addError(LOGGER, String.format("Freemarker error" + e));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        // Convert Json to Object Person
        Person personRequest = objectMapper.readValue(body, Person.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(personRequest, headers);

        return restTemplate.exchange(
                URL + "?user={USER}&password={PASSWORD}",
                HttpMethod.POST,
                requestEntity,
                MsgResponse.class,
                USER,
                PASSWORD
        ).getBody();

    }

    @Override
    public List<TemplateStored> obtainTemplateStored(){
        return templateStoredRepository.findAll();
        }
    @Override
    public TemplateStored saveTemplate(TemplateStored templateStored) throws IOException{
        return templateStoredRepository.save(templateStored);
    }

    @Override
    public TemplateStored updateTemplateFreemarker(Integer id, String templateStored) throws IOException{
        Optional<TemplateStored> templateStoredOptional = templateStoredRepository.findByPmid(id);
        templateStoredOptional.get().setTemplate(templateStored);
        return templateStoredRepository.save(templateStoredOptional.get());
    }


    public String obtainTemplateFromDB(Integer id) throws IOException, TemplateException {
        return templateRepository.findByPmid(id).orElse(null).getTemplate();
    }
}
