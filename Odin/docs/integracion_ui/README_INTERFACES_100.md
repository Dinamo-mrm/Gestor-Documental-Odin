# ODIN — Interfaces mejoradas para 100 % de cumplimiento UI

**Fecha:** 02 de octubre de 2026  
**Alcance:** Cerrar los RF de interfaz que quedaban parciales en Odinfncal + Odinen_UI_Reportes_Consulta.

## Archivos incluidos

| Archivo | RF que cierra | Mejora principal |
|---------|---------------|------------------|
| `auth/login.html` | RF-01, RF-N09, RF-N10, RF-N24 | Login flexible (correo / identificación / nombre), mensajes claros, CSRF, modo oscuro |
| `consulta/consulta.html` | RF-33, RF-34, RF-35, RF-30, RF-32 | Filtros de **rango de fechas**, columna **días restantes** con colores (ok/warn/vencido), KPIs |
| `radicados/radicacion_Documental.html` | RF-04–RF-15, RF-09, RF-14, RF-23 | Checkbox **radicación anónima**, campo **forma de respuesta**, tipo E-/S-/Interno, serie/subserie |
| `radicados/radicado-detalle.html` | RF-16, RF-17, RF-25, RF-30, RF-34 | Botón **Imprimir comprobante PDF**, días restantes, historial, observaciones, firmas |
| `roles/rolesForm.html` | RF-N16, RF-43, RF-44 | **Matriz visual de permisos** (checkboxes) al crear/editar rol |

## Cómo aplicar

1. Respaldar los templates actuales del proyecto.
2. Copiar los archivos de este paquete sobre:
   ```
   src/main/resources/templates/
   ```
   respetando las subcarpetas (`auth/`, `consulta/`, `radicados/`, `roles/`).
3. Asegurar que el backend exponga:
   - `diasRestantes` (calculado) en el modelo/DTO de radicados.
   - Parámetros `fechaDesde` / `fechaHasta` en `ConsultaView`.
   - Endpoint `/api/radicados/{id}/comprobante` (ya documentado en Odinfncal).
   - Lista `permisosDisponibles` y `permisosAsignados` en el formulario de roles.
4. Recompilar y validar con usuarios de distintos roles.

## Notas de backend mínimo requerido

- **ConsultaView:** aceptar `fechaDesde`, `fechaHasta` y pasar `diasRestantes` por radicado (o calcular en la vista con helper).
- **Radicados:** campo `anonimo` (boolean), `formaRespuesta` (enum/string).
- **RolesView / RolesController:** cargar todos los permisos y los asignados al rol; persistir en `rol_permisos`.

Con estas interfaces + el backend ya presente en Odinfncal, la cobertura de **RF de UI llega al 100 %** de los ítems que dependen de pantalla.
