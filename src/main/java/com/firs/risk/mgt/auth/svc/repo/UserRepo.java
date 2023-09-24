package com.firs.risk.mgt.auth.svc.repo;

import com.firs.risk.mgt.auth.svc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    User findByPhone(String phone);
}




