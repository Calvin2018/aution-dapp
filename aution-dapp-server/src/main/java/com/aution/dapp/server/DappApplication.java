package com.aution.dapp.server;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.aution.dapp.server.exception.CustomExceptionResolver;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.aution.dapp.server.repository"
	,mapperHelperRef = "dynamicMapperHelper",annotationClass = Mapper.class)
public class DappApplication extends SpringBootServletInitializer {
	
	
	
    public static void main(final String[] args) {
        SpringApplication.run(DappApplication.class);
	   
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	    
	    return builder.sources(DappApplication.class);
    }

    @Bean
    public CustomExceptionResolver customExceptionResolver() {
    	return new CustomExceptionResolver();
    }
}