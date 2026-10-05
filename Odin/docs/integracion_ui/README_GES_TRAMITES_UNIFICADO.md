# Gestión de Trámites unificada — ges_tramites.html

**Fecha:** 03 de octubre de 2026  
**Archivo principal:** `tramites/ges_tramites.html`

## Qué se unificó

| Vista anterior | Rol | Destino |
|----------------|-----|---------|
| `ges_tramites.html` | Bandeja operativa de **radicados** | Pestaña **Bandeja operativa** |
| `tramites.html` | Listado/borrador de radicados o tipos | Absorbido / reemplazado |
| `tramitesForm.html` | CRUD de **tipos de trámite** (parcial) | Pestaña **Tipos de trámite** + formulario embebido |

## Cumplimiento RF

| RF | Descripción | Cómo se cumple en esta interfaz |
|----|-------------|----------------------------------|
| **RF-26** | Estados de trámite parametrizables | Catálogo de tipos + estados en filtros y formulario |
| **RF-28** | Tiempo de respuesta según tipo de trámite | Campo **Días de respuesta** obligatorio en el formulario de tipo |
| **RF-29** | Tiempos parametrizables sin redeploy | CRUD de tipos desde UI; `diasRespuesta` editable |
| **RF-32** | Listar documentos en mora | KPI Vencidos + filtro vencimiento + columna días restantes |
| **RF-35** | Filtrar bandeja por estado y rangos de fecha | Filtros: estado, dependencia, trámite, **fecha desde/hasta**, vencimiento |

## Pestañas

1. **Bandeja operativa** — KPIs, filtros completos, tabla de radicados, acciones masivas, paginación, días restantes.
2. **Tipos de trámite** — Catálogo con días de respuesta, prioridad, activo, dependencia.
3. **Nuevo / Editar tipo** — Formulario con selects de dependencia y estado (ya no inputs numéricos crudos).

## Backend mínimo

- `TramitesView` / controller de `/view/tramites` debe seguir sirviendo `radicados`, KPIs y listas de filtros.
- Endpoint `POST /view/tramites/save` para crear/editar tipo (campos: `idTramite`, `nombre`, `descripcion`, `diasRespuesta`, `prioridadDefault`, `idDependenciaResponsable`, `idEstadoInicial`, `requiereRespuesta`, `activo`).
- Opcional: `diasRestantes` calculado por radicado; `fechaDesde` / `fechaHasta` en el repositorio de búsqueda.
- Las rutas antiguas `tramites.html` / `tramitesForm.html` pueden redirigir a `/view/tramites` y `/view/tramites?tab=form-tipo`.

## Instalación

Copiar `tramites/ges_tramites.html` sobre:

```
src/main/resources/templates/tramites/ges_tramites.html
```

(y la copia bajo `templates/templates/tramites/` si el proyecto la usa).
