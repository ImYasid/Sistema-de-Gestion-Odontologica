import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import axios from 'axios';
import Odontograma from '../components/odontograma/Odontograma';

const API_PAC = axios.create({ baseURL: 'http://localhost:8082' });

const OdontogramaPage = () => {
  const [searchParams] = useSearchParams();
  const pacienteQuery = searchParams.get('paciente');
  const [pacienteId, setPacienteId] = useState(pacienteQuery ? Number(pacienteQuery) : null);
  const [pacientes, setPacientes] = useState([]);
  const [creating, setCreating] = useState(false);
  const [form, setForm] = useState({ nombre: '', apellido: '', cedula: '' });

  useEffect(() => { fetchPacientes(); }, []);

  const fetchPacientes = async () => {
    try {
      const resp = await API_PAC.get('/pacientes');
      setPacientes(resp.data || []);
    } catch (err) { console.error(err); }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    setCreating(true);
    try {
      const resp = await API_PAC.post('/pacientes', form);
      const id = resp.data.id;
      setPacienteId(id);
      await fetchPacientes();
    } catch (err) { console.error(err); alert('Error creando paciente'); }
    finally { setCreating(false); }
  };

  if (!pacienteId) {
    return (
      <div style={{padding:20}}>
        <h2>Odontograma - Selecciona o crea un paciente</h2>
        <div style={{display:'flex',gap:20}}>
          <div style={{flex:1}}>
            <h3>Pacientes</h3>
            <ul>
              {pacientes.map(p=> (
                <li key={p.id} style={{marginBottom:6}}>
                  {p.nombre} {p.apellido} — {p.cedula}
                  <button style={{marginLeft:8}} onClick={()=>setPacienteId(p.id)}>Abrir Odontograma</button>
                </li>
              ))}
            </ul>
          </div>

          <div style={{width:360}}>
            <h3>Crear paciente rápido</h3>
            <form onSubmit={handleCreate}>
              <input placeholder="Nombre" required value={form.nombre} onChange={e=>setForm({...form,nombre:e.target.value})} />
              <input placeholder="Apellido" required value={form.apellido} onChange={e=>setForm({...form,apellido:e.target.value})} />
              <input placeholder="Cédula" required value={form.cedula} onChange={e=>setForm({...form,cedula:e.target.value})} />
              <div style={{marginTop:8}}>
                <button type="submit" disabled={creating}>{creating? 'Creando...':'Crear y abrir Odontograma'}</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div style={{padding:20}}>
      <h2>Odontograma del paciente #{pacienteId}</h2>
      <Odontograma pacienteId={pacienteId} />
    </div>
  );
};

export default OdontogramaPage;
