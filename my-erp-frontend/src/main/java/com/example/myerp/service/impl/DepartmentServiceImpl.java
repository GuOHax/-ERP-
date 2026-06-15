// my-erp-frontend/src/main/java/com/example/myerp/service/impl/DepartmentServiceImpl.java
package com.example.myerp.service.impl;

import com.example.myerp.entity.Department;
import com.example.myerp.repository.DepartmentRepository;
import com.example.myerp.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department save(Department department) {
        department.setCreateTime(LocalDateTime.now());
        if (department.getEmpCount() == null) {
            department.setEmpCount(0);
        }
        return departmentRepository.save(department);
    }

    @Override
    public Department update(Long id, Department department) {
        Department existing = departmentRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setDeptName(department.getDeptName());
        existing.setEmpCount(department.getEmpCount());
        existing.setDeptOrder(department.getDeptOrder());
        existing.setIsShow(department.getIsShow());
        return departmentRepository.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (!departmentRepository.existsById(id)) return false;
        departmentRepository.deleteById(id);
        return true;
    }

    @Override
    public Department findById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department toggleShow(Long id) {
        Department existing = departmentRepository.findById(id).orElse(null);
        if (existing == null) return null;
        Boolean show = existing.getIsShow();
        existing.setIsShow(show == null ? true : !show);
        return departmentRepository.save(existing);
    }

    @Override
    public Department addChild(Long parentId, Department child) {
        child.setParentID(parentId != null ? parentId.intValue() : null);
        child.setCreateTime(LocalDateTime.now());
        if (child.getEmpCount() == null) {
            child.setEmpCount(0);
        }
        return departmentRepository.save(child);
    }

    
}
