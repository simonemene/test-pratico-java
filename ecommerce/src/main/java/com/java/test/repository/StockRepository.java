package com.java.test.repository;

import com.java.test.entity.StockEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<StockEntity,Long> {

	Optional<StockEntity>findByProdotto_ProductId(String id);

	Optional<StockEntity>findByProdotto_ProductIdAndProdotto_FlgAnnullo(String id,String annullo);

	List<StockEntity>findByProdotto_ProductIdIn(List<String> id);

	@Modifying
	@Query(
			"UPDATE StockEntity s SET s.quantita = s.quantita - :quantita" +
					" WHERE (s.quantita - :quantita) >= 0 AND s.prodotto.productId = :prodottoId"
	)
	int modificaQuantitaProdotto(@Param("quantita") int quantita,@Param("prodottoId") String prodottoId);

	@Modifying
	@Query(
			"UPDATE StockEntity s SET s.quantita = s.quantita + :quantita" +
					" WHERE s.prodotto.productId = :prodottoId"
	)
	int aggiungiQuantitaProdotto(@Param("quantita") int quantita,@Param("prodottoId") String prodottoId);

    Page<StockEntity> findAll(Pageable pageable);

}
