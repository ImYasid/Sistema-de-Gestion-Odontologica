import React from 'react';
import '../styles/Odontograma.css';

const Diente = ({ numero, estado, onClick }) => {
  
  // Función auxiliar para obtener la clase CSS según el estado
  const getClassEstado = (estadoActual) => {
    switch (estadoActual) {
      case 'CARIES': return 'estado-caries';
      case 'ENDODONCIA_PENDIENTE': return 'estado-endo';
      case 'EXTRACCION': return 'estado-extraccion';
      case 'SANO': return 'estado-sano';
      default: return 'estado-sano';
    }
  };

  const claseEstado = getClassEstado(estado);

  return (
    <div className="diente-wrapper">
      {/* El Diente Geométrico */}
      <div className="diente-geometrico" onClick={() => onClick(numero)}>
        
        {/* En un odontograma real, estas caras podrían tener estados independientes.
            Por ahora, todas heredan el estado general del diente para mantener tu lógica simple. */}
            
        <div className={`cara cara-vestibular ${claseEstado}`} title="Vestibular"></div>
        <div className={`cara cara-lingual ${claseEstado}`} title="Lingual"></div>
        <div className={`cara cara-mesial ${claseEstado}`} title="Mesial"></div>
        <div className={`cara cara-distal ${claseEstado}`} title="Distal"></div>
        <div className={`cara cara-oclusal ${claseEstado}`} title="Oclusal/Incisal"></div>
        
      </div>

      {/* Número debajo */}
      <div className="numero-diente">{numero}</div>
      
      {/* Estado Texto (Opcional, pequeño) */}
      {estado && estado !== 'SANO' && (
         <span style={{fontSize: '9px', color: '#888', marginTop: '2px'}}>
           {estado.substring(0,4)}
         </span>
      )}
    </div>
  );
};

export default Diente;