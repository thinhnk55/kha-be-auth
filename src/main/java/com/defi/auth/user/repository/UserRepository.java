package com.defi.auth.user.repository;

import com.defi.auth.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u ORDER BY u.id DESC")
    List<User> listUsers(Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(u.userName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY u.id DESC")
    List<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.userName = :value OR u.phone = :value OR u.email = :value")
    Optional<User> findByUserNameOrPhoneOrEmail(@Param("value") String value);
}
