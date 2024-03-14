package com.products.infraestructure.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.products.infraestructure.input.Rest.Mapper.IProductRestMapper;
import com.products.infraestructure.ouput.jpaAdapter.Mapper.IProductMapper;

@Configuration
public class MapStructConfig {

    @Bean
    IProductMapper MapGetProductMapper() {
        return Mappers.getMapper(IProductMapper.class);
    }
    @Bean
    IProductRestMapper MapGetProductRestMapper() {
        return Mappers.getMapper(IProductRestMapper.class);
    }
}
