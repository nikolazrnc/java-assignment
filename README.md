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
Retrieves a list of all subscriptions.
```bash
  GET http://localhost:8080/subscriptions
  ```

### 2. Add  Subscription

Add new subscription to database.
  ```bash
  POST http://localhost:8080/subscriptions
  BODY: {"icaoCode": "LDZA"}
  ```

### 3. Delete Subscription
Deletes a subscription for given ICAO code.
```bash
  DELETE http://localhost:8080/subscriptions/LDZA
  ```

### METAR Endpoints
### 1. Get METAR data
Retrieves the last METAR data record from database for given ICAO code.
```bash
  GET http://localhost:8080/airport/LDZA/METAR
  ```

### 2. Add METAR data

Add new METAR data record for airport with given ICAO code to database.
  ```bash
  POST http://localhost:8080/airport/LDZA/METAR
  BODY: {"createdAt": "2025-09-07T08:30:00Z",
         "data": "LDZA 070830Z VRB02KT CAVOK 20/13 Q1022 NOSIG"
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
| Exception                          | 500         | Internal server error            |

API also uses loggers to provide additional information and stores logs up to 30 days.