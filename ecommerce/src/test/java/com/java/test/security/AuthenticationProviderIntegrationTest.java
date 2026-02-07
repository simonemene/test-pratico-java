package com.java.test.security;

import com.java.test.TestjavaApplicationTests;
import com.java.test.exception.CredenzialiInvalideException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;

@AutoConfigureTestDatabase
public class AuthenticationProviderIntegrationTest extends TestjavaApplicationTests {

    @Autowired
    private AuthenticationProvider provider;

    @Sql(scripts = "classpath:sql/service/clienti/insert-utenti-security.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void autenticazioneSuccesso()
    {
        //given
        Authentication authentication = new UsernamePasswordAuthenticationToken("prova3@prova.com","prova");
        //when
        Authentication token = provider.authenticate(authentication);
        Assertions.assertThat(token.isAuthenticated()).isTrue();
    }

    @Sql(scripts = "classpath:sql/service/clienti/insert-utenti-security.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void autenticazioneFallita()
    {
        //given
        Authentication authentication = new UsernamePasswordAuthenticationToken("prova1@prova.com","prova");
        //when
        Assertions.assertThatThrownBy(()->provider.authenticate(authentication))
                .isInstanceOf(CredenzialiInvalideException.class).hasMessageContaining("Credenziali invalide");
    }
}
