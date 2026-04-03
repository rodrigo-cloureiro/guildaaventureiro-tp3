package br.com.infnet.guildaaventureiro.dto.missao;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.NivelPerigoMissao;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.StatusMissao;
import br.com.infnet.guildaaventureiro.dto.missao.enums.TipoDataMissao;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record MissaoFiltroRequest(
        StatusMissao status,
        NivelPerigoMissao nivelPerigo,
        TipoDataMissao tipoData,
        @PastOrPresent(message = "A data não pode ser no futuro")
        LocalDateTime de,
        @PastOrPresent(message = "A data não pode ser no futuro")
        LocalDateTime ate
) {
}
