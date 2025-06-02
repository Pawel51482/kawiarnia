package com.example.coffe_shop.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.coffe_shop.R
import androidx.navigation.fragment.findNavController
import com.example.coffe_shop.data.LoginRequest
import com.example.coffe_shop.databinding.FragmentProfileBinding
import com.example.coffe_shop.network.RetrofitClient
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

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
                        Toast.makeText(context, "Zalogowano", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Błędne dane", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.logoutButton.setOnClickListener {
            prefs.edit().remove("token").apply()
            updateUI(false)
        }

        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.navigation_register)
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
