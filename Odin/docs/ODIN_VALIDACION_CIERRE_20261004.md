# ODIN — Auditoría funcional y de integración
## Fecha: 2026-10-04

## 1. Alcance de la revisión

Se revisó el paquete `Odinfnl.zip` a nivel de código fuente, cubriendo:

- 31 plantillas HTML fuente Thymeleaf.
- Controladores de vistas y controladores REST.
- Formularios, enlaces, botones, acciones `data-action` y llamadas `fetch`.
- `script.js`, `odin-ui.js` y las cinco hojas CSS compartidas.
- Entidades JPA, repositorios y configuración de Spring Security.
- Configuración de conexión JDBC a Supabase/PostgreSQL.
- Esquema y datos actuales del proyecto Supabase `Odin`.
- Integridad referencial de las relaciones principales.
- Migraciones y alertas de seguridad/performance de Supabase.

> Importante: el paquete contiene 31 plantillas fuente, no 33. Las copias bajo `target/classes/templates` fueron tratadas como artefactos compilados y no como vistas adicionales.

## 2. Resultado después de las correcciones

| Estado | Cantidad |
|---|---:|
| Vistas fuente | 31 |
| OK en auditoría estática | 31 |
| ROTO | 0 |
| HUÉRFANO | 0 |
| Botones sin acción funcional identificable | 0 |
| Vistas sin CSS compartido | 0 |
| Vistas sin JavaScript compartido/propio | 0 |

La condición **OK** significa que la vista tiene ruta de servidor, las acciones visibles tienen destino existente y las integraciones JavaScript revisadas apuntan a endpoints existentes. No equivale por sí sola a una certificación E2E en navegador.

## 3. Correcciones realizadas

### Radicación y recepción
1. `radicacion_Documental.html`
   - Se corrigió el POST de `/view/radicados/guardar` a `/view/radicados/documental/save`.
   - Se corrigió el campo de carga de archivos de `name="anexos"` a `name="archivos"`, alineándolo con `Radicados.archivos` y `RadicadosView`.
   - Se conserva `multipart/form-data`.

### Gestión de documentos
2. `documentosForm.html`
   - Se reemplazó la plantilla incorrecta que era una copia del formulario de usuarios.
   - Ahora usa `Documentos` y POST `/view/documentos/save`.
   - Se agregaron selección de radicado, metadatos y carga de archivo.
3. `DocumentosView`
   - Se agregó procesamiento multipart.
   - Se generan nombre almacenado, ruta, MIME, tamaño y checksum SHA-256 cuando se carga archivo.
   - Se utiliza `DocumentosService` para registrar la versión inicial.
   - Se agregó `/view/documentos/download/{id}` para abrir documentos cuando el archivo existe en el almacenamiento local.

### Estados
4. `estadosForm.html`
   - Se reemplazó el formulario incorrecto de documentos.
   - Ahora usa `Estados` y POST `/view/estados/save`.
   - Se agregaron nombre, color y flujo PQRSF.
5. `Estados.java`
   - Se añadieron las propiedades `color` y `flujo_pqrsf` existentes en Supabase.

### Reasignaciones
6. `reasignacionesForm.html`
   - Se reconstruyó completamente.
   - Se eliminó código Java incrustado dentro del HTML.
   - Ahora permite radicado, usuario anterior, usuario nuevo, dependencia nueva y fecha.
7. `ReasignacionesView`
   - Ahora carga catálogos de radicados, usuarios y dependencias.

### Trazabilidad / Plantilla
8. `PlantillaForm.html`
   - Se eliminaron campos inexistentes (`ip`, `accionDetalle`, `datos`) que no pertenecían a la entidad `Plantilla`.
   - Se usan únicamente los campos reales: radicado, usuario, acción, fecha y descripción.
9. `PlantillaView`
   - Se agregaron catálogos de radicados y usuarios para evitar captura manual de IDs.

### CSS / JS / UX
10. Todos los 31 HTML incluyen las hojas CSS compartidas del sistema.
11. Todas las vistas incluyen JavaScript común o propio.
12. Se agregó filtrado funcional de tablas mediante `data-table-filter` en las vistas con buscadores locales.
13. Se corrigieron los paginadores que eran botones decorativos en Dependencias, Reasignaciones, Roles y Usuarios.
14. Se agregaron paginadores funcionales a Documentos, Series, Subseries y Plantilla.
15. Series ahora tiene Editar/Eliminar funcional.
16. Subseries ahora tiene Editar/Eliminar funcional.
17. Login carga también `odin-ui.js` para mantener la integración común de tema.
18. No quedaron botones HTML sin `type`, `onclick`, `data-action` o formulario asociado en la auditoría estática.

