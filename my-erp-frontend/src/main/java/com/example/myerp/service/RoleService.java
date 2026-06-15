// my-erp-frontend/src/main/java/com/example/myerp/service/RoleService.java
package com.example.myerp.service;

import com.example.myerp.entity.Role;
import java.util.List;

public interface RoleService {
    Role save(Role role);
    Role update(Long id, Role role);
    boolean delete(Long id);
    Role findById(Long id);
    List<Role> findAll();
}
