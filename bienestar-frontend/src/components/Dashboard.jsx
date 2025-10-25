import React, { useEffect, useState } from "react";
import api from "../axiosConfig";

export default function Dashboard() {
  const [clientes, setClientes] = useState([]);
  const [servicios, setServicios] = useState([]);
  const [citas, setCitas] = useState([]);
  const [facturas, setFacturas] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [resClientes, resServicios, resCitas, resFacturas] = await Promise.all([
          api.get("/api/clientes"),
          api.get("/api/servicios"),
          api.get("/api/citas"),
          api.get("/api/facturas")
          
        ])
        ;

        setClientes(resClientes.data);
        setServicios(resServicios.data);
        setCitas(resCitas.data);
        setFacturas(
          Array.isArray(resFacturas.data)
            ? resFacturas.data
            : resFacturas.data?.content || [] 
          );
      } catch (err) {
        console.error("❌ Error cargando datos:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) return <h2>Cargando datos...</h2>;

  return (
    <div style={styles.container}>
      <h1 style={styles.title}>Panel Administrativo</h1>

      <div style={styles.grid}>
        {/* CLIENTES */}
        <div style={styles.card}>
          <h2>Clientes ({clientes.length})</h2>
          <ul>
            {clientes.map(c => (
              <li key={c.id}>
                <strong>{c.nombreCompleto}</strong> - {c.email}
              </li>
            ))}
          </ul>
        </div>

        {/* SERVICIOS */}
        <div style={styles.card}>
          <h2>Servicios ({servicios.length})</h2>
          <ul>
            {servicios.map(s => (
              <li key={s.id}>
                {s.nombre} - Q{s.precio}
              </li>
            ))}
          </ul>
        </div>

        {/* CITAS */}
        <div style={styles.card}>
          <h2>Citas ({citas.length})</h2>
          <ul>
            {citas.map(c => (
              <li key={c.id}>
                Cliente #{c.cliente?.id} – {c.fecha} – {c.servicio?.nombre}
              </li>
            ))}
          </ul>
        </div>

        {/* FACTURAS */}
        <div style={styles.card}>
          <h2>Facturas ({facturas.length})</h2>
          <ul>
            {facturas.map(f => (
              <li key={f.id}>
                Factura #{f.id} – Cliente #{f.cliente?.id} – Total: Q{f.total}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}

const styles = {
  container: {
    padding: "20px",
    fontFamily: "Arial, sans-serif",
  },
  title: {
    textAlign: "center",
    marginBottom: "20px",
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(2, 1fr)",
    gap: "20px",
  },
  card: {
    background: "#f8f9fa",
    padding: "15px",
    borderRadius: "8px",
    boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
  },
};