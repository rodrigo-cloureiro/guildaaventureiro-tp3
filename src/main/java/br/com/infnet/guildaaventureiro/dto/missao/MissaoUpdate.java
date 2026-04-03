package br.com.infnet.guildaaventureiro.dto.missao;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.NivelPerigoMissao;
import jakarta.validation.constraints.Size;

public record MissaoUpdate(
        @Size(max = 150, message = "O nome deve possuir no máximo 150 caracteres")
        String titulo,

        NivelPerigoMissao nivelPerigo
) {
}
