import React, { useEffect, useState } from "react";
import api from "../axiosConfig";

export default function Clientes() {
  const [clientes, setClientes] = useState([]);

  useEffect(() => {
    api.get("/api/clientes")
      .then(res => setClientes(res.data))
      .catch(err => console.error("Error al cargar clientes:", err));
  }, []);

  return (
    <div>
      <h2>Listado de Clientes</h2>
      <ul>
        {clientes.map(c => (
          <li key={c.id}>{c.nombreCompleto} - {c.email}</li>
        ))}
      </ul>
    </div>
  );
}