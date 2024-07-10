package com.nataliia.koval.data.runner;

import com.nataliia.koval.data.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(2)
@RequiredArgsConstructor
@Component
public class DataProcessingRunner implements CommandLineRunner {
    private final DataService dataService;

    @Override
    public void run(String... args) {
        try {
            dataService.process();
            log.info("Data processing initiated on application startup.");
        } catch (Exception e) {
            log.error("Error initiating data processing on startup: {}", e.getMessage());
        }
    }
}
