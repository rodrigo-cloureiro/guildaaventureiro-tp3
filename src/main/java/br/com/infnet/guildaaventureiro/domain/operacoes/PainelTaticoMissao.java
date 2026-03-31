package br.com.infnet.guildaaventureiro.domain.operacoes;

import br.com.infnet.guildaaventureiro.domain.aventura.enums.NivelPerigoMissao;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.StatusMissao;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "vw_painel_tatico_missao", schema = "operacoes")
public class PainelTaticoMissao {
    @Id
    @Column(name = "missao_id", nullable = false)
    private Long missaoId;

    @Column(length = 150, nullable = false)
    private String titulo;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private StatusMissao status;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "nivel_perigo", nullable = false)
    private NivelPerigoMissao nivelPerigo;

    @Column(name = "organizacao_id", nullable = false)
    private Long organizacaoId;

    @Column(name = "total_participantes", nullable = false)
    private Long totalParticipantes;

    @Column(name = "nivel_medio_equipe", precision = 10, scale = 2, nullable = false)
    private BigDecimal nivelMedioEquipe;

    @Column(name = "total_recompensa", nullable = false)
    private BigDecimal totalRecompensa;

    @Column(name = "total_mvps", nullable = false)
    private Long totalMvps;

    @Column(name = "participantes_com_companheiro", nullable = false)
    private Long participantesComCompanheiros;

    @Column(name = "ultima_atualizacao", nullable = false)
    private LocalDateTime ultimaAtualizacao;

    @Column(name = "indice_prontidao", nullable = false)
    private BigDecimal indiceProntidao;
}
