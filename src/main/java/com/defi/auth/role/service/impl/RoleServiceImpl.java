package com.defi.auth.role.service.impl;

import com.defi.auth.role.entity.Role;
import com.defi.auth.role.repository.RoleRepository;
import com.defi.auth.role.service.RoleService;
import com.defi.common.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role updateRole(Long id, Role update) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonMessage.NOT_FOUND));
        role.setName(update.getName());
        role.setCode(update.getCode());
        role.setMetadata(update.getMetadata());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
