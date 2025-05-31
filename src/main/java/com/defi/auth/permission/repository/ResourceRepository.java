package com.defi.auth.permission.repository;

import com.defi.auth.permission.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Resource findByCode(String code);

    boolean existsByCode(String code);

    @Query(value = """
                SELECT
                    r.id,
                    r.created_at,
                    r.updated_at,
                    r.code,
                    r.name,
                    r.metadata,
                    COALESCE(string_agg(rha.action_id::text, ',' ORDER BY rha.action_id), '') as action_ids
                FROM resources r
                LEFT JOIN resource_has_action rha ON r.id = rha.resource_id
                GROUP BY r.id, r.created_at, r.updated_at, r.code, r.name, r.metadata
                ORDER BY r.id
            """, nativeQuery = true)
    List<Object[]> findAllResourcesWithActions();
}
