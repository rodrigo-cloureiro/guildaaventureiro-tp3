package br.com.infnet.guildaaventureiro.dto.elastic;

import java.util.List;

public record FaixaPreco(
        String faixa,
        long quantidade,
        List<ProdutoResponse> produtos
) {
}
