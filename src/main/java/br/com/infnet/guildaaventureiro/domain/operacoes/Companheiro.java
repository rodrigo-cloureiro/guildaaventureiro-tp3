package br.com.infnet.guildaaventureiro.domain.operacoes;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.CompanheiroEspecie;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(
        name = "companheiro",
        schema = "operacoes",
        check = {
                @CheckConstraint(
                        name = "companheiro_indice_lealdade_check",
                        constraint = "indice_lealdade >= 0 AND indice_lealdade <= 100"
                )
        },
        indexes = {
                @Index(name = "idx_companheiro_especie_lealdade", columnList = "especie, indice_lealdade")
        }
)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Companheiro {
    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @PrimaryKeyJoinColumn(
            name = "aventureiro_id",
            foreignKey = @ForeignKey(name = "fk_companheiros_aventureiro")
    )
    private Aventureiro aventureiro;

    @Column(length = 120, nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanheiroEspecie especie;

    @Range(min = 0, max = 100, message = "A lealdade deve ser um inteiro entre 0 e 100")
    @Column(name = "indice_lealdade", nullable = false)
    private int indiceLealdade;

    protected Companheiro() {
    }

    public Companheiro(String nome, CompanheiroEspecie especie, int indiceLealdade) {
        this.nome = nome;
        this.especie = especie;
        this.indiceLealdade = indiceLealdade;
    }

    void definirAventureiro(Aventureiro aventureiro) {
        this.aventureiro = aventureiro;
    }

    void removerAventureiro() {
        this.aventureiro = null;
    }
}
