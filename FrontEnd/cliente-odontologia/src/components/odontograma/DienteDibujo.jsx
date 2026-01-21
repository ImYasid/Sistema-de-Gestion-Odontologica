import React from 'react';

const DienteDibujo = ({ tipo = 'superior', numero, estado, onClick }) => {
  const isSuperior = tipo === 'superior';
  
  // LÓGICA ANATÓMICA:
  // Incisivos/Caninos: terminan en 1, 2, 3 (Ej: 11, 23, 42) -> 1 Raíz
  // Molares/Premolares: terminan en 4, 5, 6, 7, 8 (Ej: 14, 26, 38) -> 2 Raíces visuales
  const digito = parseInt(numero.toString().slice(-1));
  const esMolar = digito >= 4; 

  const getFill = () => {
    switch(estado) {
      case 'CARIES': return 'url(#gradCaries)';
      case 'ENDODONCIA_PENDIENTE': return 'url(#gradEndo)';
      case 'COMPLETADO': return 'url(#gradCompletado)';
      case 'EXTRACCION': return 'url(#gradExtraccion)'; 
      case 'IMPLANTE': return 'url(#gradImplante)';
      default: return 'url(#gradSano)';
    }
  };
  // 1. INCISIVO (Rediseñado para el 11: Raíz cónica central, corona suave)
  // Dibujamos con la raíz hacia ARRIBA (Y=0) por defecto.
  const pathIncisivo = {
    // Raíz: va desde el cuerpo (Y=25) hacia arriba (Y=0) y vuelve
    raiz: "M13,25 Q15,0 20,0 Q25,0 27,25", 
    // Corona: desde Y=25 hacia abajo (Y=50)
    corona: "M10,25 L30,25 Q35,35 28,52 Q20,58 12,52 Q5,35 10,25"
  };

  // 2. MOLAR (Raíces bifurcadas, corona ancha)
  const pathMolar = {
    raiz: "M8,25 Q8,5 15,5 L15,18 L25,18 L25,5 Q32,5 32,25", 
    corona: "M4,25 L36,25 Q40,35 36,48 Q20,55 4,48 Q0,35 4,25"
  };

  // Seleccionamos el trazo
  const trazo = esMolar ? pathMolar : pathIncisivo;

  return (
    <svg 
      width="40" 
      height="55" 
      viewBox="0 0 40 60" 
      style={{ 
        cursor: 'pointer', 
        filter: 'drop-shadow(2px 3px 2px rgba(0,0,0,0.2))',
        // CORRECCIÓN DE ORIENTACIÓN:
        // Superiores: 0 grados (Raíz arriba, natural del dibujo SVG)
        // Inferiores: 180 grados (Raíz abajo)
        transform: isSuperior ? 'rotate(0deg)' : 'rotate(180deg)', 
        transition: 'transform 0.3s ease, filter 0.2s'
      }}
      onClick={onClick}
      onMouseEnter={(e) => e.currentTarget.style.filter = 'drop-shadow(0px 0px 5px rgba(52, 152, 219, 0.6))'}
      onMouseLeave={(e) => e.currentTarget.style.filter = 'drop-shadow(2px 3px 2px rgba(0,0,0,0.2))'}
    >
      <defs>
        <radialGradient id="gradSano" cx="40%" cy="30%" r="80%">
          <stop offset="0%" stopColor="#ffffff" />
          <stop offset="100%" stopColor="#d1d5db" />
        </radialGradient>
        <radialGradient id="gradCaries" cx="50%" cy="50%" r="70%">
          <stop offset="0%" stopColor="#ef4444" />
          <stop offset="100%" stopColor="#991b1b" />
        </radialGradient>
        <linearGradient id="gradEndo" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" stopColor="#fcd34d" />
          <stop offset="100%" stopColor="#d97706" />
        </linearGradient>
        <linearGradient id="gradCompletado" x1="0%" y1="0%" x2="0%" y2="100%">
          <stop offset="0%" stopColor="#4ade80" />
          <stop offset="100%" stopColor="#16a34a" />
        </linearGradient>
        <linearGradient id="gradExtraccion" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" stopColor="#4b5563" />
          <stop offset="100%" stopColor="#1f2937" />
        </linearGradient>
      </defs>

      {/* RAÍZ */}
      <path 
        d={trazo.raiz} 
        fill="#e5e7eb" 
        stroke="#9ca3af" 
        strokeWidth="1"
      />

      {/* CORONA */}
      <path 
        d={trazo.corona} 
        fill={getFill()} 
        stroke="#9ca3af" 
        strokeWidth="0.5"
      />

      {/* BRILLO */}
      <ellipse cx="15" cy="35" rx="6" ry="3" fill="white" fillOpacity="0.5" transform="rotate(-15)" />

      {/* DETALLE ENDODONCIA */}
      {estado === 'ENDODONCIA_PENDIENTE' && (
        <path 
          d={esMolar ? "M11,6 L11,30 M29,6 L29,30" : "M20,5 L20,35"} 
          stroke="#dc2626" 
          strokeWidth="2" 
          strokeDasharray="2,1"
          opacity="0.8"
        />
      )}
      
      {/* DETALLE EXTRACCIÓN */}
      {estado === 'EXTRACCION' && (
        <path d="M10,25 L30,50 M30,25 L10,50" stroke="white" strokeWidth="2" opacity="0.5" />
      )}
    </svg>
  );
};

export default DienteDibujo;