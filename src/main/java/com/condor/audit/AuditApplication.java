package com.condor.audit;

import com.condor.audit.model.utilities.PropertyReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@SpringBootApplication
public class AuditApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AuditApplication.class, args);
	}

}
