package io.rp.job.offer.viewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class JobOfferScrapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOfferScrapperApplication.class, args);
    }

}

