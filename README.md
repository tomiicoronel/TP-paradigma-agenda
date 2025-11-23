# 🏥 Agenda Accesible - Sistema de Recordatorios de Medicación

Sistema de escritorio para gestión de recordatorios de medicación dirigido a pacientes con problemas de memoria (Alzheimer, demencia, etc.).

## 📋 Características Principales

✅ **Gestión completa de datos:**
- Pacientes (con cuidador asignado)
- Cuidadores (con información de contacto)
- Medicamentos (vía, dosis, notas)

✅ **Sistema de pautas de medicación:**
- Asignación de medicamentos a pacientes
- Configuración de horarios y frecuencias
- Generación automática de recordatorios

✅ **Notificaciones automáticas:**
- Scheduler que verifica recordatorios cada 60 segundos
- Alertas en tiempo real
- Registro de adherencia

✅ **Interfaz accesible:**
- GUI con Swing (letra grande, colores claros)
- Navegación por pestañas intuitiva
- Validaciones de formularios

✅ **Persistencia de datos:**
- Base de datos H2 embebida
- Datos persistentes entre ejecuciones

## 🏗️ Arquitectura

### Patrones Implementados

- **MVC (Model-View-Controller):** Separación clara entre UI, lógica y datos
- **DAO (Data Access Object):** Abstracción del acceso a datos
- **Observer Pattern:** Notificaciones automáticas de recordatorios
- **Service Layer:** Lógica de negocio centralizada
- **Singleton:** Gestión de conexión a BD

### Estructura de Capas

```
UI Layer (Swing)
    ↓
Service Layer (Business Logic)
    ↓
DAO Layer (Data Access)
    ↓
Database (H2)
```

## 🚀 Cómo Ejecutar

### Opción 1: IntelliJ IDEA (Recomendado)

1. Abre el proyecto en IntelliJ
2. Navega a `src/app/Main.java`
3. Click derecho → "Run 'Main.main()'"
4. ¡Listo! La GUI se abrirá automáticamente

### Opción 2: Scripts de Compilación Simple (Sin Maven)

**Windows:**

```batch
# Compilar
compile_simple.bat

# Ejecutar GUI
run_simple.bat

# Ejecutar CLI
run_simple_cli.bat
```

### Opción 3: Maven

```bash
# Compilar
mvn clean compile

# Ejecutar GUI
mvn exec:java -Dexec.mainClass="app.Main"

# Ejecutar CLI
mvn exec:java -Dexec.mainClass="app.Main" -Dexec.args="--cli"
```

## 📖 Guía de Uso

### 1. Registrar un Cuidador

1. Ir a pestaña **"Gestión"**
2. Click en **"Nuevo Cuidador"**
3. Llenar:
   - Nombre: "María García"
   - Teléfono: "555-1234"
   - Relación: "Hija"
4. **Guardar**

### 2. Registrar un Paciente

1. Click en **"Nuevo Paciente"**
2. Llenar:
   - Nombre: "Juan Pérez"
   - Fecha Nacimiento: "15/03/1950" (formato dd/MM/yyyy)
   - Diagnóstico: "Alzheimer leve"
   - Preferencias: "Letra grande, sonido activado"
   - Cuidador: Seleccionar del combo
3. **Guardar**
4. ✅ La edad se calcula automáticamente

### 3. Registrar un Medicamento

1. Click en **"Nuevo Medicamento"**
2. Llenar:
   - Nombre: "Donepezilo"
   - Vía: "oral"
   - Unidad: "mg"
   - Notas: "Tomar con comida"
3. **Guardar**

### 4. Crear Pauta de Medicación

1. Ir a pestaña **"Medicación"**
2. Seleccionar **Paciente** y **Medicamento**
3. Configurar:
   - Dosis: "10"
   - Unidad: "mg"
   - Intervalo: "24" (horas)
   - Hora inicio: "09:00"
4. **Guardar**
5. ✅ Se crean recordatorios automáticos

### 5. Ver Recordatorios

1. Ir a pestaña **"Hoy"**
2. Ver lista de recordatorios del día
3. Click en **"Marcar como tomado"** cuando corresponda

### 6. Consultar Historial

1. Ir a pestaña **"Historial"**
2. Seleccionar paciente
3. Ver estadísticas de adherencia

## 🛠️ Tecnologías

- **Java SE 11+** - Lenguaje principal
- **Swing** - GUI
- **JDBC** - Acceso a datos
- **H2 Database** - Base de datos embebida
- **Maven** - Gestión de dependencias

