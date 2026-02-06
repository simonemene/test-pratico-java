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

	@Modifying()
	@Query("""
    UPDATE MovimentoEntity m
    SET m.flgAnnullo = 'S'
    WHERE m.ordine.ordineId = :idOrdine
      AND m.prodotto.productId IN (:idPubbliciProdotti)
      AND (m.flgAnnullo IS NULL OR m.flgAnnullo <> 'S')
""")
	int eliminaProdottiOrdine(
			@Param("idOrdine") String idOrdine,
			@Param("idPubbliciProdotti") List<String> idPubbliciProdotti
	);
}
