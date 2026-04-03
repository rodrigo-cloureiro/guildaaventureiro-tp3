package br.com.infnet.guildaaventureiro.dto.missao;

import br.com.infnet.guildaaventureiro.domain.operacoes.Missao;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.NivelPerigoMissao;
import br.com.infnet.guildaaventureiro.domain.operacoes.enums.StatusMissao;

import java.time.LocalDateTime;

public record MissaoResponse(
        Long id,
        String titulo,
        StatusMissao status,
        NivelPerigoMissao nivelPerigo,
        LocalDateTime criadaEm,
        LocalDateTime iniciouEm,
        LocalDateTime terminouEm
) {
    public MissaoResponse(Missao missao) {
        this(
                missao.getId(),
                missao.getTitulo(),
                missao.getStatus(),
                missao.getNivelPerigo(),
                missao.getDataCriacao(),
                missao.getDataInicio(),
                missao.getDataTermino()
        );
    }
}
