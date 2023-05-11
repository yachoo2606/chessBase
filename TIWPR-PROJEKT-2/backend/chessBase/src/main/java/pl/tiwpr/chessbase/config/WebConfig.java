package pl.tiwpr.chessbase.config;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }
}