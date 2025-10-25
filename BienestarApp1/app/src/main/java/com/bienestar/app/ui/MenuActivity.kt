package com.bienestar.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bienestar.app.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPerfil.setOnClickListener { startActivity(Intent(this, PerfilActivity::class.java)) }
        binding.btnServicios.setOnClickListener { startActivity(Intent(this, ServiciosActivity::class.java)) }
        binding.btnReservar.setOnClickListener { startActivity(Intent(this, ReservarCitaActivity::class.java)) }
        binding.btnHistorial.setOnClickListener { startActivity(Intent(this, HistorialActivity::class.java)) }
        binding.btnFacturas.setOnClickListener { startActivity(Intent(this, FacturasActivity::class.java)) }
    }
}
