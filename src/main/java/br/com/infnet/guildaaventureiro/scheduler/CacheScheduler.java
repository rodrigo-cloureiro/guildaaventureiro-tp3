package br.com.infnet.guildaaventureiro.scheduler;

import br.com.infnet.guildaaventureiro.dto.missao.TopMissoesRequest;
import br.com.infnet.guildaaventureiro.service.PainelTaticoMissaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheScheduler {
    private final PainelTaticoMissaoService painelTaticoMissaoService;

    // @Scheduled(cron = "0 */1 * * * *")
    @Scheduled(cron = "30 0 0 * * *")
    public void clearCache() {
        System.out.println("Limpando o cache...");
        painelTaticoMissaoService.evictCache();
        System.out.println("Cacheando consulta padrão");
        painelTaticoMissaoService.topMissoesDias(new TopMissoesRequest(15, 10));
    }
}
