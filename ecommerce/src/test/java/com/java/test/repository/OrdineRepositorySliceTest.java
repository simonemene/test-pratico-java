package com.java.test.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@Sql(scripts = "classpath:sql/service/prodotti/insert-prodotti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase
public class OrdineRepositorySliceTest {
    @Autowired
    private OrdineRepository repository;

    @Test
    public void trovaOrdinePerId()
    {
        //given

        //when
    }
}
