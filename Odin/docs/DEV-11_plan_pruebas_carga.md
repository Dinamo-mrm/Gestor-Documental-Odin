# ODIN — DEV-11 Plan de pruebas de carga

Objetivos del checklist: búsqueda < 2–3 s y carga de pantalla < 5 s.

## Escenarios
1. Login concurrente.
2. Consulta de radicados con texto completo.
3. Consulta filtrada por estado/dependencia/trámite.
4. Dashboard.
5. Generación de reporte.

## Carga sugerida
- 10 usuarios concurrentes: línea base.
- 25 usuarios concurrentes: carga objetivo inicial.
- 50 usuarios concurrentes: prueba de saturación exploratoria.

## Métricas
- p50, p95 y p99 de latencia.
- tasa de error.
- throughput.
- CPU/RAM de la JVM.
- conexiones PostgreSQL activas.
- consultas lentas.

## Criterio
Una ejecución se considera conforme cuando la operación evaluada mantiene p95 dentro del RNF definido por el proyecto y no presenta errores funcionales. La validación definitiva requiere ejecutar la aplicación desplegada y registrar resultados reales; este archivo es el plan, no evidencia de ejecución.
