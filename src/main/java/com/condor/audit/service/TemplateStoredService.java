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
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TemplateStoredService implements TemplateStoredI, Serializable {


    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateStoredService.class);
    private static final String URL = "http://localhost:8080/validate";
    private static String USER = "user";
    private static String PASSWORD = "password";

    private static final DSLJsoner dslJsoner = new DSLJsoner();

    public static Configuration config = new Configuration(Configuration.VERSION_2_3_31);

    private final WebClient.Builder webClientBuilder;
    @Autowired
    private TemplateStoredRepository templateStoredRepository;

    @Autowired
    private Configuration freemarkerConfiguration;

    @Autowired
    private TemplateStoredRepository templateRepository;

    public TemplateStoredService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<MsgResponse> requestPerson(Person person, String id) throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(person);
        JsonBase jb = dslJsoner.deserializeJsonPayload(json);

        return obtainTemplateFromDB(id)
                .flatMap(databaseTemplate -> {
                    StringTemplateLoader stringLoader = new StringTemplateLoader();
                    stringLoader.putTemplate("person", databaseTemplate);

                    Configuration config = new Configuration(Configuration.VERSION_2_3_31);
                    config.setTemplateLoader(stringLoader);

                    try {
                        Template freemarkerTemplate = config.getTemplate("person");
                        StringWriter stringWriter = new StringWriter();
                        Map<String, Object> root = new HashMap<>();
                        root.put("jb", jb);
                        freemarkerTemplate.process(root, stringWriter);
                        String body = stringWriter.toString();

                        ObjectMapper objectMapper = new ObjectMapper();
                        Person personRequest = objectMapper.readValue(body, Person.class);

                        return webClientBuilder.build()
                                .post()
                                .uri(URL)
                                .header("user", USER)
                                .header("password", PASSWORD)
                                .body(BodyInserters.fromValue(personRequest))
                                .retrieve()
                                .bodyToMono(MsgResponse.class);
                    } catch (TemplateException | IOException e) {
                        return Mono.error(e);
                    }
                });
    }

    @Override
    public Mono<List<TemplateStored>> obtainTemplateStored(){
        return Mono.just(templateStoredRepository.findAll());
        }
    @Override
    public TemplateStored saveTemplate(TemplateStored templateStored) throws IOException{
        if (!templateStored.getTemplate().isEmpty() || Objects.nonNull(templateStored.getTemplate())) {
            return templateStoredRepository.save(templateStored);
        } else {
            throw new IllegalArgumentException("The template can not be empty.");
        }
    }

    @Override
    public TemplateStored updateTemplateFreemarker(String pmid, String templateStored) throws IOException{
        UUID uuidPmid = UUID.fromString(pmid);
        Optional<TemplateStored> templateStoredOptional = templateStoredRepository.findByPmid(uuidPmid);
        templateStoredOptional.get().setTemplate(templateStored);
        return templateStoredRepository.save(templateStoredOptional.get());
    }


    private Mono<String> obtainTemplateFromDB(String pmid) throws IOException, TemplateException {
        return Mono.just(templateRepository.findByPmid(UUID.fromString(pmid)).orElse(null).getTemplate());
    }
}
