package com.java.test.repository;

import com.java.test.entity.OrdineEntity;
import com.java.test.enums.StatoOrdineEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase
public class OrdineRepositorySliceTest {


    @Autowired
    private OrdineRepository repository;

    @Sql(scripts = "classpath:sql/service/ordini/insert-ordini-stato.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void trovaOrdinePerId()
    {
        //given
        String id = "123A";
        //when
        OrdineEntity ordine = repository.findByOrdineId(id).get();
        //then
        Assertions.assertThat(ordine.getStatoOrdine()).isEqualTo(StatoOrdineEnum.CREATO);
        Assertions.assertThat(ordine.getOrdineId()).isEqualTo("123A");
        Assertions.assertThat(ordine.getUtente().getUtenteId()).isEqualTo("dffgdfgfgbfgbgbgbfgb454534");
    }

    @Sql(scripts = "classpath:sql/service/ordini/insert-ordini-stato.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void trovaOrdiniCreati()
    {
        //given
        //when
        OrdineEntity ordineCreato = repository.findByOrdineIdAndStatoOrdine("123A",
                StatoOrdineEnum.CREATO).get();
        //then
        Assertions.assertThat(ordineCreato.getStatoOrdine()).isEqualTo(StatoOrdineEnum.CREATO);
        Assertions.assertThat(ordineCreato.getUtente().getUtenteId()).isEqualTo("dffgdfgfgbfgbgbgbfgb454534");
    }

    @Sql(scripts = "classpath:sql/service/ordini/insert-ordini-stato.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void trovaOrdiniCreatiNonEsistente()
    {
        //given
        //when
        //then
        Assertions.assertThat(repository.findByOrdineIdAndStatoOrdine("123A",
                StatoOrdineEnum.CONSEGNATO)).isNotPresent();
    }

    @Sql(scripts = "classpath:sql/service/ordini/insert-ordini-stato.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void modificaOrdineSoftDelete()
    {
        //given
        //when
        //then
        Assertions.assertThat(repository.eliminaProdottiOrdine("123A",
                List.of("rgvbdfgdf454345"))).isEqualTo(1);
    }
}
