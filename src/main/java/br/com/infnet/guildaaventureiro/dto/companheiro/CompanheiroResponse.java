package br.com.infnet.guildaaventureiro.dto.companheiro;

import br.com.infnet.guildaaventureiro.domain.aventura.Companheiro;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.CompanheiroEspecie;

public record CompanheiroResponse(
        String nome,
        CompanheiroEspecie especie,
        int lealdade
) {
    public CompanheiroResponse(Companheiro companheiro) {
        this(
                companheiro.getNome(),
                companheiro.getEspecie(),
                companheiro.getIndiceLealdade()
        );
    }
}
