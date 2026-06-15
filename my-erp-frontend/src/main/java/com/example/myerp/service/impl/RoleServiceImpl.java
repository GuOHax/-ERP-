// my-erp-frontend/src/main/java/com/example/myerp/service/impl/RoleServiceImpl.java
package com.example.myerp.service.impl;

import com.example.myerp.entity.Role;
import com.example.myerp.repository.RoleRepository;
import com.example.myerp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        role.setCreateTime(LocalDateTime.now());
        if (role.getRoleSort() == null) role.setRoleSort(0);
        if (role.getRoleStatus() == null) role.setRoleStatus(1);
        return roleRepository.save(role);
    }

    @Override
    public Role update(Long id, Role role) {
        Role existing = roleRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setRoleName(role.getRoleName());
        existing.setRoleKey(role.getRoleKey());
        existing.setRoleSort(role.getRoleSort());
        existing.setRoleStatus(role.getRoleStatus());
        existing.setRoleRemark(role.getRoleRemark());
        return roleRepository.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (!roleRepository.existsById(id)) return false;
        roleRepository.deleteById(id);
        return true;
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
