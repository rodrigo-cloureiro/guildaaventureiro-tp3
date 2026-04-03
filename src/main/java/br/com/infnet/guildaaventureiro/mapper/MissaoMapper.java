package br.com.infnet.guildaaventureiro.mapper;

import br.com.infnet.guildaaventureiro.domain.audit.Organizacao;
import br.com.infnet.guildaaventureiro.domain.operacoes.Missao;
import br.com.infnet.guildaaventureiro.dto.missao.MissaoCreate;
import br.com.infnet.guildaaventureiro.dto.missao.MissaoMinimalResponse;
import br.com.infnet.guildaaventureiro.dto.missao.MissaoResponse;

public class MissaoMapper {
    public static Missao toMissao(MissaoCreate dto, Organizacao organizacao) {
        return new Missao(
                organizacao,
                dto.titulo(),
                dto.nivelPerigo()
        );
    }

    public static MissaoMinimalResponse toMinimalResponse(Missao missao) {
        return new MissaoMinimalResponse(missao);
    }

    public static MissaoResponse toResponse(Missao missao) {
        return new MissaoResponse(missao);
    }
}
