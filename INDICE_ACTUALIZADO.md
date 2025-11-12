# 📚 Índice de Documentación - Agenda Accesible

**Última actualización:** 11 de noviembre de 2025  
**Estado:** ✅ Backend completo y funcional

---

## 🚀 EMPIEZA AQUÍ

### Si acabas de llegar o vuelves después de un tiempo:

1. **[ESTADO_ACTUAL.md](ESTADO_ACTUAL.md)** ⭐ **LEE ESTO PRIMERO**
   - Resumen ejecutivo completo
   - Qué está hecho y qué falta
   - Métricas del proyecto

2. **[SESION_SERVICIOS.md](SESION_SERVICIOS.md)** ⭐ **ÚLTIMA SESIÓN**
   - Qué se hizo en la sesión de hoy
   - Problemas encontrados y soluciones
   - 850 líneas de código agregadas

3. **[ROADMAP.md](ROADMAP.md)**
   - Próximos pasos sugeridos
   - Prioridades y estimaciones

---

## 📖 Documentación Completa

### 🎯 Estado y Planificación

| Archivo | Descripción | Prioridad |
|---------|-------------|-----------|
| **[ESTADO_ACTUAL.md](ESTADO_ACTUAL.md)** | ⭐ Resumen ejecutivo del proyecto | 🔥 ALTA |
| **[SESION_SERVICIOS.md](SESION_SERVICIOS.md)** | ⭐ Resumen de última sesión (11/nov) | 🔥 ALTA |
| **[ROADMAP.md](ROADMAP.md)** | Próximos pasos actualizados | 🔥 ALTA |
| [README.md](README.md) | Visión general y cómo ejecutar | Media |
| [Prompts.md](Prompts.md) | Historial de prompts | Baja |

### 🏗️ Arquitectura y Diseño

| Archivo | Descripción | Cuándo leer |
|---------|-------------|-------------|
| [ARQUITECTURA.md](ARQUITECTURA.md) | Diseño completo del sistema | Al inicio |
| [PROJECT_CONTEXT.md](PROJECT_CONTEXT.md) | Contexto técnico detallado | Al inicio |
| [START_HERE.md](START_HERE.md) | Guía de inicio | Primera vez |

### ✅ Implementaciones Completadas

| Archivo | Tema | Fecha |
|---------|------|-------|
| **[SERVICIOS_COMPLETADO.md](SERVICIOS_COMPLETADO.md)** | ⭐ Capa de servicios | 11/nov/2025 |
| [PASO_SCHEDULER_COMPLETADO.md](PASO_SCHEDULER_COMPLETADO.md) | Scheduler | Anterior |
| [RESUMEN_SCHEDULER.md](RESUMEN_SCHEDULER.md) | Resumen scheduler | Anterior |
| [SCHEDULER_EXPLICACION.md](SCHEDULER_EXPLICACION.md) | Explicación técnica | Anterior |
| [PASO3_CLI_COMPLETADO.md](PASO3_CLI_COMPLETADO.md) | CLI | Anterior |

### 📘 Guías de Uso

| Archivo | Para quién | Contenido |
|---------|------------|-----------|
| [GUIA_USO.md](GUIA_USO.md) | Usuarios | Uso general |
| [GUIA_USO_CLI.md](GUIA_USO_CLI.md) | Usuarios | Uso de CLI |

---

## 🛠️ Scripts Ejecutables

### Compilación
```batch
compile.bat          # Compila todo el proyecto
```

### Ejecución
```batch
run.bat             # Ejecuta la aplicación principal (CLI)
```

### Tests
```batch
test.bat            # Verificación de BD
test_daos.bat       # Test de DAOs
test_scheduler.bat  # Test del scheduler
test_servicios.bat  # ⭐ NUEVO - Test de servicios (11/nov)
```

---

## 📁 Estructura del Código

```
src/
├── app/
│   └── Main.java                 # Punto de entrada
│
├── domain/                       # Entidades
│   ├── Paciente.java
│   ├── Cuidador.java
│   ├── Medicamento.java
│   ├── PacienteMedicamento.java
│   ├── Recordatorio.java
│   ├── Notificacion.java
│   ├── Adherencia.java
│   ├── Rutina.java
│   └── ItemRutina.java
│
├── service/                      # ⭐ NUEVO (11/nov)
│   ├── PacienteService.java
│   ├── CuidadorService.java
│   ├── MedicamentoService.java
│   └── RecordatorioService.java
│
├── controller/
│   └── TomaService.java          # Scheduler automático
│
├── infra/
│   ├── dao/                      # Interfaces
│   │   └── impl/                 # Implementaciones JDBC
│   └── db/
│       ├── ConexionDB.java       # Pool de conexiones
│       └── VerificarDB.java
│
├── shared/
│   └── observer/                 # Patrón Observer
│
├── ui/
│   ├── CLI.java                  # Interfaz de consola
│   ├── MainFrame.java            # (vacío, para GUI futura)
│   ├── menu/                     # Menús CLI
│   └── utils/
│
└── test/
    ├── TestConexionSimple.java
    ├── TestDAOs.java
    ├── TestTomaService.java
    └── TestServicios.java        # ⭐ NUEVO (11/nov)
```

