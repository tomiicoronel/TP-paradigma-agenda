# ⚡ INICIO RÁPIDO - AGENDA ACCESIBLE

## 🚀 3 PASOS PARA EJECUTAR

### **PASO 1: Compilar**

Abre una terminal CMD en la carpeta del proyecto y ejecuta:

```batch
compile_simple.bat
```

**Debe mostrar:**
```
[1/3] Limpiando directorio bin...
[2/3] Compilando codigo fuente...
[3/3] Compilacion exitosa!
```

### **PASO 2: Ejecutar**

```batch
run_simple.bat
```

**Debe mostrar en consola:**
```
=== Agenda Accesible - Iniciando ===
→ Verificando esquema de base de datos...
→ Iniciando servicio de recordatorios...
→ Lanzando interfaz gráfica...
✅ Interfaz gráfica iniciada correctamente.
```

### **PASO 3: Usar la Aplicación**

Verás una ventana con 4 pestañas:
- ⚙️ **Gestión** - Crear pacientes, cuidadores, medicamentos
- 📅 **Hoy** - Ver recordatorios del día
- 💊 **Medicación** - Crear pautas de medicación
- 📊 **Historial** - Consultar adherencia

---

## 🎯 FLUJO BÁSICO (Primera vez)

### 1️⃣ Crear un Cuidador
```
Pestaña: ⚙️ Gestión
→ Click: "➕ Nuevo Cuidador"
→ Llenar: Nombre, Teléfono, Relación
→ Click: "Guardar"
```

### 2️⃣ Crear un Paciente
```
Pestaña: ⚙️ Gestión
→ Click: "➕ Nuevo Paciente"
→ Llenar: Nombre, Fecha (dd/MM/yyyy), Diagnóstico
→ Seleccionar: Cuidador del combo
→ Click: "Guardar"
```

### 3️⃣ Crear un Medicamento
```
Pestaña: ⚙️ Gestión
→ Click: "➕ Nuevo Medicamento"
→ Llenar: Nombre, Vía, Unidad
→ Click: "Guardar"
```

### 4️⃣ Crear Pauta de Medicación
```
Pestaña: 💊 Medicación
→ Click: "➕ Nueva Pauta"
→ Seleccionar: Paciente y Medicamento
→ Configurar: Dosis, Intervalo (24 horas), Hora (09:00)
→ Click: "Guardar"
```

### 5️⃣ Ver Recordatorios
```
Pestaña: 📅 Hoy
→ Ver: Recordatorios del día
→ Click: "Marcar como tomado" (cuando corresponda)
```

---

## ⚠️ SOLUCIÓN RÁPIDA DE PROBLEMAS

### ❌ Error: "javac no se reconoce..."
**Solución:** Ejecuta desde IntelliJ IDEA
```
1. Abre IntelliJ
2. Abre el proyecto
3. Navega a: src/app/Main.java
4. Click derecho → Run 'Main.main()'
```

### ❌ Error: "Cannot find h2..."
**Solución:** Verifica que exista `lib/h2-2.4.240.jar`

### ❌ La GUI no se abre
**Solución:** Verifica que no estés usando `--cli` en los argumentos

### ❌ "No hay pacientes"
**Solución:** Primero debes crear datos en la pestaña "Gestión"

---

## ✅ TODO LISTO

Si todo funciona, verás:
- ✅ Ventana de la aplicación abierta
- ✅ 4 pestañas visibles
- ✅ Botones con íconos
- ✅ Puedes crear y guardar datos
- ✅ Los datos persisten al cerrar y abrir

**¡Listo para entregar!** 🎉

---

## 📝 NOTAS

- Los datos se guardan en: `data/db.mv.db`
- El scheduler verifica recordatorios cada 60 segundos
- Para resetear la BD: elimina `data/db.mv.db` y reinicia

---

**Documentación completa en:** `README.md` y `PROYECTO_FINALIZADO.md`

