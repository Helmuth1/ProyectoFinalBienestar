package com.bienestar.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bienestar.app.api.ApiClient
import com.bienestar.app.api.ApiService
import com.bienestar.app.databinding.ActivityServiciosBinding
import com.bienestar.app.models.Servicio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServiciosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiciosBinding
    private val adapter = ServiciosAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiciosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvServicios.layoutManager = LinearLayoutManager(this)
        binding.rvServicios.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = ApiClient.getRetrofit(this@ServiciosActivity).create(ApiService::class.java)
                val resp = api.listarServicios()
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        adapter.submitList(resp.body() ?: emptyList())
                    } else {
                        Toast.makeText(this@ServiciosActivity, "Error servicios: ${resp.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ServiciosActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

class ServiciosAdapter : androidx.recyclerview.widget.ListAdapter<Servicio, ServiciosVH>(
    object : androidx.recyclerview.widget.DiffUtil.ItemCallback<Servicio>() {
        override fun areItemsTheSame(oldItem: Servicio, newItem: Servicio) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Servicio, newItem: Servicio) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ServiciosVH {
        val v = android.view.LayoutInflater.from(parent.context).inflate(com.bienestar.app.R.layout.row_servicio, parent, false)
        return ServiciosVH(v)
    }

    override fun onBindViewHolder(holder: ServiciosVH, position: Int) {
        holder.bind(getItem(position))
    }
}

class ServiciosVH(itemView: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    fun bind(s: Servicio) {
        itemView.findViewById<android.widget.TextView>(com.bienestar.app.R.id.tvNombre).text = s.nombre ?: "-"
        itemView.findViewById<android.widget.TextView>(com.bienestar.app.R.id.tvDescripcion).text = s.descripcion ?: "-"
        itemView.findViewById<android.widget.TextView>(com.bienestar.app.R.id.tvPrecio).text = "Q${s.precio ?: 0.0}"
    }
}
