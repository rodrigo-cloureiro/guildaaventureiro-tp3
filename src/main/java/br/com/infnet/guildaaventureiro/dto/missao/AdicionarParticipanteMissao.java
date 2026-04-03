package br.com.infnet.guildaaventureiro.dto.missao;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.PapelMissao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AdicionarParticipanteMissao(
        @Min(value = 1, message = "O ID do participante não pode ser zero ou negativo")
        Long participanteId,

        @NotNull(message = "O papel na missão é obrigatório")
        PapelMissao papelMissao
) {
}
