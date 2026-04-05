package br.com.infnet.guildaaventureiro.repository.operacoes;

import br.com.infnet.guildaaventureiro.repository.BaseRepositoryTest;
import br.com.infnet.guildaaventureiro.domain.audit.Organizacao;
import br.com.infnet.guildaaventureiro.domain.audit.Usuario;
import br.com.infnet.guildaaventureiro.domain.audit.enums.UsuarioStatus;
import br.com.infnet.guildaaventureiro.domain.operacoes.Aventureiro;
import br.com.infnet.guildaaventureiro.domain.operacoes.Missao;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.AventureiroClasse;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.NivelPerigoMissao;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.PapelMissao;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.StatusMissao;
import br.com.infnet.guildaaventureiro.dto.aventureiro.AventureiroMissaoResponse;
import br.com.infnet.guildaaventureiro.dto.relatorio.RankingParticipacao;

import br.com.infnet.guildaaventureiro.dto.relatorio.RelatorioMissao;
import br.com.infnet.guildaaventureiro.repository.audit.OrganizacaoRepository;
import br.com.infnet.guildaaventureiro.repository.audit.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipacaoMissaoRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private ParticipacaoMissaoRepository participacaoMissaoRepository;
    @Autowired
    private MissaoRepository missaoRepository;
    @Autowired
    private AventureiroRepository aventureiroRepository;
    @Autowired
    private OrganizacaoRepository organizacaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private Aventureiro aventureiro;
    private Organizacao organizacao;
    private Usuario usuario;
    private Missao missao;
    private Missao missaoSemParticipante;

    @BeforeEach
    public void setUp() {
        this.aventureiro = new Aventureiro("RD", AventureiroClasse.GUERREIRO, 26);
        this.organizacao = new Organizacao("Instituto Infnet");
        this.organizacao.adicionarAventureiro(this.aventureiro);
        this.usuario = new Usuario(
                this.organizacao,
                "RCL",
                "rcl@email.com",
                "1@b#",
                UsuarioStatus.ATIVO
        );
        this.usuario.adicionarAventureiro(this.aventureiro);
        this.missao = new Missao(organizacao, "DR1 TP3", NivelPerigoMissao.ALTO);
        this.missaoSemParticipante = new Missao(organizacao, "DR1 AT", NivelPerigoMissao.EXTREMO);

        organizacaoRepository.save(this.organizacao);
        usuarioRepository.save(this.usuario);
        aventureiroRepository.save(this.aventureiro);
        missaoRepository.save(this.missao);
        missaoRepository.save(this.missaoSemParticipante);

        this.missao.adicionarParticipante(this.aventureiro, PapelMissao.LIDER);
        this.missao.iniciarMissao();
    }

    @AfterEach
    public void tearDown() {
        if (this.aventureiro != null) this.aventureiro = null;
        if (this.organizacao != null) this.organizacao = null;
        if (this.usuario != null) this.usuario = null;
        if (this.missao != null) this.missao = null;
    }

    @Test
    @DisplayName("Deve retornar ranking quando há participações no período")
    public void shouldReturnRankingWhenThereAreParticipationsInThePeriod() {
        List<RankingParticipacao> resultado = participacaoMissaoRepository.rankingParticipacao(
                null,
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now()
        );
        assertFalse(resultado.isEmpty());

        RankingParticipacao ranking = resultado.getFirst();
        assertTrue(ranking.participacoes() > 0);
    }

    @Test
    @DisplayName("Deve retornar ranking quando há participações no período apenas de missões em andamento")
    public void shouldReturnRankingWhenThereAreParticipationsOnlyForMissionsInProgress() {
        List<RankingParticipacao> resultado = participacaoMissaoRepository.rankingParticipacao(
                StatusMissao.EM_ANDAMENTO,
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now()
        );
        assertFalse(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar relatório com período válido")
    public void shouldReturnReport() {
        List<RelatorioMissao> resultado = participacaoMissaoRepository.relatorioMissoes(
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now()
        );
        assertFalse(resultado.isEmpty());
        resultado.forEach(r -> assertTrue(r.quantidadeParticipantes() >= 1));
    }

    @Test
    @DisplayName("Deve retornar a missão e seus participantes")
    public void shouldReturnParticipantsWhenMissionHasParticipants() {
        Missao missao = missaoRepository.findById(1L).orElseThrow();
        List<AventureiroMissaoResponse> participantes = participacaoMissaoRepository
                .findParticipantesByMissaoId(1L);

        assertNotNull(missao);
        assertFalse(participantes.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar a missão corretamente e lista vazia de participantes quando não há participantes")
    public void shouldReturnEmptyListWhenMissionHasNoParticipants() {
        Missao missao = missaoRepository.findById(this.missaoSemParticipante.getId()).orElseThrow();
        List<AventureiroMissaoResponse> participantes = participacaoMissaoRepository
                .findParticipantesByMissaoId(this.missaoSemParticipante.getId());

        assertNotNull(missao);
        assertTrue(participantes.isEmpty());
    }
}
