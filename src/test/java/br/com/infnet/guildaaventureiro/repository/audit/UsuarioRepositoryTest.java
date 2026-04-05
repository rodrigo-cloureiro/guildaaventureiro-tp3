package br.com.infnet.guildaaventureiro.repository.audit;

import br.com.infnet.guildaaventureiro.domain.audit.Organizacao;
import br.com.infnet.guildaaventureiro.domain.audit.Usuario;
import br.com.infnet.guildaaventureiro.domain.audit.enums.UsuarioStatus;
import br.com.infnet.guildaaventureiro.repository.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Test
    public void findAllTest() {
        List<Usuario> all = usuarioRepository.findAll();
        all.forEach(u -> {
            System.out.println("Usuário: " + u.getNome() + " - Organização: " + u.getOrganizacao().getNome());
            u.getRoles().forEach(r -> System.out.println(r.getRole().getNome()));
        });
        assertFalse(all.isEmpty());
    }

    @Test
    public void findByOrganizacaoNomeTest() {
        List<Usuario> list = usuarioRepository.findByOrganizacaoNomeIgnoreCase("Guilda do Norte");
        list.forEach(u -> System.out.println(u.getNome()));
        assertFalse(list.isEmpty());
    }

    @Test
    public void createUsuarioWithOrganizacaoTest() {
        Organizacao org = organizacaoRepository.findById(2L).orElseThrow();
        Usuario usuario = new Usuario(
                org,
                "Audit Mister",
                "audit@guildasul.com",
                "hash_fake_4",
                UsuarioStatus.ATIVO
        );
        org.adicionarUsuario(usuario);
        usuarioRepository.save(usuario);

        Optional<Usuario> saved = usuarioRepository.findByEmailIgnoreCase(usuario.getEmail());
        assertTrue(saved.isPresent());
    }
}
