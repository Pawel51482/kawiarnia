package com.example.coffe_shop.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffe_shop.SharedViewModel
import com.example.coffe_shop.adapters.ProductAdapter
import com.example.coffe_shop.databinding.FragmentShopBinding

class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding.recyclerShopItems.layoutManager = LinearLayoutManager(requireContext())

        sharedViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter = ProductAdapter(
                items,
                mode = com.example.coffe_shop.adapters.Mode.SHOP,
                onRemoveFromCart = { product ->
                    sharedViewModel.removeFromCart(product)
                }
            )
            binding.recyclerShopItems.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
