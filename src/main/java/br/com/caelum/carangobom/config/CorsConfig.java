package br.com.caelum.carangobom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        	.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
        	.allowedOrigins("https://carango-bom-base-theta.vercel.app", "http://localhost:3000")
        	.allowedHeaders("*");
    }
}