package br.com.infnet.guildaaventureiro.dto.aventureiro;

import br.com.infnet.guildaaventureiro.domain.operacoes.enums.AventureiroClasse;
import jakarta.validation.constraints.Min;

public record AventureiroFiltroRequest(
        Boolean status,
        AventureiroClasse classe,
        @Min(value = 1, message = "O nível deve ser maior ou igual a 1")
        Integer nivelMinimo
) {
}
