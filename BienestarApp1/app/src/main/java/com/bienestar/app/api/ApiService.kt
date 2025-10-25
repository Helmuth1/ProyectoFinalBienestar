package com.bienestar.app.api

import com.bienestar.app.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Perfil
    @GET("api/clientes/{id}")
    suspend fun getCliente(@Path("id") id: Long): Response<Cliente>

    @GET("clientes/perfil")
    fun getPerfil(@Header("Authorization") token: String): Call<Cliente>
    // Servicios
    @GET("api/servicios")
    suspend fun listarServicios(): Response<List<Servicio>>

    // Citas
    @POST("api/citas/registrar")
    suspend fun crearCita(@Body body: Map<String, Any>): Response<Cita>

    @GET("api/citas/cliente/{idCliente}")
    suspend fun historialCitas(@Path("idCliente") idCliente: Long): Response<List<Cita>>

    // Facturas
    @GET("api/facturas/cliente/{idCliente}")
    suspend fun facturasPorCliente(@Path("idCliente") idCliente: Long): Response<List<Factura>>

    @Streaming
    @GET("api/reportes/facturas/{idFactura}/pdf")
    suspend fun descargarFacturaPDF(@Path("idFactura") idFactura: Long): Response<ResponseBody>
}
