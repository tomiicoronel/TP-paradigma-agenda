# Paquete de Prompts para GitHub Copilot

> Copiá estos prompts como comentarios arriba de los archivos o usalos en Copilot Chat.

---
## 0) Contexto global (pegalo en README y en comentarios de archivos)
```
Contexto: App de agenda accesible para pacientes con problemas de memoria.
Stack: Java SE, Swing, JDBC, H2 embebido. Patrones: MVC + DAO + Observer (+ Strategy/Factory opcional).
Objetivo: Recordatorios de medicación y rutinas con estados PENDIENTE/APLAZADO/PERDIDO/HECHO y reprogramación de próxima toma.
```

---
## 1) DDL H2 – /db/schema.sql
```
Generá DDL para H2 con estas relaciones:
- Cuidador 1─N Paciente (paciente.cuidador_id FK)
- Paciente 1─1 Rutina; Rutina 1─N ItemRutina
- Paciente N─M Medicamento vía PacienteMedicamento (con: dosis, unidad, intervalo_min, ventana_min, hora_inicio, proxima_toma_at, activo boolean)
- Recordatorio(paciente_id, tipo{MEDICACION,ACTIVIDAD,TURN0}, referencia_tipo{PAC_MED|ITEM_RUTINA}, referencia_id, programado_at, ventana_min, estado{PENDIENTE,APLAZADO,PERDIDO,HECHO}, motivo_estado)
- Notificacion(recordatorio_id, emitida_at, canal_visual, canal_sonoro, entregada boolean)
- Adherencia(recordatorio_id, registrada_at, accion{HECHO,OMITIDO,POSPUESTO}, observaciones)
Incluí índices en FKs y búsquedas por paciente/fechas.
```

---
## 2) ConexionDB – /src/infra/db/ConexionDB.java
```
Escribí una clase ConexionDB con:
- getConnection(): Connection a jdbc:h2:./data/db;AUTO_SERVER=TRUE
- initSchemaIfAbsent(): ejecuta /db/schema.sql una sola vez (crear carpeta ./data si no existe)
- manejo de excepciones claro; logs mínimos.
```

---
## 3) Interfaces DAO – /src/infra/dao/*.java
```
Creá interfaces:
- PacienteDAO (crud + findByCuidador)
- CuidadorDAO (crud + findAll)
- MedicamentoDAO (crud + searchByNombre)
- PacienteMedicamentoDAO: findActivosByPaciente, updateProximaToma, marcarInactivo, savePauta, findByPacienteYMedicamento
- RecordatorioDAO: crearPendiente, pasarAplazado, pasarPerdido, marcarHecho, findPendientesByPacienteYFecha
- AdherenciaDAO: registrarAccion, findByPacienteRangoFechas
- NotificacionDAO: crear, marcarEntregada, findByRecordatorio
Solo firmas. Sin frameworks.
```

---
## 4) Implementación JDBC – /src/infra/dao/impl/*.java
```
Implementá los DAO anteriores con JDBC puro (PreparedStatement, try-with-resources).
Extraé mapRow(ResultSet) para evitar duplicaciones.
Manejá SQLExceptions como RuntimeException con mensajes claros.
```

---
## 5) Servicio de Toma – /src/controller/TomaService.java
```
Escribí un servicio con ScheduledExecutorService que:
- Recorre PacienteMedicamento activos.
- Si now >= proxima_toma_at - ventana_min y no hay recordatorio abierto -> crearPendiente
- Si now > proxima_toma_at + ventana_min y sigue pendiente -> pasarAplazado + notificación
- Si excede política de recuperación -> pasarPerdido
- Al marcarHecho(hora_real): proxima_toma_at = hora_real + intervalo_min; cerrar recordatorio; registrarAdherencia
Agregá método tick() para test manual y logs.
```

---
## 6) UI Swing – /src/ui/MainFrame.java
```
Ventana con pestañas: Hoy | Medicación | Rutina | Historial.
En 'Hoy': tabla de recordatorios con estado y botones 'Hecho' (Alt+H), 'Posponer' (Alt+P); tooltips accesibles.
Evitar layouts absolutos; usar BorderLayout/GridBagLayout; alto contraste y tamaño de fuente configurable.
```

---
## 7) Observer casero – /src/shared/observer/*
```
Definí Subject/Observer minimalistas para notificar a la UI cuando cambian recordatorios/adherencia.
```

---
## 8) Refactors y calidad
```
Pedir: "Refactorizá X eliminando duplicación, extraé mapRow, aplicá early return".
Pedir: "Revisá manejo de recursos/errores en DAO Y".
Pedir: "Generá casos de prueba manuales para máquina de estados PENDIENTE→APLAZADO→PERDIDO→HECHO".
```
