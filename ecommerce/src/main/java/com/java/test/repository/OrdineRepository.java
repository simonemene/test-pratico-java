package com.java.test.repository;

import com.java.test.entity.OrdineEntity;
import com.java.test.enums.StatoOrdineEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrdineRepository extends JpaRepository<OrdineEntity,Long> {

	Optional<OrdineEntity> findByOrdineId(String id);

	Optional<OrdineEntity> findByOrdineIdAndStatoOrdine(String id, StatoOrdineEnum stato);

	@Modifying
	@Query("""
    UPDATE OrdineEntity o
    SET o.flgAnnullo = 'S'
    WHERE o.ordineId = :idOrdine
      AND EXISTS (
          SELECT 1
          FROM MovimentoEntity m
          WHERE m.ordine = o
            AND m.prodotto.productId IN :idPubbliciProdotti
      )
    """)
	int eliminaOrdiniSoftDelete(@Param("idOrdine") String idOrdine,@Param("idPubbliciProdotti") List<String> idPubbliciProdotti);
}
