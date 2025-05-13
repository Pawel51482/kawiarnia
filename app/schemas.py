from pydantic import BaseModel

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