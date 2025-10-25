import React, { useState } from "react";
import axios from "axios";
import api from "../axiosConfig";
import { useNavigate } from "react-router-dom";

function Login() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { username, password } = formData; // ✅ Extraer datos correctamente

    try {
      const response = await axios.post("http://localhost:8081/api/auth/login", {
        username,
        password,
      });

      const token = response.data.token;
      localStorage.setItem("token", token); // ✅ guardar token en el navegador
      alert("Inicio de sesión exitoso");

      // ✅ Redirigir al menú principal
      navigate("/menu");
    } catch (error) {
      console.error("Error en las credenciales:", error);
      setError("Usuario o contraseña incorrectos");
    }
  };

  return (
    <div style={styles.container}>
      <h2>Iniciar sesión</h2>
      <form onSubmit={handleSubmit} style={styles.form}>
        <input
          type="text"
          name="username"
          placeholder="Usuario"
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
          Entrar
        </button>
        {error && <p style={styles.error}>{error}</p>}
      </form>
    </div>
  );
}

// 🎨 Estilos sencillos en JS
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
    backgroundColor: "#007bff",
    color: "white",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
  },
  error: {
    color: "red",
  },
};

export default Login;