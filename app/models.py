from sqlalchemy import Column, Integer, String, Float, Sequence
from app.database import Base

class Coffee(Base):
    __tablename__ = "coffees"
    id = Column(Integer, Sequence("coffee_id_seq"), primary_key=True, index=True, autoincrement=True)
    name = Column(String, nullable=False)
    description = Column(String)
    price = Column(Float, nullable=False)