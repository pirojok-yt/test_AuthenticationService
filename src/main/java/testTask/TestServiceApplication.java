package testTask;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableGlobalAuthentication

public class TestServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(TestServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}