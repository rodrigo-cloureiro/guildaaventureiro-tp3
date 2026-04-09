package br.com.infnet.guildaaventureiro.service.elastic;

import br.com.infnet.guildaaventureiro.domain.elastic.ProdutoDocument;
import br.com.infnet.guildaaventureiro.dto.elastic.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.elasticsearch.test.autoconfigure.DataElasticsearchTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataElasticsearchTest
@Import({
        ProdutoDocumentService.class,
        ProdutoQueryService.class
})
public class ProdutoDocumentServiceTest {

    @MockitoBean
    private CacheManager cacheManager;

    @Autowired
    private ProdutoDocumentService service;

    @Test
    public void buscarPorNome() {
        String termo = "Espada";

        List<ProdutoDocument> result = service.buscarPorNome(termo);
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getNome()
                .toLowerCase().
                contains(termo.toLowerCase())
        ));
    }

    @Test
    public void buscarPorDescricao() {
        String termo = "norte";

        List<ProdutoDocument> result = service.buscarPorDescricao(termo);
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getDescricao()
                .toLowerCase()
                .contains(termo)
        ));
    }

    @Test
    public void buscarPorFraseExata() {
        String termo = "artefato raro";

        List<ProdutoDocument> result = service.buscarPorFraseExata(termo);
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getDescricao()
                .toLowerCase()
                .contains(termo)
        ));
    }

    @Test
    public void buscarPorNomeComTolerancia() {
        String fuzzy = "espdaa";
        String termo = "espada";

        List<ProdutoDocument> result = service.buscarPorNomeComTolerancia(fuzzy);
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getNome()
                .toLowerCase()
                .contains(termo)
        ));
    }

    @Test
    public void buscarPorNomeEDescricao() {
        String termo = "Antiga";

        List<ProdutoDocument> result = service.buscarPorNomeEDescricao(termo);
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getNome()
                .toLowerCase()
                .contains(termo.toLowerCase()) ||
                r.getDescricao()
                        .toLowerCase()
                        .contains(termo.toLowerCase())
        ));
    }

    @Test
    public void buscarPorDescricaoECategoria() {
        String descricao = "antiga";
        String categoria = "ingredientes";

        List<ProdutoDocument> result = service.buscarPorDescricaoECategoria(descricao, categoria);
        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .allMatch(r -> r
                        .getDescricao()
                        .toLowerCase()
                        .contains(descricao.toLowerCase()) &&
                        r.getCategoria()
                                .equalsIgnoreCase(categoria))
        );
    }

    @Test
    public void buscarPorFaixaPreco() {
        double min = 100;
        double max = 150;

        List<ProdutoDocument> result = service.buscarPorFaixaPreco(min, max);
        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .allMatch(p -> p.getPreco() >= min && p.getPreco() <= max)
        );
    }

    @Test
    public void buscaCombinada() {
        String categoria = "armas";
        String raridade = "epico";
        double min = 1_990;
        double max = 2_010;

        List<ProdutoDocument> result = service.buscaCombinada(categoria, raridade, min, max);
        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .allMatch(r -> r.getCategoria()
                        .equalsIgnoreCase(categoria) &&
                        r.getRaridade()
                                .equalsIgnoreCase(raridade) &&
                        r.getPreco() >= min && r.getPreco() <= max
                )
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "categoria",
            "raridade"
    })
    public void quantidadeProdutosPorCampo(String campo) {
        List<ContagemCampoAggregation> result = service.quantidadeProdutosPorCampo(campo);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(r ->
                r.quantidade() >= 0 && r.campo().equalsIgnoreCase(campo)
        ));
    }

    @Test
    public void precoMedioProdutos() {
        PrecoMedioAggregation result = service.precoMedioProdutos();
        assertNotNull(result.precoMedio());
        assertTrue(result.precoMedio() >= 0);
    }

    @Test
    public void agruparEmFaixaPreco() {
        List<FaixaPreco> result = service.agruparEmFaixaPreco();
        assertFalse(result.isEmpty());
    }
}
