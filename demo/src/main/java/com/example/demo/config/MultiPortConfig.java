package com.example.demo.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "myapp.enable-login-api", havingValue = "true")
public class MultiPortConfig {

    @Value("${myapp.ports}")
    private String ports;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        String[] portArray = ports.split(",");

        for (String port : portArray) {
            factory.addAdditionalTomcatConnectors(createConnector(Integer.parseInt(port)));
        }

        return factory;
    }

    private Connector createConnector(int port) {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(port);
        return connector;
    }
}

