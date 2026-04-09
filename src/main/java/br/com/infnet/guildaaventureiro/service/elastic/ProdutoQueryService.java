package br.com.infnet.guildaaventureiro.service.elastic;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdutoQueryService {

    public MatchQuery matchQuery(String field, String value) {
        return MatchQuery.of(q -> q
                .field(field)
                .query(value)
        );
    }

    public MatchPhraseQuery matchPhraseQuery(String field, String value) {
        return MatchPhraseQuery.of(q -> q
                .field(field)
                .query(value)
        );
    }

    public FuzzyQuery fuzzyQuery(String field, String value) {
        return FuzzyQuery.of(q -> q
                .field(field)
                .value(value)
                .fuzziness("AUTO")
        );
    }

    public MultiMatchQuery multiMatchQuery(List<String> fields, String value) {
        return MultiMatchQuery.of(q -> q
                .fields(fields)
                .query(value)
        );
    }

    public NumberRangeQuery numberRangeQuery(String field, double min, double max) {
        return NumberRangeQuery.of(q -> q
                .field(field)
                .gte(min)
                .lte(max)
        );
    }

    public TermQuery termQuery(String field, String value) {
        return TermQuery.of(q -> q
                .field(field)
                .value(value)
        );
    }

    public Aggregation termsAggregation(String field) {
        return Aggregation.of(a -> a
                .terms(t -> t
                        .field(field))
        );
    }

    public AverageAggregation averageAggregation(String field) {
        return AverageAggregation.of(a -> a
                .field(field)
        );
    }

    public RangeAggregation rangeAggregation(String field, List<AggregationRange> ranges) {
        return Aggregation.of(a -> a
                        .range(RangeAggregation.of(r -> r
                                .field(field)
                                .ranges(ranges))
                        )
                )
                .range();
    }
}
