import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await authService.login(username, password);
      navigate('/dashboard');
    } catch (err) {
      setError('Error al iniciar sesión');
      console.error(err);
    }
  };

  return (
    <div style={{maxWidth: 360, margin: '40px auto', padding: 20, border: '1px solid #ddd', borderRadius: 8}}>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div style={{marginBottom: 8}}>
          <label>Usuario</label>
          <input value={username} onChange={e => setUsername(e.target.value)} className="form-input" />
        </div>
        <div style={{marginBottom: 8}}>
          <label>Contraseña</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} className="form-input" />
        </div>
        <button type="submit">Entrar</button>
        {error && <div style={{color: 'red', marginTop: 8}}>{error}</div>}
      </form>
    </div>
  );
};

export default Login;
