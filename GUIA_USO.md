# Agenda Accesible - Guía de Uso

## 🚀 Ejecutar la Aplicación

### Opción 1: Doble clic en `run.bat`
- Compila automáticamente si hay cambios
- Ejecuta la aplicación con interfaz gráfica

### Opción 2: Desde terminal
```cmd
java -cp "target/classes;lib/h2-2.4.240.jar" app.Main
```

---

## 🔧 Compilar el Proyecto

### Opción 1: Ejecutar `compile.bat`
- Limpia compilaciones anteriores
- Compila todo el proyecto desde cero

### Opción 2: Compilar manualmente
```cmd
javac -d target/classes -cp "lib/h2-2.4.240.jar" -sourcepath src src/app/Main.java
```

---

## ✅ Ejecutar Tests

### Opción 1: Ejecutar `test.bat`
- Ejecuta TestConexionSimple (CRUD básico)
- Ejecuta TestDAOs (tests completos de capa de datos)

### Opción 2: Ejecutar tests individualmente
```cmd
java -cp "target/classes;lib/h2-2.4.240.jar" test.TestConexionSimple
java -cp "target/classes;lib/h2-2.4.240.jar" test.TestDAOs
```

---

## 📁 Estructura del Proyecto

```
src/
├── app/
│   └── Main.java                    # Punto de entrada de la aplicación
├── domain/                          # Entidades de negocio
│   ├── Cuidador.java
│   ├── Paciente.java
│   ├── Medicamento.java
│   ├── PacienteMedicamento.java
│   ├── Recordatorio.java
│   ├── Notificacion.java
│   └── Adherencia.java
├── infra/
│   ├── dao/                         # Interfaces DAO
│   │   └── impl/                    # Implementaciones JDBC
│   │       ├── CuidadorDAOImpl.java
│   │       ├── PacienteDAOImpl.java
│   │       ├── MedicamentoDAOImpl.java
│   │       ├── PacienteMedicamentoDAOImpl.java
│   │       ├── RecordatorioDAOImpl.java
│   │       ├── NotificacionDAOImpl.java
│   │       └── AdherenciaDAOImpl.java
│   └── db/
│       ├── ConexionDB.java          # Gestión de conexión H2
│       └── VerificarDB.java         # Utilidad de verificación
├── ui/
│   └── MainFrame.java               # Interfaz gráfica principal
└── test/
    ├── TestConexionSimple.java      # Test de conexión básica
    └── TestDAOs.java                # Test de capa DAO

db/
└── schema.sql                       # Schema de base de datos

data/
└── db.mv.db                        # Base de datos H2 (auto-generada)
```

---

## 🗄️ Base de Datos

### Tablas creadas:
1. **cuidador** - Gestión de cuidadores
2. **paciente** - Pacientes con preferencias de accesibilidad
3. **rutina** - Rutinas diarias 1:1 con paciente
4. **item_rutina** - Items de rutina (actividades, turnos, medicación)
5. **medicamento** - Catálogo de medicamentos
6. **paciente_medicamento** - Relación N:M con pautas de dosificación
7. **recordatorio** - Recordatorios con estados (PENDIENTE, APLAZADO, PERDIDO, HECHO)
8. **notificacion** - Notificaciones visuales/sonoras
9. **adherencia** - Registro de acciones del paciente

### Ver datos:
La base de datos se encuentra en: `data/db.mv.db`

Para explorarla, ejecuta el servidor H2:
```cmd
java -cp lib/h2-2.4.240.jar org.h2.tools.Server
```
Luego abre: http://localhost:8082 y usa:
- JDBC URL: `jdbc:h2:./data/db`
- User: `sa`
- Password: (vacío)

---

## ✅ Estado del Proyecto

### Completado:
- ✅ Base de datos H2 configurada y funcional
- ✅ Schema SQL con 9 tablas relacionadas
- ✅ 7 entidades de dominio con getters/setters
- ✅ 7 interfaces DAO
- ✅ 7 implementaciones DAO con JDBC
- ✅ Tests de conexión y CRUD
- ✅ Interfaz gráfica básica (MainFrame)

### Pendiente:
- ⏳ TomaService (scheduler y lógica de recordatorios)
- ⏳ Patrón Observer para notificaciones reactivas
- ⏳ Completar UI con paneles funcionales
- ⏳ Gestión de rutinas e items
- ⏳ Dashboard de adherencia

---

## 🛠️ Tecnologías

- **Java SE 21**
- **Swing** (UI)
- **JDBC** (Acceso a datos)
- **H2 Database 2.4.240** (Base de datos embebida)
- **Maven** (Gestión de dependencias)

---

## 📝 Notas

- La base de datos se inicializa automáticamente en el primer arranque
- Los datos persisten entre ejecuciones en `data/db.mv.db`
- Para resetear la BD: eliminar `data/.schema_aplicado` y `data/db.mv.db`

