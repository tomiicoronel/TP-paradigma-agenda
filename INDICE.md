# 📚 Índice de Documentación del Proyecto

## 🎯 Inicio Rápido

### Si es tu primera vez aquí:
1. **[README.md](README.md)** - Vista general y cómo ejecutar
2. **[RESUMEN_SCHEDULER.md](RESUMEN_SCHEDULER.md)** - Qué se implementó hoy
3. **[Ejecutar la app](#cómo-ejecutar)** - Comandos directos

### Si quieres programar:
1. **[ROADMAP.md](ROADMAP.md)** - Próximos pasos y prioridades
2. **[PASO_SCHEDULER_COMPLETADO.md](PASO_SCHEDULER_COMPLETADO.md)** - Resumen de implementación

---

## 📖 Documentación por tema

### 🏗️ Arquitectura y diseño

| Documento | Qué contiene | Cuándo leerlo |
|-----------|--------------|---------------|
| **[ARQUITECTURA.md](ARQUITECTURA.md)** | Diagramas de capas, flujos, patrones | Cuando quieras entender TODO el sistema |
| **[PROJECT_CONTEXT.md](PROJECT_CONTEXT.md)** | Contexto del negocio, problema que resuelve | Al inicio, para entender el "por qué" |
| **[SCHEDULER_EXPLICACION.md](SCHEDULER_EXPLICACION.md)** | Explicación completa del TomaService | Para entender el scheduler en profundidad |

### 💻 Implementación técnica

| Documento | Qué contiene | Cuándo leerlo |
|-----------|--------------|---------------|
| **[PASO_SCHEDULER_COMPLETADO.md](PASO_SCHEDULER_COMPLETADO.md)** | Resumen de archivos creados/modificados | Después de implementar el scheduler |
| **[RESUMEN_SCHEDULER.md](RESUMEN_SCHEDULER.md)** | Resumen ejecutivo y próximos pasos | Para recordar qué se hizo y qué falta |
| **[ROADMAP.md](ROADMAP.md)** | Plan de desarrollo priorizado | Antes de empezar a programar cada día |

### 📚 Guías de uso

| Documento | Qué contiene | Cuándo leerlo |
|-----------|--------------|---------------|
| **[GUIA_USO.md](GUIA_USO.md)** | Cómo usar la aplicación | Para usuarios finales |
| **[GUIA_USO_CLI.md](GUIA_USO_CLI.md)** | Comandos de la interfaz CLI | Al usar la app en modo consola |
| **[README.md](README.md)** | Inicio rápido y configuración | Primera lectura obligatoria |

---

## 🗂️ Estructura de archivos del proyecto

```
📁 Raíz del proyecto
│
├── 📄 README.md                    ⭐ Inicio aquí
├── 📄 RESUMEN_SCHEDULER.md         ⭐ Resumen de hoy
├── 📄 ARQUITECTURA.md              📐 Diagramas técnicos
├── 📄 SCHEDULER_EXPLICACION.md     🎓 Aprende sobre scheduler
├── 📄 ROADMAP.md                   🗺️ Próximos pasos
├── 📄 PROJECT_CONTEXT.md           💼 Contexto del negocio
├── 📄 PASO_SCHEDULER_COMPLETADO.md ✅ Checklist de implementación
├── 📄 GUIA_USO.md                  📖 Guía de usuario
├── 📄 GUIA_USO_CLI.md              💻 Comandos CLI
│
├── 📁 src/
│   ├── 📁 app/
│   │   └── Main.java               🚀 Punto de entrada
│   ├── 📁 controller/
│   │   └── TomaService.java        ⚙️ Scheduler (NUEVO)
│   ├── 📁 domain/                  📦 Entidades
│   ├── 📁 infra/                   🗄️ Persistencia
│   ├── 📁 ui/                      🖥️ Interfaces
│   └── 📁 test/
│       ├── TestTomaService.java    🧪 Test scheduler (NUEVO)
│       └── TestDAOs.java
│
├── 📁 db/
│   └── schema.sql                  🗃️ DDL de BD
│
├── 📁 lib/
│   └── h2-2.4.240.jar              📚 Database driver
│
└── 📁 data/
    └── db.mv.db                    💾 Base de datos H2
```

---

## 🎯 Flujo de lectura recomendado

### Para entender el proyecto (2-3 horas):
```
1. README.md (5 min)
   ↓
2. PROJECT_CONTEXT.md (15 min)
   ↓
3. ARQUITECTURA.md (30 min)
   ↓
4. SCHEDULER_EXPLICACION.md (45 min)
   ↓
5. Ejecutar TestTomaService (10 min)
   ↓
6. Leer código de TomaService.java (30 min)
```

### Para empezar a programar (30 min):
```
1. RESUMEN_SCHEDULER.md (10 min)
   ↓
2. ROADMAP.md (10 min)
   ↓
3. Elegir tarea del roadmap (5 min)
   ↓
4. Implementar (∞)
```

### Para usuarios finales (15 min):
```
1. README.md (5 min)
   ↓
2. GUIA_USO.md (10 min)
   ↓
3. Ejecutar la app
```

---

## 🔍 Documentos por nivel de experiencia

### 👶 Principiante:
1. **README.md** - Qué es el proyecto
2. **GUIA_USO.md** - Cómo usarlo
3. **PROJECT_CONTEXT.md** - Por qué existe

### 🧑‍💻 Intermedio:
1. **RESUMEN_SCHEDULER.md** - Qué se implementó
2. **PASO_SCHEDULER_COMPLETADO.md** - Detalles técnicos
3. **ROADMAP.md** - Qué hacer después

### 🎓 Avanzado:
1. **ARQUITECTURA.md** - Diseño completo
2. **SCHEDULER_EXPLICACION.md** - Patrones y decisiones
3. Código fuente de `TomaService.java`

---

## 📋 Checklist de lectura

### Antes de empezar a programar:
- [ ] Leí README.md
- [ ] Entiendo el contexto (PROJECT_CONTEXT.md)
- [ ] Vi la arquitectura general (ARQUITECTURA.md)
- [ ] Ejecuté la app al menos una vez
- [ ] Leí el código de TomaService.java

### Antes de implementar cada feature:
- [ ] Consulté ROADMAP.md para prioridades
- [ ] Entiendo qué problema resuelvo
- [ ] Sé en qué archivos voy a trabajar
- [ ] Tengo claro el diseño/patrón a usar

### Después de implementar:
- [ ] Compilé sin errores
- [ ] Probé manualmente
- [ ] Actualicé documentación si es necesario
- [ ] Commiteé cambios con mensaje claro

---

## 🔗 Enlaces rápidos a secciones clave

### Conceptos técnicos:
- [¿Por qué ScheduledExecutorService?](SCHEDULER_EXPLICACION.md#scheduledexecutorservice-vs-timer)
- [Patrón Observer explicado](ARQUITECTURA.md#observer-pattern)
- [Flujo de estados de recordatorios](ARQUITECTURA.md#flujo-de-estados-de-un-recordatorio)
- [Modelo de datos](ARQUITECTURA.md#modelo-de-datos-relaciones-clave)

### Implementación:
- [Cómo crear recordatorios](ROADMAP.md#2-generar-recordatorios-automáticamente)
- [Conectar UI como Observer](ROADMAP.md#1-conectar-cli-como-observer)
- [Testing del scheduler](RESUMEN_SCHEDULER.md#cómo-probar)

---

## 🎓 Glosario de términos

| Término | Significado |
|---------|-------------|
| **TomaService** | Servicio que monitorea recordatorios periódicamente |
| **Scheduler** | Componente que ejecuta tareas en el tiempo |
| **Observer** | Patrón que permite notificar cambios a suscriptores |
| **DAO** | Data Access Object - abstrae acceso a BD |
| **Adherencia** | Cumplimiento del paciente con su tratamiento |
| **Ventana** | Tiempo de tolerancia después de hora programada |
| **PENDIENTE** | Recordatorio aún no ejecutado ni vencido |
| **APLAZADO** | Recordatorio que venció su ventana pero es recuperable |
| **PERDIDO** | Recordatorio definitivamente no cumplido |
| **HECHO** | Recordatorio confirmado por el usuario |

---

## 💡 Preguntas frecuentes

### ¿Por dónde empiezo?
**R:** Lee README.md y ejecuta `.\run.bat`

### ¿Qué documento explica mejor el scheduler?
**R:** SCHEDULER_EXPLICACION.md tiene explicación completa con ejemplos

### ¿Cómo sé qué implementar ahora?
**R:** Consulta ROADMAP.md, están priorizadas las tareas

### ¿Dónde veo el diseño completo?
**R:** ARQUITECTURA.md tiene todos los diagramas

### ¿Hay tests que pueda correr?
**R:** Sí, ejecuta `.\test_scheduler.bat`

---

## 🚀 Comandos frecuentes

```bash
# Compilar todo
.\compile.bat

# Ejecutar aplicación
.\run.bat

# Test del scheduler
.\test_scheduler.bat

# Ver estructura de BD (abrir H2 Console)
java -cp lib/h2-2.4.240.jar org.h2.tools.Console
# URL: jdbc:h2:./data/db
# User: sa
# Pass: (vacío)
```

---

## 📝 Notas de versión

### v0.4 - Scheduler implementado (10/11/2025)
- ✅ TomaService con ScheduledExecutorService
- ✅ Patrón Observer
- ✅ Documentación completa

### v0.3 - DAOs completados
- ✅ Todas las entidades con DAO

### v0.2 - Base de datos
- ✅ H2 configurado
- ✅ Schema completo

### v0.1 - Estructura inicial
- ✅ Proyecto base con Maven

---

**Última actualización:** 10 de noviembre de 2025  
**Mantenido por:** Desarrollador del proyecto  
**Estado:** 📚 Documentación completa y actualizada

