package br.com.infnet.guildaaventureiro.service;

import br.com.infnet.guildaaventureiro.dto.missao.TopMissoesRequest;
import br.com.infnet.guildaaventureiro.dto.missao.TopMissoesResponse;
import br.com.infnet.guildaaventureiro.repository.operacoes.PainelTaticoMissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PainelTaticoMissaoService {
    private final PainelTaticoMissaoRepository painelTaticoMissaoRepository;

    // ===========
    // Top X dias
    // ===========
    public List<TopMissoesResponse> topMissoesDias(TopMissoesRequest dto) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoffDate = now.minusDays(dto.dias());

        return painelTaticoMissaoRepository.topMissoesDias(cutoffDate, now, dto.limite());
    }
}
