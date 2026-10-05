package com.odin.odin.controller;

import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.ReasignacionesRepository;
import com.odin.odin.repository.RolesRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.Cell;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Controller
@RequestMapping("/view")
public class ExportacionesAdministrativasController {

    private final UsuariosRepository usuarios;
    private final DependenciasRepository dependencias;
    private final RolesRepository roles;
    private final ReasignacionesRepository reasignaciones;

    public ExportacionesAdministrativasController(UsuariosRepository usuarios,
                                                   DependenciasRepository dependencias,
                                                   RolesRepository roles,
                                                   ReasignacionesRepository reasignaciones) {
        this.usuarios = usuarios;
        this.dependencias = dependencias;
        this.roles = roles;
        this.reasignaciones = reasignaciones;
    }

    @GetMapping(value = "/usuarios/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<ByteArrayResource> usuariosExcel() throws Exception {
        return excel("Usuarios", "usuarios.xlsx",
                new String[]{"ID", "Rol", "Dependencia", "Nombre", "Identificación", "Correo", "Teléfono", "Estado"},
                usuarios.findAll().stream().map(u -> new String[]{
                        s(u.getId_usuario()), s(u.getId_rol()), s(u.getId_dependencia()), s(u.getNombre()),
                        s(u.getNum_identificacion()), s(u.getCorreo()), s(u.getTelefono()), s(u.getEstado())
                }).toList());
    }

    @GetMapping(value = "/dependencias/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<ByteArrayResource> dependenciasExcel() throws Exception {
        return excel("Dependencias", "dependencias.xlsx",
                new String[]{"ID", "Dependencia", "Estado", "Descripción", "Nombre"},
                dependencias.findAll().stream().map(d -> new String[]{
                        s(d.getId_dependencia()), s(d.getDependencia()), s(d.getEstado()), s(d.getDescripcion()), s(d.getNombre())
                }).toList());
    }

    @GetMapping(value = "/roles/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<ByteArrayResource> rolesExcel() throws Exception {
        return excel("Roles", "roles.xlsx",
                new String[]{"ID", "Nombre", "Estado", "Rol"},
                roles.findAll().stream().map(r -> new String[]{
                        s(r.getId_rol()), s(r.getNombre()), s(r.getEstado()), s(r.getRol())
                }).toList());
    }

    @GetMapping(value = "/reasignaciones/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<ByteArrayResource> reasignacionesExcel() throws Exception {
        return excel("Reasignaciones", "reasignaciones.xlsx",
                new String[]{"ID", "Radicado", "Usuario anterior", "Usuario nuevo", "Dependencia nueva", "Fecha"},
                reasignaciones.findAll().stream().map(r -> new String[]{
                        s(r.getId_reasignacion()), s(r.getId_radicado()), s(r.getId_usuario_anterior()),
                        s(r.getId_usuario_nuevo()), s(r.getId_dependencia_nueva()), s(r.getFecha())
                }).toList());
    }

    @GetMapping(value = "/usuarios/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> usuariosPdf() throws Exception {
        return pdf("Usuarios", "usuarios.pdf",
                new String[]{"ID", "Nombre", "Identificación", "Correo", "Estado"},
                usuarios.findAll().stream().map(u -> new String[]{
                        s(u.getId_usuario()), s(u.getNombre()), s(u.getNum_identificacion()), s(u.getCorreo()), s(u.getEstado())
                }).toList());
    }

    @GetMapping(value = "/dependencias/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> dependenciasPdf() throws Exception {
        return pdf("Dependencias", "dependencias.pdf",
                new String[]{"ID", "Nombre", "Estado", "Descripción"},
                dependencias.findAll().stream().map(d -> new String[]{
                        s(d.getId_dependencia()), s(d.getNombre()), s(d.getEstado()), s(d.getDescripcion())
                }).toList());
    }

    @GetMapping(value = "/roles/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> rolesPdf() throws Exception {
        return pdf("Roles", "roles.pdf",
                new String[]{"ID", "Nombre", "Estado", "Rol"},
                roles.findAll().stream().map(r -> new String[]{
                        s(r.getId_rol()), s(r.getNombre()), s(r.getEstado()), s(r.getRol())
                }).toList());
    }

    @GetMapping(value = "/reasignaciones/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> reasignacionesPdf() throws Exception {
        return pdf("Reasignaciones", "reasignaciones.pdf",
                new String[]{"ID", "Radicado", "Usuario anterior", "Usuario nuevo", "Dependencia nueva", "Fecha"},
                reasignaciones.findAll().stream().map(r -> new String[]{
                        s(r.getId_reasignacion()), s(r.getId_radicado()), s(r.getId_usuario_anterior()),
                        s(r.getId_usuario_nuevo()), s(r.getId_dependencia_nueva()), s(r.getFecha())
                }).toList());
    }

    private ResponseEntity<ByteArrayResource> excel(String sheetName, String filename, String[] headers, List<String[]> rows) throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet(sheetName);
            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }
            for (int r = 0; r < rows.size(); r++) {
                Row row = sheet.createRow(r + 1);
                String[] values = rows.get(r);
                for (int c = 0; c < headers.length; c++) {
                    row.createCell(c).setCellValue(c < values.length && values[c] != null ? values[c] : "");
                }
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            wb.write(out);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .body(new ByteArrayResource(out.toByteArray()));
        }
    }

    private ResponseEntity<byte[]> pdf(String title, String filename, String[] headers, List<String[]> rows) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();
        document.add(new Paragraph("ODIN - " + title));
        document.add(new Paragraph("Reporte generado desde el sistema de Gestión Documental"));
        Table table = new Table(headers.length);
        for (String header : headers) {
            table.addCell(new Cell(header));
        }
        for (String[] row : rows) {
            for (String value : row) {
                table.addCell(new Cell(value == null ? "" : value));
            }
        }
        document.add(table);
        document.close();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(out.toByteArray());
    }

    private static String s(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
