from sqlalchemy import Column, Integer, String, Float, Sequence
from backend.database import Base

class Coffee(Base):
    __tablename__ = "coffees"
    id = Column(Integer, Sequence("coffee_id_seq"), primary_key=True, index=True, autoincrement=True)
    name = Column(String, nullable=False)
    description = Column(String)
    price = Column(Float, nullable=False)

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, Sequence("coffee_id_seq"), primary_key=True, index=True, autoincrement=True)
    username = Column(String, nullable=False)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String, nullable=False)

