from fastapi import FastAPI, Depends, HTTPException
from app.database import get_db, init_db, engine
from app.models import Coffee
from app.schemas import UserCreate, UserLogin, Token
from app.crud import get_coffees, add_coffee, get_coffee_by_id, update_coffee, delete_coffee, register_user, login_user, get_current_user
from sqlalchemy.ext.asyncio import AsyncSession

# Tworzenie aplikacji FastAPI
app = FastAPI()

@app.on_event("startup")
async def startup():
    import time
    time.sleep(5)
    await init_db()

# Endpointy
@app.get("/coffees")
async def get_coffees_endpoint(db: AsyncSession = Depends(get_db)):
    return await get_coffees(db)

@app.post("/coffees/", status_code=201)
async def add_coffee_endpoint(coffee_data: dict, db: AsyncSession = Depends(get_db)):
    return await add_coffee(db, coffee_data)

@app.get("/coffees/{coffee_id}")
async def get_coffee_by_id_endpoint(coffee_id: int, db: AsyncSession = Depends(get_db)):
    return await get_coffee_by_id(db, coffee_id)

@app.put("/coffees/{coffee_id}")
async def update_coffee_endpoint(coffee_id: int, coffee_data: dict, db: AsyncSession = Depends(get_db)):
    return await update_coffee(db, coffee_id, coffee_data)

@app.delete("/coffees/{coffee_id}")
async def delete_coffee_endpoint(coffee_id: int, db: AsyncSession = Depends(get_db)):
    return await delete_coffee(db, coffee_id)

@app.post("/register", response_model=Token)
async def register_endpoint(user_data: UserCreate, db: AsyncSession = Depends(get_db)):
    return await register_user(db, user_data)

@app.post("/token", response_model=Token)
async def login_endpoint(user_data: UserLogin, db: AsyncSession = Depends(get_db)):
    return await login_user(db, user_data)

@app.get("/me")
async def profile_endpoint(current_user = Depends(get_current_user)):
    return {"username": current_user.username}
