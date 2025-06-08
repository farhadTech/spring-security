package com.security.notes.repository;

import com.security.notes.model.AppRole;
import com.security.notes.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(AppRole approle);
}

