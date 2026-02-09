package com.java.test.cache;

import com.java.test.TestjavaApplicationTests;
import com.java.test.dto.ProdottoRequestDto;
import com.java.test.repository.ProdottoRepository;
import com.java.test.service.IProdottoService;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

@AutoConfigureTestDatabase
public class ProdottiCacheIntegrationTest extends TestjavaApplicationTests {

    @MockitoSpyBean
    private ProdottoRepository repository;

    @Autowired
    private IProdottoService service;


    @WithMockUser(username = "prova@prova.com")
    @Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void inserisciProdotto()
    {
        //given
        ProdottoRequestDto prodotto = new ProdottoRequestDto(new BigDecimal("1.2"),"computer",10);
        //when
      service.inserisciUnProdotto(prodotto);
      service.prendiListaProdotti();
      service.prendiListaProdotti();
        //then
        verify(repository, times(1)).findAll();

    }

    @WithMockUser(username = "prova@prova.com")
    @Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void inserisciProdottoDueVolte()
    {
        //given
        ProdottoRequestDto prodotto = new ProdottoRequestDto(new BigDecimal("1.2"),"computer",10);
        //when
        service.inserisciUnProdotto(prodotto);
        service.prendiListaProdotti();
        service.prendiListaProdotti();
        ProdottoRequestDto prodotto2 = new ProdottoRequestDto(new BigDecimal("1.2"),"computer2",10);
        service.inserisciUnProdotto(prodotto2);
        service.prendiListaProdotti();
        service.prendiListaProdotti();
        //then

        verify(repository, times(2)).findAll();

    }
}
