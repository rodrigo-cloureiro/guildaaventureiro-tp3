package br.com.infnet.guildaaventureiro.service;

import br.com.infnet.guildaaventureiro.dto.relatorio.RankingParticipacao;
import br.com.infnet.guildaaventureiro.dto.relatorio.RankingPartipacaoFiltroRequest;
import br.com.infnet.guildaaventureiro.dto.relatorio.RelatorioMissao;
import br.com.infnet.guildaaventureiro.dto.relatorio.RelatorioMissaoFiltroRequest;
import br.com.infnet.guildaaventureiro.exception.BusinessException;
import br.com.infnet.guildaaventureiro.repository.operacoes.ParticipacaoMissaoRepository;
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

        LocalDateTime termino = filtro.termino() != null ?
                filtro.termino() :
                now;
        LocalDateTime inicio = filtro.inicio() != null ?
                filtro.inicio() :
                termino.minusDays(INTERVALO_DIAS_PADRAO);

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
