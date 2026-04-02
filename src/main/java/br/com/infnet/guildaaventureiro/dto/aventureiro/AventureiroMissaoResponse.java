package br.com.infnet.guildaaventureiro.dto.aventureiro;

import br.com.infnet.guildaaventureiro.domain.aventura.enums.AventureiroClasse;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.PapelMissao;

import java.math.BigDecimal;

public record AventureiroMissaoResponse(
        String nome,
        AventureiroClasse classe,
        int nivel,
        PapelMissao papelNaMissao,
        BigDecimal valorRecompensaRecebida,
        Boolean destaque
) {
}
