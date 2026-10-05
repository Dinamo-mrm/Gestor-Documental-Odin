# ODIN — Estado DEV-12 / DEV-13 / DEV-14 / DEV-10 / DEV-03

Fecha: 02 de octubre de 2026

## DEV-12 — Contratos API / OpenAPI

Implementado técnicamente para los endpoints críticos:
- Dependencia `springdoc-openapi-starter-webmvc-ui`.
- `OpenApiConfig` con título, versión y descripción de la API ODIN.
- Rutas de documentación `/v3/api-docs` y `/swagger-ui.html`.
- Tags y operaciones documentadas en Radicados, Usuarios, Trámites, Documentos, Notificaciones, Expedientes, Firmas y Reportes.
- Swagger UI y OpenAPI quedan accesibles para documentación técnica.

**Validación pendiente:** ejecutar la aplicación y comprobar `/swagger-ui.html` y `/v3/api-docs` con el entorno Maven funcional.

## DEV-13 — Firmas y expedientes

Se cerró la brecha principal de interfaz:
- El detalle del radicado muestra firmas registradas.
- Permite registrar una solicitud de firma en estado `PENDIENTE`.
- El controlador de firmas deriva `id_usuario` de la sesión autenticada; ya no acepta que el cliente suplante ese identificador.
- El detalle muestra el expediente asociado y permite asociar un expediente existente.
- Se muestran anexos asociados al radicado.

**Alcance:** esto implementa el flujo de gestión/registro. No constituye una firma digital criptográfica certificada; la tabla `firmas` conserva campos de hash/certificado para una futura integración de firma electrónica/digital.

## DEV-14 — Estados de interfaz

Se mantiene la base común de estados `.odin-empty-state` y `.odin-feedback-*` y se continúa su aplicación progresiva a las vistas.

**Pendiente:** revisión visual exhaustiva de todas las vistas Thymeleaf.

## DEV-10 — Sesiones y seguridad

Se reforzó la experiencia de autorización:
- Página `/403` para acceso denegado en lugar de redirección silenciosa al dashboard.
- Swagger/OpenAPI se publica como documentación técnica.
- Se conserva auditoría de login exitoso, login fallido y logout.
- Se conserva control de una sesión concurrente por usuario.

**Pendiente:** prueba runtime de bloqueo por intentos, cierre efectivo de sesión y actualización de última actividad. El cifrado de contraseñas se mantiene mediante BCrypt.

## DEV-03 — Notificaciones

Se completó la interacción de usuario:
- Panel carga las notificaciones reales del usuario autenticado.
- Se muestra título, mensaje y fecha.
- Una notificación puede marcarse como leída.
- El contador se actualiza después de la acción.
- Las solicitudes mutantes incorporan token CSRF.

## Validación de build

No fue posible ejecutar el build en este entorno. `./mvnw -o -DskipTests package` sigue fallando porque Maven Wrapper intenta descargar Maven 3.9.11 desde `repo.maven.apache.org`, que no está disponible en el entorno.

Por tanto, el estado indicado aquí es **validación estática de código**, no certificación de ejecución.
