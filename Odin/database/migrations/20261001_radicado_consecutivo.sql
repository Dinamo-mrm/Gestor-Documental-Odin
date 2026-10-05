-- Odin / Supabase PostgreSQL
-- Consecutivo anual de radicación: E-AAAA-000001 / S-AAAA-000001

create table if not exists public.radicado_consecutivos (
  anio integer not null,
  prefijo varchar(1) not null,
  consecutivo integer not null default 0,
  primary key (anio, prefijo),
  check (prefijo in ('E','S')),
  check (consecutivo >= 0)
);

create or replace function public.siguiente_numero_radicado(p_tipo text)
returns varchar
language plpgsql
as $$
declare
  v_prefijo varchar(1) := case when lower(coalesce(p_tipo,'')) = 'salida' then 'S' else 'E' end;
  v_anio integer := extract(year from current_date)::integer;
  v_siguiente integer;
begin
  insert into public.radicado_consecutivos(anio, prefijo, consecutivo)
  values (v_anio, v_prefijo, 1)
  on conflict (anio, prefijo)
  do update set consecutivo = public.radicado_consecutivos.consecutivo + 1
  returning consecutivo into v_siguiente;

  return v_prefijo || '-' || v_anio::text || '-' || lpad(v_siguiente::text,6,'0');
end;
$$;
