import React, { useEffect, useState } from "react";
import api from "../axiosConfig";
import Swal from "sweetalert2";

const Citas = () => {
  const [clientes, setClientes] = useState([]);
  const [servicios, setServicios] = useState([]);
  const [citas, setCitas] = useState([]);

  const [nuevaCita, setNuevaCita] = useState({
    clienteId: "",
    servicioId: "",
    fecha: "",
    hora: "",
    estado: "Pendiente",
  });

  // 🔹 Cargar datos al montar el componente
  const cargarDatos = async () => {
    try {
      const [clientesRes, serviciosRes, citasRes] = await Promise.all([
        api.get("/clientes"),
        api.get("/servicios"),
        api.get("/citas"),
      ]);
      setClientes(clientesRes.data);
      setServicios(serviciosRes.data);
      setCitas(citasRes.data);
    } catch (error) {
      Swal.fire("Error", "No se pudieron cargar los datos.", "error");
    }
  };

  useEffect(() => {
    cargarDatos();
  }, []);

  // 🔹 Registrar nueva cita
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const cita = {
        cliente: { id: parseInt(nuevaCita.clienteId) },
        servicio: { id: parseInt(nuevaCita.servicioId) },
        fecha: nuevaCita.fecha,
        hora: nuevaCita.hora,
        estado: nuevaCita.estado,
      };

      await api.post("/api/citas/registrar", cita);
      Swal.fire("Éxito", "Cita registrada correctamente", "success");
      setNuevaCita({
        clienteId: "",
        servicioId: "",
        fecha: "",
        hora: "",
        estado: "Pendiente",
      });
      cargarDatos();
    } catch (error) {
      Swal.fire("Error", "No se pudo registrar la cita", "error");
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">Gestión de Citas</h2>

      {/* Formulario */}
      <form onSubmit={handleSubmit} className="border p-4 rounded shadow-sm">
        <div className="mb-3">
          <label className="form-label">Cliente:</label>
          <select
            className="form-select"
            value={nuevaCita.clienteId}
            onChange={(e) =>
              setNuevaCita({ ...nuevaCita, clienteId: e.target.value })
            }
            required
          >
            <option value="">Seleccione un cliente</option>
            {clientes.map((c) => (
              <option key={c.id} value={c.id}>
                {c.nombreCompleto} ({c.email})
              </option>
            ))}
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Servicio:</label>
          <select
            className="form-select"
            value={nuevaCita.servicioId}
            onChange={(e) =>
              setNuevaCita({ ...nuevaCita, servicioId: e.target.value })
            }
            required
          >
            <option value="">Seleccione un servicio</option>
            {servicios.map((s) => (
              <option key={s.id} value={s.id}>
                {s.nombre} - Q{s.precio}
              </option>
            ))}
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Fecha:</label>
          <input
            type="date"
            className="form-control"
            value={nuevaCita.fecha}
            onChange={(e) =>
              setNuevaCita({ ...nuevaCita, fecha: e.target.value })
            }
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Hora:</label>
          <input
            type="time"
            className="form-control"
            value={nuevaCita.hora}
            onChange={(e) =>
              setNuevaCita({ ...nuevaCita, hora: e.target.value })
            }
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Estado:</label>
          <select
            className="form-select"
            value={nuevaCita.estado}
            onChange={(e) =>
              setNuevaCita({ ...nuevaCita, estado: e.target.value })
            }
          >
            <option value="Pendiente">Pendiente</option>
            <option value="Completada">Completada</option>
            <option value="Cancelada">Cancelada</option>
          </select>
        </div>

        <button type="submit" className="btn btn-primary w-100">
          Registrar Cita
        </button>
      </form>

      {/* Tabla */}
      <div className="mt-4">
        <h4>Listado de Citas</h4>
        <table className="table table-striped">
          <thead>
            <tr>
              <th>ID</th>
              <th>Cliente</th>
              <th>Servicio</th>
              <th>Fecha</th>
              <th>Hora</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {citas.map((cita) => (
              <tr key={cita.id}>
                <td>{cita.id}</td>
                <td>{cita.cliente?.nombreCompleto}</td>
                <td>{cita.servicio?.nombre}</td>
                <td>{cita.fecha}</td>
                <td>{cita.hora}</td>
                <td>{cita.estado}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Citas;