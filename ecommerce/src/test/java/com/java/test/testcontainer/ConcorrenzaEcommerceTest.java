package com.java.test.testcontainer;


import com.java.test.entity.StockEntity;
import com.java.test.enums.AnnulloEnum;
import com.java.test.repository.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@ActiveProfiles("test")
@AutoConfigureTestDatabase
@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:sql/service/ordini/ordini-concorrenza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConcorrenzaEcommerceTest {

    private static final String PRODOTTO_ID = "dgfgdfgdf45mnbv";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private StockRepository stockRepository;

    @Test
    void testConcorrenza() {

        TransactionTemplate tx1 = new TransactionTemplate(transactionManager);
        TransactionTemplate tx2 = new TransactionTemplate(transactionManager);

        StockEntity prodotto1 = tx1.execute(status ->
                stockRepository.findByProdotto_ProductIdAndProdotto_FlgAnnullo(PRODOTTO_ID, AnnulloEnum.N.name()).orElseThrow()
        );

        StockEntity prodotto2 = tx2.execute(status ->
                stockRepository.findByProdotto_ProductIdAndProdotto_FlgAnnullo(PRODOTTO_ID, AnnulloEnum.N.name()).orElseThrow()
        );

        tx1.execute(status -> {
            prodotto1.aumentaQuantitaMagazzino(1);
            stockRepository.saveAndFlush(prodotto1);
            return null;
        });

        Assertions.assertThatThrownBy(() ->
                tx2.execute(status -> {
                    prodotto2.aumentaQuantitaMagazzino(1);
                    stockRepository.saveAndFlush(prodotto2);
                    return null;
                })
        ).isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}
