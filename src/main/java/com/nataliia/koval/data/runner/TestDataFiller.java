package com.nataliia.koval.data.runner;

import com.nataliia.koval.data.entity.Data;
import com.nataliia.koval.data.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Order(1)
@RequiredArgsConstructor
@Component
public class TestDataFiller implements CommandLineRunner {
    private final DataRepository dataRepository;
    private static final int TOTAL_ROWS = 500;

    @Override
    @Transactional
    public void run(String... args) {
        if (dataRepository.count() > 0) {
            log.info("Data already exists in the database. Skipping data initialization.");
            return;
        }

        byte[] data = new byte[1024 * 1024];

        List<Data> batch = IntStream.range(0, TOTAL_ROWS)
                .mapToObj(number -> {
                    Data newData = new Data();
                    newData.setData(data);
                    newData.setModifyAt(LocalDateTime.now());
                    return newData;
                })
                .collect(Collectors.toList());

        try {
            dataRepository.saveAll(batch);
            log.info("Successfully inserted {} rows into the database.", batch.size());

        } catch (Exception e) {
            log.error("Error inserting data into the database: {}", e.getMessage());
            throw new RuntimeException("Error saving batch", e);
        }
    }
}
