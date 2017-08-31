package com.bbva.integration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bbva.integration.util.PropertiesExterno;

@SpringBootApplication
@ImportResource("classpath:/integration/integration2.xml")
public class SpringIntegrationHuascaranApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationHuascaranApplication.class, args);
	}
	

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	   return application.sources(SpringIntegrationHuascaranApplication.class);
	}
}


@Configuration
class DatabaseConfig  {
    
	@Autowired
	PropertiesExterno propertiesExterno;
		
	@Bean(name = "dsFINDIM")
	@Primary
    public DataSource gtsDataSource() {
        return DataSourceBuilder.create()
                .url(propertiesExterno.URL_DATASOURCE_FINDIM)
                .username(propertiesExterno.USERNAME_DATASOURCE_FINDIM)
                .password(propertiesExterno.PASSWORD_DATASOURCE_FINDIM)
                .driverClassName(propertiesExterno.DRIVER_DATASOURCE_FINDIM)
                .build();
    }

    @Bean(name = "jdbcFINDIM")
    @Autowired    
    public JdbcTemplate gtsJdbcTemplate(@Qualifier("dsFINDIM")DataSource dsFINDIM) {
        return new JdbcTemplate(dsFINDIM);
    }
    
}
