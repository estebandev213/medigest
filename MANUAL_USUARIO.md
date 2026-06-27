# Manual de Usuario — MediGest
## Sistema de Gestión Hospitalaria

**Versión:** 0.1.0  
**Institución:** Universidad La Salle  
**Asignatura:** Fundamentos de Diseño de Software  
**Fecha:** Junio 2026  

---

## Tabla de Contenidos

1. [Introducción](#1-introducción)
2. [Requisitos del Sistema](#2-requisitos-del-sistema)
3. [Acceso al Sistema](#3-acceso-al-sistema)
4. [Interfaz Principal — Dashboard](#4-interfaz-principal--dashboard)
5. [Módulo 1: Admisión y Registro de Pacientes](#5-módulo-1-admisión-y-registro-de-pacientes)
6. [Módulo 2: Gestión de Citas Médicas](#6-módulo-2-gestión-de-citas-médicas)
7. [Módulo 3: Atención Médica e Historia Clínica](#7-módulo-3-atención-médica-e-historia-clínica)
8. [Módulo 4: Facturación y Cobros](#8-módulo-4-facturación-y-cobros)
9. [Directorio de Médicos](#9-directorio-de-médicos)
10. [Perfil de Usuario](#10-perfil-de-usuario)
11. [Consola de Base de Datos (H2)](#11-consola-de-base-de-datos-h2)
12. [API REST — Referencia Técnica](#12-api-rest--referencia-técnica)
13. [Resolución de Problemas Frecuentes](#13-resolución-de-problemas-frecuentes)

---

## 1. Introducción

**MediGest** es un sistema de gestión hospitalaria diseñado para centralizar y agilizar los procesos administrativos y clínicos de una institución de salud. El sistema integra cuatro módulos funcionales:

| Módulo | Función | Patrón de Diseño |
|--------|---------|-----------------|
| Admisión y Registro | Registro y validación de pacientes | Adapter |
| Gestión de Citas | Programación y seguimiento de citas médicas | Observer |
| Historia Clínica | Registro estructurado de atenciones médicas | Builder + Director |
| Facturación y Cobros | Generación de facturas con cobertura de seguros | Composite + Strategy |

### Características principales

- Registro de pacientes con validación de identidad en tiempo real vía RENIEC (apiperu.dev)
- Programación de citas con notificaciones automáticas al médico y registro de auditoría
- Construcción paso a paso de historias clínicas con campos médicos estructurados
- Facturación inteligente con cálculo automático de cobertura según tipo de seguro (SIS, Seguro Privado o Particular)
- Interfaz web responsive accesible desde cualquier navegador moderno

---

## 2. Requisitos del Sistema

### Para el usuario final (cliente)

| Requisito | Detalle |
|-----------|---------|
| Navegador | Google Chrome 90+, Firefox 88+, Edge 90+ |
| Conexión | Red local o Internet (para validación RENIEC) |
| Resolución mínima | 1280 × 720 px |

### Para ejecutar el servidor (administrador técnico)

| Requisito | Versión mínima |
|-----------|---------------|
| Java | 17 o superior |
| Maven | 3.8+ (o usar el wrapper `mvnw` incluido) |
| Sistema Operativo | Windows 10/11, macOS, Linux |
| Puerto disponible | 8080 (TCP) |

### Iniciar el servidor

```bash
# Opción 1 — Maven Wrapper (recomendado, no requiere Maven instalado)
./mvnw spring-boot:run                        # Linux / macOS
mvnw.cmd spring-boot:run                      # Windows

# Opción 2 — Maven instalado globalmente
mvn spring-boot:run

# Con integración RENIEC real (requiere token de apiperu.dev)
mvnw.cmd spring-boot:run -Dspring.profiles.active=prod
```

Una vez iniciado, el sistema queda disponible en:

```
Aplicación web:  http://localhost:8080
Consola H2:      http://localhost:8080/h2-console
```

> **Nota:** La base de datos se almacena en archivo (`./data/medigest`) y persiste entre reinicios. No es necesario volver a cargar datos cada vez que se inicia el servidor.

---

## 3. Acceso al Sistema

### 3.1 Página de inicio

Al ingresar a `http://localhost:8080`, se muestra la página de bienvenida de MediGest con información general del sistema y acceso al portal de inicio de sesión.

### 3.2 Inicio de sesión

Navegar a `http://localhost:8080/login` o hacer clic en el botón **"Iniciar sesión"** desde la página de inicio.

**Credenciales de acceso:**

| Campo | Valor |
|-------|-------|
| Usuario | `admin` |
| Contraseña | `admin` |

Tras ingresar las credenciales correctas, el sistema redirige automáticamente al **Dashboard principal**.

> **Aviso de seguridad:** Las credenciales predeterminadas son para uso en entorno de demostración. En producción deben cambiarse antes del despliegue.

### 3.3 Cierre de sesión

Hacer clic en **"Cerrar sesión"** en el menú lateral izquierdo. La sesión se invalida inmediatamente y el sistema redirige a la página de inicio.

---

## 4. Interfaz Principal — Dashboard

Tras iniciar sesión, el sistema muestra el **Dashboard principal**, que sirve como centro de operaciones del sistema.

### 4.1 Estructura de la interfaz

```
┌─────────────────────────────────────────────────────────────┐
│  TOPBAR: Título del módulo activo          [Botón de acción] │
├───────────────┬─────────────────────────────────────────────┤
│               │                                             │
│  MENÚ LATERAL │           CONTENIDO PRINCIPAL               │
│  (Sidebar)    │                                             │
│               │                                             │
│  • Mis Citas  │                                             │
│  • Pacientes  │                                             │
│  • Médicos    │                                             │
│  • Historial  │                                             │
│  • Factura.   │                                             │
│  • Perfil     │                                             │
│  • Logout     │                                             │
│               │                                             │
└───────────────┴─────────────────────────────────────────────┘
```

### 4.2 Menú lateral

| Opción | Ruta | Función |
|--------|------|---------|
| Mis Citas | `/dashboard` | Panel de citas (vista principal) |
| Pacientes | `/dashboard/pacientes` | Módulo de admisión y registro |
| Buscar Médicos | `/dashboard/buscar-medicos` | Directorio del personal médico |
| Historial Clínico | `/dashboard/historial-clinico` | Historias clínicas |
| Facturación | `/dashboard/facturacion` | Facturación y cobros |
| Perfil | `/dashboard/perfil` | Datos del usuario activo |
| Cerrar sesión | `/logout` | Finalizar sesión |

### 4.3 Vista de citas (Dashboard principal)

La pantalla de inicio del dashboard muestra las **citas registradas en el sistema**, organizadas en tarjetas con:

- Nombre completo del paciente y DNI
- Médico asignado y especialidad
- Fecha y hora de la cita
- Motivo de consulta
- Estado actual de la cita (con etiqueta de color)

**Colores de estado:**

| Color | Estado | Significado |
|-------|--------|-------------|
| 🟡 Amarillo | `PENDIENTE` | Cita registrada, aún sin confirmar |
| 🟢 Verde | `CONFIRMADA` | Cita confirmada por el médico |
| 🔵 Azul | `ATENDIDA` | Paciente fue atendido |
| 🔴 Rojo | `CANCELADA` | Cita cancelada |

---

## 5. Módulo 1: Admisión y Registro de Pacientes

**Ruta:** `Menú lateral → Pacientes`  
**Patrón de diseño:** Adapter (integración con RENIEC vía apiperu.dev)

Este módulo permite registrar nuevos pacientes y consultar el padrón existente.

### 5.1 Listar pacientes

Al ingresar al módulo, se muestra una tabla con todos los pacientes registrados. La tabla incluye:

| Columna | Descripción |
|---------|-------------|
| ID | Identificador interno del sistema |
| Nombres y Apellidos | Nombre completo del paciente |
| DNI | Documento de identidad (8 dígitos) |
| Teléfono | Número de contacto |
| Email | Correo electrónico |
| Tipo de Seguro | SIS / Seguro Privado / Particular |

### 5.2 Buscar paciente por DNI

En la parte superior de la lista, hay un campo de búsqueda:

1. Ingresar el DNI del paciente (8 dígitos) en el campo **"Buscar por DNI"**
2. Hacer clic en el botón **"Buscar"**
3. La tabla filtra y muestra únicamente al paciente con ese DNI

Si no existe un paciente con ese DNI, la tabla aparece vacía.

### 5.3 Registrar nuevo paciente (RF01)

1. Hacer clic en el botón **"Añadir Paciente"** (parte superior derecha)
2. Se abre un formulario modal con los siguientes campos:

| Campo | Obligatorio | Descripción |
|-------|-------------|-------------|
| DNI | ✅ Sí | 8 dígitos, debe ser único en el sistema |
| Nombres | ✅ Sí | Nombres del paciente |
| Apellidos | ✅ Sí | Apellidos del paciente |
| Fecha de Nacimiento | ✅ Sí | Formato: AAAA-MM-DD |
| Tipo de Seguro | ✅ Sí | `SIS`, `SEGURO_PRIVADO` o `PARTICULAR` |
| Teléfono | No | Número de contacto |
| Dirección | No | Domicilio |
| Email | No | Correo electrónico |

3. Hacer clic en **"Guardar"** para registrar al paciente
4. Si el DNI ya existe en el sistema, se muestra un mensaje de error indicando el duplicado

> **Tip:** Si el perfil del sistema es `stub`, puede usar los DNIs de prueba `12345678`, `87654321` o `11223344` para validar datos sin consumir el token de RENIEC.

### 5.4 Validación de identidad vía RENIEC (RF02)

El sistema puede consultar los datos de un paciente directamente desde la base de RENIEC a través de la API apiperu.dev:

**Desde la API REST:**
```
GET /api/admision/pacientes/{dni}/validar
```

**Respuesta exitosa:**
```json
{
  "dni": "12345678",
  "nombres": "Juan Carlos",
  "apellidos": "Perez Lopez",
  "fechaNacimiento": "1990-05-15",
  "direccion": "Av. Lima 123"
}
```

**Modos de operación:**

| Perfil | Comportamiento |
|--------|---------------|
| `stub` (por defecto) | Retorna datos ficticios para DNIs de prueba sin llamar a la API |
| `prod` | Consulta la API real de apiperu.dev con el token configurado |

**DNIs de prueba (modo stub):**

| DNI | Nombre | Fecha Nac. |
|-----|--------|-----------|
| `12345678` | Juan Carlos Pérez López | 1990-05-15 |
| `87654321` | María Elena García Torres | 1985-11-22 |
| `11223344` | Carlos Quispe Mamani | 2000-03-08 |

---

## 6. Módulo 2: Gestión de Citas Médicas

**Ruta:** `Menú lateral → Mis Citas` (Dashboard principal)  
**Patrón de diseño:** Observer (notificación automática al médico y auditoría)

Este módulo gestiona el ciclo de vida completo de una cita médica: programación, confirmación, atención y cancelación.

### 6.1 Programar nueva cita (RF03)

1. Desde el Dashboard, hacer clic en el botón **"Nueva Cita"** (parte superior derecha)
2. Completar el formulario modal:

| Campo | Obligatorio | Descripción |
|-------|-------------|-------------|
| ID del Paciente | ✅ Sí | Identificador numérico del paciente registrado |
| Médico Asignado | ✅ Sí | Nombre del médico responsable |
| Especialidad | ✅ Sí | Especialidad médica de la consulta |
| Fecha y Hora | ✅ Sí | Fecha y hora de la cita |
| Motivo | ✅ Sí | Descripción breve del motivo de consulta |

3. Hacer clic en **"Programar Cita"**
4. La cita queda registrada con estado **PENDIENTE**
5. El sistema notifica automáticamente al médico y registra la acción en auditoría (patrón Observer)

### 6.2 Confirmar una cita (RF04)

Desde la tarjeta de la cita en el Dashboard:

1. Localizar la cita con estado **PENDIENTE**
2. Hacer clic en el botón **"Confirmar"**
3. El estado cambia a **CONFIRMADA**
4. El sistema notifica al médico del cambio de estado

### 6.3 Cancelar una cita (RF05)

1. Localizar la cita (puede estar en estado PENDIENTE o CONFIRMADA)
2. Hacer clic en el botón **"Cancelar"**
3. El estado cambia a **CANCELADA**
4. La cancelación queda registrada en auditoría

### 6.4 Marcar cita como atendida

1. Localizar la cita con estado **CONFIRMADA**
2. Hacer clic en el botón **"Atender"**
3. El estado cambia a **ATENDIDA**

> **Nota:** Una cita marcada como **ATENDIDA** puede ser vinculada a una Historia Clínica en el Módulo 3.

### 6.5 Flujo de estados de una cita

```
[PENDIENTE] ──── Confirmar ────► [CONFIRMADA]
     │                                │
     │                                │
  Cancelar                         Atender
     │                                │
     ▼                                ▼
[CANCELADA]                      [ATENDIDA]
```

---

## 7. Módulo 3: Atención Médica e Historia Clínica

**Ruta:** `Menú lateral → Historial Clínico`  
**Patrón de diseño:** Builder + Director (construcción estructurada de historias clínicas)

Este módulo permite registrar y consultar el historial médico completo de cada paciente.

### 7.1 Listar historias clínicas

La vista principal muestra una tabla con todas las historias clínicas registradas:

| Columna | Descripción |
|---------|-------------|
| ID | Identificador de la historia clínica |
| Paciente | Nombre del paciente |
| Cita vinculada | ID de la cita de origen (si aplica) |
| Médico tratante | Médico que realizó la atención |
| Fecha | Fecha de la atención |
| Diagnóstico (resumen) | Primeros caracteres del diagnóstico |
| Laboratorio | Indica si hay resultados de laboratorio |

### 7.2 Filtrar por paciente

En la barra superior de la vista:

1. Ingresar el **ID del paciente** en el campo de búsqueda
2. Hacer clic en **"Filtrar"**
3. Se muestran únicamente las historias clínicas de ese paciente

### 7.3 Crear nueva historia clínica (RF06)

1. Hacer clic en **"Nueva Historia"** (parte superior derecha)
2. Completar el formulario:

| Campo | Obligatorio | Descripción |
|-------|-------------|-------------|
| ID del Paciente | ✅ Sí | El paciente debe estar previamente registrado en el sistema |
| ID de la Cita | No | ID de una cita **ATENDIDA** asociada a esta historia |
| Médico Tratante | ✅ Sí | Nombre del médico responsable |
| Diagnóstico | ✅ Sí | Diagnóstico clínico completo |
| Alergias | No | Alergias conocidas del paciente |
| Resultados de Laboratorio | No | Resultados iniciales si se dispone de ellos |
| Tratamiento | No | Plan de tratamiento indicado |

3. Hacer clic en **"Crear Historia"**
4. El sistema construye la historia clínica paso a paso (patrón Builder) y la persiste

**Restricciones importantes:**
- El paciente debe existir en el sistema
- Si se vincula a una cita, esa cita debe pertenecer al mismo paciente
- No se puede vincular dos historias clínicas a la misma cita

### 7.4 Ver detalle de una historia clínica (RF07)

1. En la tabla de historias clínicas, hacer clic sobre la fila o en el botón **"Ver detalle"**
2. Se muestra la ficha completa de la historia clínica:

**Sección Datos del Paciente:**
- Nombre completo, DNI, tipo de seguro

**Sección Datos de la Atención:**
- Fecha de atención
- Médico tratante
- Cita vinculada (si aplica)

**Sección Contenido Médico:**
- Diagnóstico completo
- Alergias registradas
- Resultados de laboratorio
- Plan de tratamiento

### 7.5 Agregar resultados de laboratorio

Desde el detalle de una historia clínica existente:

1. Hacer clic en el botón **"Agregar Resultados de Laboratorio"**
2. Se abre un formulario con el campo **"Resultados"**
3. Ingresar los resultados obtenidos
4. Hacer clic en **"Guardar Resultados"**
5. Los resultados quedan asociados a la historia clínica

> **Nota:** Esta operación puede realizarse en cualquier momento posterior a la creación de la historia, permitiendo completar los datos cuando el laboratorio entregue sus informes.

---

## 8. Módulo 4: Facturación y Cobros

**Ruta:** `Menú lateral → Facturación`  
**Patrón de diseño:** Composite (catálogo de servicios) + Strategy (cálculo de cobertura)

Este módulo gestiona el catálogo de servicios médicos y genera facturas con cálculo automático de cobertura según el tipo de seguro del paciente.

### 8.1 Vista del módulo

La pantalla de facturación se divide en dos secciones:

**Catálogo de Servicios:** Lista todos los servicios simples y paquetes disponibles con su costo.

**Facturas Generadas:** Historial de todas las facturas emitidas con los montos desglosados.

### 8.2 Tipos de servicios (Patrón Composite)

El sistema maneja dos tipos de ítems facturables que forman una estructura de árbol:

| Tipo | Descripción | Costo |
|------|-------------|-------|
| **Servicio Simple** | Consulta, análisis, medicamento u otro concepto individual | Precio fijo configurado |
| **Paquete de Servicios** | Agrupa varios servicios simples u otros paquetes | Suma de todos sus componentes |

**Ejemplo de estructura:**
```
Paquete Quirúrgico (Costo total: suma automática)
├── Consulta Pre-operatoria    S/. 150.00
├── Análisis de Sangre         S/. 80.00
├── Cirugía General            S/. 1,500.00
└── Paquete Post-operatorio
    ├── Medicamentos            S/. 200.00
    └── Control de Enfermería   S/. 100.00
```

### 8.3 Crear servicio simple (RF10)

1. En la sección **"Catálogo de Servicios"**, hacer clic en **"Nuevo Servicio"**
2. Completar los campos:

| Campo | Descripción |
|-------|-------------|
| Nombre del Servicio | Ej.: "Consulta General", "Análisis de Sangre" |
| Precio Base | Monto en soles (formato decimal, ej.: `150.00`) |

3. Hacer clic en **"Guardar Servicio"**
4. El servicio queda disponible para incluirse en facturas o paquetes

### 8.4 Calcular cobertura por tipo de seguro (Patrón Strategy)

El sistema aplica automáticamente la estrategia de cobertura según el tipo de seguro declarado al generar la factura:

| Tipo de Cobertura | Cobertura del Seguro | Pago del Paciente |
|-------------------|---------------------|-------------------|
| **SIS** | 80% | 20% |
| **Seguro Privado** | 60% | 40% |
| **Particular** | 0% | 100% |

**Ejemplo de cálculo:**

Para un servicio de S/. 500.00:

| Seguro | Costo Total | Cubre el Seguro | Paga el Paciente |
|--------|-------------|-----------------|-----------------|
| SIS | S/. 500.00 | S/. 400.00 (80%) | S/. 100.00 (20%) |
| Seguro Privado | S/. 500.00 | S/. 300.00 (60%) | S/. 200.00 (40%) |
| Particular | S/. 500.00 | S/. 0.00 (0%) | S/. 500.00 (100%) |

### 8.5 Generar factura (RF08 / RF09)

1. En la sección de facturación, hacer clic en **"Generar Factura"**
2. Completar el formulario:

| Campo | Obligatorio | Descripción |
|-------|-------------|-------------|
| ID del Paciente | ✅ Sí | Paciente al que se le factura |
| ID del Servicio/Paquete | ✅ Sí | ID del ítem a facturar (servicio simple o paquete) |
| Tipo de Cobertura | ✅ Sí | `SIS`, `SEGURO_PRIVADO` o `PARTICULAR` |

3. Hacer clic en **"Generar Factura"**
4. El sistema:
   - Calcula el costo total del servicio o paquete (sumando recursivamente los componentes)
   - Aplica la estrategia de cobertura correspondiente
   - Genera la factura con el desglose completo de montos
5. La factura aparece en la sección **"Facturas Generadas"**

### 8.6 Consultar facturas

**Desde la interfaz web:** La sección inferior de la pantalla de Facturación muestra todas las facturas, con las siguientes columnas:

| Columna | Descripción |
|---------|-------------|
| ID | Número de factura |
| Paciente | Nombre del paciente facturado |
| Servicio | Nombre del ítem facturado |
| Tipo Cobertura | Seguro aplicado |
| Costo Total | Monto total del servicio |
| Monto Cubierto | Lo que cubre el seguro |
| Monto a Pagar | Lo que paga el paciente |
| Fecha | Fecha y hora de emisión |

---

## 9. Directorio de Médicos

**Ruta:** `Menú lateral → Buscar Médicos`

Esta sección muestra el directorio del personal médico de la institución. Cada médico se presenta en una tarjeta con:

- Fotografía de perfil
- Nombre completo
- Especialidad médica
- Breve biografía

**Personal médico registrado:**

| Nombre | Especialidad |
|--------|-------------|
| Esteban Villagarcia | — |
| Keiko Carpio | — |
| Alan Barrientos | — |
| Leonidas Frames | — |
| Carlos Delgado | — |
| Jeanfranco Lazarinos | — |
| Fernando Lizarraga | — |
| ByeHello Pauca | — |
| Franco Newvillage | — |

> Esta sección es de solo lectura y sirve como referencia para asignar médicos al programar citas.

---

## 10. Perfil de Usuario

**Ruta:** `Menú lateral → Perfil`

Muestra la información del usuario actualmente conectado al sistema:

| Campo | Valor |
|-------|-------|
| Usuario | `admin` |
| Rol | Administrador |
| Estado de sesión | Activa |

**Información del sistema:**

| Dato | Descripción |
|------|-------------|
| Nombre del sistema | MediGest |
| Institución | Universidad La Salle |
| Módulos activos | Admisión, Citas, Historial Clínico, Facturación |

---

## 11. Consola de Base de Datos (H2)

MediGest utiliza una base de datos H2 embebida que persiste en archivo. Para acceder a la consola de administración:

**URL:** `http://localhost:8080/h2-console`

**Parámetros de conexión:**

| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:file:./data/medigest` |
| User Name | `sa` |
| Password | *(dejar vacío)* |

### Tablas del sistema

| Tabla | Descripción |
|-------|-------------|
| `PACIENTES` | Registro de todos los pacientes |
| `CITAS` | Citas médicas programadas |
| `HISTORIAS_CLINICAS` | Historias clínicas de los pacientes |
| `ITEMS_FACTURABLES` | Servicios simples y paquetes (tabla única, herencia SINGLE_TABLE) |
| `ITEM_FACTURABLE_COMPONENTES` | Relación entre paquetes y sus componentes |
| `FACTURAS` | Facturas generadas |

### Consultas útiles

```sql
-- Ver todos los pacientes
SELECT * FROM PACIENTES;

-- Ver citas con su estado
SELECT c.ID, p.NOMBRES, p.APELLIDOS, c.MEDICO_ASIGNADO,
       c.FECHA_HORA, c.ESTADO
FROM CITAS c
JOIN PACIENTES p ON c.PACIENTE_ID = p.ID
ORDER BY c.FECHA_HORA;

-- Ver facturas con desglose
SELECT f.ID, p.NOMBRES, f.COSTO_TOTAL,
       f.MONTO_CUBIERTO, f.MONTO_PACIENTE, f.TIPO_COBERTURA
FROM FACTURAS f
JOIN PACIENTES p ON f.PACIENTE_ID = p.ID;

-- Ver servicios disponibles
SELECT ID, TIPO_ITEM, NOMBRE, PRECIO_BASE FROM ITEMS_FACTURABLES;
```

---

## 12. API REST — Referencia Técnica

Todos los módulos exponen endpoints REST en `/api/*` para integración con sistemas externos o pruebas directas.

### Módulo Admisión — `/api/admision`

| Método | Endpoint | Descripción | Body / Params |
|--------|----------|-------------|---------------|
| `GET` | `/pacientes` | Listar todos los pacientes | — |
| `POST` | `/pacientes` | Registrar nuevo paciente | JSON (ver abajo) |
| `GET` | `/pacientes/{id}` | Buscar paciente por ID | — |
| `GET` | `/pacientes/dni/{dni}` | Buscar paciente por DNI | — |
| `GET` | `/pacientes/{dni}/validar` | Validar DNI en RENIEC | — |

**Body para registrar paciente:**
```json
{
  "dni": "12345678",
  "nombres": "Juan Carlos",
  "apellidos": "Perez Lopez",
  "fechaNacimiento": "1990-05-15",
  "tipo": "SIS",
  "telefono": "987654321",
  "direccion": "Av. Lima 123",
  "email": "juan@email.com"
}
```

### Módulo Citas — `/api/citas`

| Método | Endpoint | Descripción | Body / Params |
|--------|----------|-------------|---------------|
| `GET` | `/` | Listar todas las citas | — |
| `POST` | `/` | Programar nueva cita | JSON (ver abajo) |
| `GET` | `/{id}` | Buscar cita por ID | — |
| `GET` | `/paciente/{pacienteId}` | Listar citas de un paciente | — |
| `PUT` | `/{id}/confirmar` | Confirmar cita | — |
| `PUT` | `/{id}/cancelar` | Cancelar cita | — |
| `PATCH` | `/{id}/estado` | Cambiar estado manualmente | `?nuevoEstado=ATENDIDA` |

**Body para programar cita:**
```json
{
  "pacienteId": 1,
  "medicoAsignado": "Dr. Carlos Delgado",
  "especialidad": "Medicina General",
  "fechaHora": "2026-07-01T10:30:00",
  "motivo": "Control rutinario"
}
```

### Módulo Atención — `/api/atencion`

| Método | Endpoint | Descripción | Body / Params |
|--------|----------|-------------|---------------|
| `POST` | `/historias` | Crear historia clínica | JSON (ver abajo) |
| `GET` | `/historias/{id}` | Consultar historia clínica | — |
| `GET` | `/historias/paciente/{id}` | Listar historias de un paciente | — |
| `PATCH` | `/historias/{id}/laboratorio` | Agregar resultados de laboratorio | JSON |

**Body para crear historia clínica:**
```json
{
  "pacienteId": 1,
  "citaId": 3,
  "medicoTratante": "Dr. Carlos Delgado",
  "diagnostico": "Hipertensión arterial grado I",
  "alergias": "Penicilina",
  "resultadosLaboratorio": "Glucosa 95 mg/dL, Creatinina 0.9 mg/dL",
  "tratamiento": "Enalapril 10mg - 1 tableta diaria, dieta hiposódica"
}
```

**Body para agregar laboratorio:**
```json
{
  "resultadosLaboratorio": "Hemograma completo: Hb 14.2 g/dL, Leucocitos 7,500"
}
```

### Módulo Facturación — `/api/facturacion`

| Método | Endpoint | Descripción | Body / Params |
|--------|----------|-------------|---------------|
| `GET` | `/servicios` | Listar servicios y paquetes | — |
| `POST` | `/servicios` | Crear servicio simple | JSON (ver abajo) |
| `POST` | `/paquetes` | Crear paquete de servicios | JSON (ver abajo) |
| `GET` | `/facturas` | Listar todas las facturas | — |
| `POST` | `/facturas` | Generar factura | JSON (ver abajo) |
| `GET` | `/facturas/{id}` | Consultar factura por ID | — |
| `GET` | `/facturas/paciente/{id}` | Facturas de un paciente | — |

**Body para crear servicio simple:**
```json
{
  "nombre": "Consulta General",
  "precioBase": 150.00
}
```

**Body para crear paquete:**
```json
{
  "nombre": "Paquete Chequeo Completo",
  "itemIds": [1, 2, 3]
}
```

**Body para generar factura:**
```json
{
  "pacienteId": 1,
  "itemId": 5,
  "tipoCobertura": "SIS"
}
```

**Respuesta de factura:**
```json
{
  "id": 1,
  "paciente": { "id": 1, "nombres": "Juan Carlos", "apellidos": "Perez Lopez" },
  "item": { "id": 5, "nombre": "Consulta General" },
  "costoTotal": 150.00,
  "montoCubierto": 120.00,
  "montoPaciente": 30.00,
  "tipoCobertura": "SIS",
  "fechaEmision": "2026-06-26T21:00:00"
}
```

---

## 13. Resolución de Problemas Frecuentes

### El sistema muestra "HTTP 400 – Bad Request" al abrir en el navegador

**Causa:** El navegador está enviando cabeceras HTTP demasiado grandes (cookies acumuladas).  
**Solución:**
1. Limpiar las cookies de `localhost` en el navegador:
   - Chrome: `Configuración → Privacidad y seguridad → Borrar datos de navegación`
   - O acceder desde una ventana de incógnito
2. Si persiste, el servidor ya tiene configurado el límite ampliado a 64KB — reiniciar el servidor resuelve el problema

---

### La validación de DNI retorna "Servicio externo no disponible"

**Causa A:** Se está usando el perfil `stub` con un DNI que no está en los datos de prueba.  
**Solución:** Usar uno de los DNIs de prueba: `12345678`, `87654321` o `11223344`.

**Causa B:** Se está usando el perfil `prod` pero el token de apiperu.dev no es válido o el DNI consultado no existe en el padrón.  
**Solución:** Verificar que el token en `application.properties` sea válido y que el DNI corresponda a una persona real.

---

### El servidor no inicia — "Port 8080 was already in use"

**Causa:** Hay otra instancia de la aplicación ya corriendo.  
**Solución:**

```powershell
# Windows — identificar y terminar el proceso en el puerto 8080
netstat -ano | findstr :8080
taskkill /PID <número_de_PID> /F
```

---

### Los datos desaparecen entre reinicios

**Causa:** La base de datos está configurada en modo en memoria en lugar de archivo.  
**Verificación:** Revisar `application.properties`:

```properties
# Debe ser jdbc:h2:file:./data/medigest (NO jdbc:h2:mem:...)
spring.datasource.url=jdbc:h2:file:./data/medigest;AUTO_SERVER=TRUE
```

---

### No se puede vincular una cita a una historia clínica

**Causas posibles:**
1. La cita no existe — verificar el ID de la cita
2. La cita no pertenece al mismo paciente
3. Ya existe otra historia clínica vinculada a esa cita

**Solución:** Verificar los datos en la consola H2 con:
```sql
SELECT * FROM CITAS WHERE ID = <id_cita>;
SELECT * FROM HISTORIAS_CLINICAS WHERE CITA_ID = <id_cita>;
```

---

### La factura muestra montos incorrectos

**Verificación:** Confirmar el tipo de cobertura seleccionado al generar la factura. Las tasas son:
- `SIS` → 80% cubierto
- `SEGURO_PRIVADO` → 60% cubierto  
- `PARTICULAR` → 0% cubierto (pago íntegro)

Si el tipo de cobertura no coincide con el seguro del paciente, generar una nueva factura con el tipo correcto.

---

*Manual elaborado para MediGest v0.1.0 — Universidad La Salle · Fundamentos de Diseño de Software · 2026*
