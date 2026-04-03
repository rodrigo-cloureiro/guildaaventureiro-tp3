package br.com.infnet.guildaaventureiro.dto.aventureiro;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.AventureiroClasse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record AventureiroUpdate(
        @Size(max = 120, message = "O nome deve possuir no máximo 120 caracteres")
        String nome,
        AventureiroClasse classe,
        @Min(value = 1, message = "O nível deve ser maior ou igual a 1")
        Integer nivel
) {
}
