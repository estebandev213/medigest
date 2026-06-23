# MediGest — Sistema de Gestión Hospitalaria

Trabajo Final — Fundamentos de Diseño de Software · La Salle

## Cómo correr el proyecto

```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## Consola H2

URL: `http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:file:./data/medigest`  
Usuario: `sa` · Contraseña: *(vacía)*

---

## Endpoints por módulo

### Módulo 1 — Admisión · Patrón Adapter

| Método | URL | Descripción |
|--------|-----|-------------|
| POST   | `/api/admision/pacientes` | RF01 Registrar paciente |
| GET    | `/api/admision/pacientes/{dni}/validar` | RF02 Validar DNI vía RENIEC (stub) |
| GET    | `/api/admision/pacientes/dni/{dni}` | UC03 Buscar paciente por DNI |
| GET    | `/api/admision/pacientes/{id}` | Buscar paciente por ID |
| GET    | `/api/admision/pacientes` | Listar todos los pacientes |

### Módulo 2 — Citas · Patrón Observer

| Método | URL | Descripción |
|--------|-----|-------------|
| POST   | `/api/citas` | RF03 Programar cita |
| PUT    | `/api/citas/{id}/confirmar` | RF04 Confirmar cita |
| PUT    | `/api/citas/{id}/cancelar` | RF05 Cancelar cita |
| PATCH  | `/api/citas/{id}/estado?nuevoEstado=ATENDIDA` | Cambio de estado genérico |
| GET    | `/api/citas/{id}` | Buscar cita |
| GET    | `/api/citas` | Listar todas las citas |
| GET    | `/api/citas/paciente/{pacienteId}` | Citas de un paciente |

### Módulo 3 — Atención Médica · Patrón Builder

| Método | URL | Descripción |
|--------|-----|-------------|
| POST   | `/api/atencion/historias` | RF06 Crear historia clínica |
| PATCH  | `/api/atencion/historias/{id}/laboratorio` | Agregar resultados de laboratorio |
| GET    | `/api/atencion/historias/{id}` | RF07 Consultar historia clínica |
| GET    | `/api/atencion/historias/paciente/{pacienteId}` | Historias de un paciente |

### Módulo 4 — Facturación · Patrones Composite + Strategy

| Método | URL | Descripción |
|--------|-----|-------------|
| POST   | `/api/facturacion/servicios` | RF10 Crear servicio simple |
| POST   | `/api/facturacion/paquetes` | RF10 Crear paquete de servicios |
| GET    | `/api/facturacion/servicios` | Listar servicios y paquetes |
| POST   | `/api/facturacion/facturas` | RF08/RF09 Generar factura con cobertura |
| GET    | `/api/facturacion/facturas` | Listar facturas |
| GET    | `/api/facturacion/facturas/{id}` | Buscar factura por ID |

---

## Ejemplos rápidos de uso

### Registrar paciente
```json
POST /api/admision/pacientes
{
  "dni": "12345678",
  "nombres": "Juan Carlos",
  "apellidos": "Pérez López",
  "tipo": "SIS"
}
```

### Programar cita
```json
POST /api/citas
{
  "paciente": { "id": 1 },
  "medicoAsignado": "Dr. Rodríguez",
  "fechaHora": "2026-06-25T10:00:00",
  "especialidad": "Medicina General",
  "motivo": "Control rutinario"
}
```

### Generar factura con cobertura SIS
```json
POST /api/facturacion/facturas
{
  "pacienteId": 1,
  "itemId": 1,
  "tipoCobertura": "SIS"
}
```

# 🚀 Guía de Configuración: Consulta **DNI** (apiperu.dev) en MediGest
Para poder registrar pacientes consultando la base de datos real de **RENIEC**, cada desarrollador del equipo debe seguir estos pasos sencillos para configurar su propio token de acceso.

## Paso 1: Registrarse y obtener el Token de apiperu.dev
Ingresa a 👉 apiperu.dev.
Inicia sesión o crea una cuenta gratuita.
Dirígete a la sección del Dashboard principal.
Copia el Token del Dashboard (tiene un formato de texto largo con letras y números).
Nota: Usa el token que aparece en el Dashboard inicial, no el de la sección ****API** Tokens**.

## Paso 2: Configurar tu Variable de Entorno (Sin tocar el código)
Para evitar exponer tu token personal en GitHub, lo ideal es guardarlo como una variable de entorno en tu máquina. El proyecto está configurado para leerla automáticamente.

### En Windows (PowerShell)
Abre tu terminal en la carpeta del proyecto y ejecuta:

- powershell

$env:APIPERU_TOKEN=**tu_token_aqui**
- En Windows (**CMD**):
- cmd

set APIPERU_TOKEN=tu_token_aqui
- En macOS / Linux (Bash o Zsh):
- bash

```
export APIPERU_TOKEN="tu_token_aqui"
```
(Si usas IntelliJ **IDEA**, también puedes agregar APIPERU_TOKEN=tu_token_aqui en las variables de entorno de tu **Run/Debug Configuration** de Spring Boot).

## Paso 3: Ejecutar la Aplicación
En la misma terminal donde configuraste la variable en el Paso 2, corre el proyecto:

- powershell

.\mvnw.cmd spring-boot:run
Paso 4: Probar la consulta de **DNI**
Abre tu navegador, cartero de **API** (Postman/Insomnia) o ejecuta en otra terminal una petición **GET** al endpoint de validación con un **DNI** real de 8 dígitos:

- http

**GET** [http://localhost:8080/api/admision/pacientes/72145844/validar](http://localhost:8080/api/admision/pacientes/72145844/validar)
Deberías recibir la respuesta con el nombre y los apellidos reales obtenidos desde **RENIEC**.
