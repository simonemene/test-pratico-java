package com.java.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UtenteDto(@NotBlank(message = "Il nome non deve essere vuoto") String nome,
						@NotBlank(message = "Il cognome non deve essere vuoto") String cognome,
						@NotBlank(message = "La e-mail non deve essere vuota") @Email(message = "La mail non rispetta i parametri") String email,
						@Length(min = 16,max = 16,message = "Il codice fiscale deve essere lungo 16") String codiceFiscale,
						@JsonProperty(access = JsonProperty.Access.READ_ONLY) String utenteId) {
}
