// ---------------------------------------------------------------------------
// SNIPPET — Ya existe en RadicadosController.java
// Endpoint: GET /api/radicados/{id}/comprobante
// ---------------------------------------------------------------------------
//
// @Autowired private ComprobanteRadicacionService comprobanteService;
//
// @GetMapping("/{id}/comprobante")
// @Operation(summary = "Descargar comprobante de radicación en PDF")
// public ResponseEntity<byte[]> comprobante(
//         @PathVariable Long id,
//         jakarta.servlet.http.HttpServletRequest request) {
//
//     var opt = radicadosRepository.findById(id);
//     if (opt.isEmpty()) {
//         return ResponseEntity.notFound().build();
//     }
//     try {
//         Radicados r = opt.get();
//         String baseUrl = request.getScheme() + "://" + request.getServerName()
//                 + (request.getServerPort() == 80 || request.getServerPort() == 443
//                     ? "" : ":" + request.getServerPort());
//         byte[] pdf = comprobanteService.generar(r, baseUrl);
//         return ResponseEntity.ok()
//                 .header(HttpHeaders.CONTENT_DISPOSITION,
//                         "inline; filename=Comprobante-" + r.getNumero_radicado() + ".pdf")
//                 .contentType(MediaType.APPLICATION_PDF)
//                 .body(pdf);
//     } catch (Exception e) {
//         return ResponseEntity.internalServerError().build();
//     }
// }
