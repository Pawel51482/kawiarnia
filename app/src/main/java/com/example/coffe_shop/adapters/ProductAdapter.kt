package com.example.coffe_shop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.coffe_shop.models.Product
import com.example.coffe_shop.R

enum class Mode {
    HOME,
    SHOP
}

class ProductAdapter(
    private var coffees: List<Product>,
    private val mode: Mode,
    private val onAddToCart: ((Product) -> Unit)? = null,
    private val onRemoveFromCart: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageProduct)
        val name: TextView = view.findViewById(R.id.textName)
        val price: TextView = view.findViewById(R.id.textPrice)
        val actionButton: ImageButton = view.findViewById(R.id.buttonAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_card, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = coffees.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val coffee = coffees[position]
        holder.name.text = coffee.name
        val priceDouble = coffee.price.toDoubleOrNull() ?: 0.0
        holder.price.text = String.format("%.2f zł", priceDouble)

        // Mapa nazw do ikon lokalnych
        val imageMap = mapOf(
            "Iced Latte" to R.drawable.ic_iced_latte,
            "Cappuccino" to R.drawable.ic_cappuccino,
            "Mocha" to R.drawable.ic_mocha,
            "Espresso" to R.drawable.ic_espresso,
            "Macchiato" to R.drawable.ic_macchiato,
            "Flat White" to R.drawable.ic_flat_white
        )

        holder.image.setImageResource(imageMap[coffee.name] ?: R.drawable.coffee_placeholder)

        when (mode) {
            Mode.HOME -> {
                holder.actionButton.setImageResource(R.drawable.ic_add)
                holder.actionButton.setOnClickListener {
                    onAddToCart?.invoke(coffee)
                    Toast.makeText(holder.itemView.context, "${coffee.name} dodana do koszyka", Toast.LENGTH_SHORT).show()
                }
            }
            Mode.SHOP -> {
                holder.actionButton.setImageResource(R.drawable.ic_remove)
                holder.actionButton.setOnClickListener {
                    onRemoveFromCart?.invoke(coffee)
                    Toast.makeText(holder.itemView.context, "${coffee.name} usunięta z koszyka", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateData(newCoffees: List<Product>) {
        coffees = newCoffees
        notifyDataSetChanged()
    }
}
