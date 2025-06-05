package com.example.coffe_shop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffe_shop.R
import com.example.coffe_shop.adapters.HomeAdapter
import com.example.coffe_shop.models.Product
import com.example.coffe_shop.adapters.ProductAdapter
import com.example.coffe_shop.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductAdapter(getDummyProducts())
        binding.recyclerProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerProducts.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDummyProducts(): List<Product> {
        return listOf(
            Product("Iced Latte", "$3.50", R.drawable.ic_iced_latte),
            Product("Cappuccino", "$3.00", R.drawable.ic_cappuccino),
            Product("Mocha", "$3.20", R.drawable.ic_mocha),
            Product("Espresso", "$2.50", R.drawable.ic_espresso),
            Product("Macchiato", "$3.10", R.drawable.ic_macchiato),
            Product("Flat White", "$3.40", R.drawable.ic_flat_white)
        )
    }
}
