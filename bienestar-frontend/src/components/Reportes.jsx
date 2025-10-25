import React, { useState, useEffect } from "react";
import api from "../axiosConfig";
import Swal from "sweetalert2";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer,
} from "recharts";

const COLORS = ["#8884d8", "#82ca9d", "#ffc658", "#ff7f7f", "#7fb3ff"];

const Reportes = () => {
  const [reporte, setReporte] = useState({});
  const [fechaInicio, setFechaInicio] = useState("");
  const [fechaFin, setFechaFin] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    cargarReporteGeneral();
  }, []);

  const cargarReporteGeneral = async () => {
    try {
      const res = await api.get("/reportes/general");
      setReporte(res.data);
      setLoading(false);
    } catch (error) {
      Swal.fire("Error", "No se pudo cargar el reporte general.", "error");
    }
  };

  const descargarPDF = async () => {
  try {
    const response = await api.get("/api/reportes/pdf", {
      responseType: "blob",
    });

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", "ReporteGeneral.pdf");
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    Swal.fire("Error", "No se pudo generar el PDF", "error");
  }
};

  const generarPorFechas = async () => {
    try {
      const res = await api.get("/api/reportes/por-fechas", {
        params: { inicio: fechaInicio, fin: fechaFin },
      });
      Swal.fire("Reporte generado", `${res.data.length} facturas encontradas`, "info");
    } catch {
      Swal.fire("Error", "No se pudo generar el reporte por fechas", "error");
    }
  };

  if (loading) return <p className="text-center mt-5">Cargando reporte...</p>;

  // Datos para la gráfica de barras
  const dataBar = [
    { name: "Clientes", total: reporte.totalClientes },
    { name: "Servicios", total: reporte.totalServicios },
    { name: "Citas", total: reporte.totalCitas },
    { name: "Facturas", total: reporte.totalFacturas },
  ];

  // Datos para la gráfica de pastel
  const dataPie = [
    { name: "Ingresos", value: reporte.totalIngresos || 0 },
    { name: "Pendiente", value: 10000 - (reporte.totalIngresos || 0) },
  ];

  return (
    <div className="container mt-4">
      <h2 className="mb-4 text-center">📊 Reporte General</h2>

      {/* 📈 Gráfica de barras */}
      <div className="card p-4 shadow-sm mb-4">
        <h5>Resumen de entidades</h5>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={dataBar}>
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Bar dataKey="total" fill="#8884d8" />
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* 🥧 Gráfica de pastel */}
      <div className="card p-4 shadow-sm mb-4">
        <h5>Distribución de ingresos</h5>
        <ResponsiveContainer width="100%" height={300}>
          <PieChart>
            <Pie
              data={dataPie}
              dataKey="value"
              nameKey="name"
              cx="50%"
              cy="50%"
              outerRadius={100}
              fill="#82ca9d"
              label
            >
              {dataPie.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
              ))}
            </Pie>
            <Tooltip />
          </PieChart>
        </ResponsiveContainer>
      </div>

      {/* 📅 Reporte por fechas */}
      <div className="card p-3 shadow-sm">
        <h5>Reporte por fechas</h5>
        <div className="d-flex gap-2 mt-2">
          <input
            type="date"
            className="form-control"
            value={fechaInicio}
            onChange={(e) => setFechaInicio(e.target.value)}
          />
          <input
            type="date"
            className="form-control"
            value={fechaFin}
            onChange={(e) => setFechaFin(e.target.value)}
          />
          <button className="btn btn-primary" onClick={generarPorFechas}>
              Generar
                </button>
                </div>
                </div>
                <div className="text-center mt-4">
                <button className="btn btn-danger" onClick={descargarPDF}>
              📄 Descargar reporte PDF
          </button>
    </div>
    </div>
  );
};

export default Reportes;