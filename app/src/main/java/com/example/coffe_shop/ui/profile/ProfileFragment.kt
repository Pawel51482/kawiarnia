package com.example.coffe_shop.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.coffe_shop.R
import com.example.coffe_shop.SharedViewModel
import com.example.coffe_shop.data.LoginRequest
import com.example.coffe_shop.databinding.FragmentProfileBinding
import com.example.coffe_shop.network.RetrofitClient
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)
        updateUI(token != null)
        val btnOrder = view.findViewById<View>(R.id.btnOrder)
        val btnLogout = view.findViewById<View>(R.id.btnLogout)


        // Logowanie
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.authApi.login(LoginRequest(email, password))
                    if (response.isSuccessful) {
                        val newToken = response.body()?.access_token ?: ""
                        prefs.edit().putString("token", newToken).apply()
                        updateUI(true)
                        sharedViewModel.setLoggedIn(true)
                        Toast.makeText(context, "Zalogowano", Toast.LENGTH_SHORT).show()
                    } else {
                        sharedViewModel.setLoggedIn(false)
                        updateUI(false)
                        Toast.makeText(context, "Błędne dane", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    sharedViewModel.setLoggedIn(false)
                    updateUI(false)
                    Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        if (token != null) {
            sharedViewModel.setLoggedIn(true)
            updateUI(true)
        }

        // Rejestracja
        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.navigation_register)
        }

        // Wylogowanie
        btnLogout.setOnClickListener {
            prefs.edit().remove("token").apply()
            sharedViewModel.setLoggedIn(false)
            updateUI(false)
        }

        // Przykład kliknięcia jednego z przycisków profilowych
        btnOrder.setOnClickListener {
            Toast.makeText(requireContext(), "Kliknięto 'Moje zamówienia'", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(isLoggedIn: Boolean) {
        binding.loginView.visibility = if (isLoggedIn) View.GONE else View.VISIBLE
        binding.profileView.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

