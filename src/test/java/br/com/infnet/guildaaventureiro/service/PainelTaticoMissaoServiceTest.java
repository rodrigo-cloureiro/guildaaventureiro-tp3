package br.com.infnet.guildaaventureiro.service;

import br.com.infnet.guildaaventureiro.dto.missao.TopMissoesRequest;
import br.com.infnet.guildaaventureiro.repository.operacoes.PainelTaticoMissaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PainelTaticoMissaoServiceTest {
    @Mock
    private PainelTaticoMissaoRepository painelTaticoMissaoRepository;
    @InjectMocks
    private PainelTaticoMissaoService painelTaticoMissaoService;

    @Test
    @DisplayName("Deve aplicar valores default quando não forem informados")
    public void shouldApplyDefaultValues() {
        int limite = 10;
        TopMissoesRequest dto = new TopMissoesRequest(null, null);

        when(painelTaticoMissaoRepository.topMissoesDias(any(), any(), eq(limite)))
                .thenReturn(List.of());

        painelTaticoMissaoService.topMissoesDias(dto);
        verify(painelTaticoMissaoRepository, times(1))
                .topMissoesDias(any(), any(), eq(limite));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "10, 15",
            "10, 20",
            "3, 15",
            "3, 20"
    })
    @DisplayName("Deve retornar as X missões mais relevantes do últimos Y dias")
    public void shouldReturnTopMissionsWithinPeriod(int limite, int dias) {
        TopMissoesRequest dto = new TopMissoesRequest(dias, limite);

        when(painelTaticoMissaoRepository.topMissoesDias(any(), any(), eq(limite)))
                .thenReturn(List.of());

        painelTaticoMissaoService.topMissoesDias(dto);
        verify(painelTaticoMissaoRepository, times(1))
                .topMissoesDias(any(), any(), eq(limite));
    }
}
