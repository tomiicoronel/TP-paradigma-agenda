# 🗺️ Roadmap del Proyecto - Próximos Pasos

## ✅ Completado hasta ahora

- [x] Base de datos H2 con esquema completo
- [x] Capa DAO para todas las entidades
- [x] **TomaService - Scheduler de notificaciones**
- [x] Patrón Observer implementado
- [x] CLI básica funcionando
- [x] Shutdown hook para cierre limpio
- [x] **🆕 Capa de Servicios (Business Logic)** ← NUEVO
  - [x] PacienteService
  - [x] CuidadorService
  - [x] MedicamentoService
  - [x] RecordatorioService
- [x] Tests de integración de servicios
- [x] Programación automática de tomas recurrentes

---

## 🎯 Estado Actual

**La aplicación está funcionalmente completa** con:
- ✅ Gestión de pacientes y cuidadores
- ✅ Gestión de medicamentos
- ✅ Creación de pautas de medicación
- ✅ Recordatorios automáticos programados
- ✅ Scheduler que verifica y notifica cada 60 segundos
- ✅ Registro de adherencia (tomas hechas/aplazadas/canceladas)
- ✅ Programación automática de siguiente toma
- ✅ CLI completamente funcional

**Ver:** `SERVICIOS_COMPLETADO.md` para documentación detallada.

---

## 📋 Próximos Pasos Sugeridos

### Opción A: 🎨 Interfaz Gráfica (Swing/JavaFX)

#### Objetivo:
Mejorar la experiencia de usuario con una GUI moderna y accesible.

**Componentes a desarrollar:**
1. **Dashboard principal** [4-6 horas]
   - Vista de recordatorios de hoy
   - Próximas tomas programadas
   - Estadísticas de adherencia

2. **Ventanas de notificación** [2-3 horas]
   - Popup cuando llega hora de toma
   - Botones: "Tomar ahora", "Aplazar", "Cancelar"
   - Sonido configurable

3. **Formularios CRUD** [6-8 horas]
   - Alta/edición de pacientes
   - Alta/edición de medicamentos
   - Configuración de pautas

4. **Gráficos de adherencia** [3-4 horas]
   - Chart de cumplimiento mensual
   - Historial visual por medicamento

**Total estimado:** 15-21 horas

---

### Opción B: 📊 Reportes y Análisis

#### Objetivo:
Proveer información valiosa sobre adherencia al tratamiento.

**Funcionalidades:**
1. **Reporte de adherencia mensual** [3-4 horas]
   - % cumplimiento por paciente
   - % cumplimiento por medicamento
   - Tomas perdidas vs completadas

2. **Exportar a PDF** [4-5 horas]
   - Usar iText o similar
   - Generar reporte imprimible
   - Incluir gráficos

3. **Alertas al cuidador** [3-4 horas]
   - Email cuando se pierde una toma
   - Resumen semanal automático
   - Usar JavaMail API

**Total estimado:** 10-13 horas

---

### Opción C: 🔧 Mejoras Técnicas

#### Objetivo:
Profesionalizar el código y facilitar mantenimiento.

**Mejoras:**
1. **Logging profesional** [2-3 horas]
   - Integrar SLF4J + Logback
   - Logs rotables por día
   - Niveles: DEBUG, INFO, WARN, ERROR

2. **Tests unitarios** [6-8 horas]
   - JUnit 5
   - Mockito para DAOs
   - Cobertura > 80%

3. **Configuración externa** [2-3 horas]
   - `application.properties`
   - Configurar intervalos del scheduler
   - Configurar preferencias de notificación

4. **JavaDoc completo** [3-4 horas]
   - Documentar todos los métodos públicos
   - Generar HTML con `mvn javadoc:javadoc`

**Total estimado:** 13-18 horas

---

### Opción D: 🚀 Funcionalidades Adicionales

1. **Recordatorios de rutinas** [4-5 horas]
   - No solo medicamentos
   - Ejercicios, citas médicas, etc.

2. **Multi-usuario** [6-8 horas]
   - Login/logout
   - Roles: paciente, cuidador, médico

3. **API REST** [8-10 horas]
   - Spring Boot
   - App móvil en el futuro

---

## 🎓 Recomendación del Mentor

### Para un proyecto académico sólido:
**Ir con Opción A (GUI) + un poco de Opción C (Tests + Logging)**

**Justificación:**
1. **Demuestra conocimiento completo del stack:**
   - Backend ✅ (ya está)
   - Frontend 🔄 (GUI con Swing)
   - Testing 🔄 (JUnit)

2. **Impacto visual:**
   - Una GUI bien hecha impresiona en presentaciones
   - Muestra preocupación por UX/Accesibilidad

3. **Tiempo realista:**
   - GUI básica: 1 semana
   - GUI completa: 2-3 semanas
   - Tests básicos: 3-4 días

### Prioridad sugerida:
1. **Dashboard principal con tabla de recordatorios** (empezar aquí)
2. **Popup de notificaciones** (crítico para demo)
3. **Formulario de creación de pautas** (core functionality)
4. **Tests unitarios de servicios** (calidad)
5. **Logging** (profesionalismo)

---

**Implementación:**
- `JOptionPane` con icono y sonido
- `SystemTray` notification (bandeja del sistema)
- Integrar con `TomaService.notifyObservers()`

