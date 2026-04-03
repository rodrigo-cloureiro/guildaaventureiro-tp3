package br.com.infnet.guildaaventureiro.dto.relatorio;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.StatusMissao;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record RankingPartipacaoFiltroRequest(
        @PastOrPresent(message = "A data de início da missão não pode ser no futuro")
        LocalDateTime inicio,
        @PastOrPresent(message = "A data de término da missão não pode ser no futuro")
        LocalDateTime termino,
        StatusMissao status
) {
}
