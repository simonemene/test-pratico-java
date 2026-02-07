package com.java.test.security;

import com.java.test.TestjavaApplicationTests;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.jdbc.Sql;

@AutoConfigureTestDatabase
public class UserDetailsServiceIntegrationTest  extends TestjavaApplicationTests {
    @Autowired
    private UserDetailsService service;

    @Sql(scripts = "classpath:sql/service/clienti/insert-utenti-security.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void recuperoUtente()
    {
        //given
        String utente = "prova@prova.com";
        //when
        UserDetails risultato = service.loadUserByUsername(utente);
        Assertions.assertThat(risultato.getAuthorities().size()).isEqualTo(1);
        Assertions.assertThat(risultato.getUsername()).isEqualTo(utente);
        Assertions.assertThat(risultato.getPassword()).isEqualTo("{bcrypt}PASSWORD");
    }
}