**Archivo:** `ui/NotificationDialog.java`

---

#### 5. **Configurar ventanas de tiempo** [2-3 horas]
**Problema que resuelve:** Cada paciente tiene necesidades diferentes.

**Implementación:**
- UI para configurar `ventanaMin` por medicamento
- UI para configurar `MINUTOS_RECUPERACION` global
- Guardar en `paciente.preferencias_accesibilidad` (JSON)

**Archivo:** `ui/menu/MenuConfiguracion.java`

---

#### 6. **Reportes de adherencia** [3-4 horas]
**Problema que resuelve:** Cuidador necesita saber si paciente cumple tratamiento.

**Implementación:**
- Consultar tabla `adherencia` por rango de fechas
- Calcular % de adherencia
- Mostrar gráfico ASCII o simple tabla

**Archivo:** `ui/menu/MenuReportes.java`

---

### 🟢 BAJA PRIORIDAD (Features avanzadas)

#### 7. **Exportar reportes PDF** [6-8 horas]
**Problema que resuelve:** Compartir con médico.

**Dependencia:** iText o Apache PDFBox

---

#### 8. **Multi-paciente con selección** [4-6 horas]
**Problema que resuelve:** Un cuidador gestiona varios pacientes.

**Implementación:**
- Menú de selección al inicio
- Filtrar recordatorios por `paciente_id`

---

#### 9. **API REST (Spring Boot)** [12-16 horas]
**Problema que resuelve:** Acceso remoto, app móvil.

**Stack:** Migrar a Spring Boot + Spring Data JPA

---

## 🎓 Recomendación de aprendizaje

### Para ser un ingeniero senior en este proyecto:

#### Semana 1: Fundamentos
- [ ] Leer `ARQUITECTURA.md` completo
- [ ] Leer `SCHEDULER_EXPLICACION.md` completo
- [ ] Ejecutar `TestTomaService` y entender cada log
- [ ] Dibujar el flujo en papel (ayuda a internalizar)

#### Semana 2: Implementación
- [ ] Implementar paso 1: CLI como Observer
- [ ] Implementar paso 2: Marcar como HECHO
- [ ] Implementar paso 3: Dashboard de hoy

#### Semana 3: Refinamiento
- [ ] Testing: escribir JUnit tests
- [ ] Refactoring: aplicar SOLID donde veas oportunidades
- [ ] Documentación: comentar código complejo

#### Semana 4: Features avanzadas
- [ ] Notificaciones Swing
- [ ] Reportes de adherencia
- [ ] Configuración de ventanas

---

## 🛠️ Herramientas para escalar como ingeniero

### Testing
```bash
# Agregar JUnit 5
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>
```

### Logging
```bash
# Reemplazar printStackTrace() con SLF4J
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>2.0.9</version>
</dependency>
```

### Code Quality
- SonarLint (plugin IDE)
- Checkstyle
- SpotBugs

---

## 📖 Recursos de estudio

### Patrones de diseño:
- "Head First Design Patterns" (libro)
- Refactoring Guru: https://refactoring.guru
- Gang of Four patterns aplicados a Java

### Concurrencia en Java:
- "Java Concurrency in Practice" (libro)
- Documentación oficial de ExecutorService
- Baeldung tutorials sobre threading

### Arquitectura:
- Clean Architecture (Robert C. Martin)
- Domain-Driven Design (Eric Evans)
- Microservices Patterns (Chris Richardson)

---

## 🚀 Criterios de "listo para producción"

### Checklist mínimo:
- [ ] Todos los DAOs tienen manejo de errores robusto
- [ ] Logging en lugar de printStackTrace()
- [ ] Tests unitarios de cobertura > 70%
- [ ] Tests de integración para flujos críticos
- [ ] Documentación de API (JavaDoc completo)
- [ ] Configuración externalizada (properties file)
- [ ] Validación de inputs del usuario
- [ ] Manejo de concurrencia en TomaService
- [ ] Backup automático de BD
- [ ] Recovery de shutdown inesperado

---

## 💡 Próxima conversación sugerida

### Opción A: Implementar Observer en CLI
```
"Quiero que la CLI se actualice automáticamente cuando haya 
notificaciones. Implementemos el patrón Observer en CLI.java"
```

### Opción B: Completar flujo de medicación
```
"Quiero implementar el flujo completo: usuario marca HECHO, 
se registra en adherencia, y se crea automáticamente el próximo 
recordatorio. Guíame paso a paso."
```

### Opción C: Dashboard visual
```
"Quiero crear un menú 'Agenda de Hoy' que muestre todos los 
recordatorios del día en orden cronológico con sus estados."
```

### Opción D: Testing
```
"Quiero escribir tests unitarios para TomaService. Enséñame 
cómo testear código con ScheduledExecutorService."
```

---

## ✨ Reflexión final

**Lo que ya lograste:**
- Sistema de notificaciones profesional
- Arquitectura limpia y extensible
- Fundamentos sólidos de patrones de diseño

**Lo que falta:**
- Conectar todas las piezas
- Pulir la experiencia de usuario
- Testing y robustez

**Estás en el 40% del proyecto funcional.** El scheduler es el corazón, 
ahora solo falta conectar las extremidades (UI completa) y el sistema 
nervioso (notificaciones visuales).

¡Excelente progreso! 🎉

