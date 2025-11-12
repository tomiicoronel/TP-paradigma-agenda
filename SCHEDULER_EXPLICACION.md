# TomaService - Scheduler de Notificaciones

## 📋 ¿Qué es y por qué existe?

El **TomaService** es el **corazón del sistema de recordatorios**. Es un servicio que corre en segundo plano monitoreando constantemente los recordatorios para:

1. **Emitir notificaciones** cuando se acerca la hora de una toma
2. **Actualizar estados** automáticamente (PENDIENTE → APLAZADO → PERDIDO)
3. **Registrar adherencia** de cada cambio de estado
4. **Notificar a la UI** cuando hay cambios relevantes

---

## 🏗️ Arquitectura

### Componentes principales:

```
TomaService (Subject)
    ├── ScheduledExecutorService → ejecuta verificaciones cada 60 seg
    ├── RecordatorioDAO → consulta/actualiza recordatorios
    ├── NotificacionDAO → crea notificaciones
    ├── AdherenciaDAO → registra histórico
    └── List<Observer> → notifica a UI cuando hay cambios
```

### Patrón Observer implementado:

- **Subject:** `TomaService`
- **Observers:** Cualquier componente de UI que se suscriba (CLI, Swing, etc.)
- **Evento:** Cambios en recordatorios (notificaciones, cambios de estado)

---

## ⚙️ Funcionamiento

### Ciclo de verificación (cada 60 segundos):

```
┌─────────────────────────────────────────┐
│  verificarRecordatorios()               │
│  ┌───────────────────────────────────┐  │
│  │ 1. procesarPendientes()           │  │
│  │    - ¿Llegó hora de notificar?    │  │
│  │      → emitirNotificacion()       │  │
│  │    - ¿Expiró ventana?             │  │
│  │      → pasarAAplazado()           │  │
│  └───────────────────────────────────┘  │
│  ┌───────────────────────────────────┐  │
│  │ 2. procesarAplazados()            │  │
│  │    - ¿Superó tiempo recuperación? │  │
│  │      → pasarAPerdido()            │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
```

### Estados de un recordatorio:

```
PENDIENTE
    │
    ├─> (llega hora - 5 min) → Emite NOTIFICACION
    │
    └─> (expira ventana) → APLAZADO
                              │
                              └─> (expira recuperación) → PERDIDO

PENDIENTE/APLAZADO
    │
    └─> (usuario marca HECHO) → HECHO
```

---

## 🔍 Detalles técnicos

### ScheduledExecutorService vs Timer

❌ **Timer:**
- No es thread-safe
- Si una tarea lanza excepción, cancela todas las futuras
- API antigua (Java 1.3)

✅ **ScheduledExecutorService:**
- Thread-safe
- Maneja excepciones sin afectar tareas futuras
- Permite concurrencia controlada
- API moderna (Java 5+)

### Configuración de ventanas:

```java
MINUTOS_ANTES_NOTIFICAR = 5   // Notifica 5 min antes
MINUTOS_RECUPERACION = 60     // 1 hora para recuperar aplazados
INTERVALO_VERIFICACION_SEG = 60 // Verifica cada minuto
```

**¿Por qué cada minuto y no cada segundo?**

- **Eficiencia:** No necesitamos precisión de segundos para medicaciones
- **Recursos:** Reduce carga en BD y CPU
- **Realismo:** En el mundo real, ±1 minuto es aceptable

---

## 💡 Casos de uso

### Caso 1: Toma programada en 10 minutos

```
T=0:  Recordatorio creado, estado=PENDIENTE, programado_at=10:00
T=5:  TomaService detecta: ahora=09:55, programado=10:00
      → Emite NOTIFICACION (5 min antes)
      → Registra adherencia: NOTIFICADO
      → notifyObservers() → UI muestra alerta
```

### Caso 2: Usuario no tomó medicación

```
T=0:  Recordatorio PENDIENTE, programado_at=10:00, ventana=15min
T=10: Notificación emitida
T=16: TomaService detecta: ahora=10:16, límite=10:15
      → Cambia estado a APLAZADO
      → Registra adherencia: APLAZADO
      → notifyObservers() → UI actualiza
```

### Caso 3: Toma definitivamente perdida

```
T=0:  Recordatorio APLAZADO desde hace 70 min
      programado_at=09:00, ahora=10:10
T=1:  TomaService detecta: retraso > 60 min
      → Cambia estado a PERDIDO
      → Registra adherencia: PERDIDO
      → notifyObservers() → UI marca como perdido
```

