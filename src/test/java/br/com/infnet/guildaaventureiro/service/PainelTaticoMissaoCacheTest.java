package br.com.infnet.guildaaventureiro.service;

import br.com.infnet.guildaaventureiro.config.TestCacheConfig;
import br.com.infnet.guildaaventureiro.dto.missao.TopMissoesRequest;
import br.com.infnet.guildaaventureiro.dto.missao.TopMissoesResponse;
import br.com.infnet.guildaaventureiro.repository.operacoes.PainelTaticoMissaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {
        PainelTaticoMissaoService.class,
        CacheAutoConfiguration.class,
        TestCacheConfig.class
})
@EnableCaching
public class PainelTaticoMissaoCacheTest {

    @Autowired
    private PainelTaticoMissaoService painelTaticoMissaoService;
    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private PainelTaticoMissaoRepository painelTaticoMissaoRepository;

    @BeforeEach
    public void clearCache() {
        cacheManager.resetCaches();
    }

    @Test
    @DisplayName("Deve retornar dados do cache a partir da segunda chamada")
    public void mustUseCache() {
        TopMissoesRequest dto = new TopMissoesRequest(10, 15);

        when(painelTaticoMissaoRepository.topMissoesDias(any(), any(), anyInt()))
                .thenReturn(List.of());

        List<TopMissoesResponse> result1 = painelTaticoMissaoService.topMissoesDias(dto);
        List<TopMissoesResponse> result2 = painelTaticoMissaoService.topMissoesDias(dto);
        List<TopMissoesResponse> result3 = painelTaticoMissaoService.topMissoesDias(dto);

        verify(painelTaticoMissaoRepository, times(1))
                .topMissoesDias(any(), any(), anyInt());
        verifyNoMoreInteractions(painelTaticoMissaoRepository);
        assertEquals(result1, result2);
        assertEquals(result1, result3);
    }

    @Test
    @DisplayName("Deve limpar o cache e forçar nova chamada ao repository")
    public void mustEvictCache() {
        TopMissoesRequest dto = new TopMissoesRequest(10, 15);

        when(painelTaticoMissaoRepository.topMissoesDias(any(), any(), anyInt()))
                .thenReturn(List.of());

        painelTaticoMissaoService.topMissoesDias(dto);
        painelTaticoMissaoService.evictCache();
        painelTaticoMissaoService.topMissoesDias(dto);

        verify(painelTaticoMissaoRepository, times(2))
                .topMissoesDias(any(), any(), anyInt());
    }
}
