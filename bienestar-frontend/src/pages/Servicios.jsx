import React, { useEffect, useState } from "react";
import axios from "axios";
import api from "../axiosConfig";

const Servicios = () => {
  const [servicios, setServicios] = useState([]);
  const [nuevoServicio, setNuevoServicio] = useState({
    nombre: "",
    descripcion: "",
    precio: "",
    duracionMinutos: "",
    activo: true,
  });
  const [modoEdicion, setModoEdicion] = useState(false);
  const [servicioEditando, setServicioEditando] = useState(null);

  // ✅ Cargar todos los servicios
  const cargarServicios = async () => {
    try {
      const response = await axios.get("http://localhost:8081/api/servicios");
      setServicios(response.data);
    } catch (error) {
      console.error("❌ Error al cargar servicios:", error);
    }
  };

  useEffect(() => {
    cargarServicios();
  }, []);

  useEffect(() => {
  api.get("/servicios")
    .then(res => setServicios(res.data))
    .catch(err => console.error("Error al obtener servicios:", err));
  }, []);

  // ✅ Registrar nuevo servicio
  const registrarServicio = async () => {
    try {
      await axios.post("http://localhost:8081/api/servicios/registrar", nuevoServicio, {
        headers: { "Content-Type": "application/json" },
      });
      alert("✅ Servicio registrado con éxito");
      setNuevoServicio({
        nombre: "",
        descripcion: "",
        precio: "",
        duracionMinutos: "",
        activo: true,
      });
      cargarServicios();
    } catch (error) {
      console.error("❌ Error al registrar servicio:", error);
      alert("Ocurrió un error al registrar el servicio.");
    }
  };

  // ✅ Eliminar servicio
  const eliminarServicio = async (id) => {
    if (window.confirm("¿Seguro que deseas eliminar este servicio?")) {
      try {
        await axios.delete(`http://localhost:8081/api/servicios/${id}`);
        alert("🗑️ Servicio eliminado correctamente");
        cargarServicios();
      } catch (error) {
        console.error("❌ Error al eliminar servicio:", error);
      }
    }
  };

  // ✅ Editar servicio (modo edición)
  const editarServicio = (servicio) => {
    setModoEdicion(true);
    setServicioEditando(servicio);
    setNuevoServicio({ ...servicio });
  };

  // ✅ Guardar cambios de edición
  const actualizarServicio = async () => {
    try {
      await axios.put(
        `http://localhost:8081/api/servicios/${servicioEditando.id}`,
        nuevoServicio,
        { headers: { "Content-Type": "application/json" } }
      );
      alert("✏️ Servicio actualizado con éxito");
      setModoEdicion(false);
      setServicioEditando(null);
      setNuevoServicio({
        nombre: "",
        descripcion: "",
        precio: "",
        duracionMinutos: "",
        activo: true,
      });
      cargarServicios();
    } catch (error) {
      console.error("❌ Error al actualizar servicio:", error);
    }
  };

  return (
    <div className="p-8 bg-gray-100 min-h-screen">
      <h1 className="text-3xl font-bold mb-6 text-blue-600">Gestión de Servicios</h1>

      {/* FORMULARIO */}
      <div className="bg-white shadow-md rounded-lg p-6 mb-8">
        <h2 className="text-xl font-semibold mb-4">
          {modoEdicion ? "Editar Servicio" : "Registrar Nuevo Servicio"}
        </h2>

        <div className="grid grid-cols-2 gap-4">
          <input
            type="text"
            placeholder="Nombre"
            className="border rounded-lg p-2"
            value={nuevoServicio.nombre}
            onChange={(e) => setNuevoServicio({ ...nuevoServicio, nombre: e.target.value })}
          />
          <input
            type="number"
            placeholder="Precio"
            className="border rounded-lg p-2"
            value={nuevoServicio.precio}
            onChange={(e) => setNuevoServicio({ ...nuevoServicio, precio: e.target.value })}
          />
          <input
            type="number"
            placeholder="Duración (minutos)"
            className="border rounded-lg p-2"
            value={nuevoServicio.duracionMinutos}
            onChange={(e) =>
              setNuevoServicio({ ...nuevoServicio, duracionMinutos: e.target.value })
            }
          />
          <input
            type="text"
            placeholder="Descripción"
            className="border rounded-lg p-2 col-span-2"
            value={nuevoServicio.descripcion}
            onChange={(e) => setNuevoServicio({ ...nuevoServicio, descripcion: e.target.value })}
          />
        </div>

        <button
          onClick={modoEdicion ? actualizarServicio : registrarServicio}
          className={`mt-4 px-6 py-2 rounded-lg text-white ${
            modoEdicion ? "bg-yellow-500" : "bg-green-600"
          }`}
        >
          {modoEdicion ? "Guardar Cambios" : "Registrar Servicio"}
        </button>
      </div>

      {/* TABLA DE SERVICIOS */}
      <div className="bg-white shadow-md rounded-lg p-6">
        <h2 className="text-xl font-semibold mb-4 text-gray-700">Lista de Servicios</h2>
        <table className="min-w-full border border-gray-300">
          <thead className="bg-gray-200">
            <tr>
              <th className="p-2 border">ID</th>
              <th className="p-2 border">Nombre</th>
              <th className="p-2 border">Precio</th>
              <th className="p-2 border">Duración</th>
              <th className="p-2 border">Estado</th>
              <th className="p-2 border">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {servicios.length > 0 ? (
              servicios.map((servicio) => (
                <tr key={servicio.id} className="text-center border-t">
                  <td className="p-2">{servicio.id}</td>
                  <td className="p-2">{servicio.nombre}</td>
                  <td className="p-2">Q{servicio.precio}</td>
                  <td className="p-2">{servicio.duracionMinutos} min</td>
                  <td className="p-2">
                    {servicio.activo ? (
                      <span className="text-green-600 font-semibold">Activo</span>
                    ) : (
                      <span className="text-red-500 font-semibold">Inactivo</span>
                    )}
                  </td>
                  <td className="p-2 flex justify-center gap-3">
                    <button
                      onClick={() => editarServicio(servicio)}
                      className="bg-yellow-400 px-3 py-1 rounded text-white hover:bg-yellow-500"
                    >
                      ✏️
                    </button>
                    <button
                      onClick={() => eliminarServicio(servicio.id)}
                      className="bg-red-500 px-3 py-1 rounded text-white hover:bg-red-600"
                    >
                      🗑️
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" className="p-4 text-gray-500">
                  No hay servicios registrados.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Servicios;