package com.odin.odin.controller;

import com.odin.odin.model.Radicados;
import com.odin.odin.repository.RadicadosRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Exportación de reportes documentales")
public class ReportesController {

    private final RadicadosRepository repo;

    public ReportesController(RadicadosRepository repo) {
        this.repo = repo;
    }

    @GetMapping(value = "/radicados.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Exportar radicados a PDF")
    public ResponseEntity<byte[]> pdf() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, out);
            doc.open();
            doc.add(new Paragraph("ODIN — Reporte de Radicados"));
            doc.add(new Paragraph("Generado: " + java.time.LocalDateTime.now()));
            doc.add(Chunk.NEWLINE);

            Table t = new Table(7);
            String[] headers = {"Numero", "Tipo", "Remitente", "Asunto", "Fecha", "Prioridad", "Estado"};
            for (String h : headers) {
                t.addCell(h);
            }
            for (Radicados r : repo.findAll()) {
                t.addCell(n(r.getNumero_radicado()));
                t.addCell(n(r.getTipo_radicado()));
                t.addCell(n(r.getRemitente()));
                t.addCell(n(r.getAsunto()));
                t.addCell(n(r.getFecha_radicado()));
                t.addCell(n(r.getPrioridad()));
                t.addCell(n(r.getId_estado() == null ? null : String.valueOf(r.getId_estado())));
            }
            doc.add(t);
            doc.close();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=radicados.pdf")
                    .body(out.toByteArray());
        } catch (DocumentException e) {
            throw new IOException(e);
        }
    }

    @GetMapping(value = "/radicados.csv", produces = "text/csv")
    @Operation(summary = "Exportar radicados a CSV")
    public ResponseEntity<byte[]> csv() {
        StringBuilder s = new StringBuilder("Numero,Tipo,Remitente,Asunto,Fecha,Prioridad,Estado\n");
        for (Radicados r : repo.findAll()) {
            s.append(csv(r.getNumero_radicado())).append(',')
                    .append(csv(r.getTipo_radicado())).append(',')
                    .append(csv(r.getRemitente())).append(',')
                    .append(csv(r.getAsunto())).append(',')
                    .append(csv(r.getFecha_radicado())).append(',')
                    .append(csv(r.getPrioridad())).append(',')
                    .append(csv(r.getId_estado() == null ? null : String.valueOf(r.getId_estado())))
                    .append('\n');
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=radicados.csv")
                .body(s.toString().getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping(value = "/radicados.xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Operation(summary = "Exportar radicados a Excel")
    public ResponseEntity<byte[]> excel() throws IOException {
        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sh = wb.createSheet("Radicados");
            Row header = sh.createRow(0);
            String[] cols = {"Numero", "Tipo", "Remitente", "Asunto", "Fecha", "Prioridad", "Estado"};
            for (int i = 0; i < cols.length; i++) {
                header.createCell(i).setCellValue(cols[i]);
            }
            int rowIdx = 1;
            for (Radicados r : repo.findAll()) {
                Row x = sh.createRow(rowIdx++);
                String[] v = {
                        r.getNumero_radicado(),
                        r.getTipo_radicado(),
                        r.getRemitente(),
                        r.getAsunto(),
                        r.getFecha_radicado(),
                        r.getPrioridad(),
                        r.getId_estado() == null ? null : String.valueOf(r.getId_estado())
                };
                for (int i = 0; i < v.length; i++) {
                    x.createCell(i).setCellValue(v[i] == null ? "" : v[i]);
                }
            }
            wb.write(out);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=radicados.xlsx")
                    .body(out.toByteArray());
        }
    }

    private String n(String x) {
        return x == null ? "" : x;
    }

    private String csv(String x) {
        if (x == null) return "";
        return "\"" + x.replace("\"", "\"\"") + "\"";
    }
}