---

## 🧩 Integración con otros componentes

### Main.java:

```java
// Iniciar servicio al arrancar app
tomaService = new TomaService();
tomaService.start();

// Detener limpiamente al cerrar
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    tomaService.stop();
}));
```

### CLI/UI:

```java
// Suscribirse a cambios
Main.getTomaService().addObserver(new Observer() {
    @Override
    public void update() {
        // Actualizar interfaz
        mostrarRecordatoriosPendientes();
    }
});
```

### Marcar toma como HECHA:

```java
// Cuando usuario confirma toma
recordatorioDAO.marcarHecho(recordatorioId);

// Crear próximo recordatorio
PacienteMedicamento pm = pmDAO.findById(pacienteId, medicamentoId);
pm.setProximaTomaAt(ahora.plusMinutes(pm.getIntervaloMin()));
pmDAO.update(pm);
```

---

## 🚀 Mejoras futuras

### 1. Notificaciones con prioridad:
```java
enum Prioridad { BAJA, MEDIA, ALTA, CRITICA }
```

### 2. Estrategia de notificación configurable:
```java
interface NotificationStrategy {
    void notify(Recordatorio r);
}

class BeepStrategy implements NotificationStrategy { ... }
class EmailStrategy implements NotificationStrategy { ... }
```

### 3. Persistencia del scheduler:
- Guardar estado del scheduler en BD
- Reanudar verificaciones tras reinicio

### 4. Métricas y monitoreo:
```java
class TomaServiceMetrics {
    int notificacionesEmitidas;
    int recordatoriosAplazados;
    int recordatoriosPerdidos;
}
```

---

## 📊 Diagrama de flujo completo

```
┌─────────────────────────────────────────────────┐
│             ScheduledExecutorService            │
│   scheduleAtFixedRate(task, 0, 60, SECONDS)     │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
        ┌─────────────────────────┐
        │ verificarRecordatorios()│
        └────────────┬────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
        ▼                         ▼
┌──────────────────┐    ┌──────────────────┐
│procesarPendientes│    │procesarAplazados │
└────────┬─────────┘    └────────┬─────────┘
         │                       │
    ┌────┴─────┐            ┌───┴────┐
    │          │            │        │
    ▼          ▼            ▼        ▼
emitir    pasarA      pasarA    [nada]
Notif     Aplazado    Perdido
    │          │            │
    └──────────┴────────────┘
               │
               ▼
       registrarAdherencia()
               │
               ▼
        notifyObservers()
               │
               ▼
           UI Update
```

---

## 🎯 Principios aplicados

### 1. **Separation of Concerns**
- TomaService solo se encarga de monitoreo
- DAOs manejan persistencia
- UI maneja presentación

### 2. **Observer Pattern**
- Desacoplamiento entre lógica y UI
- Múltiples observers pueden suscribirse

### 3. **Single Responsibility**
- Cada método tiene una responsabilidad clara
- Fácil testear y mantener

### 4. **Open/Closed Principle**
- Abierto para extensión (nuevos observers, estrategias)
- Cerrado para modificación (core logic estable)

---

## 📝 Testing

### Unit Tests:
```java
@Test
void testPasarAAplazado() {
    Recordatorio r = crearRecordatorioVencido();
    tomaService.procesarPendientes(LocalDateTime.now());
    assertEquals("APLAZADO", r.getEstado());
}
```

### Integration Tests:
```java
@Test
void testCicloCompleto() {
    // Crear recordatorio
    // Esperar notificación
    // Verificar adherencia registrada
}
```

### Manual Testing:
- Ejecutar `TestTomaService.java`
- Observar logs en consola
- Verificar cambios en BD

---

## ✅ Checklist de implementación

- [x] TomaService creado con ScheduledExecutorService
- [x] Implementado patrón Observer
- [x] Métodos de procesamiento de estados
- [x] Registro de adherencia
- [x] Integración con Main.java
- [x] Shutdown hook para cierre limpio
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] UI conectada como Observer
- [ ] Configuración de ventanas desde UI

---

## 🔗 Referencias

- [ScheduledExecutorService JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html)
- [Observer Pattern - Gang of Four](https://refactoring.guru/design-patterns/observer)
- [Concurrency in Java](https://docs.oracle.com/javase/tutorial/essential/concurrency/)

