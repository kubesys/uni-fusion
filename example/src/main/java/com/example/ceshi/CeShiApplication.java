package com.example.ceshi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan({
        "com.example.ceshi",
        "com.qnkj.clouds"
})
public class CeShiApplication implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:7070")
                .allowCredentials(true);
    }

    public static void main(String[] args) {
        SpringApplication.run(CeShiApplication.class, args);
    }

}
