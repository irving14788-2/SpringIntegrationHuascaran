package com.bbva.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
public class PropertiesConfiguration {

    @Bean
    public PropertyPlaceholderConfigurer properties() {
        final PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        //ppc.setIgnoreUnresolvablePlaceholders(true);
        ppc.setIgnoreResourceNotFound(true);
        
        final List<Resource> resourceLst = new ArrayList<Resource>();
        resourceLst.add(new ClassPathResource("integrationHuascaran.properties"));
        resourceLst.add(new FileSystemResource((System.getProperty("integracionHuascaranConfig")==null?"":System.getProperty("integracionHuascaranConfig"))+"integrationHuascaran.properties"));
        
        ppc.setLocations(resourceLst.toArray(new Resource[]{}));
        return ppc;
    }
}