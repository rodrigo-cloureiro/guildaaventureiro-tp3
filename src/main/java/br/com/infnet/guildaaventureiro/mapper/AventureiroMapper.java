package br.com.infnet.guildaaventureiro.mapper;

import br.com.infnet.guildaaventureiro.domain.operacoes.Aventureiro;
import br.com.infnet.guildaaventureiro.domain.operacoes.Companheiro;
import br.com.infnet.guildaaventureiro.domain.operacoes.Missao;
import br.com.infnet.guildaaventureiro.dto.aventureiro.*;
import org.springframework.stereotype.Component;

@Component
public class AventureiroMapper {
    public static Aventureiro toEntity(AventureiroCreate dto) {
        return new Aventureiro(
                dto.nome(),
                dto.classe(),
                dto.nivel()
        );
    }

    public static AventureiroResponse toResponse(Aventureiro aventureiro) {
        return new AventureiroResponse(aventureiro);
    }

    public static AventureiroMinimalResponse toMinimalResponse(Aventureiro aventureiro) {
        return new AventureiroMinimalResponse(aventureiro);
    }

    public static AventureiroProfileResponse toProfileResponse(
            Aventureiro aventureiro,
            long totalParticipacoesEmMissao,
            Missao missao
    ) {
        return new AventureiroProfileResponse(
                toResponse(aventureiro),
                aventureiro.getCompanheiro() != null ?
                        CompanheiroMapper.toResponse(aventureiro.getCompanheiro()) :
                        null,
                totalParticipacoesEmMissao,
                missao != null ? MissaoMapper.toMinimalResponse(missao) : null
        );
    }

    public static AventureiroCompanheiroResponse toCompanheiroResponse(String nome, Companheiro companheiro) {
        return new AventureiroCompanheiroResponse(
                nome,
                CompanheiroMapper.toResponse(companheiro)
        );
    }
}
