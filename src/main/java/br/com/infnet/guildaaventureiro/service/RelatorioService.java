package br.com.infnet.guildaaventureiro.service;

import br.com.infnet.guildaaventureiro.dto.relatorio.RankingParticipacao;
import br.com.infnet.guildaaventureiro.dto.relatorio.RankingPartipacaoFiltroRequest;
import br.com.infnet.guildaaventureiro.dto.relatorio.RelatorioMissao;
import br.com.infnet.guildaaventureiro.dto.relatorio.RelatorioMissaoFiltroRequest;
import br.com.infnet.guildaaventureiro.exception.operacoes.BusinessException;
import br.com.infnet.guildaaventureiro.repository.aventura.ParticipacaoMissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    public static final int INTERVALO_DIAS_PADRAO = 30;

    private final ParticipacaoMissaoRepository participacaoMissaoRepository;

    // =======================
    // Ranking de Participação
    // =======================
    public List<RankingParticipacao> rankingParticipacao(RankingPartipacaoFiltroRequest filtro) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime inicio = filtro.inicio() == null ? now.minusDays(INTERVALO_DIAS_PADRAO) : filtro.inicio();
        LocalDateTime termino = filtro.termino() == null ? now : filtro.termino();

        if (termino.isBefore(inicio)) {
            throw new BusinessException("A data de término não pode ser anterior a data de início");
        }

        return participacaoMissaoRepository.rankingParticipacao(
                filtro.status(),
                inicio,
                termino
        );
    }

    // ====================
    // Relatório de Missões
    // ====================
    public List<RelatorioMissao> relatorioMissoes(RelatorioMissaoFiltroRequest filtro) {
        return participacaoMissaoRepository.relatorioMissoes(filtro.inicio(), filtro.termino());
    }
}
