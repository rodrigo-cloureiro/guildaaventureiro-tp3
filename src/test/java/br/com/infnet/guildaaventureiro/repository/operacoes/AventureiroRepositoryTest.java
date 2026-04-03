package br.com.infnet.guildaaventureiro.repository.operacoes;

import br.com.infnet.guildaaventureiro.domain.audit.Organizacao;
import br.com.infnet.guildaaventureiro.domain.audit.Usuario;
import br.com.infnet.guildaaventureiro.domain.aventura.Aventureiro;
import br.com.infnet.guildaaventureiro.domain.aventura.Companheiro;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.AventureiroClasse;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.CompanheiroEspecie;
import br.com.infnet.guildaaventureiro.dto.aventureiro.AventureiroMinimalResponse;
import br.com.infnet.guildaaventureiro.dto.aventureiro.AventureiroResponse;
import br.com.infnet.guildaaventureiro.repository.audit.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AventureiroRepositoryTest {
    @Autowired
    private AventureiroRepository aventureiroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private Organizacao organizacao;
    private Usuario usuario;
    private Aventureiro aventureiro;
    private final Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    public void setUp() {
        // id fixo considerando que o teste rodará com um banco de teste populado
        this.usuario = usuarioRepository.findById(1L).orElseThrow();

        this.aventureiro = new Aventureiro("Rick Forjafogo", AventureiroClasse.ARQUEIRO, 100);

        this.usuario.adicionarAventureiro(this.aventureiro);
        this.usuario.getOrganizacao().adicionarAventureiro(this.aventureiro);
    }

    @AfterEach
    public void tearDown() {
        if (this.aventureiro != null) {
            this.aventureiro = null;
        }
        if (this.organizacao != null) {
            this.organizacao = null;
        }
        if (this.usuario != null) {
            this.usuario = null;
        }
    }

    @Test
    public void createAventureiroTest() {
        aventureiroRepository.save(aventureiro);

        Aventureiro aventureiroDb = aventureiroRepository
                .findById(aventureiro.getId())
                .orElseThrow();

        assertEquals("Rick Forjafogo", aventureiroDb.getNome());
        assertEquals(AventureiroClasse.ARQUEIRO, aventureiroDb.getClasse());
        assertEquals(100, aventureiroDb.getNivel());
        assertTrue(aventureiroDb.isAtivo());
        assertEquals(this.usuario.getId(), aventureiroDb.getUsuario().getId());
        assertNotNull(aventureiroDb.getOrganizacao());
    }

    @Test
    public void setCompanheiroDoAventureiroTest() {
        Companheiro companheiro = new Companheiro(
                "Fenrir",
                CompanheiroEspecie.LOBO,
                100
        );

        aventureiro.definirCompanheiro(companheiro);
        aventureiroRepository.save(aventureiro);

        Aventureiro aventureiroDb = aventureiroRepository
                .findById(aventureiro.getId())
                .orElseThrow();

        assertNotNull(aventureiroDb.getCompanheiro());
        assertEquals("Fenrir", aventureiroDb.getCompanheiro().getNome());
    }

    @Test
    @DisplayName("Deve listar aventureiros sem filtros")
    public void shouldReturnAllAdventurersWhenNoFiltersAreApplied() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroResponse> resultado = aventureiroRepository.findByStatusAndClasseAndNivelMinimo(
                null,
                null,
                null,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve listar aventureiros filtrando por status")
    public void shouldFilterByStatus() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroResponse> resultado = aventureiroRepository.findByStatusAndClasseAndNivelMinimo(
                true,
                null,
                null,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
        resultado.getContent().forEach(a -> assertTrue(a.ativo()));
    }

    @Test
    @DisplayName("Deve listar aventureiros filtrando por classe")
    public void shouldFilterByClass() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroResponse> resultado = aventureiroRepository.findByStatusAndClasseAndNivelMinimo(
                null,
                AventureiroClasse.ARQUEIRO,
                null,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
        resultado.getContent().forEach(a -> {
            assertEquals(AventureiroClasse.ARQUEIRO, a.classe());
        });
    }

    @Test
    @DisplayName("Deve listar aventureiros filtrando por nivel minimo")
    public void shouldFilterByMinimumLevel() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroResponse> resultado = aventureiroRepository.findByStatusAndClasseAndNivelMinimo(
                null,
                null,
                100,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
        resultado.getContent().forEach(a -> {
            assertTrue(a.nivel() >= 100);
        });
    }

    @Test
    @DisplayName("Deve listar aventureiros filtrando por status e classe")
    public void shouldFilterByStatusAndClass() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroResponse> resultado = aventureiroRepository.findByStatusAndClasseAndNivelMinimo(
                true,
                AventureiroClasse.ARQUEIRO,
                null,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
        resultado.getContent().forEach(a -> {
            assertTrue(a.ativo());
            assertEquals(AventureiroClasse.ARQUEIRO, a.classe());
        });
    }

    @Test
    @DisplayName("Deve listar aventureiros filtrando por status e nivel minimo")
    public void shouldFilterByStatusAndMinimumLevel() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroResponse> resultado = aventureiroRepository.findByStatusAndClasseAndNivelMinimo(
                true,
                null,
                100,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
        resultado.getContent().forEach(a -> {
            assertTrue(a.ativo());
            assertTrue(a.nivel() >= 100);
        });
    }

    @Test
    @DisplayName("Deve listar aventureiros filtrando por classe e nivel minimo")
    public void shouldFilterByClassAndMinimumLevel() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroResponse> resultado = aventureiroRepository.findByStatusAndClasseAndNivelMinimo(
                null,
                AventureiroClasse.ARQUEIRO,
                100,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
        resultado.getContent().forEach(a -> {
            assertEquals(AventureiroClasse.ARQUEIRO, a.classe());
            assertTrue(a.nivel() >= 100);
        });
    }

    @Test
    @DisplayName("Deve listar aventureiros filtrando por status, classe e nivel minimo")
    public void shouldFilterByStatusClassAndMinimumLevel() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroResponse> resultado = aventureiroRepository.findByStatusAndClasseAndNivelMinimo(
                true,
                AventureiroClasse.ARQUEIRO,
                100,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
        resultado.getContent().forEach(a -> {
            assertTrue(a.ativo());
            assertEquals(AventureiroClasse.ARQUEIRO, a.classe());
            assertTrue(a.nivel() >= 100);
        });
    }

    @Test
    @DisplayName("Não deve retornar aventureiros que não se enquadram nos filtros")
    public void shouldReturnEmptyWhenNoAdventurersMatchFilters() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroResponse> resultado = aventureiroRepository.findByStatusAndClasseAndNivelMinimo(
                false,
                AventureiroClasse.MAGO,
                101,
                pageable
        );
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve buscar e retornar o aventureiro com base no nome completo")
    public void shouldFindByFullName() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroMinimalResponse> resultado = aventureiroRepository.findByNomeContainingIgnoreCase(
                "Rick Forjafogo",
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve buscar e retornar o aventureiro com base no nome parcial")
    public void shouldFindByPartialName() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroMinimalResponse> resultado = aventureiroRepository.findByNomeContainingIgnoreCase(
                "Ric",
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
    }

    @Test
    @DisplayName("Não deve retornar aventureiro(s) quando nome não corresponder")
    public void shouldReturnEmptyWhenNameDoesNotMatch() {
        aventureiroRepository.save(aventureiro);

        Page<AventureiroMinimalResponse> resultado = aventureiroRepository.findByNomeContainingIgnoreCase(
                "CRVG",
                pageable
        );
        assertTrue(resultado.getContent().isEmpty());
    }
}
