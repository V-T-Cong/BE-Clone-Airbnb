package com.congvo.be_myapp.config;

import com.congvo.be_myapp.entity.Permission;
import com.congvo.be_myapp.entity.Role;
import com.congvo.be_myapp.repository.PermissionRepository;
import com.congvo.be_myapp.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public DataSeeder(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Create Permissions if they don't exist
        Permission readUser = createPermissionIfNotFound("READ_USER");
        Permission writeUser = createPermissionIfNotFound("WRITE_USER");
        Permission deleteUser = createPermissionIfNotFound("DELETE_USER");

        // 2. Create Roles and assign permissions
        // ROLE_USER gets read-only access
        Set<Permission> userPermissions = new HashSet<>();
        userPermissions.add(readUser);
        createRoleIfNotFound("ROLE_USER", userPermissions);

        // ROLE_ADMIN gets all permissions
        Set<Permission> adminPermissions = new HashSet<>();
        adminPermissions.add(readUser);
        adminPermissions.add(writeUser);
        adminPermissions.add(deleteUser);
        createRoleIfNotFound("ROLE_ADMIN", adminPermissions);
    }

    private Permission createPermissionIfNotFound(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName(name);
                    return permissionRepository.save(permission);
                });
    }

    private Role createRoleIfNotFound(String name, Set<Permission> permissions) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setPermissions(permissions);
                    return roleRepository.save(role);
                });
    }
}