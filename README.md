# Agenda Accesible – Java puro (Swing + JDBC/H2)

Proyecto de agenda accesible con recordatorios inteligentes para pacientes con dificultades de memoria.
Stack: **Java SE**, **Swing**, **JDBC**, **H2 embebido**. Patrones: **MVC**, **DAO**, **Observer**.

## ✨ Estado del proyecto

✅ **FUNCIONAL** - Scheduler de notificaciones implementado y funcionando

### Componentes completados:
- ✅ Base de datos H2 con esquema completo
- ✅ Capa DAO (Paciente, Medicamento, Recordatorio, etc.)
- ✅ **TomaService - Scheduler de notificaciones automáticas**
- ✅ CLI básica para gestión de datos
- ✅ Patrón Observer implementado
- 🔄 UI Swing (en progreso)

## Estructura
```
/src
  /app              - Main.java (punto de entrada)
  /controller       - TomaService (scheduler) ✨ NUEVO
  /ui               - CLI + MainFrame (Swing)
  /domain           - Entidades del modelo
  /infra/db         - Conexión y verificación BD
  /infra/dao        - Interfaces y implementaciones DAO
  /shared/observer  - Patrón Observer
  /test             - Tests de demostración
/db
  schema.sql        - DDL de todas las tablas
```

## Requisitos
- **Java 17/21**
- **H2 Database** (incluido en /lib)
- **Maven** (opcional, ya configurado)

## 🚀 Cómo ejecutar

### Opción 1: Ejecución normal (CLI)
```bash
.\compile.bat
.\run.bat
```
La app iniciará con:
- ✅ Base de datos H2 inicializada
- ✅ Scheduler corriendo en segundo plano
- ✅ CLI para gestionar pacientes, medicamentos y recordatorios

### Opción 2: Test del Scheduler
```bash
.\compile.bat
.\test_scheduler.bat
```
Este test demuestra:
- Creación de recordatorios de prueba
- Emisión automática de notificaciones
- Cambios de estado (PENDIENTE → APLAZADO → PERDIDO)

## 📚 Documentación

- **`PROJECT_CONTEXT.md`** - Contexto del negocio y arquitectura
- **`SCHEDULER_EXPLICACION.md`** - Explicación completa del scheduler ⭐
- **`PASO_SCHEDULER_COMPLETADO.md`** - Resumen de implementación
- **`GUIA_USO.md`** - Guía de uso de la aplicación
