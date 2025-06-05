package com.example.coffe_shop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffe_shop.adapters.ProductAdapter
import com.example.coffe_shop.databinding.FragmentHomeBinding
import com.example.coffe_shop.models.Product
import com.example.coffe_shop.network.RetrofitClient
import com.example.coffe_shop.R
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProductAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerProducts.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = ProductAdapter(emptyList())
        binding.recyclerProducts.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.updateData(products)
        }

        viewModel.fetchCoffees()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

