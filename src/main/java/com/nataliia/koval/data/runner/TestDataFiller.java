package com.nataliia.koval.data.runner;

import com.nataliia.koval.data.entity.Data;
import com.nataliia.koval.data.repository.DataRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Slf4j
@Order(1)
@RequiredArgsConstructor
@Component
public class TestDataFiller implements CommandLineRunner {
    private final DataRepository dataRepository;
    private static final int TOTAL_ROWS = 500;
    private static final int DATA_SIZE = 1000000;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void run(String... args) {
        if (dataRepository.count() > 0) {
            log.info("Data already exists in the database. Skipping data initialization.");
            return;
        }

        byte[] data = new byte[DATA_SIZE];
        LocalDateTime currentDateTime = LocalDateTime.now();

        IntStream.range(0, TOTAL_ROWS)
                .forEach(index -> {
                    Data newData = new Data();
                    newData.setData(data);
                    newData.setModifiedAt(currentDateTime);
                    entityManager.persist(newData);

                    if (index > 0 && index % batchSize == 0) {
                        entityManager.flush();
                        entityManager.clear();
                    }
                });

        entityManager.flush();
        entityManager.clear();

        log.info("Successfully inserted {} rows into the database.", TOTAL_ROWS);
    }
}
