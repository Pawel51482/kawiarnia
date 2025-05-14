from sqlalchemy import Column, Integer, String, Float, Sequence, ForeignKey
from sqlalchemy.orm import relationship
from backend.database import Base

class Coffee(Base):
    __tablename__ = "coffees"
    id = Column(Integer, Sequence("coffee_id_seq"), primary_key=True, index=True, autoincrement=True)
    name = Column(String, nullable=False)
    description = Column(String)
    price = Column(Float, nullable=False)

    # relation 1:N - coffee can can be selected to multiple orders
    orders = relationship("Order", back_populates="coffee", cascade="all, delete-orphan")
  
class User(Base):
    __tablename__ = "users"
    id = Column(Integer, Sequence("user_id_seq"), primary_key=True, index=True, autoincrement=True)
    username = Column(String, nullable=False)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String, nullable=False)

    # relation 1:N - user can have multiple orders and reservations
    orders = relationship("Order", back_populates="user", cascade="all, delete-orphan")
    reservations = relationship("Reservation", back_populates="user", cascade="all, delete-orphan")


class Order(Base):
    __tablename__ = "orders"
    id = Column(Integer, Sequence("order_id_seq"), primary_key=True, index=True, autoincrement=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    coffee_id = Column(Integer, ForeignKey("coffees.id"), nullable=False)
    quantity = Column(Integer, nullable=False, default=1)
    status = Column(String, default="pending")  # ex. "pending", "completed"

    user = relationship("User", back_populates="orders")
    coffee = relationship("Coffee", back_populates="orders")

class Reservation(Base):
    __tablename__ = "reservations"
    id = Column(Integer, Sequence("reservation_id_seq"), primary_key=True, index=True, autoincrement=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    table_number = Column(Integer, nullable=False)
    date = Column(String, nullable=False)
    time = Column(String, nullable=False)

    user = relationship("User", back_populates="reservations")

