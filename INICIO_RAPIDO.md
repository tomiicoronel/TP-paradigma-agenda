# 🎯 INICIO RÁPIDO - Agenda Accesible

**Última actualización:** 12 de noviembre de 2025

---

## ✅ Estado del Proyecto

El proyecto tiene **2 interfaces disponibles**:

1. **🖥️ Interfaz Gráfica (GUI)** - Ventana Swing profesional ⭐ **RECOMENDADO**
2. **⌨️ Interfaz de Línea de Comandos (CLI)** - Para administración y pruebas

---

## 🚀 Ejecución Rápida

### Opción 1: Interfaz Gráfica (Recomendado)

```cmd
run_gui.bat
```

Esto abrirá una ventana con:
- 📅 **Pestaña "Hoy"**: Ver y gestionar recordatorios del día
- 💊 **Pestaña "Medicación"**: (Próximamente) Gestión de medicamentos
- 📊 **Pestaña "Historial"**: (Próximamente) Reportes de adherencia

### Opción 2: Interfaz CLI

```cmd
run_cli.bat
```

Para configuración inicial, pruebas y administración avanzada.

---

## 🛠️ Compilación

Si necesitas recompilar el proyecto:

```cmd
compile.bat
```

Esto compilará todos los archivos Java en `target\classes\`.

---

## 📋 Primer Uso

### 1. Configurar datos iniciales (usar CLI)

```cmd
run_cli.bat
```

Luego en el menú:
1. **Gestionar Pacientes** → Crear un paciente de prueba
2. **Gestionar Medicamentos** → Registrar un medicamento
3. **Configurar Pautas** → Asignar medicamento a paciente con horarios

### 2. Usar la interfaz gráfica

```cmd
run_gui.bat
```

Verás:
- Los recordatorios programados en la pestaña "Hoy"
- Notificaciones automáticas cuando llega la hora
- Botones para marcar como "Hecho", "Aplazar" o "Cancelar"

---

## 🎨 Características de la GUI

### Panel "Hoy"
- ✅ **Tabla con recordatorios del día**
  - ID, Hora programada, Estado, Descripción, Ventana de tolerancia
- ✅ **Estadísticas en tiempo real**
  - Total, Hechos, Pendientes, Aplazados
- ✅ **Botones de acción:**
  - **Marcar como Hecho**: ✅ Registra que se tomó la medicación
  - **Aplazar**: ⏰ Pospone X minutos
  - **Cancelar**: ❌ Cancela el recordatorio
  - **Actualizar**: 🔄 Refresca manualmente

### Notificaciones Automáticas
- 🔔 **Popup cuando llega la hora** de un recordatorio
- Botones rápidos: Hecho / Aplazar / Cancelar
- Se cierra automáticamente al tomar acción

### Actualización Automática
- El **TomaService** verifica cada 60 segundos
- Cuando detecta cambios → **notifica a la UI** automáticamente
- Usa **patrón Observer** para mantener sincronización

---

## 🧩 Arquitectura Implementada

```
┌─────────────────────────────────┐
│      MainFrame (GUI)            │  ← Observer
│  ┌──────┬──────────┬─────────┐  │
│  │ Hoy  │ Medicac. │ Histor. │  │
│  └──────┴──────────┴─────────┘  │
└────────────┬────────────────────┘
             │ update()
             │
┌────────────┴────────────┐
│    TomaService          │  ← Subject (notifica cada 60s)
│  - verifica horarios    │
│  - emite notificaciones │
│  - cambia estados       │
└────────┬────────────────┘
         │
┌────────┴────────────┐
│ RecordatorioService │  ← Business Logic
│ - listar            │
│ - marcar hecho      │
│ - aplazar           │
│ - cancelar          │
└────────┬────────────┘
         │
┌────────┴─────────────┐
│  RecordatorioDAO     │  ← Data Access
└────────┬─────────────┘
         │
