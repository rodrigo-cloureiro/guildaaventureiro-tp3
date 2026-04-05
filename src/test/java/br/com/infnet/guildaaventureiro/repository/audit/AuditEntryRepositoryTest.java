package br.com.infnet.guildaaventureiro.repository.audit;

import br.com.infnet.guildaaventureiro.domain.audit.AuditEntry;
import br.com.infnet.guildaaventureiro.repository.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AuditEntryRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private AuditEntryRepository auditEntryRepository;

    @Test
    public void findAllTest() {
        List<AuditEntry> all = auditEntryRepository.findAll();
        all.forEach(System.out::println);
        assertFalse(all.isEmpty());
    }
}
