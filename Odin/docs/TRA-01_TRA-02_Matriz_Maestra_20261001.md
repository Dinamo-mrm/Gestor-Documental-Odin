# ODIN — Matriz maestra de trazabilidad y aceptación
Fecha: 2026-10-01

## Alcance
Esta matriz consolida el checklist oficial de corrección del 01/10/2026 (DOC-01..14, DEV-01..14 y TRA-01..06) y separa evidencia de código/documentación de validación de ejecución real.

## Regla de estado
- **Cumple técnicamente:** existe implementación identificable en el código/BD y el punto está cubierto técnicamente; la prueba runtime final puede seguir pendiente.
- **Parcial:** existe una parte implementada, pero falta UI, documentación exhaustiva o validación funcional.
- **Pendiente:** no se encontró evidencia suficiente de implementación/cierre.

## Resultado consolidado
- Cumple técnicamente: **8**
- Parcial: **9**
- Pendiente: **17**
- Total: **34**

El detalle editable está en `ODIN_Matriz_Maestra_TRA01_TRA02_20261001.xlsx`.

## Hallazgos de cierre
1. **DEV-12 permanece pendiente:** el `pom.xml` contiene `springdoc-openapi-starter-webmvc-ui`, pero la revisión estática no encontró anotaciones `@Operation`/`@Tag` ni contratos documentados de los endpoints críticos.
2. **DEV-13 permanece parcial:** existen modelos/repositorios/controladores de firmas y expedientes, pero no se encontró el flujo UI integrado en las vistas Thymeleaf.
3. **DEV-10 permanece parcial:** se implementó auditoría de login/logout y sesiones, pero las políticas de bloqueo por intentos y la validación de ejecución aún requieren pruebas reales.
4. **DEV-11 permanece parcial:** existe plan de carga, pero no resultados ejecutados.
5. **TRA-01 y TRA-02 quedan parciales:** la matriz y criterios ya están consolidados, pero la matriz oficial fuente y la ejecución de los criterios de aceptación todavía deben registrarse formalmente.
6. El build no se considera validado en este entorno porque Maven no pudo descargar su distribución desde Maven Central; por ello no se debe presentar el paquete como compilación aprobada.

## Criterios TRA-02 prioritarios
### Login
- Autenticación válida de usuario activo.
- Rechazo de usuario/rol inactivo.
- Registro de `LOGIN` y sesión `ACTIVA`.
- Registro de `LOGIN_FALLIDO` sin contraseña.
- Cierre de sesión y registro `LOGOUT`.

### Radicación
- Consecutivo E/S anual.
- Unicidad del número.
- Metadatos y documentos/anexos.
- Comprobante PDF con QR.
- Historial y auditoría.

### Dashboard
- KPI desde datos persistidos.
- Vencidos/próximos basados en vencimiento persistido.
- Notificaciones del usuario autenticado.

### RBAC
- Permisos desde `rol_permisos`.
- Protección de endpoints administrativos.
- Aislamiento de notificaciones por usuario.
- Pruebas por cada rol.
