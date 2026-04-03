package br.com.infnet.guildaaventureiro.dto.aventureiro;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.AventureiroClasse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AventureiroCreate(
        @NotBlank(message = "O nome não pode ser vazio")
        @Size(max = 120, message = "O nome deve possuir no máximo 120 caracteres")
        String nome,
        @NotNull(message = "A classe é obrigatória")
        AventureiroClasse classe,
        @Min(value = 1, message = "O nível deve ser maior ou igual a 1")
        int nivel,
        // @NotNull(message = "O ID da organização é obrigatório")
        // Long organizacaoId,
        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId
) {
}
