package com.java.test.repository;

import com.java.test.entity.OrdineEntity;
import com.java.test.enums.StatoOrdineEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Modifying
	@Query("""
    UPDATE OrdineEntity o
    SET o.flgAnnullo = 'S'
    WHERE o.ordineId = :idOrdine
      AND (o.flgAnnullo IS NULL OR o.flgAnnullo <> 'S')
""")
	int eliminaOrdine(String idOrdine);

	Page<OrdineEntity> findAll(Pageable pageable);

	Page<OrdineEntity> findByUtente_UtenteId(String id,Pageable pageable);
}
