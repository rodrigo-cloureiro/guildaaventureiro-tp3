package br.com.infnet.guildaaventureiro.dto.elastic;

public record ProdutoResponse(
        String nome,
        String descricao,
        String categoria,
        String raridade,
        Float preco
) {
}
