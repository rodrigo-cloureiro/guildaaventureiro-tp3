package br.com.infnet.guildaaventureiro.domain.aventura;

import br.com.infnet.guildaaventureiro.domain.audit.Organizacao;
import br.com.infnet.guildaaventureiro.domain.audit.Usuario;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.AventureiroClasse;
import br.com.infnet.guildaaventureiro.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
        name = "aventureiro",
        schema = "operacoes",
        check = {
                /*@CheckConstraint(
                        name = "ck_aventureiros_classe",
                        constraint = "classe IN ('GUERREIRO', 'MAGO', 'ARQUEIRO', 'CLERIGO', 'LADINO')"
                ),*/
                @CheckConstraint(name = "aventureiro_nivel_check", constraint = "nivel >= 1")
        },
        indexes = {
                @Index(
                        name = "idx_aventureiro_org_classe_nivel",
                        columnList = "organizacao_id, classe, nivel"
                ),
        }
)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aventureiro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aventureiro_id")
    @SequenceGenerator(
            name = "aventureiro_id",
            sequenceName = "aventureiro_id_seq",
            schema = "operacoes",
            allocationSize = 1
    )
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organizacao_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_aventureiros_org")
    )
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "usuario_cadastro_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_aventureiros_usuario")
    )
    @NotNull(message = "O usuário é obrigatório")
    private Usuario usuario;

    @OneToOne(
            mappedBy = "aventureiro",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Companheiro companheiro;

    @OneToMany(
            mappedBy = "aventureiro",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final Set<ParticipacaoMissao> participacoesEmMissoes = new HashSet<>();

    @Column(length = 120, nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AventureiroClasse classe;

    @Column(nullable = false)
    private int nivel;

    @Column(nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    protected Aventureiro() {
    }

    public Aventureiro(String nome, AventureiroClasse classe, int nivel) {
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
    }

    public void alterarNome(String nome) {
        if (nome == null || nome.isBlank() || nome.length() > 120) {
            throw new BusinessException("O nome deve ser informado e possuir no máximo 120 caracteres");
        }

        this.nome = nome;
    }

    public void alterarClasse(AventureiroClasse classe) {
        if (classe == null) {
            throw new BusinessException("A classe deve ser informada");
        }

        this.classe = classe;
    }

    public void alterarNivel(int nivel) {
        if (nivel < 1) {
            throw new BusinessException("O nível deve ser maior ou igual a 1");
        }
        this.nivel = nivel;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void definirOrganizacao(Organizacao organizacao) {
        this.organizacao = organizacao;
    }

    public void definirUsuario(Usuario usuario) {
        this.usuario = Objects.requireNonNull(usuario, "O usuário é obrigatório");
    }

    public void definirCompanheiro(Companheiro companheiro) {
        this.companheiro = companheiro;
        companheiro.definirAventureiro(this);
    }

    public void removerCompanheiro() {
        this.companheiro.removerAventureiro();
        this.companheiro = null;
    }

    void entrarEmMissao(ParticipacaoMissao participacao) {
        if (this.participacoesEmMissoes.contains(participacao)) {
            throw new BusinessException("O aventureiro já participa dessa missão");
        }

        this.participacoesEmMissoes.add(participacao);
    }
}
