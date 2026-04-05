package br.com.infnet.guildaaventureiro.repository.audit;

import br.com.infnet.guildaaventureiro.domain.audit.Permission;
import br.com.infnet.guildaaventureiro.repository.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PermissionRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    public void findAllTest() {
        List<Permission> all = permissionRepository.findAll();
        all.forEach(r -> System.out.println(r.getCode()));
        assertFalse(all.isEmpty());
    }

    @Test
    public void findByRoleCodeTest() {
        List<Permission> permissions = permissionRepository.findByRolesNome("ADMIN");
        permissions.forEach(p -> System.out.println(p.getCode()));
        assertFalse(permissions.isEmpty());
    }
}
