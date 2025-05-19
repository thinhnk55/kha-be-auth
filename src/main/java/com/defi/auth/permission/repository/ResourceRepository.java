package com.defi.auth.permission.repository;

import com.defi.auth.permission.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Resource findByCode(String code);

    boolean existsByCode(String code);
}