### Seguridad de acciones
19. `SecurityConfig` fue ajustado para que:
   - asignación use `asignar_tramites`/`gestionar_tramites`;
   - cambio de estado use `gestionar_tramites`;
   - reasignación use `trasladar_radicado`;
   - cierre use `finalizar_radicado`;
   - observaciones puedan ejecutarse con `ver_radicados`/`gestionar_tramites`.
   Esto evita que las acciones de la bandeja sean bloqueadas por el matcher genérico de POST.

## 4. Matriz final template → controller → acciones

| # | Template | Controller/View | Acciones principales | Estado |
|---:|---|---|---|---|
| 1 | auth/login.html | LoginController + Spring Security | Login, recuperación, tema | OK |
| 2 | auth/recuperar-password.html | PasswordResetController | Solicitar/validar recuperación | OK |
| 3 | auth/restablecer-password.html | PasswordResetController | Restablecer contraseña | OK |
| 4 | bitacora/bitacora.html | BitacoraView | Filtros de radicado/usuario/acción | OK |
| 5 | consulta/consulta.html | ConsultaView | Consulta, filtros y detalle | OK |
| 6 | dashboard/dashboard.html | DashboardController | KPIs y navegación a bandeja | OK |
| 7 | dependencias/dependencias.html | Dependenciasview | Nuevo, editar, eliminar, PDF, Excel, búsqueda, paginación | OK |
| 8 | dependencias/dependenciasForm.html | Dependenciasview | Guardar/editar | OK |
| 9 | documentos/documentos.html | DocumentosView | Nuevo, editar, abrir, eliminar, búsqueda, paginación | OK* |
| 10 | documentos/documentosForm.html | DocumentosView | Guardar, editar, cargar archivo | OK |
| 11 | error/403.html | ErrorController | Regreso al dashboard | OK |
| 12 | estados/estados.html | EstadosView | Nuevo, editar, eliminar, paginación | OK |
| 13 | estados/estadosForm.html | EstadosView | Guardar/editar | OK |
| 14 | fragments/layout.html | Fragmento compartido | Menú, tema, notificaciones, logout | OK |
| 15 | Plantilla/Plantilla.html | PlantillaView | Nuevo, editar, eliminar, búsqueda, paginación | OK |
| 16 | Plantilla/PlantillaForm.html | PlantillaView | Guardar/editar trazabilidad | OK |
| 17 | radicados/radicacion_Documental.html | RadicadosView | Radicar, adjuntar, guardar | OK |
| 18 | radicados/radicado-detalle.html | RadicadoDetalleView + REST | Observaciones, comprobante, firma, historial | OK |
| 19 | reasignaciones/reasignaciones.html | ReasignacionesView | Nuevo, editar, eliminar, PDF, Excel, búsqueda, paginación | OK |
| 20 | reasignaciones/reasignacionesForm.html | ReasignacionesView | Guardar/editar | OK |
| 21 | reportes/reportes.html | ReportesViewController + ReportesController | PDF, CSV, XLSX | OK |
| 22 | roles/roles.html | Rolesview | Nuevo, editar, eliminar, PDF, Excel, búsqueda, paginación | OK |
| 23 | roles/rolesForm.html | Rolesview + RolPermisosRepository | Guardar rol y permisos | OK |
| 24 | series/series.html | SeriesView | Nuevo, editar, eliminar, búsqueda, paginación | OK |
| 25 | series/seriesForm.html | SeriesView | Guardar/editar | OK |
| 26 | subseries/subseries.html | SubseriesView | Nuevo, editar, eliminar, búsqueda, paginación | OK |
| 27 | subseries/subseriesForm.html | SubseriesView | Guardar/editar | OK |
| 28 | tramites/ges_tramites.html | TramitesView + RadicadosController | Filtros, asignar, estado, reasignar, cerrar, catálogo | OK |
| 29 | tramites/tramitesForm.html | TramitesView | Guardar/editar tipo de trámite | OK |
| 30 | usuarios/usuarios.html | UsuariosView | Nuevo, editar, eliminar, PDF, Excel, búsqueda, paginación | OK |
| 31 | usuarios/usuariosForm.html | UsuariosView | Guardar/editar | OK |

\* La acción de abrir un documento depende de que el archivo físico esté disponible en el almacenamiento configurado.

## 5. Validación de JavaScript y CSS

- `script.js`: validación de sintaxis Node.js: OK.
- `odin-ui.js`: validación de sintaxis Node.js: OK.
- Las cinco hojas CSS están referenciadas por las vistas.
- La bandeja usa `data-action` y los endpoints existen:
  - PATCH `/api/radicados/{id}/asignar`
  - PATCH `/api/radicados/{id}/estado`
  - POST `/api/radicados/{id}/reasignar`
  - PATCH `/api/radicados/{id}/cerrar`
