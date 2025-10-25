import React, { useState } from "react";
import api from "../axiosConfig";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

const CrearCliente = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nit: "",
    nombreCompleto: "",
    email: "",
    telefono: "",
    password: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await api.post("/api/auth/register", formData);

      Swal.fire({
        title: "Cliente registrado",
        text: "El cliente se ha creado correctamente.",
        icon: "success",
        confirmButtonText: "Ir al Dashboard",
        confirmButtonColor: "#3085d6",
      }).then(() => {
        navigate("/");
      });

      setFormData({
        nit: "",
        nombreCompleto: "",
        email: "",
        telefono: "",
        password: "",
      });
    } catch (error) {
      console.error(error);

      Swal.fire({
        title: "Error al registrar",
        text:
          error.response?.data ||
          "No se pudo registrar el cliente. Verifica los datos e inténtalo de nuevo.",
        icon: "error",
        confirmButtonText: "Cerrar",
      });
    }
  };

  return (
    <div className="container mt-4" style={{ maxWidth: "500px" }}>
      <h3>Registrar Nuevo Cliente</h3>
      <form onSubmit={handleSubmit}>
        <div className="form-group mb-3">
          <label>NIT:</label>
          <input
            type="text"
            className="form-control"
            name="nit"
            value={formData.nit}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group mb-3">
          <label>Nombre Completo:</label>
          <input
            type="text"
            className="form-control"
            name="nombreCompleto"
            value={formData.nombreCompleto}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group mb-3">
          <label>Email:</label>
          <input
            type="email"
            className="form-control"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group mb-3">
          <label>Teléfono:</label>
          <input
            type="text"
            className="form-control"
            name="telefono"
            value={formData.telefono}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group mb-3">
          <label>Contraseña:</label>
          <input
            type="password"
            className="form-control"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit" className="btn btn-primary w-100">
          Registrar Cliente
        </button>
      </form>
    </div>
  );
};

export default CrearCliente;