## 📁 Estructura del Proyecto

```
src/
├── app/Main.java                    # Punto de entrada
├── controller/TomaService.java      # Scheduler de notificaciones
├── domain/                          # Entidades
├── infra/dao/                       # Capa de datos
├── service/                         # Lógica de negocio
├── shared/observer/                 # Patrón Observer
└── ui/                              # Interfaz gráfica
    ├── MainFrame.java               # Ventana principal
    ├── forms/                       # Formularios
    │   ├── FormPaciente.java
    │   ├── FormCuidador.java
    │   └── FormMedicamento.java
    └── panels/                      # Paneles
        ├── PanelGestion.java
        ├── PanelHoy.java
        ├── PanelMedicacion.java
        └── PanelHistorial.java
```

## 🎓 Conceptos Aplicados

### Programación Orientada a Objetos
- ✅ Encapsulación
- ✅ Herencia
- ✅ Polimorfismo
- ✅ Abstracción

### Principios SOLID
- ✅ Single Responsibility
- ✅ Open/Closed
- ✅ Liskov Substitution
- ✅ Interface Segregation
- ✅ Dependency Inversion

### Paradigmas
- ✅ Orientado a Objetos
- ✅ Event-Driven
- ✅ Declarativo (SQL)

## 🧪 Testing

El proyecto incluye tests para verificar:

- **TestConexionSimple:** Conexión a BD
- **TestDAOs:** Operaciones CRUD
- **TestServicios:** Lógica de negocio
- **TestTomaService:** Scheduler de notificaciones

## 🐛 Solución de Problemas

### La aplicación no compila

**Solución:**
1. Verifica que `lib/h2-2.4.240.jar` exista
2. Si usas Maven: `mvn clean compile`
3. Si no: usa `compile_simple.bat`

### No se abre la GUI

**Solución:**
1. Verifica que ejecutes `app.Main` (sin args)
2. No uses `--cli` en los argumentos
3. Intenta desde IntelliJ: Run Main.main()

### Error "Table not found"

**Solución:**
1. Elimina `data/db.mv.db`
2. Reinicia la aplicación
3. El esquema se recreará automáticamente

### No aparecen datos en las listas

**Solución:**
Primero crea datos usando los formularios (Nuevo Paciente, Nuevo Medicamento, etc.)

## 📊 Base de Datos

La base de datos H2 se guarda en: `data/db.mv.db`

**Tablas:**
- `pacientes`
- `cuidadores`
- `medicamentos`
- `paciente_medicamento` (pautas)
- `recordatorios`
- `adherencia`
- `notificaciones`

Para resetear la BD, simplemente elimina `data/db.mv.db` y reinicia.

## 🎯 Características Destacadas

### 1. Scheduler Automático
El `TomaService` corre en background y verifica recordatorios cada 60 segundos, notificando automáticamente a la UI.

### 2. Validaciones Robustas
Todos los formularios tienen validaciones:
- Campos obligatorios
- Formatos de fecha
- Mensajes claros de error

### 3. Arquitectura Escalable
- Fácil agregar nuevas entidades
- DAOs intercambiables
- Servicios reutilizables

### 4. Accesibilidad
- Colores claros y contrastados
- Letra legible
- Navegación intuitiva
- Mensajes descriptivos

## 📝 Licencia

Proyecto académico - Universidad [Nombre] - 2025

## 👥 Autor

[Tu Nombre] - Paradigmas de Programación

---

## 🚀 Inicio Rápido (5 minutos)

```batch
# 1. Compilar (si no usas IntelliJ)
compile_simple.bat

# 2. Ejecutar
run_simple.bat

# 3. Usar la aplicación:
#    - Pestaña "Gestión" → Crear pacientes, cuidadores, medicamentos
#    - Pestaña "Medicación" → Asignar pautas
#    - Pestaña "Hoy" → Ver recordatorios
```

**¡Listo para usar!** 🎉

---

## 📚 Documentación Adicional

- **RESUMEN_ENTREGA.md** - Resumen ejecutivo completo
- **GUIA_EJECUCION_RAPIDA.md** - Guía de ejecución detallada
- **ESTADO_ACTUAL_GUI.md** - Estado de implementación
- **ARQUITECTURA.md** - Detalles de arquitectura

---

**Última actualización:** 2025-11-23
**Estado:** ✅ COMPLETO Y FUNCIONAL

