package br.com.infnet.guildaaventureiro.dto.aventureiro;

import br.com.infnet.guildaaventureiro.domain.operacoes.Aventureiro;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.AventureiroClasse;

public record AventureiroMinimalResponse(
        Long id,
        String nome,
        AventureiroClasse classe,
        String nomeOrganizacao
) {
    public AventureiroMinimalResponse(Aventureiro aventureiro) {
        this(
                aventureiro.getId(),
                aventureiro.getNome(),
                aventureiro.getClasse(),
                aventureiro.getOrganizacao().getNome()
        );
    }
}
