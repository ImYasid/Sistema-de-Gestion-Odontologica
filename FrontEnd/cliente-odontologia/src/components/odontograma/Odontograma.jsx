import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import DienteDibujo from './DienteDibujo';
import DienteGeometrico from './DienteGeometrico';
import './Odontograma.css';

const Odontograma = ({ pacienteId }) => {
  const [dientes, setDientes] = useState([]);
  const navigate = useNavigate();

  // Rangos de dientes
  const upperRight = [18, 17, 16, 15, 14, 13, 12, 11];
  const upperLeft = [21, 22, 23, 24, 25, 26, 27, 28];
  const lowerRight = [48, 47, 46, 45, 44, 43, 42, 41];
  const lowerLeft = [31, 32, 33, 34, 35, 36, 37, 38];

  const cargarDientes = useCallback(async () => {
    try {
      // endpoint backend: GET /api/odontograma/{pacienteId}
      const response = await axios.get(`http://localhost:8084/api/odontograma/${pacienteId}`);
      setDientes(response.data);
    } catch (error) { console.error(error); }
  }, [pacienteId]);

  useEffect(() => { cargarDientes(); }, [cargarDientes]);

  // ESTADOS 
  const getDienteData = (numero) => {
    return dientes.find(x => x.numeroDiente === numero) || { 
        estado: 'SANO', 
        caras: { top:null, bottom:null, left:null, right:null, center:null } 
    };
  };

  // 1. CLIC EN LA RAÍZ (Tratamientos grandes)
  // Ahora solo pinta el diente localmente; la creación de la ficha/diagnóstico
  // se realizará al presionar "Guardar Odontograma".
  const handleRootClick = (numero) => {
    const data = getDienteData(numero);
    const estados = ['SANO', 'ENDODONCIA_PENDIENTE', 'EXTRACCION', 'IMPLANTE'];
    const idx = estados.indexOf(data.estado || 'SANO');
    const nuevoEstado = estados[(idx + 1) % estados.length];

    // Actualizamos solo el estado localmente (optimistic UI) sin llamar al backend aún
    setDientes(prev => {
      const copia = [...prev];
      const index = copia.findIndex(d => d.numeroDiente === numero);
      if (index >= 0) copia[index] = { ...copia[index], estado: nuevoEstado };
      else copia.push({ numeroDiente: numero, estado: nuevoEstado, pacienteId });
      return copia;
    });
  };

  // 2. CLIC EN UNA CARA ESPECÍFICA (Caries, Empastes)
  const handleFaceClick = async (numero, cara) => {
    const data = getDienteData(numero);
    const carasActuales = data.caras || {};
    
    // Ciclo de estados para la cara: Sano (null) -> Caries (Rojo) -> Obturado (Azul)
    const estadosCara = [null, 'CARIES', 'OBTURADO'];
    const estadoActual = carasActuales[cara];
    const idx = estadosCara.indexOf(estadoActual);
    const nuevoEstadoCara = estadosCara[(idx + 1) % estadosCara.length];

    const nuevasCaras = { ...carasActuales, [cara]: nuevoEstadoCara };
    
    await guardarCambio(numero, { ...data, caras: nuevasCaras });
  };

  const guardarCambio = async (numero, dataToSave) => {
    try {
      // Optimizamos la UI antes de la respuesta del server (Optimistic UI)
      const nuevosDientes = [...dientes];
      const index = nuevosDientes.findIndex(d => d.numeroDiente === numero);
      if (index >= 0) nuevosDientes[index] = { ...nuevosDientes[index], ...dataToSave };
      else nuevosDientes.push({ numeroDiente: numero, ...dataToSave });
      
      setDientes(nuevosDientes);

      // Enviamos al backend
      await axios.post('http://localhost:8084/api/odontograma', {
          pacienteId,
          numeroDiente: numero,
          estado: dataToSave.estado,
          observacion: typeof dataToSave.caras === 'string' ? dataToSave.caras : JSON.stringify(dataToSave.caras || {})
      });
    } catch(e) { console.error("Error guardando", e); }
  };

  // Guardar todo el odontograma (envía todos los dientes al backend)
  const guardarTodo = async () => {
    try {
      // 1) Guardamos/actualizamos todos los dientes en el backend
      const promesas = dientes.map(d => axios.post('http://localhost:8084/api/odontograma', {
        pacienteId,
        numeroDiente: d.numeroDiente,
        estado: d.estado || 'SANO',
        observacion: typeof d.observacion === 'string' ? d.observacion : JSON.stringify(d.caras || {})
      }).catch(e => { console.error('Error guardando diente', d.numeroDiente, e); }));

      await Promise.all(promesas);

      // 2) Revisamos si hay dientes "pintados" o con estado especial para crear una ficha
      const dientesAfectados = dientes.filter(d => {
        const tieneEstado = d.estado && d.estado !== 'SANO';
        const obs = d.observacion ? d.observacion : (d.caras ? JSON.stringify(d.caras) : '{}');
        const tieneObs = obs && obs !== '{}' && obs.length > 2;
        return tieneEstado || tieneObs;
      });

      if (dientesAfectados.length > 0) {
        try {
          // 3) Verificamos si ya existe una ficha recientemente creada por odontograma para no duplicar
          let nuevaFicha = null;
          try {
            const fichasResp = await axios.get(`http://localhost:8083/fichas/paciente/${pacienteId}`);
            const listas = fichasResp.data || [];
            // Buscamos una creada por odontograma en los últimos 5 minutos
            const ahora = Date.now();
            const existente = listas.find(f => f.numeroHistoriaClinica && f.numeroHistoriaClinica.startsWith('HC-ODO-') && f.fechaCreacion && (ahora - new Date(f.fechaCreacion).getTime()) < (5*60*1000));
            if (existente) {
              nuevaFicha = existente;
            }
          } catch (e) { /* ignoramos error y creamos ficha nueva */ }

          if (!nuevaFicha) {
            const fichaResp = await axios.post('http://localhost:8083/fichas', {
              pacienteId,
              numeroHistoriaClinica: `HC-ODO-${Date.now()}`
            });
            nuevaFicha = fichaResp.data;
          }

          // 4) Crear una anamnesis inicial VACÍA para que el doctor la edite si no existe
          try {
            await axios.post('http://localhost:8083/anamnesis', {
              fichaTecnica: { id: nuevaFicha.id },
              motivoConsulta: ''
            });
          } catch (e) {
            // Si ya existía una anamnesis, PUT fallaría; ignoramos
          }

          // 5) Obtenemos diagnosticos existentes de la ficha para evitar duplicados
          let diagnosticosExistentes = [];
          try {
            const diagResp = await axios.get(`http://localhost:8083/diagnosticos/ficha/${nuevaFicha.id}`);
            diagnosticosExistentes = diagResp.data || [];
          } catch (e) { diagnosticosExistentes = []; }

          // 6) Para cada diente afectado, crear un diagnóstico con el resumen del diente si no existe
          for (const d of dientesAfectados) {
            const ya = diagnosticosExistentes.find(x => String(x.piezaDental) === String(d.numeroDiente));
            if (ya) continue; // saltamos si ya existe diagnóstico para esa pieza

            // Determinar etiqueta legible
            let etiqueta = '';
            if (d.estado === 'ENDODONCIA_PENDIENTE') etiqueta = 'requiere endodoncia';
            else if (d.estado === 'EXTRACCION') etiqueta = 'extracción';
            else if (d.estado === 'IMPLANTE') etiqueta = 'implante';
            else {
              try {
                const caras = d.observacion && typeof d.observacion === 'string' && d.observacion.startsWith('{')
                  ? JSON.parse(d.observacion)
                  : (d.caras || {});
                if (Object.values(caras).includes('CARIES')) etiqueta = 'cariado';
                else if (Object.values(caras).includes('OBTURADO')) etiqueta = 'restaurado';
                else etiqueta = d.estado ? d.estado.toLowerCase() : 'modificado';
              } catch (e) { etiqueta = d.estado ? d.estado.toLowerCase() : 'modificado'; }
            }

            let detalleCaras = '';
            try {
              const carasObj = d.observacion && typeof d.observacion === 'string' && d.observacion.startsWith('{')
                ? JSON.parse(d.observacion)
                : (d.caras || {});
              const listaCaras = Object.entries(carasObj).filter(([,v]) => v).map(([k,v]) => `${k}:${v}`);
              if (listaCaras.length) detalleCaras = ` (caras: ${listaCaras.join(', ')})`;
            } catch (e) { detalleCaras = ''; }

            const descripcion = `${etiqueta}${detalleCaras}`;

            await axios.post('http://localhost:8083/diagnosticos', {
              fichaTecnica: { id: nuevaFicha.id },
              piezaDental: String(d.numeroDiente),
              diagnosticoPulpar: descripcion,
              planTratamiento: d.estado === 'ENDODONCIA_PENDIENTE' ? 'Evaluar necesidad de endodoncia' : ''
            });
          }

          // Abrimos la vista de fichas para que el profesional pueda completar la anamnesis y revisar diagnósticos
          navigate(`/fichas?paciente=${pacienteId}`);
          alert('Odontograma guardado; ficha creada, anamnesis inicial y diagnósticos generados.');
        } catch (e) {
          console.error('Error creando ficha/diagnosticos', e);
          alert('Odontograma guardado, pero ocurrió un error creando la ficha o diagnósticos.');
        }
      } else {
        alert('Odontograma guardado (no se detectaron cambios que requieran ficha).');
      }

    } catch (e) {
      console.error('Error guardando odontograma', e);
      alert('Error al guardar odontograma');
    }
  };

  // --- RENDER ---
  const renderColumna = (numArriba, numAbajo) => {
    const dataArriba = getDienteData(numArriba);
    const dataAbajo = getDienteData(numAbajo);

    // Parseamos las caras si vienen como string del backend
    const carasArriba = typeof dataArriba.observacion === 'string' && dataArriba.observacion.startsWith('{') 
        ? JSON.parse(dataArriba.observacion) : (dataArriba.caras || {});
    
    const carasAbajo = typeof dataAbajo.observacion === 'string' && dataAbajo.observacion.startsWith('{')
        ? JSON.parse(dataAbajo.observacion) : (dataAbajo.caras || {});

    return (
      <div className="columna-dental" key={numArriba}>
        {/* === DIENTE SUPERIOR === */}
        <div className="bloque-diente">
             {dataArriba.estado === 'ENDODONCIA_PENDIENTE' && <span className="badge-ficha" onClick={() => navigate(`/fichas?paciente=${pacienteId}`)}>+Ficha</span>}
             
             {/* Raíz Realista */}
             <DienteDibujo 
                tipo="superior" numero={numArriba} 
                estado={dataArriba.estado} 
                onClick={() => handleRootClick(numArriba)} 
             />
             
             {/* Mapa Geométrico (Recuadro) */}
             <DienteGeometrico 
                numero={numArriba} 
                caras={carasArriba} 
                onClickCara={handleFaceClick} 
             />
        </div>

        {/* NÚMEROS CENTRALES */}
        <div className="zona-numeros">
            <span>{numArriba}</span>
            <div className="linea-divisoria"></div>
            <span>{numAbajo}</span>
        </div>

        {/* === DIENTE INFERIOR === */}
        <div className="bloque-diente inferior">
             {/* Mapa Geométrico Primero (Espejo) */}
             <DienteGeometrico 
                numero={numAbajo} 
                caras={carasAbajo} 
                onClickCara={handleFaceClick} 
             />
             
             {/* Raíz Realista Despues */}
             <DienteDibujo 
                tipo="inferior" numero={numAbajo} 
                estado={dataAbajo.estado} 
                onClick={() => handleRootClick(numAbajo)} 
             />

             {dataAbajo.estado === 'ENDODONCIA_PENDIENTE' && <span className="badge-ficha" onClick={() => navigate(`/fichas?paciente=${pacienteId}`)}>+Ficha</span>}
        </div>
      </div>
    );
  };

  const limpiarTodo = async () => {
    if (!window.confirm("¿Estás seguro de borrar TODO el odontograma?")) return;

    // 1. LIMPIEZA VISUAL INMEDIATA (Optimistic UI)
    // Forzamos a que todo el array en memoria se vea limpio YA.
    const dientesLimpiosLocalmente = dientes.map(d => ({
      ...d,
      estado: 'SANO',
      color: 'white',
      observacion: '{}', // String JSON vacío
      // Si tu lógica usa 'caras' como objeto, lo reseteamos también
      caras: { top: null, bottom: null, left: null, right: null, center: null }
    }));
    
    setDientes(dientesLimpiosLocalmente);

    try {
      // 2. FILTRAR SOLO LOS QUE NECESITAN GUARDARSE EN BD
      // (Para no saturar la red con 32 peticiones si solo 1 diente está sucio)
      const dientesParaResetear = dientes.filter(d => {
         // ¿Tiene estado raro?
         const tieneEstado = d.estado && d.estado !== 'SANO';
         // ¿Tiene caras pintadas? (Verificamos si el string JSON tiene contenido real)
         const tieneObs = d.observacion && d.observacion.length > 2 && d.observacion !== '{}';
         
         return tieneEstado || tieneObs;
      });

      // 3. ENVIAR CAMBIOS AL BACKEND
      const promesas = dientesParaResetear.map(d => 
        axios.post('http://localhost:8084/api/odontograma', {
          pacienteId: pacienteId,
          numeroDiente: d.numeroDiente,
          estado: 'SANO',
          color: 'white',
          observacion: '{}' // IMPORTANTE: Esto borra el mapa geométrico en la BD
        })
      );

      await Promise.all(promesas);
      
      // 4. (Opcional) Asegurar sincronización final
      // Descomenta la siguiente línea si quieres estar 100% seguro de que el backend guardó
      // await cargarDientes(); 

    } catch (error) {
      console.error("Error al limpiar:", error);
      alert("Error de conexión. Intenta recargar la página.");
    }
  };

  return (
    <div className="odontograma-container">
        <div className="panel-operaciones">
            <h4>Operaciones</h4>
            <div className="legend-item"><span className="color-box rojo"></span> Caries</div>
            <div className="legend-item"><span className="color-box azul"></span> Restaurado</div>
            <div className="legend-item"><span className="color-box amarillo"></span> Endodoncia</div>
            <hr/>
            <div style={{display:'flex', gap:8}}>
              <button className="btn-reset" onClick={limpiarTodo}>Limpiar Todo</button>
              <button className="btn-save" onClick={guardarTodo}>Guardar Odontograma</button>
            </div>
        </div>

        <div className="panel-boca">
            <div className="cuadrante-row">
                {upperRight.map((u, i) => renderColumna(u, lowerRight[i]))}
                <div className="separador-central-boca"></div>
                {upperLeft.map((u, i) => renderColumna(u, lowerLeft[i]))}
            </div>
        </div>
    </div>
  );
};

export default Odontograma;