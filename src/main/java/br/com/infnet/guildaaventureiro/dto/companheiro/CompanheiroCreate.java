package br.com.infnet.guildaaventureiro.dto.companheiro;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.CompanheiroEspecie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record CompanheiroCreate(
        @NotBlank(message = "O nome não pode ser vazio")
        @Size(max = 120, message = "O nome deve possuir no máximo 120 caracteres")
        String nome,
        @NotNull(message = "A espécie é obrigatória")
        CompanheiroEspecie especie,
        @NotNull
        @Range(min = 0, max = 100, message = "A lealdade deve ser um inteiro entre 0 e 100")
        Integer lealdade
) {
}
