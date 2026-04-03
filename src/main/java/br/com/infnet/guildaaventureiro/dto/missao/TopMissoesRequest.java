package br.com.infnet.guildaaventureiro.dto.missao;

import org.hibernate.validator.constraints.Range;

public record TopMissoesRequest(
        @Range(min = 3, max = 90, message = "O número de dias deve estar entre 3 e 90")
        Integer dias,
        @Range(min = 3, max = 20, message = "O limite de resultados deve estar entre 3 e 20")
        Integer limite
) {
    public TopMissoesRequest {
        if (dias == null) dias = 15;
        if (limite == null) limite = 10;
    }
}
