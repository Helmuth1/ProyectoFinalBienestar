package com.bienestar.app.models

data class LoginRequest(val username: String, val password: String)
data class JwtResponse(val token: String?, val id: Long?, val username: String?, val message: String?)

data class Cliente(
    val id: Long,
    val nit: String?,
    val nombreCompleto: String?,
    val email: String?,
    val telefono: String?,
    val activo: Boolean?
)

data class Servicio(
    val id: Long? = null,
    val nombre: String? = null,
    val descripcion: String? = null,
    val precio: Double? = null,
    val duracionMinutos: Int? = null,
    val activo: Boolean? = null
)

data class Cita(
    val id: Long? = null,
    val cliente: Cliente? = null,
    val servicio: Servicio? = null,
    val fecha: String? = null,
    val hora: String? = null,
    val estado: String? = null
)

data class Factura(
    val id: Long? = null,
    val cliente: Cliente? = null,
    val fechaEmision: String? = null,
    val total: Double? = null,
    val estado: String? = null,
    val citas: List<Cita>? = null
)
