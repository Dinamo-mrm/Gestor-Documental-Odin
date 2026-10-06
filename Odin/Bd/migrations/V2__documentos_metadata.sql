ALTER TABLE public.documentos
    ADD COLUMN IF NOT EXISTS mime_type varchar(100),
    ADD COLUMN IF NOT EXISTS checksum varchar(64),
    ADD COLUMN IF NOT EXISTS version_actual integer;

CREATE INDEX IF NOT EXISTS idx_documentos_checksum
    ON public.documentos (checksum);
