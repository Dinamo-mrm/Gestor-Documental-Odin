# ODIN — Estado de implementación DEV-04 / DEV-10 / DEV-13 / DEV-14

Fecha: 2026-10-02
Base oficial: Supabase PostgreSQL del proyecto ODIN.

## DEV-04 — Administración y seguridad

### Implementado
- Los endpoints mutables de radicados dejaron de confiar en `X-User-Id`.
- El actor de auditoría se obtiene desde `Authentication` / `OdinUserDetails`.
- Crear radicado asigna el usuario autenticado como responsable de creación.
- Actualizar radicado conserva el responsable persistido y registra la modificación con el actor autenticado.
- Cambio de estado requiere `editar_radicado`.
- Asignación y reasignación requieren `trasladar_radicado`.
- Cierre y eliminación requieren `finalizar_radicado`.
- Observaciones y asociación de expediente requieren `editar_radicado`.
- Documentos y nuevas versiones toman el usuario autenticado para `creado_por`.
- La interfaz de Roles incorpora gestión de permisos por rol sobre `rol_permisos`.

### Verificación estática
- No quedan referencias a `X-User-Id` en controladores Java ni JS del proyecto.
- Se confirmó en Supabase la existencia de los permisos granulares usados por estos controles.

### Pendiente
- Prueba de ejecución HTTP con perfiles de usuario reales.
- Verificación de bloqueo 403 por rol/permisos en navegador.
- Validación de actualización de `ultima_actividad` de sesión en cada request.

## DEV-10 — Seguridad de sesión y auditoría

### Implementado previamente
- Registro de login exitoso y fallido.
- Registro de logout.
- Persistencia en `sesiones_usuario` y `log_accesos`.
- Límite de una sesión concurrente.
- Página `/403` para accesos denegados.

### Pendiente
- Verificación runtime del cierre correcto de sesión.
- Actualización automática de última actividad durante la sesión.

## DEV-13 — Documentos, anexos, expedientes y firmas

### Implementado previamente
- Documentos y versiones persistidos.
- Versionado incremental.
- Expedientes y asociación al radicado.
- Auditoría del radicado.
- Consulta de anexos y firmas en detalle.
- Solicitud de firma desde el detalle.

### Pendiente
- Carga de nuevos anexos desde la interfaz de detalle.
- Historial visual completo de versiones y creación de nueva versión desde la interfaz.
- Integración criptográfica de firma electrónica/digital; actualmente se gestiona el registro y estado de la solicitud.

## DEV-14 — Estados de interfaz

### Implementado
- Estados base de vacío, éxito, error e información en componentes compartidos.
- Panel de notificaciones real y marcado como leído.
- Manejo de acceso denegado mediante `/403`.

### Pendiente
- Revisión exhaustiva de todas las vistas Thymeleaf para asegurar aplicación uniforme de estados, mensajes y componentes SENA.

## Exportaciones administrativas

Se agregaron controladores funcionales para las rutas ya utilizadas por la interfaz:
- `/view/usuarios/pdf` y `/view/usuarios/excel`
- `/view/dependencias/pdf` y `/view/dependencias/excel`
- `/view/roles/pdf` y `/view/roles/excel`
- `/view/reasignaciones/pdf` y `/view/reasignaciones/excel`

Los reportes de usuarios no incluyen el campo de contraseña.

## Limitación de validación

La compilación Maven no pudo ejecutarse en este entorno porque el Maven Wrapper intenta descargar Maven 3.9.11 desde `repo.maven.apache.org` y el entorno no dispone de esa descarga. Se realizó validación estática de estructura Java, referencias y balance de llaves; la validación final requiere ejecutar `./mvnw -DskipTests package` en un entorno con acceso al repositorio Maven o con Maven 3.9.11 disponible localmente.
