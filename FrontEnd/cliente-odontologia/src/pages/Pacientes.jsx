import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const API = axios.create({ baseURL: 'http://localhost:8082' });

const emptyForm = { nombre: '', apellido: '', cedula: '', telefono: '', email: '' };

const Pacientes = () => {
  const [pacientes, setPacientes] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const fetchPacientes = async () => {
    try {
      const resp = await API.get('/pacientes');
      setPacientes(resp.data || []);
    } catch (err) { console.error('Error fetching pacientes', err); }
  };

  useEffect(() => { fetchPacientes(); }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      // Limpiar campos vacíos
      const datosLimpios = {};
      if (form.nombre && form.nombre.trim()) datosLimpios.nombre = form.nombre.trim();
      if (form.apellido && form.apellido.trim()) datosLimpios.apellido = form.apellido.trim();
      if (form.cedula && form.cedula.trim()) datosLimpios.cedula = form.cedula.trim();
      if (form.telefono && form.telefono.trim()) datosLimpios.telefono = form.telefono.trim();
      if (form.email && form.email.trim()) datosLimpios.email = form.email.trim();
      
      const resp = await API.post('/pacientes', datosLimpios);
      setForm(emptyForm);
      // Refresh
      await fetchPacientes();
      // After creating, navigate to odontograma for this patient
      if (resp.data && resp.data.id) {
        navigate(`/odontograma?paciente=${resp.data.id}`);
      }
    } catch (err) {
      console.error('Error creating paciente', err);
      alert('Error creando paciente');
    } finally { setLoading(false); }
  };

  return (
    <div style={{padding: 20}}>
      <h2>Pacientes</h2>

      <div style={{display: 'flex', gap: 20}}>
        <div style={{flex: 1}}>
          <h3>Lista</h3>
          <table className="table table-pacientes">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Cédula</th>
                <th>Teléfono</th>
                <th>Email</th>
                <th style={{width:195}}>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {pacientes.map(p => (
                <tr key={p.id}>
                  <td>{p.nombre} {p.apellido}</td>
                  <td>{p.cedula}</td>
                  <td>{p.telefono}</td>
                  <td>{p.email}</td>
                  <td>
                    <div style={{display:'flex', gap:3}}>
                      <button className="btn btn-primary" onClick={() => navigate(`/fichas?paciente=${p.id}`)}>Ver Fichas</button>
                      <button className="btn" onClick={() => navigate(`/odontograma?paciente=${p.id}`)}>Odontograma</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div style={{width: 360}}>
          <h3>Crear Paciente</h3>
          <form onSubmit={handleCreate}>
            <div style={{marginBottom:8}}>
              <input placeholder="Nombre" required value={form.nombre} onChange={e=>setForm({...form,nombre:e.target.value})} />
            </div>
            <div style={{marginBottom:8}}>
              <input placeholder="Apellido" required value={form.apellido} onChange={e=>setForm({...form,apellido:e.target.value})} />
            </div>
            <div style={{marginBottom:8}}>
              <input placeholder="Cédula" required value={form.cedula} onChange={e=>setForm({...form,cedula:e.target.value})} />
            </div>
            <div style={{marginBottom:8}}>
              <input placeholder="Teléfono" value={form.telefono} onChange={e=>setForm({...form,telefono:e.target.value})} />
            </div>
            <div style={{marginBottom:8}}>
              <input placeholder="Email" type="email" value={form.email} onChange={e=>setForm({...form,email:e.target.value})} />
            </div>
            <button type="submit" className="btn btn-primary" disabled={loading}>{loading ? 'Creando...' : 'Crear y abrir Odontograma'}</button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Pacientes;
