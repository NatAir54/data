package com.nataliia.koval.data.service.impl;

import com.nataliia.koval.data.entity.Data;
import com.nataliia.koval.data.repository.DataRepository;
import com.nataliia.koval.data.service.DataService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultDataService implements DataService {
    private final EntityManager entityManager;
    private final DataRepository dataRepository;
    private static final int PAGE_SIZE = 50;

//
//    @Override
//    @Transactional
//    public void process() {
//        LocalDateTime currentDateTime = LocalDateTime.now();
//
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaUpdate<Data> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Data.class);
//        Root<Data> root = criteriaUpdate.from(Data.class);
//
//        criteriaUpdate.set(root.get("modifiedAt"), currentDateTime);
//
//        int updatedEntities = entityManager.createQuery(criteriaUpdate)
//                .executeUpdate();
//
//        log.info("Updated {} entities.", updatedEntities);
//        log.info("Data processing completed.");
//    }

//    @Override
//    @Transactional
//    public void process() {
//        int pageNumber = 0;
//        Page<Data> page;
//
//        while (true) {
////            List<Data> dataPage = fetchPage(pageNumber);
//            page = dataRepository.findAll(
//                    PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "id"))
//            );
//
//            if (dataPage.isEmpty()) {
//                break;
//            }
//
//            LocalDateTime currentDateTime = LocalDateTime.now();
//            updateDataPage(dataPage, currentDateTime);
//
//            pageNumber++;
//
//            entityManager.flush();
//            entityManager.clear();
//
//            log.info("Data processing completed.");
//        }
//    }

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

            List<Data> listData = new ArrayList<>(page.getContent());

            modifyData(listData);

            log.debug("Saved {} rows for page {}", page.getContent().size(), page.getNumber());

            pageNumber++;

        } while (page.hasNext());

        log.info("Data processing completed.");
    }

    private void modifyData(List<Data> listData) {
        LocalDateTime localDateTime = LocalDateTime.now();
        listData.forEach(data -> data.setModifiedAt(localDateTime));
        dataRepository.saveAll(listData);
        entityManager.flush();
        entityManager.clear();
    }

//    private List<Data> fetchPage(int pageNumber) {
//        TypedQuery<Data> query = entityManager.createQuery(
//                "SELECT d FROM Data d ORDER BY d.id ASC", Data.class
//        );
//        query.setFirstResult(pageNumber * PAGE_SIZE);
//        query.setMaxResults(DefaultDataService.PAGE_SIZE);
//
//        return query.getResultList(); // OutOfMemory error here
//    }
//
//
//    private void updateDataPage(List<Data> dataPage, LocalDateTime dateTime) {
//        dataPage.forEach(data -> data.setModifiedAt(dateTime));
//
//        entityManager.flush();
//        entityManager.clear();
//    }
}
