package com.defi.auth.user.repository;

import com.defi.auth.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.userName = :value OR u.phone = :value OR u.email = :value")
    Optional<User> findByUserNameOrPhoneOrEmail(@Param("value") String value);
}
