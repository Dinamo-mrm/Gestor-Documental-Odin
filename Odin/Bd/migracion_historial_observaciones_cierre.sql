-- Migración para PostgreSQL/Supabase
-- Ejecutar una sola vez sobre la base de datos ODIN.

CREATE TABLE IF NOT EXISTS historial_radicado (
    id_historial BIGSERIAL PRIMARY KEY,
    id_radicado BIGINT NOT NULL,
    id_usuario INTEGER NULL,
    accion VARCHAR(80) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    fecha VARCHAR(40) NOT NULL DEFAULT CURRENT_TIMESTAMP::text
);

CREATE TABLE IF NOT EXISTS observaciones (
    id_observacion BIGSERIAL PRIMARY KEY,
    id_radicado BIGINT NOT NULL,
    id_usuario INTEGER NULL,
    comentario VARCHAR(255) NOT NULL,
    fecha VARCHAR(40) NOT NULL DEFAULT CURRENT_TIMESTAMP::text
);

ALTER TABLE radicados ADD COLUMN IF NOT EXISTS fecha_cierre VARCHAR(40);
ALTER TABLE radicados ADD COLUMN IF NOT EXISTS id_usuario_cierre INTEGER;

CREATE INDEX IF NOT EXISTS idx_historial_radicado ON historial_radicado(id_radicado);
CREATE INDEX IF NOT EXISTS idx_observaciones_radicado ON observaciones(id_radicado);
