package br.com.infnet.guildaaventureiro.service.elastic;

import br.com.infnet.guildaaventureiro.domain.elastic.ProdutoDocument;
import br.com.infnet.guildaaventureiro.dto.elastic.*;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.RangeBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProdutoDocumentService {

    private final ElasticsearchClient client;

    public List<ProdutoDocument> buscarPorNome(String nome) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .match(m -> m
                                    .field("nome")
                                    .query(nome)
                            )
                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProdutoDocument> buscarPorDescricao(String descricao) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .match(m -> m
                                    .field("descricao")
                                    .query(descricao)
                            )
                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProdutoDocument> buscarPorFraseExata(String descricaoExata) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .matchPhrase(mp -> mp
                                    .field("descricao")
                                    .query(descricaoExata)
                            )
                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProdutoDocument> buscarPorNomeComTolerancia(String nome) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .fuzzy(f -> f
                                    .field("nome")
                                    .value(nome)
                                    .fuzziness("AUTO")
                            )
                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProdutoDocument> buscarPorNomeEDescricao(String termo) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .multiMatch(mm -> mm
                                    .fields(List.of("nome", "descricao"))
                                    .query(termo)
                            )
                    )
                    .size(20), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProdutoDocument> buscarPorDescricaoECategoria(String descricao, String categoria) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .bool(b -> b
                                    .must(m -> m
                                            .match(v -> v
                                                    .field("descricao")
                                                    .query(descricao)
                                            )
                                    )
                                    .filter(f -> f
                                            .term(t -> t
                                                    .field("categoria")
                                                    .value(categoria)
                                            )
                                    )
                            )
                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProdutoDocument> buscarPorFaixaPreco(double min, double max) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                            .query(q -> q
                                    .range(r -> r
                                            .number(n -> n
                                                    .field("preco")
                                                    .gte(min)
                                                    .lte(max)
                                            )
                                    )
                            )/*.sort(so -> so
                                    .field(f -> f
                                            .field("preco")
                                            .order(SortOrder.Asc)
                                    )
                            )*/,
                    ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProdutoDocument> buscaCombinada(String categoria, String raridade, double min, double max) {
        TermQuery categoriaTermQuery = TermQuery.of(t -> t.field("categoria").value(categoria));
        TermQuery raridadeTermQuery = TermQuery.of(t -> t.field("raridade").value(raridade));
        RangeQuery faixaPreco = RangeQuery.of(r -> r.number(n -> n.field("preco").gte(min).lte(max)));

        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .bool(b -> b
                                    .filter(
                                            categoriaTermQuery,
                                            raridadeTermQuery,
                                            faixaPreco
                                    )
                            )

                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CategoriaAggregation> quantidadeProdutosPorCategoria() {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .aggregations("por_categoria", a -> a
                            .terms(t -> t
                                    .field("categoria")
                            )
                    ), ProdutoDocument.class
            );

            return response.aggregations()
                    .get("por_categoria")
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .map(bucket -> new CategoriaAggregation(
                            bucket.key().stringValue(),
                            bucket.docCount()
                    ))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RaridadeAggregation> quantidadeProdutosPorRaridade() {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .aggregations("por_raridade", a -> a
                            .terms(t -> t
                                    .field("raridade")
                            )
                    ), ProdutoDocument.class
            );

            return response.aggregations()
                    .get("por_raridade")
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .map(bucket -> new RaridadeAggregation(
                            bucket.key().stringValue(),
                            bucket.docCount()
                    ))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrecoMedioAggregation precoMedioProdutos() {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .aggregations("preco_medio", a -> a
                            .avg(avg -> avg
                                    .field("preco"))
                    ), ProdutoDocument.class
            );

            return new PrecoMedioAggregation(response.aggregations()
                    .get("preco_medio")
                    .avg()
                    .value()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FaixaPreco> agruparEmFaixaPreco() {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .aggregations("faixa_preco", a -> a
                            .range(r -> r
                                    .field("preco")
                                    .ranges(getRangesPreco())
                            )
                            .aggregations("produtos", sa -> sa
                                    .topHits(th -> th)
                            )
                    ), ProdutoDocument.class
            );

            return response.aggregations()
                    .get("faixa_preco")
                    .range()
                    .buckets()
                    .array()
                    .stream()
                    .map(this::bucketToFaixaPreco)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<AggregationRange> getRangesPreco() {
        return List.of(
                AggregationRange.of(a -> a.to(100.0).key("Abaixo de 100")),
                AggregationRange.of(a -> a.from(100.0).to(300.0).key("De 100 a 300")),
                AggregationRange.of(a -> a.from(300.0).to(700.0).key("De 300 a 700")),
                AggregationRange.of(a -> a.from(700.0).key("Acima de 700"))
        );
    }

    // TODO mapper
    private ProdutoResponse toProdutoResponse(ProdutoDocument document) {
        return new ProdutoResponse(
                document.getNome(),
                document.getDescricao(),
                document.getCategoria(),
                document.getRaridade(),
                document.getPreco()
        );
    }

    // TODO mapper
    private FaixaPreco bucketToFaixaPreco(RangeBucket bucket) {
        List<ProdutoResponse> produtos = bucket.aggregations()
                .get("produtos")
                .topHits()
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(source -> source.to(ProdutoDocument.class))
                .map(this::toProdutoResponse)
                .toList();

        return new FaixaPreco(
                bucket.key(),
                bucket.docCount(),
                produtos
        );
    }
}
