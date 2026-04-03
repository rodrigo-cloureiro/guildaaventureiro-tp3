package br.com.infnet.guildaaventureiro.repository.operacoes;

import br.com.infnet.guildaaventureiro.domain.aventura.ParticipacaoMissao;
import br.com.infnet.guildaaventureiro.domain.aventura.ParticipacaoMissaoId;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.StatusMissao;
import br.com.infnet.guildaaventureiro.dto.relatorio.RankingParticipacao;
import br.com.infnet.guildaaventureiro.dto.aventureiro.AventureiroMissaoResponse;
import br.com.infnet.guildaaventureiro.dto.relatorio.RelatorioMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipacaoMissaoRepository extends JpaRepository<ParticipacaoMissao, ParticipacaoMissaoId> {
    long countByAventureiroId(Long id);

    Optional<ParticipacaoMissao> findTopByAventureiroIdOrderByDataRegistroDesc(Long id);

    Optional<ParticipacaoMissao> findByMissaoIdAndAventureiroId(Long missaoId, Long aventureiroId);

    @Query(value = """
            SELECT pm
            FROM ParticipacaoMissao pm
            WHERE pm.missao.id = :missaoId AND
            pm.recompensaOuro = (
                        SELECT MAX(pm2.recompensaOuro)
                        FROM ParticipacaoMissao pm2
                        WHERE pm2.missao.id = :missaoId
                        )
            """)
    List<ParticipacaoMissao> findParticipacoesComMaiorRecompensa(@Param(value = "missaoId") Long missaoId);

    @Query(value = """
            SELECT new br.com.infnet.guildaaventureiro.dto.aventureiro.AventureiroMissaoResponse(
                        a.nome,
                        a.classe,
                        a.nivel,
                        pm.papelMissao,
                        pm.recompensaOuro,
                        pm.mvp
            )
            FROM ParticipacaoMissao pm
            JOIN pm.aventureiro a
            WHERE pm.missao.id = :id
            """)
    List<AventureiroMissaoResponse> findParticipantesByMissaoId(@Param(value = "id") Long id);

    @Query(value = """
            SELECT new br.com.infnet.guildaaventureiro.dto.relatorio.RankingParticipacao(
                        a.nome,
                        COUNT(pm.missao.id),
                        SUM(pm.recompensaOuro),
                        SUM(CASE WHEN pm.mvp = true THEN 1 ELSE 0 END)
                    )
            FROM ParticipacaoMissao pm
            JOIN pm.aventureiro a
            WHERE (:status IS NULL OR pm.missao.status = :status)
            AND pm.missao.dataInicio >= :inicio
            AND (pm.missao.dataTermino <= :termino OR pm.missao.dataTermino IS NULL)
            GROUP BY a.id, a.nome
            ORDER BY SUM(pm.recompensaOuro) DESC NULLS LAST,
                     SUM(CASE WHEN pm.mvp = true THEN 1 ELSE 0 END) DESC,
                     COUNT(pm.missao.id) DESC
            """)
    List<RankingParticipacao> rankingParticipacao(
            @Param(value = "status") StatusMissao status,
            @Param(value = "inicio") LocalDateTime inicio,
            @Param(value = "termino") LocalDateTime termino
    );

    @Query(value = """
            SELECT new br.com.infnet.guildaaventureiro.dto.relatorio.RelatorioMissao(
                        m.titulo,
                        m.status,
                        m.nivelPerigo,
                        COUNT(pm.missao.id),
                        SUM(pm.recompensaOuro)
            )
            FROM ParticipacaoMissao pm
            LEFT JOIN pm.missao m
            WHERE pm.missao.dataInicio >= :inicio
            AND (pm.missao.dataTermino <= :termino OR pm.missao.dataTermino IS NULL)
            GROUP BY m.id
            ORDER BY m.dataCriacao
            """)
    List<RelatorioMissao> relatorioMissoes(
            @Param(value = "inicio") LocalDateTime inicio,
            @Param(value = "termino") LocalDateTime termino
    );
}
