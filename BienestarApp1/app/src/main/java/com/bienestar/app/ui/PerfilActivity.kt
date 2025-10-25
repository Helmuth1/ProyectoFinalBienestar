package com.bienestar.app.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bienestar.app.R
import com.bienestar.app.models.Cliente
import com.bienestar.app.api.ApiService
import com.bienestar.app.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilActivity : AppCompatActivity() {

    private lateinit var txtNombre: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var txtTelefono: TextView

    private lateinit var prefs: SharedPreferences
    private lateinit var service: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializar vistas
        txtNombre = findViewById(R.id.tvNombre)
        txtCorreo = findViewById(R.id.tvEmail)
        txtTelefono = findViewById(R.id.tvTelefono)

        // Inicializar Retrofit y SharedPreferences
        prefs = getSharedPreferences("BienestarAppPrefs", MODE_PRIVATE)
        service = RetrofitInstance.retrofit.create(ApiService::class.java)

        // Cargar perfil
        cargarPerfil()
    }

    private fun cargarPerfil() {
        val token = prefs.getString("jwt_token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token no encontrado. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        Log.d("PERFIL", "Usando token: $token")

        service.getPerfil("Bearer $token").enqueue(object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                if (response.isSuccessful) {
                    val cliente = response.body()
                    if (cliente != null) {
                        txtNombre.text = cliente.nombreCompleto
                        txtCorreo.text = cliente.email
                        txtTelefono.text = cliente.telefono
                    } else {
                        Toast.makeText(applicationContext, "Perfil vacío", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("PERFIL", "Error ${response.code()}: ${response.errorBody()?.string()}")
                    Toast.makeText(applicationContext, "Error al cargar el perfil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                Log.e("PERFIL", "Fallo de conexión: ${t.message}")
                Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


