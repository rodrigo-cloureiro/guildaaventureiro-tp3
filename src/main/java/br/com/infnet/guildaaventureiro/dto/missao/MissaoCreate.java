package br.com.infnet.guildaaventureiro.dto.missao;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.NivelPerigoMissao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MissaoCreate(
        @Min(value = 1, message = "O ID não pode ser zero ou negativo")
        Long organizacaoId,

        @NotBlank(message = "O nome não pode ser vazio")
        @Size(max = 150, message = "O nome deve possuir no máximo 150 caracteres")
        String titulo,

        @NotNull(message = "O nível de perigo da missão é obrigatório")
        NivelPerigoMissao nivelPerigo
) {
}
