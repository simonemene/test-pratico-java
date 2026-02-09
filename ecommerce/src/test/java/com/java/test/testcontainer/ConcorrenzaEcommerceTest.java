package com.java.test.testcontainer;

import com.java.test.dto.DiminuisciProdottoOrdineRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.*;


@Disabled
@ActiveProfiles("test")
@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-completo.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import({TestContainerConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConcorrenzaEcommerceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String ORDINE_ID = "123A";
    private static final String PRODOTTO_ID = "rgvbdfgdf454345";

    @Test
    void dueThread_concorrenza_diminuzioneProdotto_lanciaOptimisticLock() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(2);

        Callable<ResponseEntity<String>> task = () -> {
            start.await();

            DiminuisciProdottoOrdineRequestDto request =
                    new DiminuisciProdottoOrdineRequestDto(PRODOTTO_ID, 1);

            HttpEntity<DiminuisciProdottoOrdineRequestDto> entity =
                    new HttpEntity<>(request);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            "/api/ordine/" + ORDINE_ID + "/prodotti/diminuisci",
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );

            end.countDown();
            return response;
        };

        Future<ResponseEntity<String>> f1 = executor.submit(task);
        Future<ResponseEntity<String>> f2 = executor.submit(task);

        start.countDown();
        end.await();

        ResponseEntity<String> r1 = f1.get();
        ResponseEntity<String> r2 = f2.get();

        boolean unoOk =
                r1.getStatusCode().is2xxSuccessful()
                        || r2.getStatusCode().is2xxSuccessful();

        boolean unoOptimisticLock =
                (r1.getStatusCode() == HttpStatus.CONFLICT
                        && r1.getBody().contains("OptimisticLockException"))
                        || (r2.getStatusCode() == HttpStatus.CONFLICT
                        && r2.getBody().contains("OptimisticLockException"));

        Assertions.assertThat(unoOk).isTrue();
        Assertions.assertThat(unoOptimisticLock).isTrue();

        executor.shutdown();
    }
}
