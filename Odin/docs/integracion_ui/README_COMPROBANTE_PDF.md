# Comprobante de Radicación ODIN — Plantilla PDF

Plantilla base institucional para generar el **Comprobante de Radicación** desde ODIN.

## Contenido del paquete

| Archivo | Acción |
|---------|--------|
| `src/main/java/com/odin/odin/service/ComprobanteRadicacionService.java` | **Reemplazar** el servicio existente |
| `src/main/java/com/odin/odin/controller/RadicadosController_COMPROBANTE_SNIPPET.java` | Solo referencia (el endpoint ya existe) |
| `ejemplo/ODIN_Comprobante_Radicacion_Ejemplo.pdf` | PDF de referencia visual |

## Dependencias (ya en `pom.xml`)

```xml
<dependency>
  <groupId>com.github.librepdf</groupId>
  <artifactId>openpdf</artifactId>
  <version>2.0.3</version>
</dependency>
<dependency>
  <groupId>com.google.zxing</groupId>
  <artifactId>core</artifactId>
  <version>3.5.3</version>
</dependency>
<dependency>
  <groupId>com.google.zxing</groupId>
  <artifactId>javase</artifactId>
  <version>3.5.3</version>
</dependency>
```

## Integración (3 pasos)

1. Copiar `ComprobanteRadicacionService.java` sobre:
   ```
   src/main/java/com/odin/odin/service/ComprobanteRadicacionService.java
   ```
2. Verificar que el endpoint exista en `RadicadosController`:
   ```
   GET /api/radicados/{id}/comprobante
   ```
3. Recompilar y probar:
   ```bash
   mvn -q -DskipTests package
   # Abrir en navegador o:
   curl -o comprobante.pdf http://localhost:8080/api/radicados/1/comprobante
   ```

## Diseño incluido

- Barra superior / inferior institucionales (verde SENA `#39A900`)
- **Sello COPIA CONTROLADA** (superior derecha) con:
  - Radicado · Serie/Subserie · Fecha · Asunto
- Caja destacada del N.º de radicado
- Datos en dos columnas, asunto, CCD
- QR de verificación electrónica + URL
- Nota legal de copia controlada

## UI — enlace de descarga

En la vista de detalle del radicado (Thymeleaf), por ejemplo:

```html
<a th:href="@{/api/radicados/{id}/comprobante(id=${radicado.id_radicado})}"
   class="btn btn-outline" target="_blank" rel="noopener">
  Descargar comprobante PDF
</a>
```

## Notas

- El sello usa borde rojo sólido (OpenPDF no dibuja dash nativo en `PdfPCell`).
- El QR apunta a la misma URL del comprobante para verificación.
- Campos nulos se muestran como `"No registrado"`.
- Asunto en el sello se trunca a ~42 caracteres.
