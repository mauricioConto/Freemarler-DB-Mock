package com.condor.audit.service;

import com.condor.audit.model.MsgResponse;
import com.condor.audit.model.Person;
import com.condor.audit.model.TemplateStored;
import com.condor.audit.model.utilities.JsonBase;
import com.condor.audit.model.utilities.json.DSLJsoner;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TemplateStoredService implements TemplateStoredI, Serializable {

    private static final HttpClient HTTP_CLIENT = getHttpClient();
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateStoredService.class);
    private static String USER = "user";
    private static String PASSWORD = "password";

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

    @Override
    public MsgResponse requestPerson(Person person, String id) throws Exception {
        DSLJsoner dslJsoner1 = new DSLJsoner();
        ObjectMapper objectMapper = new ObjectMapper();
        String jb1 = objectMapper.writeValueAsString(person);
        JsonBase jb = dslJsoner1.deserializeJsonPayload(jb1);
        UUID pmid = UUID.fromString(id);

        TemplateStored templateStored = templateRepository.findByPmid(pmid).orElseThrow();
        String url = templateStored.getUrl();
        String headerTemplate = templateStored.getHeaderTemplate();
        String httpMethod = templateStored.getHttpMethod();
        String pmidAdc = templateStored.getPmid().toString().concat(templateStored.getAdc().toString());

        String templateFromDb = templateRepository.findByPmid(pmid).orElseThrow().getTemplate();

        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate(pmidAdc, templateFromDb);

        Configuration config = new Configuration(Configuration.VERSION_2_3_31);
        config.setTemplateLoader(stringLoader);
        HttpResponse<String> response = null;

        try {
            Template freemarkerTemplate = config.getTemplate(pmidAdc);
            StringWriter stringWriter = new StringWriter();
            Map<String, Object> root = new HashMap<>();
            root.put("jb", jb);
            freemarkerTemplate.process(root, stringWriter);
            String body = stringWriter.toString();

            Person personRequest = objectMapper.readValue(body, Person.class);

             HttpRequest httpRequest = getRequest(body, new URI(url), httpMethod, headerTemplate);

            response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            LOGGER.info(String.format("Request sent ok %s", body));


        } catch (TemplateException | IOException e) {
            throw new Exception();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new MsgResponse(response.body());


}

    @Override
    public List<TemplateStored> obtainTemplateStored() {
        return templateStoredRepository.findAll();
    }

    @Override
    public TemplateStored saveTemplate(TemplateStored templateStored) throws IOException {
        if (!templateStored.getTemplate().isEmpty() || Objects.nonNull(templateStored.getTemplate())) {
            return templateStoredRepository.save(templateStored);
        } else {
            throw new IllegalArgumentException("The template can not be empty.");
        }
    }

    @Override
    public TemplateStored updateTemplateFreemarker(TemplateStored templateStored) throws IOException {

        return templateStoredRepository.findByPmid(templateStored.getPmid()).map(ts -> {
            ts.setHeaderTemplate(templateStored.getHeaderTemplate());
            ts.setUrl(templateStored.getUrl());
            ts.setTemplate(templateStored.getTemplate());
            ts.setHttpMethod(templateStored.getHttpMethod());
            return templateStoredRepository.save(ts);

        }).get();
    }

    private HttpRequest getRequest(String body, URI uri, String httpMethod, String headerTemplate) {

            ObjectMapper objectMapper = new ObjectMapper();
            HttpRequest.Builder httpRequestBuilder = HttpRequest
                    .newBuilder(uri)
                    .header("Content-Type", "application/json")
                    .method(httpMethod, HttpRequest.BodyPublishers.ofString(body));
        try {
            Map<String, String> headerMap = objectMapper.readValue(headerTemplate, Map.class);
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpRequestBuilder.setHeader(entry.getKey(), entry.getValue());
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return httpRequestBuilder.build();
    }

    public static HttpClient getHttpClient() {
        return HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

}
