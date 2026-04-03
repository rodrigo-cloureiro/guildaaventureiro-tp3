package br.com.infnet.guildaaventureiro.mapper;

import br.com.infnet.guildaaventureiro.domain.operacoes.Companheiro;
import br.com.infnet.guildaaventureiro.dto.companheiro.CompanheiroResponse;

public class CompanheiroMapper {
    public static CompanheiroResponse toResponse(Companheiro companheiro) {
        return new CompanheiroResponse(companheiro);
    }
}
