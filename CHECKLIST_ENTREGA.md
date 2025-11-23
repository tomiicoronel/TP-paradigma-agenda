# ✅ CHECKLIST PRE-ENTREGA

## 📋 VERIFICACIÓN FINAL - Marca cada item antes de entregar

### **1. Compilación**
- [ ] El proyecto compila sin errores
- [ ] Ejecuté `compile_simple.bat` exitosamente (o compilé en IntelliJ)
- [ ] No hay errores rojos en IntelliJ (solo warnings amarillos están OK)

### **2. Ejecución**
- [ ] La aplicación inicia correctamente
- [ ] Se abre la ventana GUI
- [ ] Veo las 4 pestañas: ⚙️Gestión, 📅Hoy, 💊Medicación, 📊Historial
- [ ] Los íconos se ven correctamente

### **3. Funcionalidad Básica**
- [ ] Puedo crear un cuidador
- [ ] Puedo crear un paciente
- [ ] Puedo crear un medicamento
- [ ] Puedo ver las listas de datos creados
- [ ] Los datos se guardan correctamente

### **4. Funcionalidad de Pautas** ⭐ NUEVO
- [ ] El botón "➕ Nueva Pauta" funciona
- [ ] Se abre el formulario de pautas
- [ ] Puedo seleccionar paciente y medicamento
- [ ] Puedo guardar una pauta
- [ ] La pauta aparece en la lista

### **5. Recordatorios**
- [ ] Puedo ver recordatorios en la pestaña "Hoy"
- [ ] Puedo marcar un recordatorio como tomado
- [ ] El scheduler está funcionando (mensajes en consola cada 60 seg)

### **6. Persistencia**
- [ ] Cerré la aplicación
- [ ] Volví a abrir la aplicación
- [ ] Los datos que creé siguen ahí
- [ ] No se perdió información

### **7. Documentación**
- [ ] Existe `README.md` con instrucciones
- [ ] Existe `PROYECTO_FINALIZADO.md` con resumen completo
- [ ] Existe `INICIO_RAPIDO.md` para inicio rápido
- [ ] Los archivos `.md` se leen correctamente

### **8. Archivos Necesarios**
- [ ] Existe `lib/h2-2.4.240.jar`
- [ ] Existen scripts `.bat` de compilación y ejecución
- [ ] Existe `pom.xml` (configuración Maven)
- [ ] Existe `db/schema.sql` (esquema de BD)

### **9. Estructura del Código**
- [ ] Carpeta `src/` con todo el código fuente
- [ ] Paquetes: app, controller, domain, infra, service, shared, ui
- [ ] Todos los archivos `.java` están presentes
- [ ] No hay archivos `.class` sueltos en `src/`

### **10. Prueba Completa (5 min)**
- [ ] Inicié la app
- [ ] Creé 1 cuidador: "María García"
- [ ] Creé 1 paciente: "Juan Pérez" (15/03/1950)
- [ ] Creé 1 medicamento: "Donepezilo"
- [ ] Creé 1 pauta: Juan + Donepezilo, 10mg, cada 24h
- [ ] Vi que se creó la pauta
- [ ] Cerré y abrí, los datos persisten

---

## 🎯 SI TODOS LOS CHECKS ESTÁN ✅

**¡Estás listo para entregar!**

El proyecto está:
- ✅ Completo
- ✅ Funcional
- ✅ Documentado
- ✅ Probado

---

## ⚠️ SI ALGÚN CHECK ESTÁ ❌

### **Problema: No compila**
→ Verifica que `lib/h2-2.4.240.jar` exista
→ Ejecuta desde IntelliJ en vez de scripts

### **Problema: No ejecuta**
→ Abre IntelliJ → Run Main.java
→ Verifica Java 11+

### **Problema: No se ve la GUI**
→ No uses `--cli` en argumentos
→ Ejecuta `run_simple.bat` (no `run_simple_cli.bat`)

### **Problema: No persisten datos**
→ Verifica permisos en carpeta `data/`
→ No elimines `data/db.mv.db` entre pruebas

### **Problema: No funciona el botón "Nueva Pauta"**
→ Primero crea al menos 1 paciente y 1 medicamento
→ Si no hay, te mostrará un mensaje de advertencia

---

## 📦 ARCHIVOS A ENTREGAR

### **Incluir:**
✅ Carpeta completa del proyecto
✅ `src/` con todo el código
✅ `lib/h2-2.4.240.jar`
✅ Todos los `.md` de documentación
✅ Scripts `.bat`
✅ `pom.xml`
✅ `db/schema.sql`

### **NO Incluir:**
❌ `target/` (se genera al compilar)
❌ `bin/` (se genera al compilar)
❌ `.idea/` (configuración de IDE)
❌ `*.iml` (archivos de IntelliJ)
❌ `data/db.mv.db` (base de datos de prueba)

---

## 🎓 PARA LA DEFENSA

### **Prepara estas demos:**

1. **Mostrar Arquitectura (2 min)**
   - Abrir estructura de paquetes
   - Explicar separación en capas
   - Mencionar patrones (MVC, DAO, Observer)

2. **Demo Funcional (3 min)**
   - Iniciar aplicación
   - Crear 1 paciente
   - Crear 1 pauta
   - Mostrar recordatorio generado

3. **Explicar Código (2 min)**
   - Mostrar un Service (ej: PacienteService)
   - Mostrar un DAO (ej: PacienteDAOImpl)
   - Explicar Observer en TomaService

### **Preguntas probables:**

**Q: ¿Qué patrones usaste?**
A: MVC, DAO, Observer, Service Layer, Singleton

**Q: ¿Cómo funciona el scheduler?**
A: TomaService con ScheduledExecutorService cada 60 segundos

**Q: ¿Por qué Swing y no JavaFX?**
A: Swing es más estable, parte de Java SE, mejor accesibilidad

**Q: ¿Los datos persisten?**
A: Sí, en H2 embebido en archivo `data/db.mv.db`

---

## ✅ CONFIRMACIÓN FINAL

**Antes de entregar, confirma:**

- [ ] ✅ El proyecto funciona en MI computadora
- [ ] ✅ Probé TODO el flujo al menos 1 vez
- [ ] ✅ Leí el `README.md` y `PROYECTO_FINALIZADO.md`
- [ ] ✅ Sé explicar la arquitectura
- [ ] ✅ Sé demostrar la funcionalidad
- [ ] ✅ Tengo todos los archivos necesarios
- [ ] ✅ No incluí archivos innecesarios

---

## 🎉 SI TODO ESTÁ ✅

**¡FELICITACIONES!**

Tu proyecto está:
- Completo
- Funcional
- Documentado
- Listo para entregar y defender

**¡Mucha suerte!** 🚀

---

**Última revisión:** 23/11/2025
**Estado:** LISTO PARA ENTREGA

