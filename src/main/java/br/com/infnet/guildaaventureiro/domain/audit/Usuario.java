package br.com.infnet.guildaaventureiro.domain.audit;

import br.com.infnet.guildaaventureiro.domain.operacoes.Aventureiro;
import br.com.infnet.guildaaventureiro.domain.audit.enums.UsuarioStatus;
import jakarta.persistence.*;
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
        name = "usuarios",
        schema = "audit",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_usuarios_email_por_org",
                        columnNames = {"organizacao_id", "email"}
                )
        },
        check = @CheckConstraint(
                name = "ck_usuarios_status",
                constraint = "status IN ('ATIVO', 'BLOQUEADO', 'PENDENTE')"
        ),
        indexes = {
                @Index(name = "idx_usuarios_email", columnList = "email"),
                @Index(name = "idx_usuarios_org", columnList = "organizacao_id")
        }
)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarios_id")
    @SequenceGenerator(
            name = "usuarios_id",
            sequenceName = "usuarios_id_seq",
            schema = "audit",
            allocationSize = 1
    )
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "organizacao_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuarios_org")
    )
    private Organizacao organizacao;

    @OneToMany(mappedBy = "usuario")
    private final Set<UsuarioRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    private final Set<Aventureiro> aventureiros = new HashSet<>();

    @Column(length = 120, nullable = false)
    private String nome;

    @Column(length = 180, nullable = false)
    private String email;

    @Column(name = "senha_hash", length = 255, nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private UsuarioStatus status;

    @Column(name = "ultimo_login_em", nullable = true)
    private LocalDateTime ultimoLoginEm;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Usuario() {
    }

    public Usuario(Organizacao organizacao, String nome, String email, String senhaHash, UsuarioStatus status) {
        this.organizacao = Objects.requireNonNull(organizacao, "A organização é obrigatória");
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.status = status;
    }

    public void adicionarAventureiro(Aventureiro aventureiro) {
        this.aventureiros.add(Objects.requireNonNull(aventureiro, "O aventureiro não pode ser nulo"));
        aventureiro.definirUsuario(this);
    }

    public void registrarLogin() {
        this.ultimoLoginEm = LocalDateTime.now();
    }
}
