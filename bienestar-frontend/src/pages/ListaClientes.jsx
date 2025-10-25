import React, { useEffect, useState } from "react";
import api from "../axiosConfig";
import Swal from "sweetalert2";
import { Link } from "react-router-dom";

const ListaClientes = () => {
  const [clientes, setClientes] = useState([]);

  // 🔹 Cargar clientes al iniciar
  const cargarClientes = async () => {
    try {
      const res = await api.get("/clientes");
      setClientes(res.data);
    } catch (error) {
      console.error("Error al cargar clientes:", error);
      Swal.fire("Error", "No se pudieron cargar los clientes", "error");
    }
  };

  const cambiarEstado = async (id, nuevoEstado) => {
  const accion = nuevoEstado ? "activar" : "desactivar";

  const confirm = await Swal.fire({
    title: `¿Deseas ${accion} este cliente?`,
    text: `El cliente será marcado como ${nuevoEstado ? "activo" : "inactivo"}.`,
    icon: "question",
    showCancelButton: true,
    confirmButtonText: `Sí, ${accion}`,
    cancelButtonText: "Cancelar",
    confirmButtonColor: nuevoEstado ? "#28a745" : "#6c757d",
  });

  if (confirm.isConfirmed) {
    try {
      await api.put(`/clientes/${id}/estado?activo=${nuevoEstado}`);
      Swal.fire("Éxito", `La accion ${accion} cliente ha sido realizada correctamente.`, "success");
      cargarClientes(); // Recarga la lista
    } catch (error) {
      Swal.fire("Error", `No se pudo ${accion} el cliente.`, "error");
    }
  }
};

  // 🔹 Eliminar cliente
  const eliminarCliente = async (id) => {
    const confirm = await Swal.fire({
      title: "¿Eliminar cliente?",
      text: "Esta acción no se puede deshacer",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar",
      confirmButtonColor: "#d33",
    });

    if (confirm.isConfirmed) {
      try {
        await api.delete(`/clientes/${id}`);
        Swal.fire("Eliminado", "Cliente eliminado correctamente", "success");
        cargarClientes();
      } catch (error) {
        Swal.fire("Error", "No se pudo eliminar el cliente", "error");
      }
    }
  };

  useEffect(() => {
    cargarClientes();
  }, []);

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h3>Lista de Clientes</h3>
        <Link to="/clientes/crear" className="btn btn-success">
          ➕ Nuevo Cliente
        </Link>
      </div>

      {clientes.length === 0 ? (
        <p>No hay clientes registrados.</p>
      ) : (
        <table className="table table-striped table-hover">
          <thead className="table-dark">
            <tr>
              <th>ID</th>
              <th>NIT</th>
              <th>Nombre</th>
              <th>Email</th>
              <th>Teléfono</th>
              <th>Activo</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {clientes.map((cliente) => (
              <tr key={cliente.id}>
                <td>{cliente.id}</td>
                <td>{cliente.nit}</td>
                <td>{cliente.nombreCompleto}</td>
                <td>{cliente.email}</td>
                <td>{cliente.telefono}</td>
                <td>{cliente.activo ? "✅" : "❌"}</td>
                <td>
                 <button
                  onClick={() => cambiarEstado(cliente.id, !cliente.activo)}
                    className={`btn btn-${cliente.activo ? "secondary" : "success"} btn-sm me-2`}
                 >
                    {cliente.activo ? "Desactivar" : "Reactivar"}
                </button>

                <Link
                    to={`/clientes/editar/${cliente.id}`}
                    className="btn btn-warning btn-sm me-2"
                >
                    Editar
                </Link>

                <button
                    onClick={() => eliminarCliente(cliente.id)}
                    className="btn btn-danger btn-sm"
                >
                    Eliminar
                </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default ListaClientes;