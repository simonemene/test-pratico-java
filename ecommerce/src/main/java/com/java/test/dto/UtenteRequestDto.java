package com.java.test.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UtenteRequestDto(@NotBlank(message = "Il nome non deve essere vuoto") String nome,
							  @NotBlank(message = "Il cognome non deve essere vuoto") String cognome,
							  @NotBlank(message = "La e-mail non deve essere vuota") @Email(message = "La mail non rispetta i parametri") String email,
							  @Length(min = 16,max = 16,message = "Il codice fiscale deve essere lungo 16") String codiceFiscale,
							   @NotBlank(message = "La password deve essere valorizzata")
							   @Length(min = 10, message = "La password deve essere lunga almeno 10 caratteri") String password) {
}
