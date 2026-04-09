package br.com.infnet.guildaaventureiro.dto.elastic;

public record ContagemCampoAggregation(
        String campo,
        String valor,
        long quantidade
) {
}
