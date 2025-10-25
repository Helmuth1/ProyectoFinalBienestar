import React, { useEffect, useState } from "react";
import axios from "axios";
import api from "../axiosConfig";
import "./Facturas.css"; // Opcional para estilos adicionales

const Facturas = () => {
  const [facturas, setFacturas] = useState([]);
  const [clienteFiltro, setClienteFiltro] = useState("");
  const [fechaInicio, setFechaInicio] = useState("");
  const [fechaFin, setFechaFin] = useState("");

  // 🔄 Cargar facturas al iniciar
  useEffect(() => {
    fetchFacturas();
  }, []);

  useEffect(() => {
  api.get("/facturas")
    .then(res => setFacturas(res.data))
    .catch(err => console.error("Error al obtener facturas:", err));
  }, []);

  const fetchFacturas = async () => {
    try {
      const response = await axios.get("http://localhost:8081/api/facturas");
      setFacturas(response.data);
    } catch (error) {
      console.error("❌ Error cargando facturas:", error);
    }
  };

  // 🔍 Filtrar por cliente
  const filtrarPorCliente = () => {
    if (clienteFiltro.trim() === "") {
      fetchFacturas();
      return;
    }

    axios
      .get(`http://localhost:8081/api/facturas/cliente/${clienteFiltro}`)
      .then((response) => setFacturas(response.data))
      .catch((error) => console.error("❌ Error al filtrar:", error));
  };

  // 📅 Filtrar por fechas
  const filtrarPorFechas = () => {
    if (!fechaInicio || !fechaFin) {
      alert("Por favor selecciona ambas fechas");
      return;
    }

    axios
      .get("http://localhost:8081/api/facturas/reporte", {
        params: { inicio: fechaInicio, fin: fechaFin },
      })
      .then((response) => setFacturas(response.data))
      .catch((error) => console.error("❌ Error al filtrar fechas:", error));
  };

  // 💾 Descargar PDF
  const descargarPDF = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8081/api/reportes/descargar",
        { responseType: "blob" }
      );

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "reporte_facturas.pdf");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error("❌ Error al descargar el PDF:", error);
    }
  };

  // Marcar factura como cobrada
  const marcarComoCobrada = async (idFactura) => {
  try {
    // 1️⃣ Cambiar estado en el backend
    await axios.put(`http://localhost:8081/api/facturas/${idFactura}/cobrar`);
    alert("✅ Factura marcada como cobrada correctamente");

    // 2️⃣ Descargar automáticamente el PDF
    const pdfResponse = await axios.get(
      `http://localhost:8081/api/facturas/${idFactura}/pdf`,
      { responseType: "blob" }
    );

    const url = window.URL.createObjectURL(new Blob([pdfResponse.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", `factura_${idFactura}.pdf`);
    document.body.appendChild(link);
    link.click();
    link.remove();

    // 3️⃣ Refrescar la tabla
    fetchFacturas();
  } catch (error) {
    console.error("❌ Error al marcar como cobrada:", error);
    alert("Error al actualizar el estado de la factura o descargar el PDF");
  }
};

  return (
    <div className="contenedor-facturas">
      <h2 className="titulo">📄 Gestión de Facturas</h2>

      {/* 🔍 Filtros */}
      <div className="filtros">
        <input
          type="text"
          placeholder="ID Cliente"
          value={clienteFiltro}
          onChange={(e) => setClienteFiltro(e.target.value)}
        />
        <button onClick={filtrarPorCliente}>Buscar Cliente</button>

        <input
          type="date"
          value={fechaInicio}
          onChange={(e) => setFechaInicio(e.target.value)}
        />
        <input
          type="date"
          value={fechaFin}
          onChange={(e) => setFechaFin(e.target.value)}
        />
        <button onClick={filtrarPorFechas}>Filtrar por Fechas</button>

        <button onClick={descargarPDF}>📥 Descargar PDF</button>
      </div>

      {/* 🧾 Tabla de facturas */}
      <table className="tabla-facturas">
        <thead>
          <tr>
            <th>ID</th>
            <th>Cliente</th>
            <th>Fecha Emisión</th>
            <th>Total</th>
            <th>Citas Asociadas</th>
            <th>Estado</th>
            <th>Acción</th>
          </tr>
        </thead>
        <tbody>
          {facturas.map((factura) => (
            <tr key={factura.id}>
              <td>{factura.id}</td>
              <td>{factura.cliente?.nombreCompleto || "Sin cliente"}</td>
              <td>{factura.fechaEmision}</td>
              <td>Q{factura.total?.toFixed(2) || "0.00"}</td>
              <td>{factura.citas?.length || 0}</td>
              <td
                style={{
                  color: factura.estado === "COBRADA" ? "green" : "red",
                    fontWeight: "bold",
                }}
              >
                {factura.estado}
              </td>
              <td>
                {!factura.citas?.some((c) => c.estado === "COBRADA") && (
                  <button
                    onClick={() => marcarComoCobrada(factura.id)}
                    className="btn-cobrar"
                  >
                     Marcar como Cobrada
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Facturas;
