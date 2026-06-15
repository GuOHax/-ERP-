// my-erp-frontend/src/main/java/com/example/myerp/service/impl/AuthServiceImpl.java
package com.example.myerp.service.impl;

import com.example.myerp.entity.Employee;
import com.example.myerp.repository.EmployeeRepository;
import com.example.myerp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee authenticate(String emNo, String emPwd) {
        return employeeRepository.findByEmNoAndEmPwd(emNo, emPwd);
    }

    @Override
    public Employee register(Employee employee) {
        Employee exists = employeeRepository.findByEmNo(employee.getEmNo());
        if (exists != null ) {
            return null;
        }
        employee.setEmStatus(1);
        return employeeRepository.save(employee);
    }
}
