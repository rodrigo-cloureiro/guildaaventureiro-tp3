package br.com.infnet.guildaaventureiro.repository.operacoes;

import br.com.infnet.guildaaventureiro.domain.audit.Usuario;
import br.com.infnet.guildaaventureiro.domain.aventura.Aventureiro;
import br.com.infnet.guildaaventureiro.domain.aventura.Missao;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.AventureiroClasse;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.NivelPerigoMissao;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.PapelMissao;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.StatusMissao;
import br.com.infnet.guildaaventureiro.dto.missao.MissaoResponse;
import br.com.infnet.guildaaventureiro.dto.missao.enums.TipoDataMissao;
import br.com.infnet.guildaaventureiro.repository.audit.UsuarioRepository;
import br.com.infnet.guildaaventureiro.repository.aventura.AventureiroRepository;
import br.com.infnet.guildaaventureiro.repository.aventura.MissaoRepository;
import br.com.infnet.guildaaventureiro.repository.aventura.ParticipacaoMissaoRepository;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MissaoRepositoryTest {
    @Autowired
    private MissaoRepository missaoRepository;
    @Autowired
    private AventureiroRepository aventureiroRepository;
    @Autowired
    private ParticipacaoMissaoRepository participacaoMissaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;
    private Aventureiro aventureiro;
    private Missao missao;
    private final Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    public void setUp() {
        this.usuario = usuarioRepository.findById(1L).orElseThrow();
        this.aventureiro = new Aventureiro("Rodrigo", AventureiroClasse.GUERREIRO, 100);
        this.usuario.adicionarAventureiro(this.aventureiro);
        this.usuario.getOrganizacao().adicionarAventureiro(this.aventureiro);
        this.missao = new Missao(this.usuario.getOrganizacao(), "Testes unitários", NivelPerigoMissao.ALTO);
    }

    @AfterEach
    public void tearDown() {
        this.usuario = null;
        this.aventureiro = null;
        this.missao = null;
    }

    @Test
    @DisplayName("Deve criar missão")
    public void createMissaoTest() {
        aventureiroRepository.save(aventureiro);

        Missao missao = new Missao(
                usuario.getOrganizacao(),
                "Finalizar o TP2",
                NivelPerigoMissao.EXTREMO
        );
        missao.adicionarParticipante(aventureiro, PapelMissao.SUPORTE);
        missaoRepository.save(missao);

        assertFalse(missaoRepository.findAll().isEmpty());
        assertFalse(participacaoMissaoRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Deve listar missões sem filtros")
    public void shouldListMissionsWhenNoFilters() {
        missaoRepository.save(missao);

        Page<MissaoResponse> resultado = missaoRepository.findMissoesByFilters(
                null,
                null,
                null,
                null,
                null,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve listar missões filtrando por status")
    public void shouldListMissionsWhenFilteringByStatus() {
        missaoRepository.save(missao);

        Page<MissaoResponse> resultado = missaoRepository.findMissoesByFilters(
                StatusMissao.PLANEJADA,
                null,
                null,
                null,
                null,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve listar missões filtrando por nivel de perigo")
    public void shouldListMissionsWhenFilteringByDangerLevel() {
        missaoRepository.save(missao);

        Page<MissaoResponse> resultado = missaoRepository.findMissoesByFilters(
                null,
                NivelPerigoMissao.EXTREMO,
                null,
                null,
                null,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve listar missões filtrando por intervalo nas datas")
    public void shouldListMissionsWhenFilteringByDateRange() {
        missaoRepository.save(missao);

        Page<MissaoResponse> resultado = missaoRepository.findMissoesByFilters(
                null,
                null,
                TipoDataMissao.CRIACAO.name(),
                LocalDateTime.of(2026, 3, 17, 0, 0, 0),
                LocalDateTime.of(2026, 3, 17, 23, 59, 59),
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve listar missões filtrando por status e nivel de perigo")
    public void shouldListMissionsWhenFilteringByStatusAndDangerLevel() {
        missaoRepository.save(missao);

        Page<MissaoResponse> resultado = missaoRepository.findMissoesByFilters(
                StatusMissao.PLANEJADA,
                NivelPerigoMissao.ALTO,
                null,
                null,
                null,
                pageable
        );
        assertFalse(resultado.getContent().isEmpty());
    }

    @Test
    @DisplayName("Não deve retornar missões fora do período")
    public void shouldNotReturnMissionsWhenOutsideDateRange() {
        missaoRepository.save(missao);

        Page<MissaoResponse> resultado = missaoRepository.findMissoesByFilters(
                null,
                null,
                TipoDataMissao.CRIACAO.name(),
                LocalDateTime.now().minusMinutes(2),
                LocalDateTime.now(),
                pageable
        );
        assertTrue(resultado.getContent().isEmpty());
    }
}
