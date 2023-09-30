package com.firs.risk.mgt.auth.svc.repo;

import com.firs.risk.mgt.auth.svc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPhone(String phone);


    @Query(
            value = "SELECT * FROM users u ",
            countQuery = "SELECT count(*) FROM users",
            nativeQuery = true)
    List<User> findAllUsers();
}




