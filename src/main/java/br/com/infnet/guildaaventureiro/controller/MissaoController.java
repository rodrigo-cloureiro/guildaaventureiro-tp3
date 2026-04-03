package br.com.infnet.guildaaventureiro.controller;

import br.com.infnet.guildaaventureiro.dto.missao.AdicionarParticipanteMissao;
import br.com.infnet.guildaaventureiro.dto.missao.MissaoCreate;
import br.com.infnet.guildaaventureiro.dto.PagedResponse;
import br.com.infnet.guildaaventureiro.dto.missao.*;
import br.com.infnet.guildaaventureiro.service.MissaoService;
import br.com.infnet.guildaaventureiro.service.PainelTaticoMissaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/missoes")
public class MissaoController {
    private final MissaoService missaoService;
    private final PainelTaticoMissaoService painelTaticoMissaoService;

    // ==============
    // Listar Missões
    // ==============
    @GetMapping(value = "")
    public ResponseEntity<PagedResponse<MissaoResponse>> listarMissoes(
            @Valid MissaoFiltroRequest filtro,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        PagedResponse<MissaoResponse> pagedResponse = missaoService.listar(filtro, pageable);
        return ResponseEntity.ok()
                .header("X-Page", String.valueOf(pagedResponse.page()))
                .header("X-Size", String.valueOf(pagedResponse.size()))
                .header("X-Total-Count", String.valueOf(pagedResponse.total()))
                .header("X-Total-Pages", String.valueOf(pagedResponse.totalPages()))
                .body(pagedResponse);
    }

    // ====================
    // Buscar Missão por ID
    // ====================
    @GetMapping(value = "/{id}")
    public ResponseEntity<MissaoDetailedResponse> detalharMissao(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(missaoService.missaoDetalhada(id));
    }

    // ===========
    // Top X dias
    // ===========
    @GetMapping(value = "/top") // TODO adicionar validação/range de dias (min = 3 e max = 90)
    public ResponseEntity<List<TopMissoesResponse>> topMissoes15Dias(
            @RequestParam(name = "dias", defaultValue = "15") int dias
    ) {
        return ResponseEntity.ok()
                .body(painelTaticoMissaoService.topMissoesDias(dias));
    }

    // ================
    // Registrar Missão
    // ================
    @PostMapping(value = "")
    public ResponseEntity<MissaoResponse> registrarMissao(@RequestBody @Valid MissaoCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(missaoService.criar(dto));
    }

    // ================
    // Atualizar Missão
    // ================
    @PatchMapping(value = "/{id}")
    public ResponseEntity<MissaoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid MissaoUpdate dto
    ) {
        return ResponseEntity.ok()
                .body(missaoService.atualizar(id, dto));
    }

    // ==============
    // Iniciar Missão
    // ==============
    @PatchMapping(value = "/{id}/iniciar")
    public ResponseEntity<Void> iniciar(@PathVariable Long id) {
        missaoService.iniciar(id);
        return ResponseEntity.noContent()
                .build();
    }

    // ===============
    // Concluir Missão
    // ===============
    @PatchMapping(value = "/{id}/concluir")
    public ResponseEntity<Void> concluir(@PathVariable Long id) {
        missaoService.concluir(id);
        return ResponseEntity.noContent()
                .build();
    }

    // ===============
    // Cancelar Missão
    // ===============
    @PatchMapping(value = "/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        missaoService.cancelar(id);
        return ResponseEntity.noContent()
                .build();
    }

    // ========================
    // Recompensar Participante
    // ========================
    @PatchMapping(value = "/{missaoId}/participantes/{participanteId}/recompensar")
    public ResponseEntity<Void> recompensar(
            @PathVariable Long missaoId,
            @PathVariable Long participanteId,
            @RequestBody @Valid RecompensarParticipante dto
    ) {
        missaoService.recompensarParticipante(missaoId, participanteId, dto);
        return ResponseEntity.noContent()
                .build();
    }

    // ================================
    // Adicionar Participante na Missão
    // ================================
    @PostMapping(value = "/{id}/participantes")
    public ResponseEntity<Void> adicionarParticipante(
            @PathVariable Long id,
            @RequestBody @Valid AdicionarParticipanteMissao dto
    ) {
        missaoService.adicionarParticipante(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
