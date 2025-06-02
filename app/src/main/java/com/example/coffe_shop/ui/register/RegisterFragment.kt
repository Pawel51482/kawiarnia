package com.example.coffe_shop.ui.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.coffe_shop.R
import com.example.coffe_shop.data.RegisterRequest
import com.example.coffe_shop.databinding.FragmentRegisterBinding
import com.example.coffe_shop.network.RetrofitClient
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.registerButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(context, "Hasła nie są takie same", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.registerButton.setOnClickListener {
                val email = binding.emailInput.text.toString()
                val password = binding.passwordInput.text.toString()
                val confirmPassword = binding.confirmPasswordInput.text.toString()

                if(password != confirmPassword) {
                    Toast.makeText(context, "Hasła się nie zgadzają", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    try {
                        val response = RetrofitClient.authApi.register(RegisterRequest(email, password, confirmPassword))
                        if (response.isSuccessful) {
                            val token = response.body()?.access_token ?: ""
                            requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                                .edit().putString("token", token).apply()
                            Toast.makeText(context, "Zarejestrowano i zalogowano", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.navigation_profile)
                        } else {
                            Toast.makeText(context, "Rejestracja nie powiodła się", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
