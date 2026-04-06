package br.com.infnet.guildaaventureiro.controller.elastic;

import br.com.infnet.guildaaventureiro.domain.elastic.ProdutoDocument;
import br.com.infnet.guildaaventureiro.service.elastic.ProdutoDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos/busca")
@RequiredArgsConstructor
public class ProdutoDocumentSearchController {

    private final ProdutoDocumentService produtoDocumentService;

    @GetMapping(value = "/nome")
    public ResponseEntity<List<ProdutoDocument>> buscarPorNomeProduto(
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorNome(termo));
    }

    @GetMapping(value = "/descricao")
    public ResponseEntity<List<ProdutoDocument>> buscarPorDescricaoProduto(
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorDescricao(termo));
    }

    @GetMapping(value = "/frase")
    public ResponseEntity<List<ProdutoDocument>> buscarPorFraseExata(
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorFraseExata(termo));
    }

    @GetMapping(value = "/fuzzy")
    public ResponseEntity<List<ProdutoDocument>> buscarPorNomeComTolerancia(
            @RequestParam(value = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorNomeComTolerancia(termo));
    }

    @GetMapping(value = "/multicampos")
    public ResponseEntity<List<ProdutoDocument>> buscarPorNomeEDescricao(
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorNomeEDescricao(termo));
    }

    @GetMapping(value = "/com-filtro")
    public ResponseEntity<List<ProdutoDocument>> buscarPorDescricaoECategoria(
            @RequestParam(name = "termo") String termo,
            @RequestParam(name = "categoria") String categoria
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorDescricaoECategoria(termo, categoria));
    }

    @GetMapping(value = "/faixa-preco")
    public ResponseEntity<List<ProdutoDocument>> buscarPorFaixaPreco(
            @RequestParam(name = "min", defaultValue = "0") double min,
            @RequestParam(name = "max", defaultValue = "100") double max
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorFaixaPreco(min, max));
    }

    @GetMapping(value = "/avancada")
    public ResponseEntity<List<ProdutoDocument>> buscaCombinada(
            @RequestParam(name = "categoria") String categoria,
            @RequestParam(name = "raridade") String raridade,
            @RequestParam(name = "min") double min,
            @RequestParam(name = "max") double max
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscaCombinada(categoria, raridade, min, max));
    }
}
