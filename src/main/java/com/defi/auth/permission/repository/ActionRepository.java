package com.defi.auth.permission.repository;

import com.defi.auth.permission.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
    Action findByCode(String code);
    boolean existsByCode(String code);
}
