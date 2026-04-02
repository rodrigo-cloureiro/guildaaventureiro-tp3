package br.com.infnet.guildaaventureiro.repository.operacoes;

import br.com.infnet.guildaaventureiro.dto.missao.TopMissoesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PainelTaticoMissaoRepositoryTest {
    @Autowired
    private PainelTaticoMissaoRepository painelTaticoMissaoRepository;

    @Test
    @DisplayName("Deve retornar as 10 missões mais relevantes do últimos 15 dias")
    public void shouldReturnTop10MostRelevantMissionsFromLast15Days() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoffDate = now.minusDays(15);

        List<TopMissoesResponse> top15Dias = painelTaticoMissaoRepository.top15Dias(cutoffDate, now);
        assertFalse(top15Dias.isEmpty());
        assertTrue(top15Dias.stream().allMatch(m ->
                        !m.ultimaAtualizacao().isBefore(cutoffDate) && !m.ultimaAtualizacao().isAfter(now)
                )
        );
    }
}
