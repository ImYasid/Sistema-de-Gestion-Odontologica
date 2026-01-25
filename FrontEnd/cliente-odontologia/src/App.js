import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Odontograma from './components/odontograma/Odontograma';
import Navbar from './components/Navbar';
import Login from './components/Login';
import Dashboard from './pages/Dashboard';
import Pacientes from './pages/Pacientes';
import Fichas from './pages/Fichas';
import authService from './services/authService';
import OdontogramaPage from './pages/OdontogramaPage';

const Protected = ({ children }) => {
  return authService.isAuthenticated() ? children : <Navigate to="/login" replace />;
};

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar />

        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/dashboard" element={<Protected><Dashboard /></Protected>} />
          <Route path="/pacientes" element={<Protected><Pacientes /></Protected>} />
          <Route path="/fichas" element={<Protected><Fichas /></Protected>} />
          <Route path="/odontograma" element={<Protected><OdontogramaPage /></Protected>} />
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
