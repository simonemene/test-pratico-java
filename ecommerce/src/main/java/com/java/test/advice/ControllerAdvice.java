package com.java.test.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> validazioneCampiException(MethodArgumentNotValidException exception)
	{
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setTitle("Errore nella validazione dei campi");
		problemDetail.setDetail("Uno o più campi sono invalidi");
		Map<String,String> errori = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.collect(Collectors.toMap(FieldError::getField,
						valore-> Optional.ofNullable(valore.getDefaultMessage()).orElse("errore sconosciuto"),
						(campoInErrore,campoNuovoInErrore)->campoInErrore + "; " + campoNuovoInErrore

				));
		problemDetail.setProperty("errori",errori);
		return ResponseEntity.badRequest().body(problemDetail);
	}
}
