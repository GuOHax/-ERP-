// my-erp-frontend/src/main/java/com/example/myerp/service/impl/EmployeeServiceImpl.java
package com.example.myerp.service.impl;

import com.example.myerp.entity.Employee;
import com.example.myerp.repository.EmployeeRepository;
import com.example.myerp.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee save(Employee employee) {
        employee.setEmCreateTime(LocalDateTime.now());
        if (employee.getEmStatus() == null) {
            employee.setEmStatus(1);
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Long id, Employee employee) {
        Employee existing = employeeRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setEmName(employee.getEmName());
        existing.setEmPwd(employee.getEmPwd());
        existing.setEmTel(employee.getEmTel());
        existing.setEmEmail(employee.getEmEmail());
        existing.setEmIcon(employee.getEmIcon());
        existing.setEmRoleId(employee.getEmRoleId());
        existing.setEmRemark(employee.getEmRemark());
        existing.setEmSex(employee.getEmSex());
        existing.setEmStoreId(employee.getEmStoreId());
        return employeeRepository.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            return false;
        }
        employeeRepository.deleteById(id);
        return true;
    }

    @Override
    public Employee toggleStatus(Long id) {
        Employee existing = employeeRepository.findById(id).orElse(null);
        if (existing == null) return null;
        Integer status = existing.getEmStatus();
        existing.setEmStatus(status != null && status == 1 ? 0 : 1);
        return employeeRepository.save(existing);
    }

    @Override
    public Employee copy(Long id) {
        Employee existing = employeeRepository.findById(id).orElse(null);
        if (existing == null) return null;
        Employee copy = new Employee();
        BeanUtils.copyProperties(existing, copy, "emId", "emNo");
        copy.setEmNo(existing.getEmNo() + "_copy");
        copy.setEmCreateTime(LocalDateTime.now());
        return employeeRepository.save(copy);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Employee> findByPage(String keyword, Pageable pageable) {
        Employee probe = new Employee();
        probe.setEmNo(keyword);
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("emNo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("emName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("emTel", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Employee> ex = Example.of(probe, matcher);
        return employeeRepository.findAll(ex, pageable);
    }
}
