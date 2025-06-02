from jose import JWTError, jwt
from sqlalchemy.future import select
from fastapi import FastAPI, Depends, HTTPException
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from backend.schemas import UserCreate, UserLogin
from backend.models import User
from backend.models import Coffee
from backend.auth import ALGORITHM, SECRET_KEY, hash_password, verify_password, create_access_token
from backend.database import get_db
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

async def get_user_by_email(db: AsyncSession, email: str):
    result = await db.execute(select(User).where(User.email == email))
    return result.scalars().first()

async def register_user(db: AsyncSession, user_data: UserCreate):
    existing_user = await get_user_by_email(db, user_data.email)
    if existing_user:
        raise HTTPException(status_code=400, detail="Email already taken")
    new_user = User(
        email=user_data.email,
        hashed_password=hash_password(user_data.password)
    )
    db.add(new_user)
    await db.commit()
    await db.refresh(new_user)
    token = create_access_token({"sub": new_user.email})
    return {"access_token": token}

async def login_user(db: AsyncSession, user_data: UserLogin):
    if not user_data.email:
        raise HTTPException(status_code=400, detail="Email is required")
    user = await get_user_by_email(db, user_data.email)
    if not user or not verify_password(user_data.password, user.hashed_password):
        raise HTTPException(status_code=401, detail="Invalid credentials")
    token = create_access_token({"sub": user.email})
    return {"access_token": token}

async def get_current_user(token: str = Depends(oauth2_scheme), db: AsyncSession = Depends(get_db)):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        email = payload.get("sub")
        if email is None:
            raise HTTPException(status_code=401, detail="Invalid token")
    except JWTError:
        raise HTTPException(status_code=401, detail="Invalid token")
    user = await get_user_by_email(db, email)
    if user is None:
        raise HTTPException(status_code=401, detail="User not found")
    return user
# Order
async def create_order(db: AsyncSession, user_id: int, coffee_id: int, quantity: int):
    new_order = Order(user_id=user_id, coffee_id=coffee_id, quantity=quantity)
    db.add(new_order)
    await db.commit()
    await db.refresh(new_order)
    return new_order

async def get_orders_by_user(db: AsyncSession, user_id: int):
    result = await db.execute(select(Order).where(Order.user_id == user_id))
    return result.scalars().all()

# Reservations
async def create_reservation(db: AsyncSession, user_id: int, table_number: int, date, time):
    reservation = Reservation(user_id=user_id, table_number=table_number, date=date, time=time)
    db.add(reservation)
    await db.commit()
    await db.refresh(reservation)
    return reservation

async def get_reservations_by_user(db: AsyncSession, user_id: int):
    result = await db.execute(select(Reservation).where(Reservation.user_id == user_id))
    return result.scalars().all()