┌────────┴─────┐
│   H2 DB      │  ← Persistencia
└──────────────┘
```

---

## 📂 Estructura del Proyecto

```
src/
├── app/
│   └── Main.java              # Punto de entrada (GUI por defecto, --cli para CLI)
├── domain/                    # Entidades
├── infra/
│   ├── dao/                   # Acceso a datos
│   └── db/                    # Conexión y esquema
├── service/                   # Lógica de negocio
├── controller/
│   └── TomaService.java       # Scheduler automático
├── shared/observer/           # Patrón Observer
└── ui/
    ├── CLI.java               # Interfaz CLI
    ├── MainFrame.java         # Ventana principal GUI
    └── panels/
        ├── PanelHoy.java      # Vista de recordatorios del día
        ├── PanelMedicacion.java   # Placeholder
        └── PanelHistorial.java    # Placeholder
```

---

## 📚 Documentación Detallada

| Archivo | Descripción |
|---------|-------------|
| `GUI_IMPLEMENTACION.md` | Detalles de la implementación GUI |
| `SERVICIOS_COMPLETADO.md` | Documentación de la capa de servicios |
| `SCHEDULER_EXPLICACION.md` | Cómo funciona el TomaService |
| `GUIA_USO_CLI.md` | Manual de uso del CLI |
| `PROJECT_CONTEXT.md` | Contexto general del proyecto |

---

## 🎯 Próximos Pasos Recomendados

### Funcionalidades Core (siguientes)
1. **Implementar PanelMedicacion** con formularios CRUD
   - Alta de medicamentos desde GUI
   - Asignación a pacientes
   - Configuración de pautas

2. **Implementar PanelHistorial**
   - Gráficos de adherencia mensual
   - Reporte por medicamento
   - Estadísticas de cumplimiento

### Mejoras UX
1. **Sonido en notificaciones** (javax.sound)
2. **Colores en tabla** según estado (verde/amarillo/rojo)
3. **Iconos visuales** en botones y pestañas

### Mejoras Técnicas
1. **Tests unitarios de UI** (mockito + junit)
2. **Logging robusto** (SLF4J + Logback)
3. **Configuración externa** (properties file)

---

## ❓ FAQ - Preguntas Frecuentes

### ¿Puedo usar ambas interfaces al mismo tiempo?
No, solo una a la vez. Pero puedes cerrar una y abrir la otra sin perder datos.

### ¿Dónde se guardan los datos?
En `data/db.mv.db` (base de datos H2 embebida). Persisten entre ejecuciones.

### ¿Cómo creo recordatorios de prueba?
Usa el CLI para crear: paciente → medicamento → pauta. El TomaService creará automáticamente los recordatorios.

### ¿La GUI se actualiza sola?
Sí, cada 60 segundos el TomaService verifica cambios y notifica a la GUI.

### ¿Puedo cambiar el intervalo de verificación?
Sí, en `TomaService.java`, línea 52: `INTERVALO_VERIFICACION_SEG`

---

## 🐛 Solución de Problemas

### Problema: "No se encuentran las clases compiladas"
**Solución:** Ejecuta `compile.bat` primero.

### Problema: "Error al conectar a base de datos"
**Solución:** Verifica que `lib\h2-2.4.240.jar` exista.

### Problema: No aparecen recordatorios en GUI
**Solución:** 
1. Usa CLI para crear datos de prueba
2. Verifica que la pauta tenga horarios configurados
3. Revisa que la fecha sea hoy

### Problema: La ventana se cierra inmediatamente
**Solución:** Ejecuta desde cmd (no doble-click) para ver errores.

---

## 👨‍💻 Desarrollo

### Para agregar una nueva funcionalidad:

1. **Modelo (domain/)**: Crear/modificar entidad
2. **DAO (infra/dao/)**: Agregar métodos de acceso a datos
3. **Service (service/)**: Implementar lógica de negocio
4. **UI (ui/panels/)**: Crear panel Swing
5. **Actualizar MainFrame**: Agregar pestaña si es necesario

### Patrones de diseño usados:
- ✅ **MVC** (Model-View-Controller)
- ✅ **DAO** (Data Access Object)
- ✅ **Observer** (TomaService → MainFrame)
- ✅ **Service Layer** (separación de lógica de negocio)
- ✅ **Singleton** (ConexionDB con pool)

---

## 📄 Licencia

Proyecto académico - TP Paradigmas de Programación

---

## 📞 Soporte

Para consultas o problemas:
1. Revisa la documentación en `/docs`
2. Consulta los archivos `*_COMPLETADO.md`
3. Revisa el código comentado en cada clase

**¡Disfruta usando Agenda Accesible!** 🎉

