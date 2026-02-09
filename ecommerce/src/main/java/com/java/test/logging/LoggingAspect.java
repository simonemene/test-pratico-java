package com.java.test.logging;

import com.java.test.exception.ApplicationException;
import com.java.test.exception.MagazzinoException;
import com.java.test.exception.ProdottoException;
import com.java.test.exception.UtenteException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LoggingAspect {

	@AfterThrowing(value = "execution(* com.java.test.service..* *)",throwing = "ex")
	public void logggingMagazzino( JoinPoint joinPoint, Throwable ex)
	{
        switch(ex)
		{
		case MagazzinoException ma->

			log.warn("""
				[ERRORE MAGAZZINO] :
				[MESSAGGIO] -> {}
				[CLASSE] -> {}
				[METODO] -> {}
				[PARAMETRI METODO] -> {}
				[PARAMETRI ERRORE] -> id prodotto {}, quantita = {}
				""",
					ma.getMessage(),
					joinPoint.getTarget().getClass().getSimpleName(),
					joinPoint.getSignature(),
					Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()),
					ma.getProductId(),
					ma.getQuantita());

		case ApplicationException ap ->

			log.error("""
				[ERRORE GENERICO] :
				[MESSAGGIO] -> {}
				[CLASSE] -> {}
				[METODO] -> {}
				[PARAMETRI METODO] -> {}
				""",
					ap.getMessage(),
					joinPoint.getTarget().getClass().getSimpleName(),
					joinPoint.getSignature(),
					Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()),ap);


		case UtenteException ut ->

			log.warn("""
				[ERRORE UTENTE] :
				[MESSAGGIO] -> {}
				[CLASSE] -> {}
				[METODO] -> {}
				[PARAMETRI METODO] -> {}
				[PARAMETRI ERRORE] -> utente id {}
				""",
					ut.getMessage(),
					joinPoint.getTarget().getClass().getSimpleName(),
					joinPoint.getSignature(),
					Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()),
					ut.getUtenteId());


		case ProdottoException p ->

			log.warn("""
				[ERRORE PRODOTTO] :
				[MESSAGGIO] -> {}
				[CLASSE] -> {}
				[METODO] -> {}
				[PARAMETRI METODO] -> {}
				[PARAMETRI ERRORE] -> id prodotto {}
				""",
					p.getMessage(),
					joinPoint.getTarget().getClass().getSimpleName(),
					joinPoint.getSignature(),
					Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()),
					Arrays.toString(p.getProductId()));

		case ObjectOptimisticLockingFailureException ole ->

								log.warn("""
					[OPTIMISTIC LOCK]
					[MESSAGGIO] -> {}
					[CLASSE] -> {}
					[METODO] -> {}
					[PARAMETRI METODO] -> {}
					""",
							ole.getMessage(),
							joinPoint.getTarget().getClass().getSimpleName(),
							joinPoint.getSignature(),
							Arrays.toString(joinPoint.getArgs())
					);

			default->
			log.error("""
					[ATTENZIONE] ERRORE NON PREVISTO
					""",ex);
		}

	}
}
