// my-erp-frontend/src/main/java/com/example/myerp/repository/RoleRepository.java
package com.example.myerp.repository;

import com.example.myerp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
