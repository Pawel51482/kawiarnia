from sqlalchemy.ext.asyncio import AsyncSession, create_async_engine
from sqlalchemy.orm import sessionmaker, declarative_base

# Konfiguracja bazy danych
DATABASE_URL = "postgresql+asyncpg://user:password@db/coffee_shop"
engine = create_async_engine(DATABASE_URL, echo=True)
SessionLocal = sessionmaker(bind=engine, class_=AsyncSession, expire_on_commit=False)
Base = declarative_base()

# Tworzenie tabel w bazie danych
async def init_db():
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)

# Dependency do sesji bazy danych
async def get_db():
    async with SessionLocal() as session:
        try:
            yield session
        finally:
            await session.close()