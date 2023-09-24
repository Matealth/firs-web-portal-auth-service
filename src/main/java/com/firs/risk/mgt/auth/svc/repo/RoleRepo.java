package com.firs.risk.mgt.auth.svc.repo;


import com.firs.risk.mgt.auth.svc.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID> {
    Role findRoleByName(String name);
}
