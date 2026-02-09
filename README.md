# Ecommerce – Test Pratico Java

## Scelte Architetturali

### Modello di Dominio

- Utente e Ordine sono modellati come entità distinte
- Un utente può effettuare più ordini
- La creazione di un ordine non modifica direttamente i prodotti

### Movimento Ordine–Prodotto

- Quando viene effettuato un ordine, viene creato un Movimento
- Il Movimento è un’entità che associa:
  - Ordine
  - Prodotto
- Questa scelta evita una relazione diretta rigida e consente di tracciare le operazioni sugli ordini in modo esplicito

### Prodotto e Magazzino

- Il Prodotto rappresenta il bene acquistabile
- Lo Stock è un’entità separata associata al Prodotto
- Lo Stock contiene le quantità di magazzino
- Questa separazione consente una gestione più chiara delle operazioni critiche e della concorrenza

---

## Concorrenza e Consistenza dei Dati

- Sulle tabelle critiche (Ordini e Stock) è stato utilizzato il controllo di concorrenza ottimistico
- È stato introdotto il campo `@Version` tramite JPA
- Questo consente di:
  - Evitare aggiornamenti persi
  - Gestire correttamente accessi concorrenti
- È stato sviluppato un test di concorrenza utilizzando Testcontainers
- Il test verifica il comportamento in caso di due thread che modificano contemporaneamente lo stock
- Per eseguire i test di concorrenza è necessario avere Docker attivo

---

## Persistenza e Database

- È stato utilizzato H2 come database
- Flyway viene usato per:
  - Versionare lo schema
  - Separare il database dall’applicazione
  - Validare la struttura tramite JPA
- H2 è utilizzato sia in produzione che nei test, per semplicità e velocità di esecuzione

---

## JPA Auditing

- È stato utilizzato JPA Auditing per gestire automaticamente:
  - Chi ha creato un record
  - Quando è stato creato
  - Quando è stato modificato
- È presente un flag di annullo logico per la gestione delle cancellazioni senza eliminazione fisica

---

## Sicurezza

- È stato utilizzato Spring Security
- Autenticazione tramite Basic Auth
- Gestione dei ruoli tramite filter chain dedicata
- Sono presenti due utenti admin creati di default:
  - email: admin@test.it
  - password: admin
- Sono stati aggiunti test di sicurezza per verificare:
  - Risposte 401 (non autenticato)
  - Risposte 403 (non autorizzato)

---

## Gestione delle Eccezioni e Logging

- È stato introdotto un RestControllerAdvice per la gestione centralizzata delle eccezioni
- Le eccezioni vengono mappate in risposte HTTP coerenti
- È stato utilizzato AOP per il logging
- Il logging è separato dalla logica di business per mantenere la separazione delle responsabilità

---

## Documentazione API

- È stato utilizzato Springdoc OpenAPI
- Swagger UI è disponibile per la documentazione delle API REST
- La documentazione viene generata automaticamente a partire dai controller

---

## Strategia di Testing

### Unit Test

- Utilizzati solo per testare i casi negativi
- Isolano il comportamento di singole unità di codice

### Integration Test

- Testano l’integrazione tra i componenti
- Utilizzati principalmente per i casi positivi dei service

### End-to-End Test

- Verificano il flusso completo controller → service → repository
- Utilizzati per i casi positivi principali

### Slice Test JPA

- Utilizzati per:
  - Introdurre nuove funzionalità
  - Verificare query custom nei repository
- Limitano il contesto solo al layer di persistenza

### Slice Test Controller

- Utilizzano MockMvc
- Servono principalmente per testare la validazione dei DTO

---

## Riduzione del Boilerplate

- Sono state create annotazioni custom
- L’obiettivo è ridurre il boilerplate e rendere il codice più leggibile
- Le annotazioni incapsulano configurazioni ripetitive

---

## Tecnologie Utilizzate

- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Springdoc OpenAPI
- Flyway
- H2
- Testcontainers
- JUnit 5
- Mockito
