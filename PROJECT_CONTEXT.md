# Proyecto: Agenda Accesible – Java puro

## 🧠 Idea general
Aplicación de **agenda accesible y recordatorios inteligentes** para personas con **problemas cognitivos o de memoria**.  
Permite **organizar medicaciones, rutinas y actividades**, con recordatorios automáticos y seguimiento de adherencia, mejorando la autonomía del paciente y facilitando el trabajo del cuidador.

---

## 💡 Problema que resuelve
Muchas personas con alteraciones de memoria olvidan:
- Tomar su medicación en horario.
- Asistir a turnos médicos o terapias.
- Realizar actividades importantes de su rutina diaria.

Esto genera **riesgos clínicos**, **estrés familiar** y pérdida de adherencia al tratamiento.

La app busca **automatizar los recordatorios** y simplificar la supervisión del cuidador, **sin depender de conexión a internet** ni de configuraciones complejas.

---

## 💼 Modelo de negocio
### Segmento principal
- **Pacientes con trastornos de memoria o dependencia parcial.**
- **Cuidadores familiares o profesionales.**

### Propuesta de valor
- Agenda visual, simple y accesible.
- Recordatorios automáticos (sonoros y visuales).
- Reprogramación inteligente si se omite una toma.
- Reportes de adherencia para cuidadores o profesionales.

### Futuro escalable
- Versión **B2B** (instituciones de salud).
- Integración con **API médicas** o apps de telemedicina.
- Suscripción premium para exportes y reportes avanzados.

---

## ⚙️ Implementación técnica
**Stack:**
- Lenguaje: **Java SE 21**
- UI: **Swing (desktop accesible)**
- Persistencia: **H2 embebido (JDBC)**
- Arquitectura: **MVC + DAO + Observer + Strategy/Factory opcional**

### Capas del sistema
1. **Presentación (UI):** Swing con pestañas “Hoy”, “Medicación”, “Rutina”, “Historial”.
2. **Aplicación (Controladores):** gestión de recordatorios, lógica de reprogramación, notificaciones.
3. **Dominio:** entidades con reglas (Paciente, Cuidador, Medicamento, Recordatorio, etc.).
4. **Persistencia:** DAOs JDBC sobre H2.

---

## 🧩 Entidades y relaciones

| Entidad | Descripción | Relaciones clave |
|----------|--------------|------------------|
| **Cuidador** | Persona responsable del paciente. | 1─N con Paciente |
| **Paciente** | Usuario principal de la app. | N─M con Medicamento, 1─1 con Rutina, N─1 con Cuidador |
| **Medicamento** | Medicación recetada. | N─M con Paciente |
| **PacienteMedicamento** | Pauta personalizada. | (paciente_id, medicamento_id) como PK |
| **Rutina** | Plan diario del paciente. | 1─N con ItemRutina |
| **ItemRutina** | Actividad (levantarse, ejercicio, medicación). | 1─N con Recordatorio |
| **Recordatorio** | Evento programado. | 1─N con Notificación, 1─N con Adherencia |
| **Notificación** | Alerta (visual/sonora). | FK recordatorio_id |
| **Adherencia** | Registro de acción del paciente. | FK recordatorio_id |

**Diagrama de texto:**
```
[Cuidador] 1 ── N [Paciente] N ── M [Medicamento]
   │                     │
   │                     └── 1:1 [Rutina] 1 ── N [ItemRutina] ── 1 ── N [Recordatorio]
   │                                                           │
   │                                                           ├── 1:N [Notificacion]
   │                                                           └── 1:N [Adherencia]
```

---

## 🔄 Lógica de funcionamiento

### Ciclo de vida de una toma
1. Se programa el **primer recordatorio** a la hora de inicio.  
2. Si no se confirma dentro de la **ventana de tolerancia**, el recordatorio pasa a **APLAZADO**.  
3. Si se supera la política de recuperación (ej. 1h), pasa a **PERDIDO**.  
4. Al marcar **HECHO**, se actualiza **proxima_toma_at = hora_real + intervalo_min**.  
5. Todos los cambios se registran en **Adherencia**.

### Máquina de estados
```
PENDIENTE → (expira ventana) → APLAZADO → (expira recuperación) → PERDIDO
PENDIENTE/APLAZADO → (marcar HECHO) → HECHO
```

---

## 📋 Requisitos funcionales
- Registrar pacientes, cuidadores y medicaciones.
- Configurar rutinas diarias.
- Generar y mostrar recordatorios automáticos.
- Detectar atrasos y marcar “Aplazado” o “Perdido”.
- Reprogramar automáticamente la próxima toma.
- Registrar adherencia.
- Permitir visualización accesible de recordatorios e historial.

---

## ⚙️ Requisitos no funcionales
- **Accesibilidad:** alto contraste, tipografía grande, soporte teclado.
- **Desempeño:** respuesta < 1 s.
- **Autonomía:** funciona offline (H2 embebido).
- **Persistencia local:** ./data/db.
- **Mantenibilidad:** arquitectura en capas.
- **Seguridad:** sin datos externos; uso local.

---

## 🚀 Flujo general
1. El cuidador registra pacientes y medicaciones.
2. El sistema genera recordatorios automáticos.
3. El paciente recibe alertas.
4. Si no marca HECHO a tiempo, el sistema reprograma.
5. El cuidador consulta adherencia e historial.

---

## 🧩 Instrucciones para Copilot
Este documento provee contexto para Copilot sobre:
- Relaciones de entidades y reglas de negocio.
- Patrones de diseño aplicados.
- Requisitos funcionales y técnicos.
- Lógica de reprogramación y adherencia.
