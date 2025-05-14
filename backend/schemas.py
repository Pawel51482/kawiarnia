from pydantic import BaseModel, EmailStr

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
