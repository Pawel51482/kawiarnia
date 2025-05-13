from sqlalchemy.future import select
from fastapi import HTTPException
from app.models import Coffee

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