import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const API_PAC = axios.create({ baseURL: 'http://localhost:8082' });
const API_FICHAS = axios.create({ baseURL: 'http://localhost:8083' });

const Dashboard = () => {
  const [pacientesCount, setPacientesCount] = useState(0);
  const [fichasCount, setFichasCount] = useState(0);
  const [endosPendientes, setEndosPendientes] = useState(0);
  const [recentPacientes, setRecentPacientes] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    (async () => {
      try {
        const [pRes, fRes, dRes] = await Promise.all([
          API_PAC.get('/pacientes'),
          API_FICHAS.get('/fichas'),
          // buscamos diagnosticos que contengan 'Endodoncia' (approx) - fallback: get all and filter
          API_FICHAS.get('/diagnosticos')
        ]);

        const pacs = pRes.data || [];
        setPacientesCount(pacs.length);
        setRecentPacientes(pacs.slice(0,5));

        const fichas = fRes.data || [];
        setFichasCount(fichas.length);

        const diags = dRes.data || [];
        const endos = diags.filter(d => d.diagnosticoPulpar && d.diagnosticoPulpar.toLowerCase().includes('endodonci'));
        setEndosPendientes(endos.length);
      } catch (e) {
        console.error('Error cargando métricas', e);
      }
    })();
  }, []);

  return (
    <div className="dashboard-container">
      <div className="hero">
        <div className="hero-text">
          <h1>Bienvenido a EndoNova</h1>
          <p>Gestiona pacientes, fichas y odontogramas de forma rápida y clara.</p>
          <div className="quick-actions">
            <button className="btn btn-primary" onClick={() => navigate('/pacientes')}>Gestionar Pacientes</button>
            <button className="btn" style={{marginLeft:8}} onClick={() => navigate('/fichas')}>Ver Fichas</button>
            <div style={{marginTop:10}}>
              <button className="btn btn-success btn-odontograma" onClick={() => navigate('/odontograma')}>Abrir Odontograma</button>
            </div>
          </div>
        </div>
        <div className="hero-graphic">
          <div className="stat-card">
            <div className="stat-value">{pacientesCount}</div>
            <div className="stat-label">Pacientes</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{fichasCount}</div>
            <div className="stat-label">Fichas</div>
          </div>
          <div className="stat-card stat-warning">
            <div className="stat-value">{endosPendientes}</div>
            <div className="stat-label">Endodoncias</div>
          </div>
        </div>
      </div>

      <div className="recent-section">
        <h3>Pacientes recientes</h3>
        {recentPacientes.length === 0 ? <div>No hay pacientes aún.</div> : (
          <ul className="recent-list">
            {recentPacientes.map(p => (
              <li key={p.id} className="recent-item">
                <div>
                  <strong>{p.nombre} {p.apellido}</strong>
                  <div className="muted">{p.cedula} — {p.telefono}</div>
                  <button className="btn" style={{ marginTop: 8 }} onClick={() => navigate(`/odontograma?paciente=${p.id}`)}>Abrir Odontograma</button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>

    </div>
  );
};

export default Dashboard;
