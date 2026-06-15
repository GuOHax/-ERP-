// my-erp-frontend/src/main/java/com/example/myerp/service/DepartmentService.java
package com.example.myerp.service;

import com.example.myerp.entity.Department;

import java.util.List;

public interface DepartmentService {
    Department save(Department department);
    Department update(Long id, Department department);
    boolean delete(Long id);
    Department findById(Long id);
    List<Department> findAll();
    Department toggleShow(Long id);
    Department addChild(Long parentId, Department child);
}
