package br.com.infnet.guildaaventureiro.dto.missao;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.NivelPerigoMissao;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.StatusMissao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TopMissoesResponse(
        Long missaoId,
        String titulo,
        StatusMissao status,
        NivelPerigoMissao nivelPerigo,
        Long organizacaoId,
        Long totalParticipantes,
        BigDecimal nivelMedioEquipe,
        BigDecimal totalRecompensa,
        Long totalMvps,
        Long participantesComCompanheiros,
        LocalDateTime ultimaAtualizacao,
        BigDecimal indiceProntidao
) {
}
