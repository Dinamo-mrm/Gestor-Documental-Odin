-- ============================================================================
-- ODIN - MIGRACIÓN DEFINITIVA: SEPARACIÓN DE TRÁMITES PQRS EN CATÁLOGO REAL BD
-- Fecha: 2026-10-30 | Integración limpia a main: 2026-11-02
-- Base de Datos: PostgreSQL (Supabase)
-- ============================================================================
-- OBJETIVOS:
-- 1. Separar los trámites PQRS en registros independientes con 15 días hábiles.
-- 2. Preservar integridad referencial de radicados históricos vinculados a id_tramite = 1.
-- 3. Idempotente: se puede ejecutar varias veces sin duplicar.
-- ============================================================================

BEGIN;

-- 1. Desactivar trámite agrupado PQRS (ID 1) preservando histórico intacto
UPDATE tramites
SET activo = false,
    dias_respuesta = 15,
    fecha_actualizacion = NOW()
WHERE id_tramite = 1;

-- 2. Reutilizar y actualizar ID 5 para 'Petición' (si existe)
UPDATE tramites
SET nombre = 'Petición',
    descripcion = 'Petición general o solicitud ciudadana en ejercicio del derecho fundamental',
    dias_respuesta = 15,
    activo = true,
    fecha_actualizacion = NOW()
WHERE id_tramite = 5;

-- 3. Crear 'Queja' si no existe
INSERT INTO tramites (
    nombre, descripcion, id_dependencia_responsable, id_estado_inicial,
    dias_respuesta, prioridad_default, requiere_respuesta, activo, fecha_creacion
)
SELECT
    'Queja',
    'Manifestación de protesta, censura o inconformidad por la conducta de un servidor o atención brindada',
    2, 1, 15, 'MEDIA', true, true, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM tramites WHERE lower(trim(nombre)) = 'queja'
);

-- 4. Crear 'Reclamo' si no existe
INSERT INTO tramites (
    nombre, descripcion, id_dependencia_responsable, id_estado_inicial,
    dias_respuesta, prioridad_default, requiere_respuesta, activo, fecha_creacion
)
SELECT
    'Reclamo',
    'Manifestación de insatisfacción sobre la prestación indebida o deficiente de un servicio o trámite institucional',
    2, 1, 15, 'MEDIA', true, true, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM tramites WHERE lower(trim(nombre)) = 'reclamo'
);

-- 5. Crear 'Sugerencia' si no existe
INSERT INTO tramites (
    nombre, descripcion, id_dependencia_responsable, id_estado_inicial,
    dias_respuesta, prioridad_default, requiere_respuesta, activo, fecha_creacion
)
SELECT
    'Sugerencia',
    'Proposición, idea o recomendación formulada por un ciudadano para mejorar la prestación de servicios institucionales',
    2, 1, 15, 'MEDIA', true, true, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM tramites WHERE lower(trim(nombre)) = 'sugerencia'
);

COMMIT;
