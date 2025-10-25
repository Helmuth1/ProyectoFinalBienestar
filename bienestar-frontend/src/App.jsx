import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Login from "./components/Login";
import Register from "./components/Register";
import Dashboard from "./components/Dashboard";
import CrearCliente from "./components/CrearCliente";
import ListaClientes from "./pages/ListaClientes";
import EditarCliente from "./components/EditarCliente";
import Citas from "./components/Citas";
import Reportes from "./components/Reportes";
import Servicios from "./pages/Servicios";
import Facturas from "./components/Facturas"; 
import Menu from "./components/Menu";
import PrivateRoute from "./components/PrivateRoute";

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
          <Route path="/menu" element={<PrivateRoute><Menu /></PrivateRoute>} />
          <Route path="/clientes/crear" element={<PrivateRoute><CrearCliente /></PrivateRoute>} />
          <Route path="/clientes" element={<PrivateRoute><ListaClientes /></PrivateRoute>} />
          <Route path="/clientes/editar/:id" element={<PrivateRoute><EditarCliente /></PrivateRoute>} />
          <Route path="/citas" element={<PrivateRoute><Citas /></PrivateRoute>} />
          <Route path="/reportes" element={<PrivateRoute><Reportes /></PrivateRoute>} />
          <Route path="/servicios" element={<PrivateRoute><Servicios /></PrivateRoute>} />
          <Route path="/facturas" element={<PrivateRoute><Facturas /></PrivateRoute>} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;