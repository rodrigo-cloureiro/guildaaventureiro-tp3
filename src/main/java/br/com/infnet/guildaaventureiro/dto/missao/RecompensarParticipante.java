package br.com.infnet.guildaaventureiro.dto.missao;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record RecompensarParticipante(
        @Min(value = 1, message = "A recompensa deve ser um valor positivo")
        BigDecimal recompensa
) {
}
