package com.nataliia.koval.data.service.impl;

import com.nataliia.koval.data.entity.Data;
import com.nataliia.koval.data.repository.DataRepository;
import com.nataliia.koval.data.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultDataService implements DataService {
    private final DataRepository dataRepository;
    private static final int PAGE_SIZE = 50;

    @Override
    @Transactional
    public void process() {
        AtomicInteger processedRows = new AtomicInteger(0);

        Page<Data> page = dataRepository.findAll(PageRequest.of(0, PAGE_SIZE));

        while (page.hasContent()) {
            log.info("Processing page {} of size {}", page.getNumber(), page.getSize());

            page.getContent().forEach(data -> {
                data.setModifyAt(LocalDateTime.now());
                processedRows.incrementAndGet();
            });

            dataRepository.saveAll(page.getContent());
            log.debug("Saved {} rows for page {}", page.getContent().size(), page.getNumber());

            if (processedRows.get() >= 300) {
                throw new RuntimeException("Simulated exception after processing 300 rows");
            }

            page = dataRepository.findAll(page.nextPageable());
        }

        log.info("Data processing completed. Total processed rows: {}", processedRows);
    }
}
