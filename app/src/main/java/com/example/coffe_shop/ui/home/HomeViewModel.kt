package com.example.coffe_shop.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffe_shop.models.Product
import com.example.coffe_shop.network.RetrofitClient
import com.example.coffe_shop.R
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun fetchCoffees() {
        viewModelScope.launch {
            try {
                val coffees = RetrofitClient.authApi.getCoffees()
                val productsList = coffees.map { coffee ->
                    Product(
                        name = coffee.name,
                        price = String.format("%.2f", coffee.price), // formatowanie ceny
                        imageResId = R.drawable.coffee_placeholder
                    )
                }
                _products.postValue(productsList)
            } catch (e: Exception) {
                e.printStackTrace()
                _products.postValue(emptyList())
            }
        }
    }
}
