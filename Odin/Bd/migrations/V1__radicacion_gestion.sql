-- ODIN - Migración funcional Grupo 1 y Grupo 2
-- No modifica ni depende de Odin/Bd/odin.sql.

BEGIN;

CREATE SEQUENCE IF NOT EXISTS radicados_numero_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1;

CREATE INDEX IF NOT EXISTS idx_radicados_busqueda
    ON radicados (id_estado, id_dependencia, id_tramite);

CREATE INDEX IF NOT EXISTS idx_radicados_fecha_vencimiento
    ON radicados (fecha_vencimiento);

CREATE INDEX IF NOT EXISTS idx_radicados_fecha_limite
    ON radicados (fecha_limite);

CREATE INDEX IF NOT EXISTS idx_radicados_serie_subserie
    ON radicados (id_serie, id_subserie);

CREATE INDEX IF NOT EXISTS idx_documentos_radicado
    ON documentos (id_radicado);

CREATE INDEX IF NOT EXISTS idx_tramites_dependencia
    ON tramites (id_dependencia_responsable);

CREATE INDEX IF NOT EXISTS idx_tramites_activo
    ON tramites (activo);

CREATE UNIQUE INDEX IF NOT EXISTS uq_tramites_nombre_ci
    ON tramites (lower(trim(nombre)));

-- La clasificación CCD se normaliza por las FK ya existentes.
-- Se fuerza la coherencia serie/subserie en radicados mediante trigger.
CREATE OR REPLACE FUNCTION validar_clasificacion_radicado()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.id_subserie IS NOT NULL AND NEW.id_serie IS NULL THEN
        RAISE EXCEPTION 'La subserie requiere una serie documental';
    END IF;

    IF NEW.id_serie IS NOT NULL
       AND NOT EXISTS (
           SELECT 1 FROM ccd_series s WHERE s.id_serie = NEW.id_serie
       ) THEN
        RAISE EXCEPTION 'La serie documental no existe';
    END IF;

    IF NEW.id_subserie IS NOT NULL
       AND NOT EXISTS (
           SELECT 1
           FROM ccd_subseries ss
           WHERE ss.id_subserie = NEW.id_subserie
             AND ss.id_serie = NEW.id_serie
       ) THEN
        RAISE EXCEPTION 'La subserie no pertenece a la serie seleccionada';
    END IF;

    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_validar_clasificacion_radicado ON radicados;

CREATE TRIGGER trg_validar_clasificacion_radicado
BEFORE INSERT OR UPDATE OF id_serie, id_subserie
ON radicados
FOR EACH ROW
EXECUTE FUNCTION validar_clasificacion_radicado();

COMMIT;
