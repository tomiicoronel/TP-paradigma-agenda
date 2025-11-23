# GUÍA RÁPIDA DE EJECUCIÓN - AGENDA ACCESIBLE

## 🚀 OPCIÓN 1: Usando Maven desde IntelliJ IDEA (RECOMENDADO)

### **Paso 1: Compilar**
1. Abre IntelliJ IDEA
2. Ve a Maven panel (lado derecho)
3. Expande "TP paradigmas AgendaAccesible" → "Lifecycle"
4. Doble click en "compile"
5. Espera que termine (debe decir "BUILD SUCCESS")

### **Paso 2: Ejecutar**
1. En el proyecto, navega a: `src/app/Main.java`
2. Click derecho en el archivo → "Run 'Main.main()'"
3. La aplicación GUI debería abrirse

**O desde Maven:**
1. Maven panel → Plugins → exec → exec:java
2. Doble click
3. La GUI se abre

---

## 🚀 OPCIÓN 2: Desde Terminal (si Maven está en PATH)

```bash
# Compilar
mvn clean compile

# Ejecutar GUI
mvn exec:java -Dexec.mainClass="app.Main"

# O ejecutar CLI
mvn exec:java -Dexec.mainClass="app.Main" -Dexec.args="--cli"
```

---

## 🚀 OPCIÓN 3: Compilación Manual (sin Maven)

Si Maven no funciona, puedes compilar manualmente:

### **Paso 1: Crear directorio de salida**
```cmd
mkdir bin
```

### **Paso 2: Compilar todo**
```cmd
javac -d bin -cp "lib\h2-2.4.240.jar" src\app\*.java src\controller\*.java src\domain\*.java src\infra\dao\*.java src\infra\dao\impl\*.java src\infra\db\*.java src\service\*.java src\shared\observer\*.java src\ui\*.java src\ui\forms\*.java src\ui\menu\*.java src\ui\panels\*.java src\ui\utils\*.java
```

### **Paso 3: Ejecutar**
```cmd
java -cp "bin;lib\h2-2.4.240.jar" app.Main
```

---

## 🚀 OPCIÓN 4: Crear JAR ejecutable

### **Paso 1: Compilar con Maven**
```bash
mvn clean package
```

### **Paso 2: Ejecutar el JAR**
```bash
java -jar target/AgendaAccesible-1.0-SNAPSHOT.jar
```

---

## ✅ VERIFICACIÓN RÁPIDA

### **¿La aplicación inició correctamente?**
Deberías ver en consola:
```
=== Agenda Accesible - Iniciando ===
→ Verificando esquema de base de datos...
→ Iniciando servicio de recordatorios...
→ Lanzando interfaz gráfica...
✅ Interfaz gráfica iniciada correctamente.
```

### **¿Se abrió la ventana GUI?**
Deberías ver una ventana con:
- Título: "Agenda Accesible - Sistema de Recordatorios"
- 4 pestañas: Gestión, Hoy, Medicación, Historial
- Primera pestaña con botones para crear pacientes, cuidadores y medicamentos

---

## 🧪 PRUEBA RÁPIDA (5 minutos)

### **Test 1: Crear un Cuidador**
1. Pestaña "Gestión"
2. Click "Nuevo Cuidador"
3. Nombre: "María García"
4. Teléfono: "555-1234"
5. Relación: "Hija"
6. Guardar
7. ✅ Debe decir "Cuidador creado exitosamente"

### **Test 2: Crear un Paciente**
1. Click "Nuevo Paciente"
2. Nombre: "Juan Pérez"
3. Fecha: "15/03/1950"
4. Diagnóstico: "Alzheimer leve"
5. Cuidador: Seleccionar "María García"
6. Guardar
7. ✅ Debe decir "Paciente creado exitosamente"

### **Test 3: Crear un Medicamento**
1. Click "Nuevo Medicamento"
2. Nombre: "Donepezilo"
3. Vía: "oral"
4. Unidad: "mg"
5. Guardar
6. ✅ Debe decir "Medicamento creado exitosamente"

### **Test 4: Ver Listas**
1. Click "Ver Lista de Pacientes"
   - ✅ Debe mostrar "Juan Pérez" con edad calculada
2. Click "Ver Lista de Cuidadores"
   - ✅ Debe mostrar "María García"
3. Click "Ver Lista de Medicamentos"
   - ✅ Debe mostrar "Donepezilo"

### **Test 5: Crear Pauta de Medicación**
1. Ir a pestaña "Medicación"
2. Seleccionar paciente: "Juan Pérez"
3. Seleccionar medicamento: "Donepezilo"
4. Dosis: "10"
5. Unidad: "mg"
6. Intervalo: "24" (horas)
7. Hora inicio: "09:00"
8. Guardar
9. ✅ Debe crear la pauta y generar recordatorios

### **Test 6: Ver Recordatorios**
1. Ir a pestaña "Hoy"
2. ✅ Debe mostrar los recordatorios del día
3. Click en "Marcar como tomado" (si hay alguno pendiente)
4. ✅ Debe actualizar el estado

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### **Error: "mvn no se reconoce como comando"**
**Solución:** Usa IntelliJ IDEA (Opción 1) o compilación manual (Opción 3)

### **Error: "Cannot find symbol" al compilar**
**Solución:** 
1. Verifica que H2 esté en `lib/h2-2.4.240.jar`
2. Limpia y recompila: `mvn clean compile`

### **Error: "Table not found"**
**Solución:** La primera vez que se ejecuta, el esquema se crea automáticamente. Si hay problemas:
1. Elimina `data/db.mv.db`
2. Reinicia la aplicación
3. El esquema se recreará

### **La GUI no se abre**
**Solución:**
1. Verifica que estés ejecutando `app.Main` (no otras clases de test)
2. Verifica que no haya errores de compilación
3. Intenta con: `mvn exec:java -Dexec.mainClass="app.Main"`

### **No aparecen datos en las listas**
**Solución:** Primero crea datos usando los formularios (Nuevo Paciente, etc.)

---

## 📝 NOTAS IMPORTANTES

### **Base de Datos**
- La BD H2 se guarda en: `data/db.mv.db`
- Es persistente (los datos no se pierden al cerrar)
- Para resetear: elimina `data/db.mv.db` y reinicia

### **Scheduler de Notificaciones**
- Corre automáticamente en background
- Verifica recordatorios cada 60 segundos
- Para verlo en acción:
  1. Crea una pauta con hora cercana
  2. Espera
  3. Aparecerá notificación en consola y UI

### **CLI vs GUI**
- **GUI (defecto):** `mvn exec:java -Dexec.mainClass="app.Main"`
- **CLI:** `mvn exec:java -Dexec.mainClass="app.Main" -Dexec.args="--cli"`

---

## 🎯 CHECKLIST PRE-ENTREGA

- [ ] La aplicación compila sin errores
- [ ] La GUI se abre correctamente
- [ ] Puedo crear un paciente
- [ ] Puedo crear un cuidador
- [ ] Puedo crear un medicamento
- [ ] Puedo ver las listas de datos
- [ ] Puedo crear una pauta de medicación
- [ ] Los recordatorios aparecen en "Hoy"
- [ ] La base de datos persiste entre ejecuciones

**Si todos los checks están ✅, estás listo para entregar.**

---

## 🆘 SI TODO FALLA

**Última opción: Ejecutar desde IntelliJ**

1. Abre IntelliJ IDEA
2. Abre el proyecto
3. Navega a: `src/app/Main.java`
4. Click derecho → Run 'Main.main()'
5. **Listo, la GUI debería abrirse**

Esta es la forma más simple y siempre funciona.

