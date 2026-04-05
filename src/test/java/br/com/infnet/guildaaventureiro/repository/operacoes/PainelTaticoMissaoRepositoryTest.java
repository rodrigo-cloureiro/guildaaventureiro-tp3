package br.com.infnet.guildaaventureiro.repository.operacoes;

import br.com.infnet.guildaaventureiro.repository.BaseRepositoryTest;
import br.com.infnet.guildaaventureiro.dto.missao.TopMissoesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PainelTaticoMissaoRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private PainelTaticoMissaoRepository painelTaticoMissaoRepository;

    @ParameterizedTest
    @CsvSource(value = {
            "10, 15",
            "10, 20",
            "3, 15",
            "3, 20"
    })
    @DisplayName("Deve retornar as X missões mais relevantes do últimos Y dias")
    public void shouldReturnTopMostRelevantMissionsFromLastDays(int limite, int dias) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoffDate = now.minusDays(dias);

        List<TopMissoesResponse> topMissoesDias = painelTaticoMissaoRepository.topMissoesDias(cutoffDate, now, limite);
        assertTrue(topMissoesDias.size() <= limite);
        assertTrue(topMissoesDias.stream().allMatch(m ->
                        !m.ultimaAtualizacao().isBefore(cutoffDate) && !m.ultimaAtualizacao().isAfter(now)
                )
        );
    }
}
