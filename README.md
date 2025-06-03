
# ☕ Coffee Shop Backend

Backend aplikacji mobilnej dla kawiarni

## 🔧 Technologie

- Python
- FastAPI
- SQLAlchemy
- PostgreSQL
- Docker

## 📦 Instalacja

Musisz zainstalować 🐳 Docker https://www.docker.com/ 

Następnie w folderze projektu
```
docker-compose build
docker-compose up
```
Serwer powinien się automatycznie zbudować oraz uruchomić  
Możesz go sprawdzić i testować pod http://0.0.0.0:8000/docs

## 🚀 Endpointy 
| Metoda | Endpoint         | Opis                        |
| ------ | ---------------- | --------------------------- |
| POST   | `/register`      | Rejestracja                 |
| POST   | `/token`         | Logowanie                   |
| GET    | `/me`            | Dane aktualnego użytkownika |
| GET    | `/coffees`       | Lista kaw                   |
| POST   | `/coffees/`      | Dodaj kawę                  |
| GET    | `/coffees/{id}`  | Szczegóły kawy              |
| PUT    | `/coffees/{id}`  | Aktualizuj kawę             |
| DELETE | `/coffees/{id}`  | Usuń kawę                   |
| POST   | `/orders/`       | Złóż zamówienie             |
| GET    | `/orders/`       | Zamówienia użytkownika      |
| POST   | `/reservations/` | Zarezerwuj stolik           |
| GET    | `/reservations/` | Rezerwacje użytkownika      |

## 🔒 Autoryzacja  

Po logowaniu otrzymasz token JWT.

Przykład nagłówka:

Authorization: Bearer <twój_token>

## 📁 Struktura katalogu

backend/  
├── main.py           # Główna aplikacja FastAPI  
├── crud.py           # Logika bazodanowa  
├── models.py         # Modele SQLAlchemy  
├── schemas.py        # Schematy Pydantic  
├── auth.py           # JWT i hashowanie  
├── database.py       # Połączenie z bazą danych  
└── .env              # Klucz SECRET_KEY  

## 🗄️ Baza danych

Projekt korzysta z bazy danych PostgreSQL, uruchamianej w kontenerze Docker.  
Aby wejsc do bazy danych:
```
docker exec -it <container_id> psql -U user -d coffee_shop
```
aby sprawdzic <container id> 
```
docker ps
```
aby wypisac wszystkie tabele:
```
\dt
```
Aby wybrac daną dabele:
```
SELECT * FROM tabelname;
```

## ✅ Modele danych

User: email, hashed_password  
Coffee: name, description, price  
Order: user_id, coffee_id, quantity, status  
Reservation: user_id, table_number, date, time

