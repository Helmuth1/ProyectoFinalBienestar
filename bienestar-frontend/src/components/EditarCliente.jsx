import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../axiosConfig";
import Swal from "sweetalert2";

function EditarCliente() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [cliente, setCliente] = useState({
    nombreCompleto: "",
    email: "",
    telefono: "",
    nit: "",
  });

  useEffect(() => {
    const cargarCliente = async () => {
      try {
        const response = await api.get(`/api/clientes/${id}`);
        setCliente(response.data);
      } catch (error) {
        Swal.fire("Error", "No se pudo cargar el cliente.", "error");
      }
    };
    cargarCliente();
  }, [id]);

  const handleChange = (e) => {
    setCliente({
      ...cliente,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.put(`/api/clientes/${id}`, cliente);
      Swal.fire("Éxito", "Cliente actualizado correctamente", "success");
      navigate("/clientes");
    } catch (error) {
      Swal.fire("Error", "No se pudo actualizar el cliente", "error");
    }
  };

  return (
    <div className="container mt-4">
      <h2>Editar Cliente</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label>NIT</label>
          <input
            type="text"
            name="nit"
            value={cliente.nit}
            onChange={handleChange}
            className="form-control"
          />
        </div>

        <div className="mb-3">
          <label>Nombre Completo</label>
          <input
            type="text"
            name="nombreCompleto"
            value={cliente.nombreCompleto}
            onChange={handleChange}
            className="form-control"
          />
        </div>

        <div className="mb-3">
          <label>Email</label>
          <input
            type="email"
            name="email"
            value={cliente.email}
            onChange={handleChange}
            className="form-control"
          />
        </div>

        <div className="mb-3">
          <label>Teléfono</label>
          <input
            type="text"
            name="telefono"
            value={cliente.telefono}
            onChange={handleChange}
            className="form-control"
          />
        </div>

        <button type="submit" className="btn btn-primary">
          Guardar Cambios
        </button>
      </form>
    </div>
  );
}

export default EditarCliente;