package com.example.app.logging.config;

import com.example.app.logging.interceptor.AccessLogInterceptor;
import com.example.app.logging.property.LogProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties({LogProperty.class})
@RequiredArgsConstructor
public class LoggingMvcConfig implements WebMvcConfigurer {
    private final AccessLogInterceptor interceptor;
    private final LogProperty logProperty;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (logProperty.enabled() && logProperty.accessLogEnabled()) {
            registry.addInterceptor(interceptor).addPathPatterns("/**");
        }
    }
}
