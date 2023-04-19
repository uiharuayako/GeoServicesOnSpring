package com.geospringworks.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
@Component
public class ProjectUrl {
    public String getProjectUrl(){
        return "http://" + SERVER_ADDRESS.getHostAddress() + ":" + SERVER_PORT.toString();
    }
    @Value("${server.address}")
    private InetAddress SERVER_ADDRESS;
    @Value("${server.port}")
    private Integer SERVER_PORT;
}
