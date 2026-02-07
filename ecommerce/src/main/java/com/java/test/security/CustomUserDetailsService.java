package com.java.test.security;

import com.java.test.entity.UtenteEntity;
import com.java.test.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(null != email)
        {
            UtenteEntity utente = repository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
            GrantedAuthority authorities =
                                    new SimpleGrantedAuthority(utente.getRuolo().getRuolo().name());
            return new User(utente.getEmail(), utente.passwordHash(), List.of(authorities));
        }
        throw new UsernameNotFoundException("Utente non trovato");
    }
}
