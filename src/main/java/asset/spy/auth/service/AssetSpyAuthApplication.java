package asset.spy.auth.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AssetSpyAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetSpyAuthApplication.class, args);
    }
}
