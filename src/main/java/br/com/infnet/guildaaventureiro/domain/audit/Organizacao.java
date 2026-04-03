package br.com.infnet.guildaaventureiro.domain.audit;

import br.com.infnet.guildaaventureiro.domain.operacoes.Aventureiro;
import br.com.infnet.guildaaventureiro.domain.operacoes.Missao;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
        name = "organizacoes",
        schema = "audit",
        uniqueConstraints = {
                @UniqueConstraint(name = "organizacoes_nome_key", columnNames = {"nome"})
        }
)
@Getter
public class Organizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organizacoes_id")
    @SequenceGenerator(
            name = "organizacoes_id",
            sequenceName = "organizacoes_id_seq",
            schema = "audit",
            allocationSize = 1
    )
    private Long id;

    @Column(length = 120, nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private final Set<Usuario> usuarios = new HashSet<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private final Set<Missao> missoes = new HashSet<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private final Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private final Set<ApiKey> apiKeys = new HashSet<>();

    @OneToMany(
            mappedBy = "organizacao",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, // Se a organização for removida, todos os aventureiros serão removidos
            orphanRemoval = true // Se o aventureiro for removido da organização, será removido do banco
    )
    private final Set<Aventureiro> aventureiros = new HashSet<>();

    protected Organizacao() {
    }

    public Organizacao(String nome) {
        this.nome = Objects.requireNonNull(nome, "O nome é obrigatório");
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void adicionarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public void adicionarAventureiro(Aventureiro aventureiro) {
        this.aventureiros.add(aventureiro);
        aventureiro.definirOrganizacao(this);
    }

    public void removerAventureiro(Aventureiro aventureiro) {
        this.aventureiros.remove(aventureiro);
        aventureiro.definirOrganizacao(null);
    }
}
