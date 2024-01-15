package com.store;

import com.intuit.karate.junit5.Karate;
import com.store.model.DB;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class WorkflowTest {
    private static ConfigurableApplicationContext context;

    @BeforeAll
    public static void setUp() throws InterruptedException {
        DB.INSTANCE.resetDB();
        context = SpringApplication.run(Application.class);
    }

    @Karate.Test
    Karate runTests() {
        return new Karate().path("classpath:com/store/workflowTest.feature");
    }

    @AfterAll
    public static void tearDown() {
        if(context != null) {
            SpringApplication.exit(context, () -> 0);
            context.close();
        }
    }
}
