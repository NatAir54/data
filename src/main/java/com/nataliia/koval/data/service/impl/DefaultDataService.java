package com.nataliia.koval.data.service.impl;

import com.nataliia.koval.data.entity.Data;
import com.nataliia.koval.data.repository.DataRepository;
import com.nataliia.koval.data.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private static final int PAGE_SIZE = 50;

    @Override
    @Transactional
    public void process() {
        Page<Data> page;
        int pageNumber = 0;

        do {
            page = dataRepository.findAll(
                    PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "id"))
            );
            log.info("Processing page {} of size {}", page.getNumber(), page.getSize());

            List<Data> dataList = page.getContent().stream()
                    .peek(data -> data.setModifyAt(LocalDateTime.now()))
                    .toList();

            dataRepository.saveAll(dataList);
            log.debug("Saved {} rows for page {}", page.getContent().size(), page.getNumber());

            pageNumber++;

        } while (page.hasNext());

        log.info("Data processing completed.");
    }
}
