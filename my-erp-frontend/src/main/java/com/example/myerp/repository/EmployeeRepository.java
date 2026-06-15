// my-erp-frontend/src/main/java/com/example/myerp/repository/EmployeeRepository.java
package com.example.myerp.repository;

import com.example.myerp.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmNoAndEmPwd(String emNo, String emPwd);
    Employee findByEmNo(String emNo);
}
