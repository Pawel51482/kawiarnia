from pydantic import BaseModel, EmailStr
from typing import Optional
from datetime import date, time

class CoffeeBase(BaseModel):
    name: str
    description: str | None = None
    price: float

class CoffeeCreate(CoffeeBase):
    pass

class Coffee(CoffeeBase):
    id: int

    class Config:
        orm_mode = True

class UserCreate(BaseModel):
    username: str
    email: EmailStr
    password: str

class UserLogin(BaseModel):
    username: str | None = None
    email: EmailStr | None = None
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str = "bearer"

class OrderCreate(BaseModel):
    coffee_id: int
    quantity: int

class OrderRead(BaseModel):
    id: int
    coffee_id: int
    quantity: int
    status: str

    class Config:
        orm_mode = True

class ReservationCreate(BaseModel):
    table_number: int
    date: date
    time: time

class ReservationRead(BaseModel):
    id: int
    table_number: int
    date: date
    time: time

    class Config:
        orm_mode = True


