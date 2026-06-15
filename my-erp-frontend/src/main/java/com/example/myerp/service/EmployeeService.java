// my-erp-frontend/src/main/java/com/example/myerp/service/EmployeeService.java
package com.example.myerp.service;

import com.example.myerp.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    Employee save(Employee employee);
    Employee update(Long id, Employee employee);
    boolean delete(Long id);
    Employee toggleStatus(Long id);
    Employee copy(Long id);
    List<Employee> findAll();
    Employee findById(Long id);
    Page<Employee> findByPage(String keyword, Pageable pageable);
}
