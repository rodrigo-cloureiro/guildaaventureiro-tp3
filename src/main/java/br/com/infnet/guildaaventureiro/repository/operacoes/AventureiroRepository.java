package br.com.infnet.guildaaventureiro.repository.operacoes;

import br.com.infnet.guildaaventureiro.domain.aventura.Aventureiro;
import br.com.infnet.guildaaventureiro.domain.aventura.enums.AventureiroClasse;
import br.com.infnet.guildaaventureiro.dto.aventureiro.AventureiroMinimalResponse;
import br.com.infnet.guildaaventureiro.dto.aventureiro.AventureiroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AventureiroRepository extends JpaRepository<Aventureiro, Long> {
    @Query(value = """
            SELECT new br.com.infnet.guildaaventureiro.dto.aventureiro.AventureiroResponse(
                        a.id, a.nome, a.classe, a.nivel, a.ativo
            )
            FROM Aventureiro a
            WHERE (:status IS NULL OR a.ativo = :status) AND
            (:classe IS NULL OR a.classe = :classe) AND
            (:nivelMinimo IS NULL OR a.nivel >= :nivelMinimo)
            """)
    Page<AventureiroResponse> findByStatusAndClasseAndNivelMinimo(
            @Param(value = "status") Boolean status,
            @Param(value = "classe") AventureiroClasse classe,
            @Param(value = "nivelMinimo") Integer nivelMinimoInteger,
            Pageable pageable
    );

    @Query(value = """
            SELECT new br.com.infnet.guildaaventureiro.dto.aventureiro.AventureiroMinimalResponse(
                        a.id, a.nome, a.classe, a.organizacao.nome
            )
            FROM Aventureiro a
            WHERE LOWER(a.nome) LIKE CONCAT('%', LOWER(:nome), '%')
            """)
    Page<AventureiroMinimalResponse> findByNomeContainingIgnoreCase(
            @Param(value = "nome") String nome,
            Pageable pageable
    );
}