---

## 🎯 Guías de Lectura por Objetivo

### 🆕 "Acabo de llegar al proyecto"
1. **ESTADO_ACTUAL.md** (10 min)
2. README.md (5 min)
3. Ejecutar `run.bat` para ver la app
4. GUIA_USO_CLI.md

### 💻 "Quiero continuar desarrollando"
1. **SESION_SERVICIOS.md** (contexto de última sesión)
2. **ROADMAP.md** (qué hacer ahora)
3. **SERVICIOS_COMPLETADO.md** (entender servicios)
4. Elegir una tarea del roadmap

### 📊 "Voy a presentar el proyecto"
1. **ESTADO_ACTUAL.md** (tiene métricas y mensajes clave)
2. README.md
3. ARQUITECTURA.md (para diagramas)
4. GUIA_USO_CLI.md (para la demo)

### 🐛 "Estoy debuggeando"
1. Ver tests correspondientes:
   - test_daos.bat
   - test_scheduler.bat
   - test_servicios.bat
2. ARQUITECTURA.md (entender flujos)

### 🎓 "Quiero entender cómo funciona"
**Capa de Servicios:**
- **SERVICIOS_COMPLETADO.md**
- src/service/*.java

**Scheduler:**
- PASO_SCHEDULER_COMPLETADO.md
- SCHEDULER_EXPLICACION.md

**Base de Datos:**
- ARQUITECTURA.md
- db/schema.sql

---

## 🔍 Buscar por Tema

### Servicios (Business Logic) ⭐ NUEVO
| Qué | Dónde |
|-----|-------|
| Documentación completa | SERVICIOS_COMPLETADO.md |
| Resumen de implementación | SESION_SERVICIOS.md |
| Código fuente | src/service/*.java |
| Tests | test/TestServicios.java |
| Script de test | test_servicios.bat |

### Scheduler y Notificaciones
| Qué | Dónde |
|-----|-------|
| Documentación | PASO_SCHEDULER_COMPLETADO.md |
| Explicación técnica | SCHEDULER_EXPLICACION.md |
| Código fuente | src/controller/TomaService.java |
| Tests | test/TestTomaService.java |

### Base de Datos
| Qué | Dónde |
|-----|-------|
| Esquema SQL | db/schema.sql |
| Conexión | src/infra/db/ConexionDB.java |
| DAOs | src/infra/dao/impl/*.java |
| Tests | test/TestDAOs.java |

### Interfaz de Usuario
| Qué | Dónde |
|-----|-------|
| CLI | src/ui/CLI.java |
| Menús | src/ui/menu/*.java |
| Guía de uso | GUIA_USO_CLI.md |

---

## 📊 Métricas del Proyecto

| Métrica | Valor |
|---------|-------|
| **Líneas de código** | ~4,350+ |
| **Clases Java** | 35+ |
| **Servicios** | 4 |
| **DAOs** | 7 |
| **Tests** | 3 suites |
| **Tablas BD** | 9 |
| **Patrones** | 5 |

---

## ✅ Estado por Capa

| Capa | Estado | Documentación |
|------|--------|---------------|
| **Base de Datos** | ✅ Completo | ARQUITECTURA.md |
| **Domain** | ✅ Completo | PROJECT_CONTEXT.md |
| **DAO** | ✅ Completo | - |
| **Servicios** | ✅ Completo | **SERVICIOS_COMPLETADO.md** ⭐ |
| **Controller** | ✅ Completo | PASO_SCHEDULER_COMPLETADO.md |
| **UI (CLI)** | ✅ Completo | PASO3_CLI_COMPLETADO.md |
| **UI (GUI)** | ⏳ Pendiente | ROADMAP.md |

---

## 🎯 Próximo Paso Sugerido

**Interfaz Gráfica con Swing**

Ver detalles en: **ROADMAP.md** sección "Opción A"

---

## ⭐ Documentos Nuevos Hoy (11/nov/2025)

1. **ESTADO_ACTUAL.md** - Resumen ejecutivo
2. **SERVICIOS_COMPLETADO.md** - Documentación de servicios
3. **SESION_SERVICIOS.md** - Resumen de sesión
4. **ROADMAP.md** - Actualizado con nuevos pasos
5. **test_servicios.bat** - Script de test
6. **src/service/*.java** - 4 servicios nuevos (850 líneas)
7. **test/TestServicios.java** - Test de integración

---

## 💡 Tips Rápidos

- **¿Perdiste el contexto?** → ESTADO_ACTUAL.md
- **¿Qué se hizo hoy?** → SESION_SERVICIOS.md  
- **¿Qué sigue?** → ROADMAP.md
- **¿Cómo funciona X?** → Busca en esta tabla

---

*Índice actualizado: 2025-11-11*  
*⭐ = Nuevo o actualizado hoy*

