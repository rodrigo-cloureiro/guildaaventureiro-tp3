package br.com.infnet.guildaaventureiro.domain.elastic;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "guilda_loja", createIndex = false)
@Getter
@ToString
public class ProdutoDocument {
    @Id
    private String id;

    @MultiField(
            mainField = @Field(name = "nome", type = FieldType.Text),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword)
    )
    private String nome;

    @Field(name = "categoria", type = FieldType.Keyword)
    private String categoria;

    @Field(name = "descricao", type = FieldType.Text, analyzer = "portuguese_custom")
    private String descricao;

    @Field(name = "preco", type = FieldType.Float)
    private float preco;

    @Field(name = "raridade", type = FieldType.Keyword)
    private String raridade;

}
