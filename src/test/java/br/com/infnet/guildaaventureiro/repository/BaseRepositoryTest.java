package br.com.infnet.guildaaventureiro.repository;

import br.com.infnet.guildaaventureiro.config.TestCacheConfig;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestCacheConfig.class)
public abstract class BaseRepositoryTest {
}
