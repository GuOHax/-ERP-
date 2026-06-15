// my-erp-frontend/src/main/java/com/example/myerp/repository/DepartmentRepository.java
package com.example.myerp.repository;

import com.example.myerp.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
