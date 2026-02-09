package com.java.test.testcontainer;

import org.junit.jupiter.api.Disabled;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

@Disabled
@Configuration
public class TestContainerConfiguration {

    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0.36")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

}
