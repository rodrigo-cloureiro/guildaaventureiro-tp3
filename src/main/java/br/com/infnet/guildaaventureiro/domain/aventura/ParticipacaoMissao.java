package br.com.infnet.guildaaventureiro.domain.aventura;

import br.com.infnet.guildaaventureiro.domain.aventura.enums.PapelMissao;
import br.com.infnet.guildaaventureiro.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "participacao_missao",
        schema = "operacoes",
        indexes = {
                @Index(name = "idx_participacao_missao_aventureiro", columnList = "aventureiro_id")
        }
)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipacaoMissao {
    @EmbeddedId
    private final ParticipacaoMissaoId id = new ParticipacaoMissaoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "missaoId")
    @JoinColumn(
            name = "missao_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_participacao_missao_missao")
    )
    @EqualsAndHashCode.Include
    private Missao missao;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "aventureiroId")
    @JoinColumn(
            name = "aventureiro_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_participacao_missao_aventureiro")
    )
    @EqualsAndHashCode.Include
    private Aventureiro aventureiro;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false)
    private PapelMissao papelMissao;

    @Min(value = 0, message = "A recompensa em ouro deve ser maior ou igual a zero")
    @Column(name = "recompensa_ouro", precision = 10, scale = 2, nullable = true)
    private BigDecimal recompensaOuro = BigDecimal.ZERO;

    @Column(name = "destaque", nullable = false)
    private boolean mvp = false;

    @CreationTimestamp
    @Column(name = "data_registro", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

    protected ParticipacaoMissao() {
    }

    public ParticipacaoMissao(Missao missao, Aventureiro aventureiro, PapelMissao papelMissao) {
        this.missao = missao;
        this.aventureiro = Objects.requireNonNull(aventureiro, "O aventureiro é obrigatório");
        this.papelMissao = Objects.requireNonNull(papelMissao, "O papel do aventureiro na missão é obrigatório");
    }

    void definirMvp() {
        this.mvp = true;
    }

    public void recompensar(BigDecimal recompensa) {
        if (recompensa.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Recompensa deve ser um valor positivo");
        }

        this.recompensaOuro = this.recompensaOuro.add(recompensa);
    }
}
