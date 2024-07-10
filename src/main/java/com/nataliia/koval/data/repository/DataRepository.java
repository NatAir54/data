package com.nataliia.koval.data.repository;

import com.nataliia.koval.data.entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<Data, Integer> {
}
