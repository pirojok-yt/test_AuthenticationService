package main;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TestServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(TestServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}