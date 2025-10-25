import React, { useState } from "react";
import axios from "axios";
import api from "../axiosConfig";
import { useNavigate } from "react-router-dom";

function Register() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    try {
      const response = await axios.post(
        "http://localhost:8081/api/auth/register",
        formData
      );

      console.log("✅ Registro exitoso:", response.data);
      setSuccess("Usuario registrado correctamente. Ahora puedes iniciar sesión.");
      
      // Redirigir al login después de 1 segundo
      setTimeout(() => navigate("/login"), 1000);

    } catch (err) {
      console.error("❌ Error en registro:", err);
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError("Ocurrió un error al registrar el usuario.");
      }
    }
  };

  return (
    <div style={styles.container}>
      <h2>Registrar usuario</h2>
      <form onSubmit={handleSubmit} style={styles.form}>
        <input
          type="text"
          name="username"
          placeholder="Correo electrónico"
          value={formData.username}
          onChange={handleChange}
          required
          style={styles.input}
        />
        <input
          type="password"
          name="password"
          placeholder="Contraseña"
          value={formData.password}
          onChange={handleChange}
          required
          style={styles.input}
        />
        <button type="submit" style={styles.button}>
          Registrar
        </button>
        {error && <p style={styles.error}>{error}</p>}
        {success && <p style={styles.success}>{success}</p>}
      </form>
    </div>
  );
}

// Estilos
const styles = {
  container: {
    width: "300px",
    margin: "100px auto",
    textAlign: "center",
    fontFamily: "Arial",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    gap: "10px",
  },
  input: {
    padding: "10px",
    borderRadius: "5px",
    border: "1px solid #ccc",
  },
  button: {
    padding: "10px",
    backgroundColor: "#28a745",
    color: "white",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
  },
  error: {
    color: "red",
  },
  success: {
    color: "green",
  },
};

export default Register;