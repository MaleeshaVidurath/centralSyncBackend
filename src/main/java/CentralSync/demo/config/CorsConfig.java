package CentralSync.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("http://localhost:8080","http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS","PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
