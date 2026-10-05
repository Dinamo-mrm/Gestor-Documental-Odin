# ODIN — Informe de cierre técnico

Fecha: 2026-10-02
Base oficial: Supabase PostgreSQL
Arquitectura: Spring Boot + Thymeleaf + JPA/Hibernate

## Resultado ejecutivo

La versión entregada integra las interfaces Thymeleaf con controladores MVC, servicios/repositorios JPA y la base oficial Supabase. Se corrigieron rutas de formularios, se añadió la vista de Consulta y Seguimiento, se eliminó la dependencia de `X-User-Id`, se reforzó la autorización de rutas, se incorporó la administración de `rol_permisos`, y se mantuvieron los módulos de radicación, documentos, expedientes, firmas, auditoría, notificaciones, búsqueda y reportes.

La compilación completa no puede certificarse en este entorno porque Maven Wrapper requiere descargar Maven 3.9.11 desde Maven Central y la red de ejecución bloquea esa descarga. Por ello, la evidencia de código es estática; la prueba de ejecución debe realizarse en el equipo de despliegue.

## Correcciones realizadas

- Actor de radicación obtenido de la sesión autenticada.
- Eliminadas todas las referencias a `X-User-Id`.
- Seguridad específica para radicación, documentos, trámites, consulta, bitácora, reasignaciones y reportes.
- Vista `ConsultaView` + plantilla `consulta/consulta.html` con búsqueda full-text y filtros.
- Corrección de `documentosForm.html`, que apuntaba erróneamente a usuarios.
- Corrección de `estadosForm.html`, que apuntaba erróneamente a documentos.
- Eliminación del campo de usuario manual en observaciones del detalle de radicado.
- Eliminación del duplicado de plantilla `templates/tramites/ges_tramites.html`.
- Protección de exportaciones administrativas mediante autorización de reportes.
- Credenciales externas mediante variables de entorno.
- `.gitignore` para `target/`, `.env`, `uploads/` y artefactos locales.
- El paquete no contiene `Bd/` ni `target/`.

## Estado por grupo

| Grupo | Estado | Evidencia |
|---|---|---|
| 1. Radicación y recepción | Implementado técnicamente | RadicadosView, RadicadosController, consecutivo E/S, comprobante PDF+QR, anexos, auditoría |
| 2. Gestión integral de trámites | Implementado técnicamente | TramitesView/Controller, filtros, asignación, vencimientos, notificaciones |
| 3. Consulta y seguimiento | Implementado técnicamente | ConsultaView, FTS PostgreSQL, detalle, historial, observaciones |
| 4. Administración y seguridad | Implementado técnicamente | Usuarios/Roles/rol_permisos, SecurityConfig, sesiones y auditoría |
| 5. Búsqueda y filtrado | Implementado técnicamente | `busqueda_tsv`, GIN, `websearch_to_tsquery` |
| 6. Reportes | Implementado técnicamente | PDF/Excel/CSV y exportaciones administrativas |
| 7. Notificaciones | Implementado técnicamente | API, contador, listado, marcar como leída |

## Pendientes para certificación operacional

1. Ejecutar `mvnw clean test/package` en un equipo con acceso a Maven Central.
2. Configurar `SUPABASE_DB_URL`, `SUPABASE_DB_USERNAME` y `SUPABASE_DB_PASSWORD` sin incluir valores reales en Git.
3. Ejecutar migraciones de `database/migrations/` sobre el proyecto Supabase oficial.
4. Probar login con cada rol y validar permisos contra `rol_permisos`.
5. Ejecutar pruebas E2E frontend → backend → Supabase para los 7 grupos.
6. Revisar las alertas del Supabase Advisor sobre políticas RLS y duplicidad de índices. Las tablas están protegidas por RLS, pero el Advisor informa tablas sin políticas; esto debe evaluarse según el modelo de acceso directo PostgreSQL de ODIN y cualquier Data API que se habilite.
7. Ejecutar la prueba de carga definida en `DEV-11_plan_pruebas_carga.md`.

## Criterio de entrega

Esta versión se considera **candidata a entrega técnica**, no certificado de producción, hasta completar las pruebas runtime y E2E indicadas arriba.
