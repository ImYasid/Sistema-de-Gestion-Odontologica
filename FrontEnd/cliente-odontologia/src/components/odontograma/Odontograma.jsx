import React, { useState, useEffect } from 'react';
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

  useEffect(() => { cargarDientes(); }, [pacienteId]);

  const cargarDientes = async () => {
    try {
      const response = await axios.get(`http://localhost:8084/odontograma/${pacienteId}`);
      setDientes(response.data);
    } catch (error) { console.error(error); }
  };

  // ESTADOS 
  const getDienteData = (numero) => {
    return dientes.find(x => x.numeroDiente === numero) || { 
        estado: 'SANO', 
        caras: { top:null, bottom:null, left:null, right:null, center:null } 
    };
  };

  // 1. CLIC EN LA RAÍZ (Tratamientos grandes)
  const handleRootClick = async (numero) => {
    const data = getDienteData(numero);
    const estados = ['SANO', 'ENDODONCIA_PENDIENTE', 'EXTRACCION', 'IMPLANTE'];
    const idx = estados.indexOf(data.estado || 'SANO');
    const nuevoEstado = estados[(idx + 1) % estados.length];

    await guardarCambio(numero, { ...data, estado: nuevoEstado });
    
    if(nuevoEstado === 'ENDODONCIA_PENDIENTE') {
        if(window.confirm(`¿Crear ficha de Endodoncia para el diente ${numero}?`)) {
            navigate(`/fichas/nueva?paciente=${pacienteId}&diente=${numero}`);
        }
    }
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
          // Truco: Si tu backend solo tiene un campo de texto extra, guardamos el JSON de caras ahí
          observacion: JSON.stringify(dataToSave.caras) 
      });
    } catch(e) { console.error("Error guardando", e); }
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
             {dataArriba.estado === 'ENDODONCIA_PENDIENTE' && <span className="badge-ficha" onClick={() => navigate(`/fichas/nueva?diente=${numArriba}`)}>+Ficha</span>}
             
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

             {dataAbajo.estado === 'ENDODONCIA_PENDIENTE' && <span className="badge-ficha" onClick={() => navigate(`/fichas/nueva?diente=${numAbajo}`)}>+Ficha</span>}
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
            <button className="btn-reset" onClick={limpiarTodo}>
            Limpiar Todo
            </button>
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