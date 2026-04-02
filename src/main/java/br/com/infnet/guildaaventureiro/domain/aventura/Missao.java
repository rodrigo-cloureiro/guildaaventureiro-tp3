package br.com.infnet.guildaaventureiro.domain.aventura;

import br.com.infnet.guildaaventureiro.domain.audit.Organizacao;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.NivelPerigoMissao;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.PapelMissao;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.StatusMissao;
import br.com.infnet.guildaaventureiro.exception.aventura.AventureiroInativoException;
import br.com.infnet.guildaaventureiro.exception.aventura.BusinessException;
import br.com.infnet.guildaaventureiro.exception.aventura.MissaoNaoAceitaParticipantesException;
import br.com.infnet.guildaaventureiro.exception.aventura.OrganizacaoInvalidaException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "missao",
        schema = "operacoes",
        indexes = {
                @Index(name = "idx_missao_nivel_status", columnList = "nivel_perigo, status")
        }
)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Missao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "missao_id")
    @SequenceGenerator(
            name = "missao_id",
            sequenceName = "missao_id_seq",
            schema = "operacoes",
            allocationSize = 1
    )
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organizacao_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_missoes_org")
    )
    private Organizacao organizacao;

    @OneToMany(
            mappedBy = "missao",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final Set<ParticipacaoMissao> participacoesEmMissoes = new HashSet<>();

    @Column(length = 150, nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_perigo", nullable = false)
    private NivelPerigoMissao nivelPerigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMissao status;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_inicio", nullable = true, updatable = true)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = true, updatable = true)
    private LocalDateTime dataTermino;

    protected Missao() {
    }

    public Missao(Organizacao organizacao, String titulo, NivelPerigoMissao nivelPerigo) {
        this.organizacao = Objects.requireNonNull(organizacao, "A organização é obrigatória");
        this.titulo = Objects.requireNonNull(titulo, "O título é obrigatório");
        this.nivelPerigo = Objects.requireNonNull(nivelPerigo, "O nível do perigo é obrigatório");
        this.status = StatusMissao.PLANEJADA;
    }

    public Set<ParticipacaoMissao> getParticipacoesEmMissoes() {
        return Collections.unmodifiableSet(this.participacoesEmMissoes);
    }

    public void validarAlteracao() {
        if (this.status == StatusMissao.CANCELADA) {
            throw new BusinessException("Não é possível alterar missões canceladas");
        }
    }

    public void alterarTitulo(String titulo) {
        if (titulo.length() > 150) {
            throw new BusinessException("O título deve possuir no máximo 150 caracteres");
        }

        this.titulo = titulo;
    }

    public void alterarNivelPerigo(NivelPerigoMissao nivelPerigo) {
        if (nivelPerigo == null) {
            throw new BusinessException("O nível de perigo deve ser informado");
        }

        this.nivelPerigo = nivelPerigo;
    }

    public void iniciarMissao() {
        if (this.status != StatusMissao.PLANEJADA) {
            throw new BusinessException("Não é possível iniciar missões que não estejam planejadas");
        }

        this.status = StatusMissao.EM_ANDAMENTO;
        this.dataInicio = LocalDateTime.now();
    }

    public void concluirMissao() {
        if (this.status != StatusMissao.EM_ANDAMENTO) {
            throw new BusinessException("A missão precisa estar em andamento para ser concluída");
        }

        this.status = StatusMissao.CONCLUIDA;
        this.dataTermino = LocalDateTime.now();
    }

    public void cancelarMissao() {
        if (this.status == StatusMissao.CONCLUIDA) {
            throw new BusinessException("Não é possível cancelar missões concluídas");
        }

        this.status = StatusMissao.CANCELADA;
        this.dataTermino = LocalDateTime.now();
    }

    public void adicionarParticipante(Aventureiro aventureiro, PapelMissao papelMissao) {
        verificarOrganizacao(aventureiro);
        verificarStatus();
        verificarInatividadeAventureiro(aventureiro);

        ParticipacaoMissao participacao = new ParticipacaoMissao(this, aventureiro, papelMissao);

        if (this.participacoesEmMissoes.contains(participacao)) {
            throw new BusinessException("O aventureiro já participa dessa missão");
        }

        this.participacoesEmMissoes.add(participacao);
        aventureiro.entrarEmMissao(participacao);
    }

    public void definirMvp(List<ParticipacaoMissao> participacoes) {
        if (!this.getStatus().equals(StatusMissao.CONCLUIDA)) {
            throw new BusinessException("Não é possível definir MVP para missões que não estão concluídas");
        }

        participacoes.forEach(ParticipacaoMissao::definirMvp);
    }

    private void verificarStatus() {
        if (this.status != StatusMissao.PLANEJADA) {
            throw new MissaoNaoAceitaParticipantesException(
                    "Não é possível adicionar aventureiros quando a missão está " + this.status.name()
            );
        }
    }

    private void verificarInatividadeAventureiro(Aventureiro aventureiro) {
        if (!aventureiro.isAtivo()) {
            throw new AventureiroInativoException("Aventureiros inativos não podem ser associados");
        }
    }

    private void verificarOrganizacao(Aventureiro aventureiro) {
        if (!Objects.equals(this.organizacao.getId(), aventureiro.getOrganizacao().getId())) {
            throw new OrganizacaoInvalidaException("O aventureiro não pertence a organização");
        }
    }
}
