import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Odontograma from '../src/components/odontograma/Odontograma';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <header style={{ backgroundColor: '#282c34', padding: '20px', color: 'white', marginBottom: '20px' }}>
          <h1>Sistema Gestión Odontológica</h1>
        </header>

        <Routes>
          {/* Ruta de prueba: Simulamos que vemos al paciente con ID 1 */}
          <Route path="/" element={<Odontograma pacienteId={1} />} />
          
          {/* Ruta dummy para cuando hagan clic en "+ Ficha" */}
          <Route path="/fichas/nueva" element={<h2>Aquí iría el formulario de Endodoncia (Persona 3)</h2>} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
