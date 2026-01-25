import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import axios from 'axios';

const API = axios.create({ baseURL: 'http://localhost:8083' });
const API_PAC = axios.create({ baseURL: 'http://localhost:8082' });

const Fichas = () => {
  const [searchParams] = useSearchParams();
  const pacienteId = searchParams.get('paciente');
  const [fichas, setFichas] = useState([]);
  const [creating, setCreating] = useState(false);
  const [paciente, setPaciente] = useState(null);
  const [expanded, setExpanded] = useState({});
  const dienteParam = searchParams.get('diente');
  const [processedDiente, setProcessedDiente] = useState(false);

  const fetchPaciente = async () => {
    if (!pacienteId) return;
    try {
      const resp = await API_PAC.get(`/pacientes/${pacienteId}`);
      setPaciente(resp.data || null);
    } catch (err) { console.error('Error fetching paciente', err); }
  };

  const fetchFichas = async () => {
    if (!pacienteId) return;
    try {
      const resp = await API.get(`/fichas/paciente/${pacienteId}`);
      const lista = resp.data || [];

      // Para cada ficha, cargamos anamnesis, diagnosticos y tratamientos en paralelo
      const listaConDetalles = await Promise.all(lista.map(async (f) => {
        try {
          const [anamRes, diagRes, tratRes] = await Promise.all([
            API.get(`/anamnesis/ficha/${f.id}`).catch(()=>({status:404})),
            API.get(`/diagnosticos/ficha/${f.id}`).catch(()=>({data:[] })),
            API.get(`/tratamientos/ficha/${f.id}`).catch(()=>({data:[] }))
          ]);

          return {
            ...f,
            anamnesis: anamRes && anamRes.data ? anamRes.data : null,
            diagnosticos: diagRes && diagRes.data ? diagRes.data : [],
            tratamientos: tratRes && tratRes.data ? tratRes.data : []
          };
        } catch (e) {
          console.error('Error cargando detalles ficha', e);
          return { ...f, anamnesis: null, diagnosticos: [], tratamientos: [] };
        }
      }));

      setFichas(listaConDetalles);
    } catch (err) { console.error('Error fetching fichas', err); }
  };

  useEffect(() => { fetchPaciente(); fetchFichas(); }, [pacienteId]);

  // Si venimos con ?diente=NN, ofrecemos crear ficha + diagnostico rápidamente
  useEffect(() => {
    if (!pacienteId || !dienteParam || processedDiente) return;

    // Marcamos como procesado inmediatamente para evitar ejecuciones dobles
    // (React.StrictMode puede ejecutar efectos dos veces en desarrollo)
    setProcessedDiente(true);

    if (window.confirm(`Crear ficha y diagnóstico inicial para el diente ${dienteParam}?`)) {
      (async () => {
        try {
          setCreating(true);
          // 1) crear ficha
          const payload = { pacienteId: Number(pacienteId), numeroHistoriaClinica: `HC-${Date.now()}` };
          const resp = await API.post('/fichas', payload);
          const nuevaFicha = resp.data;
          // 2) crear diagnostico asociado a la ficha
          const diag = {
            fichaTecnica: { id: nuevaFicha.id },
            piezaDental: String(dienteParam),
            diagnosticoPulpar: 'Pendiente - Generado desde Odontograma',
            planTratamiento: 'Evaluar necesidad de endodoncia'
          };
          await API.post('/diagnosticos', diag);
          // 3) recargar
          await fetchFichas();
          alert('Ficha y diagnóstico creados.');
        } catch (e) {
          console.error('Error creando ficha+diagnostico', e);
          alert('Error al crear ficha y diagnóstico');
        } finally {
          setCreating(false);
        }
      })();
    }
  }, [pacienteId, dienteParam, processedDiente]);

  const handleCreate = async () => {
    if (!pacienteId) return alert('Selecciona un paciente');
    setCreating(true);
    try {
      const payload = { pacienteId: Number(pacienteId), numeroHistoriaClinica: `HC-${Date.now()}` };
      await API.post('/fichas', payload);
      // recargar detalles
      await fetchFichas();
    } catch (err) { console.error('Error creando ficha', err); alert('Error creando ficha'); }
    finally { setCreating(false); }
  };

  const toggle = (id) => setExpanded(prev => ({ ...prev, [id]: !prev[id] }));

  return (
    <div style={{padding: 20}}>
      <h2>Fichas Técnicas {pacienteId ? `(Paciente ${pacienteId})` : ''}</h2>
      {!pacienteId && <p>Selecciona un paciente desde la lista de Pacientes para ver sus fichas.</p>}

      {paciente && (
        <div style={{border:'1px solid #ddd', padding:12, marginBottom:12}}>
          <strong>{paciente.nombre} {paciente.apellido}</strong>
          <div>Cédula: {paciente.cedula}</div>
          <div>Teléfono: {paciente.telefono}</div>
          <div>Email: {paciente.email}</div>
        </div>
      )}

      {pacienteId && (
        <div>
          <div style={{marginBottom:12}}>
            <button onClick={handleCreate} disabled={creating}>{creating ? 'Creando...' : 'Crear nueva ficha'}</button>
            <button onClick={fetchFichas} style={{marginLeft:8}}>Recargar</button>
          </div>

          {fichas.length === 0 && <p>No se encontraron fichas para este paciente.</p>}

          {fichas.map(f => (
            <div key={f.id} style={{border:'1px solid #eee', padding:12, marginBottom:10}}>
              <div style={{display:'flex', justifyContent:'space-between'}}>
                <div>
                  <strong>{f.numeroHistoriaClinica}</strong>
                  <div style={{fontSize:12, color:'#666'}}>Estado: {f.estado} — Creada: {f.fechaCreacion ? new Date(f.fechaCreacion).toLocaleString() : ''}</div>
                </div>
                <div>
                  <button onClick={() => toggle(f.id)}>{expanded[f.id] ? 'Ocultar' : 'Mostrar detalles'}</button>
                </div>
              </div>

              {expanded[f.id] && (
                <div style={{marginTop:10}}>
                  <h4>Anamnesis</h4>
                  {f.anamnesis ? (
                    <div>
                      <div>Dr. referidor: {f.anamnesis.drReferidor}</div>
                      <div>Motivo: {f.anamnesis.motivoConsulta}</div>
                      <div>Antecedentes: {f.anamnesis.antecedentesEnfermedadActual}</div>
                      <div>Enfermedad sistemática: {String(f.anamnesis.enfermedadSistematica)}</div>
                      <div>Alergias: {String(f.anamnesis.alergias)} {f.anamnesis.cualAlergia ? `(${f.anamnesis.cualAlergia})` : ''}</div>
                    </div>
                  ) : (<div>No hay anamnesis registrada.</div>)}

                  <h4>Diagnósticos</h4>
                  {f.diagnosticos && f.diagnosticos.length > 0 ? (
                    <ul>
                      {f.diagnosticos.map(d => (
                        <li key={d.id}><strong>Pieza:</strong> {d.piezaDental} — <strong>Diagnóstico pulpar:</strong> {d.diagnosticoPulpar} — <strong>Plan:</strong> {d.planTratamiento}</li>
                      ))}
                    </ul>
                  ) : (<div>No hay diagnósticos registrados.</div>)}

                  <h4>Tratamientos</h4>
                  {f.tratamientos && f.tratamientos.length > 0 ? (
                    <ul>
                      {f.tratamientos.map(t => (
                        <li key={t.id}>{t.fecha ? new Date(t.fecha).toLocaleDateString() : ''} — {t.anestesiaUtilizada} — {t.observaciones}</li>
                      ))}
                    </ul>
                  ) : (<div>No hay tratamientos registrados.</div>)}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Fichas;
