-- DDL base H2 (editable con Copilot)

CREATE TABLE IF NOT EXISTS cuidador (
  id IDENTITY PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL,
  contacto VARCHAR(160)
);

CREATE TABLE IF NOT EXISTS paciente (
  id IDENTITY PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL,
  preferencias_accesibilidad VARCHAR(200),
  cuidador_id BIGINT NOT NULL,
  CONSTRAINT fk_paciente_cuidador FOREIGN KEY (cuidador_id) REFERENCES cuidador(id)
);

CREATE INDEX IF NOT EXISTS idx_paciente_cuidador ON paciente(cuidador_id);

CREATE TABLE IF NOT EXISTS rutina (
  id IDENTITY PRIMARY KEY,
  paciente_id BIGINT NOT NULL,
  nombre VARCHAR(120),
  descripcion VARCHAR(255),
  CONSTRAINT fk_rutina_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
  CONSTRAINT uq_rutina_paciente UNIQUE(paciente_id)
);

CREATE TABLE IF NOT EXISTS item_rutina (
  id IDENTITY PRIMARY KEY,
  rutina_id BIGINT NOT NULL,
  titulo VARCHAR(160) NOT NULL,
  tipo VARCHAR(20) NOT NULL, -- ACTIVIDAD | TURNO | MEDICACION
  hora_fija TIME,
  ventana_min INT,
  icono VARCHAR(120),
  instrucciones VARCHAR(255),
  creado_por_cuidador_id BIGINT,
  CONSTRAINT fk_item_rutina_rutina FOREIGN KEY (rutina_id) REFERENCES rutina(id)
);

CREATE INDEX IF NOT EXISTS idx_item_rutina_rutina ON item_rutina(rutina_id);

CREATE TABLE IF NOT EXISTS medicamento (
  id IDENTITY PRIMARY KEY,
  nombre VARCHAR(160) NOT NULL,
  via VARCHAR(40),
  unidad_dosis VARCHAR(40),
  notas VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS paciente_medicamento (
  paciente_id BIGINT NOT NULL,
  medicamento_id BIGINT NOT NULL,
  dosis DECIMAL(10,2),
  unidad VARCHAR(20),
  intervalo_min INT NOT NULL,
  ventana_min INT NOT NULL,
  hora_inicio TIMESTAMP,
  proxima_toma_at TIMESTAMP,
  activo BOOLEAN DEFAULT TRUE,
  PRIMARY KEY (paciente_id, medicamento_id),
  CONSTRAINT fk_pm_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
  CONSTRAINT fk_pm_medicamento FOREIGN KEY (medicamento_id) REFERENCES medicamento(id)
);

CREATE INDEX IF NOT EXISTS idx_pm_proxima ON paciente_medicamento(paciente_id, proxima_toma_at);

CREATE TABLE IF NOT EXISTS recordatorio (
  id IDENTITY PRIMARY KEY,
  paciente_id BIGINT NOT NULL,
  tipo VARCHAR(20) NOT NULL, -- MEDICACION | ACTIVIDAD | TURNO
  referencia_tipo VARCHAR(20) NOT NULL, -- PAC_MED | ITEM_RUTINA
  referencia_id BIGINT NOT NULL,
  programado_at TIMESTAMP NOT NULL,
  ventana_min INT NOT NULL,
  estado VARCHAR(20) NOT NULL, -- PENDIENTE | APLAZADO | PERDIDO | HECHO
  motivo_estado VARCHAR(120),
  CONSTRAINT fk_recordatorio_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id)
);

CREATE INDEX IF NOT EXISTS idx_recordatorio_paciente_fecha ON recordatorio(paciente_id, programado_at);

CREATE TABLE IF NOT EXISTS notificacion (
  id IDENTITY PRIMARY KEY,
  recordatorio_id BIGINT NOT NULL,
  emitida_at TIMESTAMP NOT NULL,
  canal_visual VARCHAR(40),
  canal_sonoro VARCHAR(40),
  entregada BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_notif_recordatorio FOREIGN KEY (recordatorio_id) REFERENCES recordatorio(id)
);

CREATE TABLE IF NOT EXISTS adherencia (
  id IDENTITY PRIMARY KEY,
  recordatorio_id BIGINT NOT NULL,
  registrada_at TIMESTAMP NOT NULL,
  accion VARCHAR(20) NOT NULL, -- HECHO | OMITIDO | POSPUESTO
  observaciones VARCHAR(255),
  CONSTRAINT fk_adh_recordatorio FOREIGN KEY (recordatorio_id) REFERENCES recordatorio(id)
);
