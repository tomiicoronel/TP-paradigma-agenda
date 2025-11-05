# 🏥 Agenda Accesible - Guía de Uso Rápido

## 🚀 Inicio Rápido

### Compilar
```bash
.\compile.bat
```

### Ejecutar
```bash
.\run.bat
```

---

## 📖 Tutorial Paso a Paso

### 1️⃣ Crear un Cuidador

```
Menú Principal → Opción 1 (Gestión de Pacientes y Cuidadores)
→ Opción 2 (Crear Cuidador)
```

**Ejemplo:**
```
Nombre completo: María González
Email (opcional): maria.gonzalez@email.com
Teléfono (opcional): 555-1234
```

**Resultado:**
```
✅ Cuidador creado exitosamente con ID: 1
```

---

### 2️⃣ Crear un Paciente

```
Menú Principal → Opción 1
→ Opción 4 (Crear Paciente)
```

**Ejemplo:**
```
Nombre completo: Juan Pérez
Fecha de nacimiento (dd/MM/yyyy): 15/03/1950
Diagnóstico (opcional): Alzheimer leve
ID del cuidador responsable: 1
```

**Resultado:**
```
✅ Paciente creado exitosamente con ID: 1
```

---

### 3️⃣ Crear Medicamentos

```
Menú Principal → Opción 2 (Gestión de Medicamentos)
→ Opción 2 (Crear Medicamento)
```

**Ejemplo 1:**
```
Nombre comercial: Aspirina
Droga (principio activo, opcional): Ácido acetilsalicílico
Presentación (opcional): Comprimido 100mg
```

**Ejemplo 2:**
```
Nombre comercial: Enalapril
Droga: Enalapril maleato
Presentación: Comprimido 10mg
```

**Resultado:**
```
✅ Medicamento creado exitosamente con ID: 1
✅ Medicamento creado exitosamente con ID: 2
```

---

### 4️⃣ Configurar Pautas de Medicación

```
Menú Principal → Opción 3 (Configuración de Pautas)
→ Opción 2 (Crear Nueva Pauta)
```

**Ejemplo:**
```
ID del paciente: 1
ID del medicamento: 1
Hora de inicio (HH:mm): 08:00
Intervalo entre tomas (minutos): 720
Dosis (ej: 1 comprimido, opcional): 1 comprimido
¿Activar pauta ahora? (S/N): S
```

**Resultado:**
```
✅ Pauta creada exitosamente.
⏰ Se generarán recordatorios automáticamente según el intervalo configurado.
```

**Nota:** 720 minutos = 12 horas (dos tomas al día: 08:00 y 20:00)

---

### 5️⃣ Ver Pautas del Paciente

```
Menú Principal → Opción 3
→ Opción 1 (Listar Pautas de un Paciente)
```

**Ejemplo:**
```
ID del paciente: 1
```

**Resultado:**
```
═══ Pautas de: Juan Pérez ═══
──────────────────────────────────────────────────────────────────────────────────────
Medicamento                    Hora Inicio  Intervalo  Dosis           Activa    
──────────────────────────────────────────────────────────────────────────────────────
Aspirina                       08:00        720 min    1 comprimido    ✓ Sí      
Enalapril                      09:00        1440 min   1 comprimido    ✓ Sí      
```

---

### 6️⃣ Ver Recordatorios Pendientes

```
Menú Principal → Opción 4 (Recordatorios Pendientes)
→ Opción 1 (Ver Recordatorios Pendientes)
```

**Resultado:**
```
📋 RECORDATORIOS PENDIENTES:
────────────────────────────────────────────────────────────────────────────────────────────────────
ID    Paciente                  Medicamento               Programado Para      Estado         
────────────────────────────────────────────────────────────────────────────────────────────────────
1     Juan Pérez                Aspirina                  05/11/2025 08:00     PENDIENTE      
2     Juan Pérez                Enalapril                 05/11/2025 09:00     PENDIENTE      
```

---

### 7️⃣ Marcar Recordatorio como Realizado

```
Menú Principal → Opción 4
→ Opción 3 (Marcar Recordatorio como HECHO)
```

**Ejemplo:**
```
ID del recordatorio: 1
```

