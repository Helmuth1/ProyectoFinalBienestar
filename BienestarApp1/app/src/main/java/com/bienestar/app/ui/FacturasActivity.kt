package com.bienestar.app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bienestar.app.api.ApiClient
import com.bienestar.app.api.ApiService
import com.bienestar.app.databinding.ActivityFacturasBinding
import com.bienestar.app.models.Factura
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FacturasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacturasBinding
    private val adapter = FacturasAdapter { facturaId ->
        // Abrir descarga de PDF en el navegador
        val url = "http://10.0.2.2:8081/api/reportes/facturas/${facturaId}/pdf"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
    private val clienteId: Long = 19L // TODO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacturasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFacturas.layoutManager = LinearLayoutManager(this)
        binding.rvFacturas.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = ApiClient.getRetrofit(this@FacturasActivity).create(ApiService::class.java)
                val resp = api.facturasPorCliente(clienteId)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        adapter.submitList(resp.body() ?: emptyList())
                    } else Toast.makeText(this@FacturasActivity, "Error: ${resp.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { Toast.makeText(this@FacturasActivity, e.message, Toast.LENGTH_LONG).show() }
            }
        }
    }
}

class FacturasAdapter(private val onDescargar: (Long) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<Factura, FacturaVH>(
        object : androidx.recyclerview.widget.DiffUtil.ItemCallback<Factura>() {
            override fun areItemsTheSame(oldItem: Factura, newItem: Factura) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Factura, newItem: Factura) = oldItem == newItem
        }
    ) {
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): FacturaVH {
        val v = android.view.LayoutInflater.from(parent.context).inflate(com.bienestar.app.R.layout.row_factura, parent, false)
        return FacturaVH(v, onDescargar)
    }
    override fun onBindViewHolder(holder: FacturaVH, position: Int) { holder.bind(getItem(position)) }
}

class FacturaVH(itemView: android.view.View, private val onDescargar: (Long) -> Unit) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    fun bind(f: Factura) {
        itemView.findViewById<android.widget.TextView>(com.bienestar.app.R.id.tvFacturaId).text = "Factura #${f.id}"
        itemView.findViewById<android.widget.TextView>(com.bienestar.app.R.id.tvFecha).text = "Fecha: ${f.fechaEmision ?: "-"}"
        itemView.findViewById<android.widget.TextView>(com.bienestar.app.R.id.tvTotal).text = "Total: Q${f.total ?: 0.0}"

        itemView.setOnClickListener {
            f.id?.let { onDescargar(it) }
        }
    }
}
