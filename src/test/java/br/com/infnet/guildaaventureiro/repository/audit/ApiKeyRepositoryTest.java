package br.com.infnet.guildaaventureiro.repository.audit;

import br.com.infnet.guildaaventureiro.domain.audit.ApiKey;
import br.com.infnet.guildaaventureiro.repository.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ApiKeyRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Test
    public void findAllTest() {
        List<ApiKey> all = apiKeyRepository.findAll();
        all.forEach(apiKey -> {
            System.out.println(apiKey.getId());
            System.out.println(apiKey.getNome());
            System.out.println(apiKey.getKeyHash());
            System.out.println(apiKey.isAtivo());
            System.out.println(apiKey.getCreatedAt());
            System.out.println(apiKey.getLastUsedAt());
            System.out.println(apiKey.getOrganizacao());
            System.out.println("#################################");
        });
        assertFalse(all.isEmpty());
    }
}
