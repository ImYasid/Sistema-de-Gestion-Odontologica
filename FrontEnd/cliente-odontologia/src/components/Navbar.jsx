import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import authService from '../services/authService';

const Navbar = () => {
  const navigate = useNavigate();
  const isAuth = authService.isAuthenticated();

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <nav style={{display: 'flex', gap: 12, padding: '12px 24px', background: '#f5f5f5', alignItems:'center'}}>
      <Link to="/dashboard" className="navbar-brand">EndoNova</Link>
      {isAuth ? (
        <>
          <Link to="/dashboard">Dashboard</Link>
          <Link to="/pacientes">Pacientes</Link>
          <Link to="/fichas">Fichas</Link>
          <Link to="/odontograma">Odontograma</Link>
          <button className="btn btn-logout" onClick={handleLogout}>Salir</button>
        </>
      ) : (
        <Link to="/login">Login</Link>
      )}
    </nav>
  );
};

export default Navbar;
