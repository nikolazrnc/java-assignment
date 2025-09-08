# Backend Developer Assignment - 2e Systems

## Automated Task

To set up the automated task, the `cron_setup.sh` script needs to be executed first.  
This will configure the crontab to run the `script.sh` automated task.

---

## Database

The application uses **PostgresSQL** and **pgAdmin**.  
The database schema is managed and initialized using **Liquibase**.

---

## API Endpoints

### Subscription Endpoints
### 1. Get List of Subscriptions
Retrieves a list of all subscriptions. Subscriptions can be filtered by their activity status and ICAO code letters.
```bash
  GET http://localhost:8080/subscriptions?active=true&name=LD
  ```

### 2. Add  Subscription

Adds a new subscription to database.
  ```bash
  POST http://localhost:8080/subscriptions
  BODY: {"icaoCode": "LDZA"}
  ```

### 3. Delete Subscription
Deletes a subscription for given ICAO code. Deleting a subscription also deletes all METAR records for that subscription.
```bash
  DELETE http://localhost:8080/subscriptions/LDZA
  ```

### 4. Update  Subscription
Updates subscription activity status. METAR data can be retrieved only for active subscriptions. Automated task also fetches METAR data only for active subscriptions.
```bash
  PUT http://localhost:8080/subscriptions/LDZA
  BODY {"active": false}
  ```

### METAR Endpoints
### 1. Get METAR data
Retrieves the last METAR data record from database for a given ICAO code.
```bash
  GET http://localhost:8080/airport/LDZA/METAR
  ```

### 2. Add METAR data

Adds new METAR data record for the airport with the given ICAO code. The METAR data is passed to MetarParserService, which parses METAR data to natural language before storing it in the database.
  ```bash
  POST http://localhost:8080/airport/LDZA/METAR
  BODY: {"createdAt": "2025-09-07T08:30:00Z",
         "data": "LDZA 070830Z VRB02KT CAVOK 20/13 Q1022 NOSIG"
         }
  ```

### 3. Get filtered METAR data

Retrieves a subset of METAR data. In the request body, attributes to be retrieved should be set to true, and others set to false or null.
  ```bash
  POST http://localhost:8080/airport/LDZA/METAR/filter
  BODY: {"airportName":true,
         "weather":true,
         "temperature":true
         }
  ```


## Error Handling

The API provides meaningful error responses:

| Exception                          | HTTP Status | Description                      |
|------------------------------------|-------------|----------------------------------|
| SubscriptionNotFoundException      | 404         | Subscription does not exist      |
| SubscriptionAlreadyExistsException | 409         | Subscription already exists      |
| MetarDataNotFoundException         | 404         | METAR data for airport not found |
| InvalidIcaoCodeFormatException     | 400         | Invalid ICAO code format         |
| SubscriptionIsInactiveException    | 403         | Subscription is inactive         |
| InvalidMetarFormatException        | 400         | Invalid METAR format             |
API also uses loggers to provide additional information and stores logs up to 30 days.