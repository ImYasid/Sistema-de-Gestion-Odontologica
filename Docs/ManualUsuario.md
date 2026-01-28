# Manual de Usuario - Sistema de Gestión Odontológica

## 📋 Tabla de Contenidos
1. [Requisitos del Sistema](#requisitos-del-sistema)
2. [Instalación de Dependencias](#instalación-de-dependencias)
3. [Configuración de Docker](#configuración-de-docker)
4. [Compilación e Instalación](#compilación-e-instalación)
5. [Iniciación de Servicios](#iniciación-de-servicios)
6. [Acceso a la Aplicación](#acceso-a-la-aplicación)
7. [Funcionalidades Principales](#funcionalidades-principales)
8. [Solución de Problemas](#solución-de-problemas)

---

## ✅ Requisitos del Sistema

### Hardware Mínimo
- **Procesador**: Intel/AMD moderno (2+ núcleos)
- **Memoria RAM**: 4 GB mínimo (8 GB recomendado)
- **Espacio en Disco**: 5 GB libres
- **Conexión a Internet**: Para descargar dependencias

### Software Requerido
- **Windows 10/11** con PowerShell 5.1+
- **Docker Desktop** (versión 20.10 o superior)
- **Java Development Kit (JDK) 17** o superior
- **Node.js** versión 16 o superior (incluye npm)

---

## 🔧 Instalación de Dependencias

### 1. Instalar Docker Desktop

1. Descarga Docker Desktop desde: https://www.docker.com/products/docker-desktop
2. Ejecuta el instalador y sigue los pasos de instalación
3. **Importante**: Durante la instalación, asegúrate de que esté habilitado:
   - ✅ WSL 2 (Windows Subsystem for Linux 2)
   - ✅ Hyper-V
4. Reinicia tu computadora después de instalar
5. Verifica la instalación abriendo PowerShell y ejecutando:
   ```powershell
   docker --version
   ```

### 2. Instalar Java 17

1. Descarga desde: https://jdk.java.net/17/ (o usa un distribuidor como Oracle, Eclipse Temurin, etc.)
2. Ejecuta el instalador
3. Sigue los pasos por defecto (instala en `C:\Program Files\Java\`)
4. Verifica con:
   ```powershell
   java -version
   ```

### 3. Instalar Node.js y npm

1. Descarga desde: https://nodejs.org/ (versión LTS recomendada)
2. Ejecuta el instalador
3. Sigue los pasos por defecto
4. Verifica con:
   ```powershell
   node --version
   npm --version
   ```

### 4. Instalar Maven (Opcional - se usa Docker por defecto)

Si prefieres tener Maven localmente:
1. Descarga desde: https://maven.apache.org/download.cgi
2. Descomprime en una carpeta (ej: `C:\maven`)
3. Agrega a la variable de entorno `PATH` la ruta `C:\maven\bin`
4. Verifica con:
   ```powershell
   mvn --version
   ```

---

## 🐳 Configuración de Docker

### Iniciar Docker Desktop

1. Abre Docker Desktop desde el menú Inicio
2. Espera a que diga "Docker is running" (puede tomar 1-2 minutos)
3. Verifica que está ejecutándose:
   ```powershell
   docker ps
   ```
   Deberías ver una tabla sin errores

### Crear la Base de Datos con Docker Compose

1. Navega a la carpeta raíz del proyecto:
   ```powershell
   cd "~\Sistema-de-Gestion-Odontologica"
   ```

2. Inicia los contenedores de PostgreSQL y pgAdmin:
   ```powershell
   docker-compose up -d
   ```

3. Verifica que están corriendo:
   ```powershell
   docker ps
   ```
   
   Deberías ver dos contenedores: `postgres:15-alpine` y `dpage/pgadmin4`

4. **Credenciales de la Base de Datos**:
   - **Host**: localhost
   - **Puerto**: 5433
   - **Base de Datos**: odontologia_db
   - **Usuario**: admin
   - **Contraseña**: admin

---

## 🔨 Compilación e Instalación

### Opción 1: Compilar con Maven en Docker (Recomendado)

1. Abre PowerShell en la carpeta BackEnd:
   ```powershell
   cd "~\Sistema-de-Gestion-Odontologica\BackEnd"
   ```

2. Ejecuta la compilación con Maven en Docker:
   ```powershell
   docker run --rm -v "~\Sistema-de-Gestion-Odontologica\BackEnd:/workspace" -w /workspace maven:3.9.6-eclipse-temurin-17 mvn clean package -DskipTests
   ```

3. Espera a que termine (puede tomar 5-10 minutos en la primera ejecución)

### Opción 2: Compilar con Maven Local

Si tienes Maven instalado:
```powershell
cd ~\Sistema-de-Gestion-Odontologica\BackEnd"
mvn clean package -DskipTests
```

### Instalar Dependencias del Frontend

1. Navega a la carpeta del frontend:
   ```powershell
   cd "~\Sistema-de-Gestion-Odontologica\FrontEnd\cliente-odontologia"
   ```

2. Instala las dependencias:
   ```powershell
   npm install
   ```

---

## 🚀 Iniciación de Servicios

### Paso 1: Abre Docker Desktop

1. Busca **Docker Desktop** en el menú Inicio y ábrelo
2. Espera a que diga **"Docker is running"** (puede tomar 1-2 minutos)
3. Verifica en la bandeja del sistema que Docker está activo ✓

### Paso 2: Inicia la Base de Datos

1. Abre una terminal PowerShell en la carpeta raíz del proyecto:
   ```powershell
   cd "~\Sistema-de-Gestion-Odontologica"
   ```

2. Inicia los contenedores:
   ```powershell
   docker-compose up -d
   ```

3. Verifica en **Docker Desktop** que ves dos contenedores corriendo:
   - `postgres:15-alpine` (base de datos)
   - `dpage/pgadmin4` (administrador)

### Paso 3: Abre Spring Tools Suite e Inicia los Servicios

> **Alternativa**: También puedes usar Eclipse, IntelliJ IDEA u otro IDE de Java

1. Abre **Spring Tool Suite (STS)**
2. Abre el proyecto BackEnd:
   - Archivo → Abrir Proyectos desde el Filesystem
   - Selecciona la carpeta `BackEnd`

3. En el panel **Project Explorer**, verás 4 proyectos:
   - `auth-service`
   - `pacientes-service`
   - `fichas-service`
   - `odontograma-service`

4. **Inicia cada servicio** (haz clic derecho → Run As → Spring Boot App):
   - Haz clic derecho en **auth-service** → Run As → Spring Boot App
   - Haz clic derecho en **pacientes-service** → Run As → Spring Boot App
   - Haz clic derecho en **fichas-service** → Run As → Spring Boot App
   - Haz clic derecho en **odontograma-service** → Run As → Spring Boot App

5. En la consola inferior, verás un mensaje para cada servicio:
   ```
   Started [ServiceName]Application in X.XXX seconds
   ```

6. Cuando todos estén iniciados, verás en **Docker Desktop** → **Containers**:
   - PostgreSQL y pgAdmin corriendo
   - 4 procesos Java escuchando en sus puertos

### Paso 4: Verifica la Base de Datos (Opcional)

1. Abre tu navegador en: http://localhost:5050
2. Inicia sesión con:
   - **Email**: admin@admin.com
   - **Contraseña**: admin
3. Verás todas las tablas creadas automáticamente:
   - `pacientes`
   - `dientes_paciente`
   - Y otras según los servicios

### Paso 5: Inicia el Frontend en VS Code

1. Abre **Visual Studio Code**
2. Abre la carpeta: `FrontEnd\cliente-odontologia`
3. Abre una terminal integrada en VS Code (Ctrl + `)
4. Ejecuta:
   ```powershell
   npm start
   ```

5. Automáticamente se abrirá http://localhost:3000 en tu navegador
6. Si no se abre, ingresa manualmente en la barra de direcciones

---

## 🌐 Acceso a la Aplicación

### URL de la Aplicación
- **Frontend**: http://localhost:3000

### Puertos de los Servicios Backend
- **Auth Service**: http://localhost:8081
- **Pacientes Service**: http://localhost:8082
- **Fichas Service**: http://localhost:8083
- **Odontograma Service**: http://localhost:8084

### Administración de Base de Datos
- **pgAdmin**: http://localhost:5050
  - **Usuario**: admin@admin.com
  - **Contraseña**: admin

---

## 📱 Funcionalidades Principales

### 1. Crear un Paciente

1. En la página principal, haz clic en **"Crear Paciente"**
2. Completa los campos obligatorios:
   - **Cédula**: Número de identificación único
   - **Nombre**: Nombre del paciente
   - **Apellido**: Apellido del paciente
3. Los campos opcionales (teléfono, email, dirección) puedes dejarlos vacíos
4. Haz clic en **"Guardar"**

### 2. Ver Pacientes

1. La página principal muestra una lista de todos los pacientes registrados
2. Puedes ver:
   - Cédula
   - Nombre completo
   - Acciones (editar, eliminar)

### 3. Registrar Odontograma

1. Después de crear un paciente, harás clic en **"Ver Odontograma"**
2. Se mostrará un diagrama dental digital con 32 dientes
3. Puedes seleccionar dientes para registrar información:
   - Estado del diente (sano, cariado, faltante, etc.)
   - Color
   - Observaciones

### 4. Crear Ficha Técnica

1. Selecciona un paciente
2. Haz clic en **"Ver Fichas"**
3. Completa la información:
   - Diagnóstico
   - Tratamiento propuesto
   - Observaciones

---

## 📞 Información de Contacto

Para reportar problemas o sugerencias, contacta al equipo de desarrollo.

---

**Última actualización**: Enero 2026
**Versión del Sistema**: 1.0.0


