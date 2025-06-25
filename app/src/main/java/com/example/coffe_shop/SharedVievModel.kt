package com.example.coffe_shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffe_shop.models.Product

class SharedViewModel : ViewModel() {
    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _cartItems = MutableLiveData<MutableList<Product>>(mutableListOf())
    val cartItems: LiveData<MutableList<Product>> = _cartItems

    fun setLoggedIn(loggedIn: Boolean) {
        _isLoggedIn.value = loggedIn
    }

    // Dodaj produkt do koszyka
    fun addToCart(product: Product) {
        val currentList = _cartItems.value ?: mutableListOf()
        currentList.add(product)
        _cartItems.value = currentList
    }

    // Usuń produkt z koszyka
    fun removeFromCart(product: Product) {
        val currentList = _cartItems.value ?: mutableListOf()
        currentList.remove(product)
        _cartItems.value = currentList
    }
}
