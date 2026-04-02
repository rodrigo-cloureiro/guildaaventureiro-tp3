package br.com.infnet.guildaaventureiro.dto.relatorio;

import br.com.infnet.guildaaventureiro.domain.aventura.enums.NivelPerigoMissao;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.StatusMissao;

import java.math.BigDecimal;

public record RelatorioMissao(
        String titulo,
        StatusMissao status,
        NivelPerigoMissao nivelPerigo,
        Long quantidadeParticipantes,
        BigDecimal totalRecompensaDistribuida
) {
}
