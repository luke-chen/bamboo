package com.luke.cms.config.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@ComponentScan(basePackages = {"com.luke.cms.controller", "com.luke.cms.model", "com.luke.cms.service"})
@MapperScan("com.luke.cms.persistence.mapper")
public class MvcConfig extends WebMvcConfigurerAdapter {
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("*.html", "/ui/*").addResourceLocations("/views/", "/ui/");
//        registry.addResourceHandler("*.html").addResourceLocations("/views/");
//        registry.addResourceHandler(pathPatterns)
    }
 
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
 
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        bean.setPrefix("/WEB-INF/views/");
        bean.setSuffix(".jsp");
        return bean;
    }
 
//    @Bean(name = "multipartResolver")
//    public CommonsMultipartResolver getMultipartResolver() {
//        return new CommonsMultipartResolver();
//    } 
//    @Bean(name = "messageSource")
//    public ReloadableResourceBundleMessageSource getMessageSource() {
//        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
//        resource.setBasename("classpath:messages");
//        resource.setDefaultEncoding("UTF-8");
//        return resource;
//    }
}