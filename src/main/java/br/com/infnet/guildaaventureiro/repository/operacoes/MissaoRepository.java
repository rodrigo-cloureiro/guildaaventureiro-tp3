package br.com.infnet.guildaaventureiro.repository.operacoes;

import br.com.infnet.guildaaventureiro.domain.operacoes.Missao;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.NivelPerigoMissao;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.StatusMissao;
import br.com.infnet.guildaaventureiro.dto.missao.MissaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long> {
    @Query(value = """
            SELECT new br.com.infnet.guildaaventureiro.dto.missao.MissaoResponse(m)
            FROM Missao m
            WHERE (:status IS NULL OR m.status = :status)
            AND (:nivelPerigo IS NULL OR m.nivelPerigo = :nivelPerigo)
            AND (
                    (:tipoData IS NULL) OR
                    (:tipoData = 'CRIACAO' AND m.dataCriacao BETWEEN :de AND :ate) OR
                    (:tipoData = 'INICIO' AND m.dataInicio BETWEEN :de AND :ate) OR
                    (:tipoData = 'TERMINO' AND m.dataTermino BETWEEN :de AND :ate)
            )
            """)
    Page<MissaoResponse> findMissoesByFilters(
            @Param(value = "status") StatusMissao status,
            @Param(value = "nivelPerigo") NivelPerigoMissao nivelPerigo,
            @Param("tipoData") String tipoData,
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate,
            Pageable pageable
    );
}
