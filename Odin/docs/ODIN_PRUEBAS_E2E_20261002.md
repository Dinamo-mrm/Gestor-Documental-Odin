# ODIN — Plan de pruebas E2E de entrega

## 1. Login y seguridad
- Login correcto con usuario válido.
- Login fallido y registro en `log_accesos`.
- Redirección a `/403` sin permiso.
- Máximo una sesión concurrente.
- Logout y cierre de sesión en `sesiones_usuario`.

## 2. Grupo 1 — Radicación
1. Abrir `/view/radicados/documental`.
2. Registrar entrada.
3. Verificar número `E-AAAA-######` en Supabase.
4. Adjuntar documento.
5. Descargar comprobante PDF.
6. Leer QR.
7. Consultar auditoría e historial.

## 3. Grupo 2 — Trámites
1. Abrir `/view/tramites`.
2. Filtrar por estado/dependencia/trámite.
3. Verificar vencimiento.
4. Asignar/reasignar.
5. Confirmar notificación.

## 4. Grupo 3 — Consulta
1. Abrir `/view/consulta`.
2. Buscar por número/asunto/remitente.
3. Filtrar estado/dependencia/trámite.
4. Abrir detalle.
5. Revisar historial, observaciones, anexos, firmas y auditoría.

## 5. Grupo 4 — Administración
1. Crear/editar usuario.
2. Crear/editar rol.
3. Abrir permisos del rol.
4. Asignar permiso.
5. Iniciar sesión con ese rol.
6. Confirmar acceso permitido/denegado.

## 6. Grupo 5 — Búsqueda
- Validar búsqueda exacta y por términos en español.
- Confirmar resultados contra `radicados.busqueda_tsv`.

## 7. Grupo 6 — Reportes
- PDF, Excel y CSV de radicados.
- PDF/Excel de usuarios, dependencias, roles y reasignaciones.
- Confirmar que no se exporten contraseñas.

## 8. Grupo 7 — Notificaciones
- Generar una notificación.
- Ver contador.
- Abrir listado.
- Marcar como leída.
- Confirmar persistencia.

## 9. Evidencia
Registrar para cada prueba: fecha/hora, usuario, resultado HTTP, captura de pantalla, consulta SQL de verificación y resultado esperado/obtenido.
