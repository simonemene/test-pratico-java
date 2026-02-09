package com.java.test.repository;

import com.java.test.entity.MovimentoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovimentoRepository extends JpaRepository<MovimentoEntity,Long> {

	boolean existsByOrdine_OrdineIdAndProdotto_ProductIdAndFlgAnnullo(
			String idOrdine,
			String idProdotto,
			String annullo
	);

	Optional<MovimentoEntity> findByOrdine_OrdineIdAndProdotto_ProductId(String id,String prodotto);

	List<MovimentoEntity> findByOrdine_OrdineId(String id);

	@Query
			("""
					SELECT m.prodotto.productId
					FROM MovimentoEntity m
					WHERE m.ordine.ordineId = :ordine
					AND (m.flgAnnullo IS NULL OR m.flgAnnullo <> 'S')
					"""
			)
	List<String> elencoProdottiOrdine(@Param("ordine") String ordine);
}
