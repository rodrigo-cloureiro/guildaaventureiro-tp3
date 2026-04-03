package br.com.infnet.guildaaventureiro.dto.aventureiro;

import br.com.infnet.guildaaventureiro.domain.operacoes.Aventureiro;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.AventureiroClasse;

public record AventureiroResponse(
        Long id,
        String nome,
        AventureiroClasse classe,
        int nivel,
        boolean ativo
) {
    public AventureiroResponse(Aventureiro aventureiro) {
        this(
                aventureiro.getId(),
                aventureiro.getNome(),
                aventureiro.getClasse(),
                aventureiro.getNivel(),
                aventureiro.isAtivo()
        );
    }
}
