package com.nataliia.koval.data.service.impl;

import com.nataliia.koval.data.entity.Data;
import com.nataliia.koval.data.repository.DataRepository;
import com.nataliia.koval.data.service.DataService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultDataService implements DataService {
    private final DataRepository dataRepository;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public void process() {
        Page<Data> page;
        int pageNumber = 0;

        do {
            page = fetchDataPage(pageNumber, batchSize);

            log.info("Processing page {} of size {}", page.getNumber(), page.getSize());

            List<Data> dataList = page.getContent();
            LocalDateTime currentDateTime = LocalDateTime.now();

            modifyData(dataList, currentDateTime);

            pageNumber++;

        } while (page.hasNext());

        log.info("Data processing completed.");
    }

    private Page<Data> fetchDataPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        return dataRepository.findAll(pageable);
    }

    private void modifyData(List<Data> dataList, LocalDateTime dateTime) {
        dataList.forEach(data -> data.setModifiedAt(dateTime));
        entityManager.flush();
        entityManager.clear();
    }
}
