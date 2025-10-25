package com.bienestar.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bienestar.app.api.ApiClient
import com.bienestar.app.api.ApiService
import com.bienestar.app.databinding.ActivityReservarCitaBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservarCitaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservarCitaBinding
    private val clienteId: Long = 19L // TODO: obtener de sesión real

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservarCitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReservar.setOnClickListener {
            val servicioId = binding.etServicioId.text.toString().toLongOrNull()
            val fecha = binding.etFecha.text.toString().trim() // YYYY-MM-DD
            val hora = binding.etHora.text.toString().trim()   // HH:mm

            if (servicioId == null || fecha.isEmpty() || hora.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val body = mapOf(
                "clienteId" to clienteId,
                "servicioId" to servicioId,
                "fecha" to fecha,
                "hora" to hora
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val api = ApiClient.getRetrofit(this@ReservarCitaActivity).create(ApiService::class.java)
                    val resp = api.crearCita(body)
                    withContext(Dispatchers.Main) {
                        if (resp.isSuccessful) {
                            Toast.makeText(this@ReservarCitaActivity, "Cita reservada", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@ReservarCitaActivity, "Error: ${resp.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ReservarCitaActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
