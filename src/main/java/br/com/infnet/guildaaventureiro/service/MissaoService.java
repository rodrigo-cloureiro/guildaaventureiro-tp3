package br.com.infnet.guildaaventureiro.service;

import br.com.infnet.guildaaventureiro.domain.audit.Organizacao;
import br.com.infnet.guildaaventureiro.domain.aventura.Aventureiro;
import br.com.infnet.guildaaventureiro.domain.aventura.Missao;
import br.com.infnet.guildaaventureiro.domain.aventura.ParticipacaoMissao;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.StatusMissao;
import br.com.infnet.guildaaventureiro.dto.AdicionarParticipanteMissao;
import br.com.infnet.guildaaventureiro.dto.missao.MissaoCreate;
import br.com.infnet.guildaaventureiro.dto.aventureiro.AventureiroMissaoResponse;
import br.com.infnet.guildaaventureiro.dto.PagedResponse;
import br.com.infnet.guildaaventureiro.dto.missao.*;
import br.com.infnet.guildaaventureiro.exception.operacoes.BusinessException;
import br.com.infnet.guildaaventureiro.mapper.MissaoMapper;
import br.com.infnet.guildaaventureiro.repository.operacoes.MissaoRepository;
import br.com.infnet.guildaaventureiro.repository.operacoes.ParticipacaoMissaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MissaoService {
    private final MissaoRepository missaoRepository;
    private final ParticipacaoMissaoRepository participacaoMissaoRepository;
    private final AventureiroService aventureiroService;
    private final OrganizacaoService organizacaoService;

    // ==============
    // Listar Missões
    // ==============
    public PagedResponse<MissaoResponse> listar(MissaoFiltroRequest filtro, Pageable pageable) {
        Page<MissaoResponse> responsePage = missaoRepository.findMissoesByFilters(
                filtro.status(),
                filtro.nivelPerigo(),
                filtro.tipoData() != null ? filtro.tipoData().name() : null,
                filtro.de(),
                filtro.ate(),
                pageable
        );
        return new PagedResponse<>(
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.getContent()
        );
    }

    // ====================
    // Buscar Missão por ID
    // ====================
    @Transactional(readOnly = true)
    public MissaoDetailedResponse missaoDetalhada(Long id) {
        Missao missao = missaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Missão não encontrada"));

        List<AventureiroMissaoResponse> participantes = participacaoMissaoRepository.findParticipantesByMissaoId(id);

        return new MissaoDetailedResponse(MissaoMapper.toResponse(missao), participantes);
    }

    // ================
    // Registrar missão
    // ================
    public MissaoResponse criar(MissaoCreate dto) {
        Organizacao organizacao = organizacaoService.findById(dto.organizacaoId());
        Missao missao = MissaoMapper.toMissao(dto, organizacao);
        return MissaoMapper.toResponse(missaoRepository.save(missao));
    }

    // ================
    // Atualizar Missão
    // ================
    @Transactional(readOnly = false)
    public MissaoResponse atualizar(Long id, MissaoUpdate dto) {
        Missao missao = findById(id);
        missao.validarAlteracao();

        if (dto.titulo() != null) {
            missao.alterarTitulo(dto.titulo());
        }
        if (dto.nivelPerigo() != null) {
            missao.alterarNivelPerigo(dto.nivelPerigo());
        }

        return MissaoMapper.toResponse(missao);
    }

    // ==============
    // Iniciar Missão
    // ==============
    @Transactional(readOnly = false)
    public void iniciar(Long id) {
        Missao missao = findById(id);
        missao.iniciarMissao();
    }

    // ===============
    // Concluir Missão
    // ===============
    @Transactional(readOnly = false)
    public void concluir(Long id) {
        Missao missao = findById(id);
        missao.concluirMissao();

        List<ParticipacaoMissao> participacao = participacaoMissaoRepository
                .findParticipacoesComMaiorRecompensa(missao.getId());

        missao.definirMvp(participacao);
    }

    // ===============
    // Cancelar Missão
    // ===============
    @Transactional(readOnly = false)
    public void cancelar(Long id) {
        Missao missao = findById(id);
        missao.cancelarMissao();
    }

    // ========================
    // Recompensar Participante
    // ========================
    @Transactional(readOnly = false)
    public void recompensarParticipante(
            Long missaoId,
            Long participanteId,
            RecompensarParticipante dto
    ) {
        Missao missao = findById(missaoId);

        if (!missao.getStatus().equals(StatusMissao.EM_ANDAMENTO)) {
            throw new BusinessException(
                    "Não é possível recompensar participantes em missões que não foram iniciadas"
            );
        }

        ParticipacaoMissao participacao = participacaoMissaoRepository
                .findByMissaoIdAndAventureiroId(missaoId, participanteId)
                .orElseThrow(() -> new EntityNotFoundException("Não foi encontrada participação na missão"));

        participacao.recompensar(dto.recompensa());
    }

    // ================================
    // Adicionar Participante na Missão
    // ================================
    @Transactional(readOnly = false)
    public void adicionarParticipante(Long missaoId, AdicionarParticipanteMissao dto) {
        Missao missao = findById(missaoId);
        Aventureiro aventureiro = aventureiroService.buscarAventureiroPorId(dto.participanteId());

        missao.adicionarParticipante(aventureiro, dto.papelMissao());
    }

    private Missao findById(Long id) {
        return missaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Missão não encontrada"));
    }
}
