-- Migración complementaria para PostgreSQL/Supabase.
-- Las tablas historial_radicado y observaciones ya existen en Supabase.
-- Este script NO las recrea ni modifica sus claves foráneas existentes.

ALTER TABLE public.radicados
    ADD COLUMN IF NOT EXISTS fecha_cierre timestamp without time zone;

ALTER TABLE public.radicados
    ADD COLUMN IF NOT EXISTS id_usuario_cierre bigint;

CREATE INDEX IF NOT EXISTS idx_historial_radicado_id_radicado
    ON public.historial_radicado (id_radicado);

CREATE INDEX IF NOT EXISTS idx_observaciones_id_radicado
    ON public.observaciones (id_radicado);

CREATE INDEX IF NOT EXISTS idx_historial_radicado_fecha
    ON public.historial_radicado (fecha);

CREATE INDEX IF NOT EXISTS idx_observaciones_fecha
    ON public.observaciones (fecha);

-- Validación opcional de columnas esperadas:
-- historial_radicado: id_historial, id_radicado, id_usuario, accion, descripcion, fecha
-- observaciones: id_observacion, id_radicado, id_usuario, comentario, fecha
