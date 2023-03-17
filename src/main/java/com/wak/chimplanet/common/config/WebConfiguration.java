package com.wak.chimplanet.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
            .allowCredentials(false).maxAge(3600);
    }


    /**
     * 이미지 파일 리소스 핸들러
     * 이미지 파일 요청 위치 ex) http://localhost:/images/${file_name}
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
            // .addResourceLocations("file:///home/wak/images/"); // 운영계 위치
            .addResourceLocations("file:///D:/TemporaryFiles/"); // 로컬테스트 위치
    }
}