- Firmas usa POST `/api/firmas`, existente.
- Notificaciones usa `/api/notificaciones/resumen`, `/api/notificaciones/` y PUT `/{id}/leer`, existentes.

## 6. Supabase / base de datos

Proyecto validado: `Odin` (`qafgkqsqzinxenyosmln`), estado `ACTIVE_HEALTHY`, PostgreSQL 17.6.1.

Datos actuales verificados:

- roles: 5
- usuarios: 5
- dependencias: 4
- estados: 7
- trámites: 8
- series CCD: 11
- subseries CCD: 6
- radicados: 33
- documentos: 2
- reasignaciones: 3
- historial_radicado: 23
- plantilla: 13
- notificaciones: 3

Integridad referencial comprobada sobre las relaciones principales:

- radicados → trámites huérfanos: 0
- radicados → estados huérfanos: 0
- radicados → dependencias huérfanas: 0
- radicados → usuarios huérfanos: 0
- radicados → series huérfanas: 0
- radicados → subseries huérfanas: 0
- documentos → radicados huérfanos: 0

La función `public.siguiente_numero_radicado` requerida por `RadicadosView` existe.

## 7. Conexión frontend → backend → Supabase

La arquitectura encontrada es:

`Thymeleaf/HTML + JS → Spring Boot → JPA/JDBC → PostgreSQL Supabase`

No se encontró `supabase-js` ni conexión directa del navegador a Supabase. Esto es correcto para la arquitectura actual: el navegador habla con Spring y Spring habla con PostgreSQL.

La configuración de backend está preparada para:

- `SUPABASE_DB_URL`
- `SUPABASE_DB_USERNAME`
- `SUPABASE_DB_PASSWORD`

Sin embargo, el ZIP no contiene `.env` ni credenciales. Por seguridad no se deben incluir en el entregable.

Por tanto:
- **Supabase está operativo:** comprobado mediante acceso administrativo y consultas reales.
- **Compatibilidad backend ↔ esquema:** comprobada estáticamente y contra el esquema actual.
- **Conexión runtime del proceso Spring Boot:** no puede certificarse al 100 % desde este ZIP porque no contiene las credenciales de ejecución.
- El Maven Wrapper intentó descargar Maven 3.9.11 desde Maven Central, pero el entorno de auditoría no tuvo acceso a esa descarga; por ello no se pudo ejecutar una prueba E2E real del servidor.

## 8. Hallazgo de datos documentales

Los dos registros actuales de `documentos` tienen rutas físicas heredadas de otro equipo/instalación. Ejemplos encontrados:

- `c:local/escritorio`
- una ruta bajo `C:\Users\julia\...\uploads\...`

El código corregido permite guardar nuevos archivos en el `app.upload-dir` actual, pero esos dos archivos históricos no están dentro del ZIP ni en el almacenamiento de la instancia auditada. Su botón de apertura puede devolver 404 hasta migrar esos archivos.

Esto es un **pendiente de datos/almacenamiento**, no un fallo de enlace template → controller.

## 9. Seguridad Supabase

Supabase reporta RLS habilitado pero sin políticas en las 30 tablas públicas auditadas.

Esto no bloquea el backend actual si este usa conexión PostgreSQL privilegiada y la autorización se ejecuta en Spring Security; pero es un riesgo de seguridad si las tablas se exponen directamente mediante Data API.

No se agregaron políticas genéricas porque ODIN usa autenticación propia en `usuarios` y no Supabase Auth; crear políticas basadas ciegamente en `auth.uid()` sería incorrecto.

## 10. Estado de cierre

### Funcionalmente corregido a nivel de código
- Vistas y rutas.
- Formularios.
- Botones.
- Buscadores locales.
- Paginación.
- Acciones de bandeja.
- Carga documental.
- Descarga/apertura documental.
- Trazabilidad.
- Reasignaciones.
- Seguridad de endpoints específicos.

### Pendientes externos al código
1. Probar arranque real de Spring Boot con las variables de entorno del despliegue.
2. Ejecutar pruebas E2E en navegador con usuarios reales de cada rol.
3. Migrar los dos archivos documentales históricos que tienen rutas de otra máquina.
4. Definir e implementar la estrategia RLS/Data API si Supabase será accesible directamente fuera del backend.

## Conclusión

La matriz de las 31 vistas queda en **OK a nivel estático e integración de código**, sin vistas huérfanas y sin botones decorativos sin destino. La base Supabase está activa y sus relaciones principales no presentan huérfanos.

La certificación final de operación real queda condicionada únicamente a ejecutar ODIN con las credenciales de Supabase del entorno objetivo y realizar la prueba E2E por rol. El código entregado queda preparado para esa validación.
