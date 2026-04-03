package br.com.infnet.guildaaventureiro.repository.operacoes;

import br.com.infnet.guildaaventureiro.domain.operacoes.PainelTaticoMissao;
import br.com.infnet.guildaaventureiro.dto.missao.TopMissoesResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PainelTaticoMissaoRepository extends JpaRepository<PainelTaticoMissao, Long> {
    @Query(value = """
            SELECT new br.com.infnet.guildaaventureiro.dto.missao.TopMissoesResponse(
                        ptm.missaoId,
                        ptm.titulo,
                        ptm.status,
                        ptm.nivelPerigo,
                        ptm.organizacaoId,
                        ptm.totalParticipantes,
                        ptm.nivelMedioEquipe,
                        ptm.totalRecompensa,
                        ptm.totalMvps,
                        ptm.participantesComCompanheiros,
                        ptm.ultimaAtualizacao,
                        ptm.indiceProntidao
                        )
            FROM PainelTaticoMissao ptm
            WHERE ptm.ultimaAtualizacao BETWEEN :de AND :ate
            ORDER BY ptm.indiceProntidao DESC NULLS LAST
            LIMIT :limite
            """)
    List<TopMissoesResponse> topMissoesDias(
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate,
            @Param("limite") int limite
    );
}
