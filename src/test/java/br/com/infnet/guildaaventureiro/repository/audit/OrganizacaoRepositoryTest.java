package br.com.infnet.guildaaventureiro.repository.audit;

import br.com.infnet.guildaaventureiro.domain.audit.Organizacao;
import br.com.infnet.guildaaventureiro.repository.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class OrganizacaoRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Test
    public void findAllTest() {
        List<Organizacao> all = organizacaoRepository.findAll();
        all.forEach(o -> {
            System.out.println("Organização: " + o.getNome());
            System.out.println("Roles da organização:");
            o.getRoles().forEach(r -> System.out.println("\t - " + r.getNome()));
            System.out.println("Usuários da organização:");
            o.getUsuarios().forEach(u -> System.out.println("\t - " + u.getNome()));
        });
        assertFalse(all.isEmpty());
    }
}
