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

  // UI editing state: controlan si el editor está visible para cada ficha/diagnóstico
  const [editingAnamnesis, setEditingAnamnesis] = useState({});
  const [editingDiagnosticos, setEditingDiagnosticos] = useState({});

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
      // Expand todas las fichas por defecto para que los datos no queden colapsados
      const expandedMap = {};
      listaConDetalles.forEach(f => { expandedMap[f.id] = true; });
      setExpanded(expandedMap);
    } catch (err) { console.error('Error fetching fichas', err); }
  };

  useEffect(() => { fetchPaciente(); fetchFichas(); }, [pacienteId]);

  // Si venimos con ?diente=NN, mostramos una acción directa para crear ficha+diagnóstico
  // (No la creamos automáticamente para evitar duplicados; el usuario debe confirmar)
  const handleCreateForDiente = async () => {
    if (!pacienteId || !dienteParam) return;
    try {
      setCreating(true);
      // 1) crear ficha
      const payload = { pacienteId: Number(pacienteId), numeroHistoriaClinica: `HC-${Date.now()}` };
      const resp = await API.post('/fichas', payload);
      const nuevaFicha = resp.data;

      // 2) intentar obtener información del diente desde el odontograma para incluir en la anamnesis
      try {
        const API_OD = axios.create({ baseURL: 'http://localhost:8084' });
        const odResp = await API_OD.get(`/api/odontograma/${pacienteId}`);
        const dientes = odResp.data || [];
        const diente = dientes.find(d => String(d.numeroDiente) === String(dienteParam));

        let observacionText = '';
        if (diente && diente.observacion) {
          try {
            const caras = typeof diente.observacion === 'string' ? JSON.parse(diente.observacion) : diente.observacion;
            const listado = Object.entries(caras)
              .filter(([k, v]) => v !== null && v !== undefined && v !== '')
              .map(([k, v]) => `${k}:${v}`);
            observacionText = listado.length > 0 ? listado.join(', ') : '';
          } catch (e) {
            observacionText = String(diente.observacion || '');
          }
        }

        // 3) crear anamnesis con la info del odontograma (si aplica)
        const anam = {
          fichaTecnica: { id: nuevaFicha.id },
          motivoConsulta: `Diente ${dienteParam} - Observaciones odontograma: ${observacionText}`
        };
        await API.post('/anamnesis', anam);
      } catch (e) {
        console.error('No se pudo leer el odontograma para obtener observaciones del diente:', e);
      }

      // 4) crear diagnostico asociado a la ficha
      const diag = {
        fichaTecnica: { id: nuevaFicha.id },
        piezaDental: String(dienteParam),
        diagnosticoPulpar: 'Pendiente - Generado desde Odontograma',
        planTratamiento: 'Evaluar necesidad de endodoncia'
      };
      await API.post('/diagnosticos', diag);

      // 5) recargar
      await fetchFichas();
      alert('Ficha y diagnóstico creados para el diente ' + dienteParam + '.');
    } catch (e) {
      console.error('Error creando ficha+diagnostico', e);
      alert('Error al crear ficha y diagnóstico');
    } finally {
      setCreating(false);
      setProcessedDiente(true);
    }
  };

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

  const handleDeleteFicha = async (id) => {
    if (!window.confirm('¿Eliminar esta ficha? Esta operación no se puede deshacer.')) return;
    try {
      await API.delete(`/fichas/${id}`);
      await fetchFichas();
      alert('Ficha eliminada.');
    } catch (e) { console.error('Error eliminando ficha', e); alert('Error al eliminar ficha'); }
  }; 

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
            <button className="btn btn-primary" onClick={handleCreate} disabled={creating}>{creating ? 'Creando...' : 'Crear nueva ficha'}</button>
            <button className="btn" onClick={fetchFichas} style={{marginLeft:8}}>Recargar</button>

            {dienteParam && !processedDiente && (
              <div style={{marginTop:8, padding:10, border:'1px dashed #ccc'}}>
                <div>Se detectó diente <strong>{dienteParam}</strong> en la URL.</div>
                <div style={{marginTop:6}}>
                  <button className="btn btn-primary" onClick={handleCreateForDiente} disabled={creating}>{creating ? 'Procesando...' : `Crear ficha y diagnóstico para diente ${dienteParam}`}</button>
                  <button className="btn" style={{marginLeft:8}} onClick={() => setProcessedDiente(true)}>Ignorar</button>
                </div>
              </div>
            )}
          </div>

          {fichas.length === 0 && <p>No se encontraron fichas para este paciente.</p>}

          {fichas.map(f => (
            <div key={f.id} className="ficha-card">
              <div className="ficha-header">
                <div>
                  <strong>{f.numeroHistoriaClinica}</strong>
                  <div className="ficha-meta">Estado: {f.estado} — Creada: {f.fechaCreacion ? new Date(f.fechaCreacion).toLocaleString() : ''}</div>
                </div>
                <div>
                  <button className="btn" onClick={() => toggle(f.id)}>{expanded[f.id] ? 'Ocultar' : 'Mostrar detalles'}</button>
                  <button className="btn btn-delete" style={{marginLeft:8}} onClick={() => handleDeleteFicha(f.id)}>Eliminar ficha</button>
                </div>
              </div>

              {expanded[f.id] && (
                <div style={{marginTop:10}}>
                  <h4>Anamnesis</h4>
                  {f.anamnesis ? (
                    !editingAnamnesis[f.id] ? (
                      <div>
                        <div>Dr. referidor: {f.anamnesis.drReferidor}</div>
                        <div>Motivo: {f.anamnesis.motivoConsulta}</div>
                        <div>Antecedentes: {f.anamnesis.antecedentesEnfermedadActual}</div>
                        <div>Enfermedad sistemática: {String(f.anamnesis.enfermedadSistematica)}</div>
                        <div>Alergias: {String(f.anamnesis.alergias)} {f.anamnesis.cualAlergia ? `(${f.anamnesis.cualAlergia})` : ''}</div>
                        <div style={{marginTop:8}}>
                          <button className="btn btn-edit" onClick={()=> setEditingAnamnesis(prev=>({...prev,[f.id]:true}))}>Editar Anamnesis</button>
                        </div>
                      </div>
                    ) : (
                      <AnamnesisEditor ficha={f} onSaved={async () => { setEditingAnamnesis(prev=>({...prev,[f.id]:false})); await fetchFichas(); }} />
                    )
                  ) : (
                    <div>
                      <div>No hay anamnesis registrada.</div>
                      <button className="btn" onClick={async () => {
                        try {
                          const payload = { fichaTecnica: { id: f.id }, motivoConsulta: 'Anamnesis inicial' };
                          await API.post('/anamnesis', payload);
                          await fetchFichas();
                          alert('Anamnesis creada.');
                        } catch (e) { console.error(e); alert('Error creando anamnesis'); }
                      }}>Crear anamnesis</button>
                    </div>
                  )} 

                  <h4>Diagnósticos</h4>
                  {f.diagnosticos && f.diagnosticos.length > 0 ? (
                    <ul>
                      {f.diagnosticos.map(d => (
                        <li key={d.id} style={{marginBottom:12}}>
                          {!editingDiagnosticos[d.id] ? (
                            <div>
                              <div><strong>Pieza:</strong> {d.piezaDental}</div>
                              <div><strong>Diagnóstico pulpar:</strong><div className="diagnostico-preview">{d.diagnosticoPulpar}</div></div>
                              <div><strong>Plan:</strong> {d.planTratamiento}</div>
                              <div style={{marginTop:8}}>
                                <button className="btn btn-edit" onClick={()=> setEditingDiagnosticos(prev=>({...prev,[d.id]:true}))}>Editar diagnóstico</button>
                              </div>
                            </div>
                          ) : (
                            <DiagnosticoEditor diag={d} fichaId={f.id} onSaved={async () => { setEditingDiagnosticos(prev=>({...prev,[d.id]:false})); await fetchFichas(); }} />
                          )}
                        </li>
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



// --- Helper components ---

const AnamnesisEditor = ({ ficha, onSaved }) => {
  const [form, setForm] = useState({ ...(ficha.anamnesis || {}) });
  const [saving, setSaving] = useState(false);

  const handleSave = async () => {
    try {
      setSaving(true);
      if (form.id) {
        await API.put(`/anamnesis/${form.id}`, form);
      } else {
        await API.post('/anamnesis', { ...form, fichaTecnica: { id: ficha.id } });
      }
      await onSaved();
      alert('Anamnesis guardada.');
    } catch (e) { console.error(e); alert('Error guardando anamnesis'); } finally { setSaving(false); }
  };

  return (
    <div style={{border:'1px solid #ddd', padding:8}}>
      <div>
        <label>Dr. Referidor</label>
        <input value={form.drReferidor || ''} onChange={e=>setForm({...form,drReferidor:e.target.value})} />
      </div>
      <div>
        <label>Motivo</label>
        <textarea value={form.motivoConsulta || ''} onChange={e=>setForm({...form,motivoConsulta:e.target.value})} />
      </div>
      <div>
        <label>Antecedentes</label>
        <textarea value={form.antecedentesEnfermedadActual || ''} onChange={e=>setForm({...form,antecedentesEnfermedadActual:e.target.value})} />
      </div>
      <div>
        <label>Alergias</label>
        <input type="checkbox" checked={Boolean(form.alergias)} onChange={e=>setForm({...form,alergias:e.target.checked})} />
        <input placeholder="Cual" value={form.cualAlergia || ''} onChange={e=>setForm({...form,cualAlergia:e.target.value})} />
      </div>
      <div style={{marginTop:8}}>
        <button className="btn btn-save" onClick={handleSave} disabled={saving}>{saving ? 'Guardando...' : 'Guardar Anamnesis'}</button>
      </div>
    </div>
  );
};

const DiagnosticoEditor = ({ diag, fichaId, onSaved }) => {
  const [form, setForm] = useState({ ...(diag || {}) });
  const [saving, setSaving] = useState(false);

  const handleSave = async () => {
    try {
      setSaving(true);
      if (form.id) {
        await API.put(`/diagnosticos/${form.id}`, form);
      } else {
        await API.post('/diagnosticos', { ...form, fichaTecnica: { id: fichaId } });
      }
      await onSaved();
      alert('Diagnóstico guardado.');
    } catch (e) { console.error(e); alert('Error guardando diagnóstico'); } finally { setSaving(false); }
  };

  return (
    <div style={{border:'1px solid #ddd', padding:8}}>
      <div><strong>Pieza:</strong> <input value={form.piezaDental || ''} onChange={e=>setForm({...form,piezaDental:e.target.value})} /></div>
      <div>
        <label>Diagnóstico pulpar</label>
        <textarea value={form.diagnosticoPulpar || ''} onChange={e=>setForm({...form,diagnosticoPulpar:e.target.value})} />
      </div>
      <div>
        <label>Plan tratamiento</label>
        <textarea value={form.planTratamiento || ''} onChange={e=>setForm({...form,planTratamiento:e.target.value})} />
      </div>
      <div style={{marginTop:8}}>
        <button className="btn btn-save" onClick={handleSave} disabled={saving}>{saving ? 'Guardando...' : 'Guardar Diagnóstico'}</button>
      </div>
    </div>
  );
};

export default Fichas;
