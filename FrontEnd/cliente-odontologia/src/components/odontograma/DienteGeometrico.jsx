import React from 'react';
import './Odontograma.css';

const DienteGeometrico = ({ numero, caras = {}, onClickCara }) => {

  // Función de color (Igual que tenías)
  const getColor = (estado) => {
    switch(estado) {
      case 'CARIES': return '#e74c3c'; 
      case 'OBTURADO': return '#3498db'; 
      case 'SELLANTE': return '#2ecc71'; 
      default: return 'white'; 
    }
  };

  return (
    <svg width="40" height="40" viewBox="0 0 100 100" className="diente-geometrico-svg">
      
      {/* 1. Cara SUPERIOR (Top) */}
      <path 
        className="cara-interactiva"
        d="M0,0 L100,0 L80,20 L20,20 Z" 
        fill={getColor(caras.top)} 
        onClick={(e) => { e.stopPropagation(); onClickCara(numero, 'top'); }}
      />

      {/* 2. Cara INFERIOR (Bottom) */}
      <path 
        className="cara-interactiva"
        d="M0,100 L100,100 L80,80 L20,80 Z" 
        fill={getColor(caras.bottom)} 
        onClick={(e) => { e.stopPropagation(); onClickCara(numero, 'bottom'); }}
      />

      {/* 3. Cara IZQUIERDA (Left) */}
      <path 
        className="cara-interactiva"
        d="M0,0 L20,20 L20,80 L0,100 Z" 
        fill={getColor(caras.left)} 
        onClick={(e) => { e.stopPropagation(); onClickCara(numero, 'left'); }}
      />

      {/* 4. Cara DERECHA (Right) */}
      <path 
        className="cara-interactiva"
        d="M100,0 L80,20 L80,80 L100,100 Z" 
        fill={getColor(caras.right)} 
        onClick={(e) => { e.stopPropagation(); onClickCara(numero, 'right'); }}
      />

      {/* 5. Cara CENTRAL (Center) */}
      <rect 
        className="cara-interactiva"
        x="20" y="20" width="60" height="60" 
        fill={getColor(caras.center)} 
        onClick={(e) => { e.stopPropagation(); onClickCara(numero, 'center'); }}
      />
      
    </svg>
  );
};

export default DienteGeometrico;