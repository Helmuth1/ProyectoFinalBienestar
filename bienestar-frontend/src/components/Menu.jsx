import React from "react";
import { useNavigate } from "react-router-dom";

function Menu() {
  const navigate = useNavigate();

  const goTo = (path) => {
    navigate(path);
  };

  const logout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <div style={styles.container}>
      <h2>Menú Principal</h2>

      <div style={styles.buttonsContainer}>
        <button onClick={() => goTo("/clientes")} style={styles.button}>Clientes</button>
        <button onClick={() => goTo("/servicios")} style={styles.button}>Servicios</button>
        <button onClick={() => goTo("/citas")} style={styles.button}>Citas</button>
        <button onClick={() => goTo("/facturas")} style={styles.button}>Facturas</button>
        <button onClick={() => goTo("/reportes")} style={styles.button}>Reportes</button>
      </div>

      <button onClick={logout} style={{ ...styles.button, backgroundColor: "#d9534f" }}>
        Cerrar Sesión
      </button>
    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    height: "100vh",
    fontFamily: "Arial",
  },
  buttonsContainer: {
    display: "flex",
    flexDirection: "column",
    gap: "15px",
    margin: "20px 0",
  },
  button: {
    padding: "12px 20px",
    border: "none",
    borderRadius: "6px",
    backgroundColor: "#007bff",
    color: "white",
    cursor: "pointer",
    fontSize: "16px",
    width: "200px",
  },
};

export default Menu;
