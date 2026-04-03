package br.com.infnet.guildaaventureiro.domain.operacoes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class ParticipacaoMissaoId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "missao_id", nullable = false)
    private Long missaoId;

    @Column(name = "aventureiro_id", nullable = false)
    private Long aventureiroId;

    protected ParticipacaoMissaoId() {
    }

    public ParticipacaoMissaoId(Long missaoId, Long aventureiroId) {
        this.missaoId = missaoId;
        this.aventureiroId = aventureiroId;
    }
}
