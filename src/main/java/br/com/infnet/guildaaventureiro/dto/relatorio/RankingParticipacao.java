package br.com.infnet.guildaaventureiro.dto.relatorio;

import java.math.BigDecimal;

public record RankingParticipacao(
        String nome,
        Long participacoes,
        BigDecimal recompensaRecebida,
        Long destaquesObtidos
) {
}
