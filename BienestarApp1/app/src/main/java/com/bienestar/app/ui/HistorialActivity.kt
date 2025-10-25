package com.bienestar.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bienestar.app.api.ApiClient
import com.bienestar.app.api.ApiService
import com.bienestar.app.databinding.ActivityHistorialBinding
import com.bienestar.app.models.Cita
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistorialBinding
    private val adapter = CitasAdapter()
    private val clienteId: Long = 19L // TODO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvHistorial.layoutManager = LinearLayoutManager(this)
        binding.rvHistorial.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = ApiClient.getRetrofit(this@HistorialActivity).create(ApiService::class.java)
                val resp = api.historialCitas(clienteId)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        adapter.submitList(resp.body() ?: emptyList())
                    } else Toast.makeText(this@HistorialActivity, "Error: ${resp.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { Toast.makeText(this@HistorialActivity, e.message, Toast.LENGTH_LONG).show() }
            }
        }
    }
}

class CitasAdapter : androidx.recyclerview.widget.ListAdapter<Cita, CitaVH>(
    object : androidx.recyclerview.widget.DiffUtil.ItemCallback<Cita>() {
        override fun areItemsTheSame(oldItem: Cita, newItem: Cita) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Cita, newItem: Cita) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): CitaVH {
        val v = android.view.LayoutInflater.from(parent.context).inflate(com.bienestar.app.R.layout.row_cita, parent, false)
        return CitaVH(v)
    }
    override fun onBindViewHolder(holder: CitaVH, position: Int) { holder.bind(getItem(position)) }
}

class CitaVH(itemView: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    fun bind(c: Cita) {
        itemView.findViewById<android.widget.TextView>(com.bienestar.app.R.id.tvId).text = "Cita #${c.id}"
        itemView.findViewById<android.widget.TextView>(com.bienestar.app.R.id.tvFecha).text = "Fecha: ${c.fecha ?: "-"}"
        itemView.findViewById<android.widget.TextView>(com.bienestar.app.R.id.tvHora).text = "Hora: ${c.hora ?: "-"}"
        itemView.findViewById<android.widget.TextView>(com.bienestar.app.R.id.tvEstado).text = "Estado: ${c.estado ?: "-"}"
    }
}
