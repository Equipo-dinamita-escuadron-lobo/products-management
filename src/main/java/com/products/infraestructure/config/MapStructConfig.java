package com.products.infraestructure.config;

import com.products.infraestructure.input.Rest.Mapper.IProductRestMapper;
import com.products.infraestructure.ouput.jpaAdapter.Mapper.IProductMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MapStructConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST");
            }
        };
    }
    @Bean
    IProductMapper MapGetProductMapper() {
        return Mappers.getMapper(IProductMapper.class);
    }
    @Bean
    IProductRestMapper MapGetProductRestMapper() {
        return Mappers.getMapper(IProductRestMapper.class);
    }
}
