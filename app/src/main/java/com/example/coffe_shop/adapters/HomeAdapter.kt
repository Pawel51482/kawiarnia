package com.example.coffe_shop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffe_shop.R
import com.example.coffe_shop.models.Product

class HomeAdapter(
    private val products: List<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_FILTERS = 0
        private const val VIEW_TYPE_PRODUCT = 1
        private const val VIEW_TYPE_SPECIAL = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_FILTERS
            itemCount - 1 -> VIEW_TYPE_SPECIAL
            else -> VIEW_TYPE_PRODUCT
        }
    }

    override fun getItemCount(): Int = products.size + 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FILTERS -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_home_filters, parent, false)
                FiltersViewHolder(view)
            }
            VIEW_TYPE_SPECIAL -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_special_offer, parent, false)
                SpecialOfferViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_card, parent, false)
                ProductViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FiltersViewHolder -> {
                // Statyczny układ przycisków, nie trzeba nic robić
            }
            is SpecialOfferViewHolder -> {
                val coffeeName = holder.itemView.findViewById<TextView>(R.id.textCoffeeName)
                val price = holder.itemView.findViewById<TextView>(R.id.textSpecialPrice)
                coffeeName.text = "Kawa dnia"
                price.text = "$10.00"
            }
            is ProductViewHolder -> {
                val product = products[position - 1] // -1 bo pierwszy item to filtry
                holder.image.setImageResource(product.imageResId)
                holder.name.text = product.name
                holder.price.text = product.price
            }
        }
    }

    inner class FiltersViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class SpecialOfferViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageProduct)
        val name: TextView = view.findViewById(R.id.textName)
        val price: TextView = view.findViewById(R.id.textPrice)
    }
}
