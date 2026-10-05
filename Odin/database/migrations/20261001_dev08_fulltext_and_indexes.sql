-- DEV-08: búsqueda full-text y soporte de consultas/alertas
alter table public.radicados add column if not exists busqueda_tsv tsvector;
create or replace function public.radicados_busqueda_tsv_trigger() returns trigger language plpgsql as $$ begin new.busqueda_tsv := to_tsvector('spanish', coalesce(new.numero_radicado,'') || ' ' || coalesce(new.asunto,'') || ' ' || coalesce(new.remitente,'')); return new; end; $$;
drop trigger if exists trg_radicados_busqueda_tsv on public.radicados;
create trigger trg_radicados_busqueda_tsv before insert or update of numero_radicado, asunto, remitente on public.radicados for each row execute function public.radicados_busqueda_tsv_trigger();
update public.radicados set busqueda_tsv = to_tsvector('spanish', coalesce(numero_radicado,'') || ' ' || coalesce(asunto,'') || ' ' || coalesce(remitente,''));
create index if not exists idx_radicados_busqueda_fts on public.radicados using gin (busqueda_tsv);
create index if not exists idx_notificaciones_usuario_fecha on public.notificaciones (id_usuario, fecha desc);
create index if not exists idx_sesiones_usuario_estado on public.sesiones_usuario (id_usuario, estado, ultima_actividad);
create index if not exists idx_log_accesos_usuario_fecha on public.log_accesos (id_usuario, fecha desc);
