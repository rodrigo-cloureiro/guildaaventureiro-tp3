package br.com.infnet.guildaaventureiro.repository.audit;

import br.com.infnet.guildaaventureiro.domain.audit.Role;
import br.com.infnet.guildaaventureiro.repository.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RoleRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void findAllTest() {
        List<Role> all = roleRepository.findAll();
        all.forEach(r -> {
            System.out.println("Role: " + r.getNome());
            r.getPermissions().forEach(p -> System.out.println("\t- " + p.getCode()));
        });
        assertFalse(all.isEmpty());
    }

    @Test
    public void findByOrganizacaoNomeTest() {
        List<Role> roles = roleRepository.findByOrganizacaoNome("Guilda do Norte");
        roles.forEach(r -> {
            System.out.println(r.getNome());
            r.getPermissions().forEach(p -> System.out.println("\t- " + p.getCode()));
        });
        assertFalse(roles.isEmpty());
    }
}
