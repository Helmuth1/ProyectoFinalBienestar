package com.bienestar.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bienestar.app.R
import com.bienestar.app.api.ApiService
import com.bienestar.app.models.LoginRequest
import com.bienestar.app.models.LoginResponse
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.etEmail)
        edtPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            iniciarSesion()
        }
    }

    private fun iniciarSesion() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tus credenciales", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ Instancia Retrofit (esto responde a tu pregunta del paso 3)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/api/") // usa tu backend local
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        val loginRequest = LoginRequest(email, password)

        // 🔐 Llamada al endpoint de autenticación
        service.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    Log.d("TOKEN_RECIBIDO", token)

                    // ✅ Guardar el token en SharedPreferences
                    val editor = getSharedPreferences("BienestarAppPrefs", MODE_PRIVATE).edit()
                    editor.putString("jwt_token", token)
                    editor.apply()

                    Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                    // Redirige al menú principal
                    val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("LOGIN_ERROR", t.message.toString())
            }
        })
    }
}
