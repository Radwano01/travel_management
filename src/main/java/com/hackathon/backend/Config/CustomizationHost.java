package com.hackathon.backend.Config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class CustomizationHost implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        try {
            factory.setAddress(InetAddress.getByName("0.0.0.0"));
        } catch (UnknownHostException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
    }
}
