# Ecommerce – Test Pratico Java

## Scelte Architetturali

## Entità del Dominio

### AuditEntity (MappedSuperclass)

- Classe base estesa da tutte le entità principali
- Gestisce automaticamente le informazioni di auditing tramite JPA Auditing
- Responsabilità:
  - Tracciamento dell’utente che ha inserito il record
  - Tracciamento della data di inserimento
  - Tracciamento dell’utente che ha modificato il record
  - Tracciamento della data di modifica
  - Gestione del flag di annullo logico
- Il flag di annullo (`FLG_ANNULLO`) viene inizializzato automaticamente a `N` alla creazione
- Permette di centralizzare logica comune ed evitare duplicazioni

---

### UtenteEntity

- Rappresenta l’utente del sistema
- È utilizzato per l’autenticazione e l’autorizzazione tramite Spring Security
- Ogni utente è associato a un ruolo
- Un utente può effettuare più ordini
- Campi principali:
  - email (univoca)
  - utenteId pubblico (UUID)
  - nome e cognome
  - codice fiscale (univoco)
  - password (gestita in modo sicuro)
  - ruolo
  - campo `@Version` per il controllo di concorrenza
  - campi di auditing
  - flag di annullo logico

---

### RuoloEntity

- Rappresenta il ruolo associato a un utente
- Utilizzato da Spring Security per la gestione delle autorizzazioni
- I ruoli sono definiti tramite enum
- Campi principali:
  - ruolo (ENUM)
  - ruoloId pubblico (UUID)
- I ruoli amministrativi vengono inizializzati di default

---

### OrdineEntity

- Rappresenta un ordine effettuato da un utente
- Non contiene direttamente i prodotti
- I prodotti sono collegati tramite l’entità Movimento
- Alla creazione:
  - viene generato automaticamente un identificativo pubblico ordine
  - lo stato iniziale è impostato a `CREATO`
- Campi principali:
  - ordineId pubblico (UUID)
  - riferimento all’utente
  - stato dell’ordine
  - lista dei movimenti associati
  - campo `@Version` per il controllo di concorrenza
  - campi di auditing
  - flag di annullo logico

---

### MovimentoEntity

- Rappresenta una riga d’ordine
- È l’entità di collegamento tra Ordine e Prodotto
- Permette di modellare una relazione molti-a-molti con attributi
- Esiste un vincolo di unicità su coppia ordine–prodotto
- Campi principali:
  - riferimento all’ordine
  - riferimento al prodotto
  - quantità ordinata
  - campo `@Version` per il controllo di concorrenza
  - campi di auditing
  - flag di annullo logico

---

### ProdottoEntity

- Rappresenta il prodotto acquistabile
- Non contiene informazioni sul magazzino
- È separato dallo stock per isolare la logica di concorrenza
- Ha un identificativo pubblico distinto dall’ID tecnico
- Campi principali:
  - productId pubblico (UUID)
  - nome (univoco)
  - prezzo
  - riferimento allo stock
  - campo `@Version` per il controllo di concorrenza
  - campi di auditing
  - flag di annullo logico

---

### StockEntity

- Rappresenta il magazzino associato a un singolo prodotto
- Contiene la quantità disponibile
- È una delle entità critiche per la concorrenza
- Utilizza il controllo di concorrenza ottimistico tramite `@Version`
- Implementa la logica di aumento e diminuzione della quantità
- In caso di quantità insufficiente viene sollevata un’eccezione di dominio
- Campi principali:
  - riferimento al prodotto
  - quantità disponibile
  - campo `@Version`
  - campi di auditing
  - flag di annullo logico

---

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
