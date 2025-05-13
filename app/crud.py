from sqlalchemy.future import select
from fastapi import FastAPI, Depends, HTTPException
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from app.schemas import UserCreate, UserLogin
from app.models import User
from app.models import Coffee
from app.auth import hash_password
from app.database import get_db
from sqlalchemy.ext.asyncio import AsyncSession

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/token")

async def get_coffees(db):
    try:
        result = await db.execute(select(Coffee))
        coffees = result.scalars().all()
        if not coffees:
            raise HTTPException(status_code=404, detail="No coffees found")
        return coffees
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")

async def add_coffee(db, coffee_data: dict):
    try:
        new_coffee = Coffee(**coffee_data)
        db.add(new_coffee)
        await db.commit()
        return new_coffee
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")

async def get_coffee_by_id(db, coffee_id: int):
    try:
        result = await db.execute(select(Coffee).filter(Coffee.id == coffee_id))
        coffee = result.scalar_one_or_none()

        if coffee is None:
            raise HTTPException(status_code=404, detail="Coffee not found")

        return coffee
    except HTTPException as e:
        raise e
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")

async def update_coffee(db, coffee_id: int, coffee_data: dict):
    try:
        result = await db.execute(select(Coffee).filter(Coffee.id == coffee_id))
        coffee = result.scalar_one_or_none()

        if coffee is None:
            raise HTTPException(status_code=404, detail="Coffee not found")

        for key, value in coffee_data.items():
            setattr(coffee, key, value)

        await db.commit()
        return coffee
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")

async def delete_coffee(db, coffee_id: int):
    try:
        result = await db.execute(select(Coffee).filter(Coffee.id == coffee_id))
        coffee = result.scalar_one_or_none()

        if coffee is None:
            raise HTTPException(status_code=404, detail="Coffee not found")

        await db.delete(coffee)
        await db.commit()
        return {"Coffee deleted successfully"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")

async def get_user_by_username(db, username: str):
    result = await db.execute(select(User).where(User.username == username))
    return result.scalars().first()

async def create_user(db, username: str, password: str):
    user = User(username=username, hashed_password=hash_password(password))
    db.add(user)
    await db.commit()
    await db.refresh(user)
    return user

async def register_user(db: AsyncSession, user_data: UserCreate):
    existing_user = await get_user_by_username(db, user_data.username)
    if existing_user:
        raise HTTPException(status_code=400, detail="Username already taken")
    
    new_user = User(
        username=user_data.username,
        hashed_password=hash_password(user_data.password)
    )
    db.add(new_user)
    await db.commit()
    await db.refresh(new_user)

    token = create_access_token({"sub": new_user.username})
    return {"access_token": token}

async def login_user(db: AsyncSession, user_data: UserLogin):
    user = await get_user_by_username(db, user_data.username)
    if not user or not verify_password(user_data.password, user.hashed_password):
        raise HTTPException(status_code=401, detail="Invalid credentials")
    
    token = create_access_token({"sub": user.username})
    return {"access_token": token}

async def get_current_user(token: str = Depends(oauth2_scheme), db: AsyncSession = Depends(get_db)):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username = payload.get("sub")
        if username is None:
            raise HTTPException(status_code=401, detail="Invalid token")
    except JWTError:
        raise HTTPException(status_code=401, detail="Invalid token")
    
    user = await get_user_by_username(db, username)
    if user is None:
        raise HTTPException(status_code=401, detail="User not found")
    return user
