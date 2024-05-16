package com.example.teste.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/imagens/**") 
        .addResourceLocations("classpath:/static/imagens/") 
        .setCachePeriod(31556926); 

registry.addResourceHandler("/static/**") 
        .addResourceLocations("classpath:/static/") 
        .setCachePeriod(0); 
    }

    @Override
    public void  addCorsMappings(@SuppressWarnings("null") CorsRegistry registry){
                      registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET","POST","PUT","DELETE");
    }
}