**Resultado:**
```
✅ Recordatorio marcado como HECHO exitosamente.
   Hora de realización: 05/11/2025 08:15
```

---

### 8️⃣ Ver Estadísticas de Adherencia

```
Menú Principal → Opción 5 (Historial de Adherencia)
→ Opción 4 (Estadísticas de Adherencia)
```

**Ejemplo:**
```
ID del paciente: 1
```

**Resultado:**
```
═══ Estadísticas de: Juan Pérez ═══

╔═══════════════════════════════════════════════════╗
  Total de recordatorios:    10
  ✓ Hechos:                  8
  ⏰ Pendientes:              1
  ⏳ Aplazados:               1
  ✗ Perdidos:                0
  ─────────────────────────────────────────────────
  📈 Adherencia:              80.0%
╚═══════════════════════════════════════════════════╝
```

---

## 🎯 Casos de Uso Comunes

### Configurar medicación matutina y nocturna
```
Pauta 1: Hora 08:00, Intervalo 720 min (12h)
→ Tomas a las 08:00 y 20:00

Pauta 2: Hora 09:00, Intervalo 1440 min (24h)
→ Una toma diaria a las 09:00
```

### Medicación cada 8 horas
```
Hora: 08:00, Intervalo 480 min
→ Tomas a las 08:00, 16:00, 00:00
```

### Medicación cada 6 horas
```
Hora: 06:00, Intervalo 360 min
→ Tomas a las 06:00, 12:00, 18:00, 00:00
```

---

## 🔍 Tips y Trucos

### Ver detalle completo de un paciente
```
Menú 1 → Opción 5
```
Muestra: nombre, fecha de nacimiento, diagnóstico, y datos del cuidador

### Buscar medicamentos
```
Menú 2 → Opción 1
```
Lista todos los medicamentos con ID, nombre, droga y presentación

### Ver historial completo de adherencia
```
Menú 5 → Opción 1
```
Muestra todos los recordatorios y su estado para un paciente

---

## ⚠️ Notas Importantes

1. **IDs Secuenciales**: Los IDs se generan automáticamente al crear registros
2. **Intervalos en Minutos**: Siempre especificar el tiempo entre tomas en minutos
3. **Formatos de Fecha/Hora**:
   - Fecha: `dd/MM/yyyy` (ejemplo: 15/03/1950)
   - Hora: `HH:mm` (ejemplo: 08:30)
4. **Campos Opcionales**: Puedes dejar vacíos presionando ENTER
5. **Navegación**: Usa 0 para volver al menú anterior

---

## 🆘 Solución de Problemas

### "No hay cuidadores/pacientes/medicamentos registrados"
→ Debes crear los registros en orden:
1. Cuidador
2. Paciente (requiere cuidador)
3. Medicamento
4. Pauta (requiere paciente y medicamento)

### "Paciente/Medicamento no encontrado"
→ Verifica el ID usando las opciones de listar

### Error de formato de fecha/hora
→ Usa el formato exacto: `dd/MM/yyyy` para fechas, `HH:mm` para horas

---

## 📞 Flujo Completo de Ejemplo

```bash
# 1. Ejecutar
.\run.bat

# 2. Crear cuidador (Opción 1-2)
Nombre: Ana López
Email: ana@email.com
Tel: 555-9876

# 3. Crear paciente (Opción 1-4)
Nombre: Pedro Martínez
Fecha: 20/05/1948
Diagnóstico: Demencia vascular
Cuidador ID: 1

# 4. Crear medicamento (Opción 2-2)
Nombre: Donepezilo
Droga: Donepezilo HCl
Presentación: Comprimido 5mg

# 5. Crear pauta (Opción 3-2)
Paciente: 1
Medicamento: 1
Hora: 20:00
Intervalo: 1440 (una vez al día)
Dosis: 1 comprimido
Activar: S

# 6. Ver pautas (Opción 3-1)
Paciente: 1

# 7. Ver estadísticas (Opción 5-4)
Paciente: 1
```

---

✅ **¡Listo!** Ya sabes cómo usar la Agenda Accesible.

Para más información, consulta el archivo `PASO3_CLI_COMPLETADO.md`

