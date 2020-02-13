//package com.aution.dapp.server.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.ViewResolver;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//
//@Configuration
//public class MyInterceptorConfig extends WebMvcConfigurationSupport {
//
//    @Bean
//    public MyInterceptor myInterceptor() {
//        return new MyInterceptor();
//    }
//
//    @Bean
//    public ViewResolver viewResolver(){
//
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setCache(true);
//        return resolver;
//    }
//
//
//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(myInterceptor()).addPathPatterns("/api/goods/create","/api/order/bid");
//        super.addInterceptors(registry);
//    }
//}
