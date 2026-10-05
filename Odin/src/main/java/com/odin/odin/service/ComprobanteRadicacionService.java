package com.odin.odin.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.odin.odin.model.Radicados;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Plantilla base del Comprobante de Radicación ODIN.
 * Diseño institucional SENA (#39A900) + sello COPIA CONTROLADA (superior derecha).
 *
 * Endpoint existente: GET /api/radicados/{id}/comprobante
 */
@Service
public class ComprobanteRadicacionService {

    private static final DateTimeFormatter FECHA_GEN =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Identidad SENA / ODIN
    private static final Color SENA_GREEN = new Color(0x39, 0xA9, 0x00);
    private static final Color SENA_DARK = new Color(0x1F, 0x5C, 0x00);
    private static final Color SENA_LIGHT = new Color(0xE8, 0xF5, 0xE0);
    private static final Color GRAY = new Color(0x4A, 0x55, 0x68);
    private static final Color LIGHT_GRAY = new Color(0xF7, 0xFA, 0xFC);
    private static final Color BORDER = new Color(0xCB, 0xD5, 0xE0);
    private static final Color DARK = new Color(0x1A, 0x20, 0x2C);
    private static final Color STAMP_RED = new Color(0xC5, 0x30, 0x30);
    private static final Color STAMP_BG = new Color(0xFF, 0xF5, 0xF5);

    public byte[] generar(Radicados r, String baseUrl)
            throws IOException, DocumentException, WriterException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 56, 42);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setPageEvent(new HeaderFooterEvent());
        document.open();

        // —— Cabecera: título + sello COPIA CONTROLADA ——
        PdfPTable header = new PdfPTable(new float[]{1.55f, 1.15f});
        header.setWidthPercentage(100);
        header.setSpacingAfter(10);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setVerticalAlignment(Element.ALIGN_TOP);
        titleCell.setPadding(0);
        titleCell.setPaddingRight(8);

        Font fTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, SENA_DARK);
        Font fSub = FontFactory.getFont(FontFactory.HELVETICA, 8, GRAY);
        Paragraph pTitle = new Paragraph("COMPROBANTE DE RADICACIÓN", fTitle);
        pTitle.setSpacingAfter(2);
        Paragraph pSub = new Paragraph(
                "Sistema de Seguimiento y Gestión Documental ODIN\n"
                        + "Servicio Nacional de Aprendizaje — SENA", fSub);
        titleCell.addElement(pTitle);
        titleCell.addElement(pSub);
        header.addCell(titleCell);

        PdfPCell stampCell = buildStampCell(r);
        stampCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        stampCell.setVerticalAlignment(Element.ALIGN_TOP);
        stampCell.setBorder(Rectangle.NO_BORDER);
        stampCell.setPadding(0);
        header.addCell(stampCell);
        document.add(header);

        // —— Número de radicado destacado ——
        PdfPTable radBox = new PdfPTable(1);
        radBox.setWidthPercentage(100);
        radBox.setSpacingAfter(10);
        Font fRad = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, SENA_GREEN);
        PdfPCell radCell = new PdfPCell(new Phrase(
                "N.º de Radicado:  " + safe(r.getNumero_radicado()), fRad));
        radCell.setBackgroundColor(SENA_LIGHT);
        radCell.setBorderColor(SENA_GREEN);
        radCell.setBorderWidth(1.3f);
        radCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        radCell.setPadding(9);
        radBox.addCell(radCell);
        document.add(radBox);

        // —— Datos principales (2 columnas) ——
        String estado = r.getEstado() != null ? safe(r.getEstado().getNombre()) : "Registrado";
        String dependencia = r.getDependencias() != null
                ? safe(r.getDependencias().getNombre()) : "No definida";

        PdfPTable datos = new PdfPTable(2);
        datos.setWidthPercentage(100);
        datos.setSpacingAfter(8);
        addPair(datos, "Fecha de radicación", safe(r.getFecha_radicado()),
                "Remitente", safe(r.getRemitente()));
        addPair(datos, "Tipo de radicado", safe(r.getTipo_radicado()),
                "Dependencia destino", dependencia);
        addPair(datos, "Medio de recepción", safe(r.getMedio_recepcion()),
                "Fecha límite de respuesta", safe(r.getFecha_limite()));
        addPair(datos, "Estado", estado, "ID interno",
                r.getId_radicado() != null ? String.valueOf(r.getId_radicado()) : "—");
        document.add(datos);

        // —— Asunto ——
        document.add(sectionLabel("ASUNTO"));
        PdfPTable asuntoTbl = new PdfPTable(1);
        asuntoTbl.setWidthPercentage(100);
        asuntoTbl.setSpacingAfter(8);
        Font fAsunto = FontFactory.getFont(FontFactory.HELVETICA, 9, DARK);
        PdfPCell asuntoCell = new PdfPCell(new Phrase(safe(r.getAsunto()), fAsunto));
        asuntoCell.setBorderColor(SENA_GREEN);
        asuntoCell.setBorderWidth(0.8f);
        asuntoCell.setPadding(8);
        asuntoTbl.addCell(asuntoCell);
        document.add(asuntoTbl);

        // —— Clasificación documental ——
        document.add(sectionLabel("CLASIFICACIÓN DOCUMENTAL (CCD)"));
        PdfPTable ccd = new PdfPTable(2);
        ccd.setWidthPercentage(100);
        ccd.setSpacingAfter(12);
        addCcdCell(ccd, "Código Serie", safe(r.getCodigo_serie()));
        addCcdCell(ccd, "Código Subserie", safe(r.getCodigo_subserie()));
        document.add(ccd);

        // —— QR + verificación ——
        String url = (baseUrl == null ? "" : baseUrl)
                + "/api/radicados/" + r.getId_radicado() + "/comprobante";
        byte[] qrBytes = generarQr(url);
        Image qrImg = Image.getInstance(qrBytes);
        qrImg.scaleAbsolute(78, 78);

        document.add(buildVerifBlock(qrImg, url));

        // —— Nota legal ——
        Font fNote = FontFactory.getFont(FontFactory.HELVETICA, 7, GRAY);
        Paragraph note = new Paragraph(
                "Este comprobante corresponde al registro almacenado en el sistema ODIN y constituye "
                        + "constancia de la recepción del documento en la fecha y hora indicadas. "
                        + "El código QR permite la verificación en línea de su autenticidad. "
                        + "Documento de uso institucional — SENA Colombia. "
                        + "Copia controlada: cualquier reproducción no autorizada carece de validez oficial.",
                fNote);
        note.setAlignment(Element.ALIGN_CENTER);
        note.setSpacingBefore(6);
        document.add(note);

        document.close();
        return out.toByteArray();
    }

    // -------------------------------------------------------------------------
    // Sello COPIA CONTROLADA
    // -------------------------------------------------------------------------

    private PdfPCell buildStampCell(Radicados r) {
        PdfPTable stamp = new PdfPTable(1);
        stamp.setWidthPercentage(100);

        Font fStampTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, STAMP_RED);
        Font fStampSub = FontFactory.getFont(FontFactory.HELVETICA, 6, STAMP_RED);
        Font fLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6.5f, STAMP_RED);
        Font fValue = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, DARK);

        PdfPCell box = new PdfPCell();
        box.setBackgroundColor(STAMP_BG);
        box.setBorderColor(STAMP_RED);
        box.setBorderWidth(1.4f);
        // OpenPDF no soporta dash nativo en PdfPCell; borde sólido rojo institucional
        box.setPadding(6);

        Paragraph t = new Paragraph("COPIA CONTROLADA", fStampTitle);
        t.setAlignment(Element.ALIGN_CENTER);
        t.setSpacingAfter(1);
        box.addElement(t);

        Paragraph sub1 = new Paragraph("Documento oficial ODIN", fStampSub);
        sub1.setAlignment(Element.ALIGN_CENTER);
        box.addElement(sub1);
        Paragraph sub2 = new Paragraph("No reproducir sin autorización", fStampSub);
        sub2.setAlignment(Element.ALIGN_CENTER);
        sub2.setSpacingAfter(3);
        box.addElement(sub2);

        // Datos clave del sello
        String serie = safe(r.getCodigo_serie());
        String subserie = safe(r.getCodigo_subserie());
        String serieTxt = serie;
        if (!"No registrado".equals(subserie)) {
            serieTxt = serie + " / " + subserie;
        }
        String fechaCorta = safe(r.getFecha_radicado());
        if (fechaCorta.length() >= 10) {
            fechaCorta = fechaCorta.substring(0, 10);
        }
        String asuntoCorto = trunc(safe(r.getAsunto()), 42);

        box.addElement(stampRow("Radicado:", safe(r.getNumero_radicado()), fLabel, fValue));
        box.addElement(stampRow("Serie:", serieTxt, fLabel, fValue));
        box.addElement(stampRow("Fecha:", fechaCorta, fLabel, fValue));
        box.addElement(stampRow("Asunto:", asuntoCorto, fLabel, fValue));

        PdfPCell outer = new PdfPCell(box);
        outer.setBorder(Rectangle.NO_BORDER);
        outer.setPadding(0);
        return outer;
    }

    private Paragraph stampRow(String label, String value, Font fl, Font fv) {
        Paragraph p = new Paragraph();
        p.setLeading(10f);
        p.add(new Chunk(label + "  ", fl));
        p.add(new Chunk(value, fv));
        return p;
    }

    // -------------------------------------------------------------------------
    // Bloques auxiliares
    // -------------------------------------------------------------------------

    private PdfPTable buildVerifBlock(Image qrImg, String url) {
        PdfPTable outer = new PdfPTable(1);
        outer.setWidthPercentage(100);

        PdfPTable inner = new PdfPTable(new float[]{0.26f, 0.74f});
        inner.setWidthPercentage(100);

        PdfPCell qrCell = new PdfPCell(qrImg, false);
        qrCell.setBorder(Rectangle.NO_BORDER);
        qrCell.setPadding(7);
        qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        inner.addCell(qrCell);

        Font fVerifB = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, DARK);
        Font fVerif = FontFactory.getFont(FontFactory.HELVETICA, 8, DARK);
        Font fUrl = FontFactory.getFont(FontFactory.HELVETICA, 7.5f, SENA_GREEN);
        Font fGen = FontFactory.getFont(FontFactory.HELVETICA, 7, GRAY);

        Paragraph verifText = new Paragraph();
        verifText.add(new Chunk("Verificación electrónica\n\n", fVerifB));
        verifText.add(new Chunk(
                "Escanee el código QR o visite la URL para validar este comprobante:\n", fVerif));
        verifText.add(new Chunk(url + "\n\n", fUrl));
        verifText.add(new Chunk("Generado: " + FECHA_GEN.format(LocalDateTime.now()), fGen));

        PdfPCell txtCell = new PdfPCell();
        txtCell.setBorder(Rectangle.NO_BORDER);
        txtCell.setPadding(8);
        txtCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        txtCell.addElement(verifText);
        inner.addCell(txtCell);

        PdfPCell wrap = new PdfPCell();
        wrap.setBorderColor(BORDER);
        wrap.setBorderWidth(0.6f);
        wrap.setBackgroundColor(LIGHT_GRAY);
        wrap.setPadding(2);
        wrap.addElement(inner);
        outer.addCell(wrap);
        return outer;
    }

    private void addPair(PdfPTable table, String l1, String v1, String l2, String v2) {
        table.addCell(pairCell(l1, v1));
        table.addCell(pairCell(l2, v2));
    }

    private PdfPCell pairCell(String label, String value) {
        Font fl = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7.5f, GRAY);
        Font fv = FontFactory.getFont(FontFactory.HELVETICA, 9.5f, DARK);
        Paragraph p = new Paragraph();
        p.add(new Chunk(label + "\n", fl));
        p.add(new Chunk(value, fv));
        PdfPCell c = new PdfPCell();
        c.setBackgroundColor(LIGHT_GRAY);
        c.setBorderColor(BORDER);
        c.setBorderWidth(0.4f);
        c.setPadding(6);
        c.addElement(p);
        return c;
    }

    private void addCcdCell(PdfPTable table, String label, String value) {
        Font fl = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7.5f, GRAY);
        Font fv = FontFactory.getFont(FontFactory.HELVETICA, 9.5f, DARK);
        Paragraph p = new Paragraph();
        p.add(new Chunk(label + "\n", fl));
        p.add(new Chunk(value, fv));
        PdfPCell c = new PdfPCell();
        c.setBackgroundColor(SENA_LIGHT);
        c.setBorderColor(SENA_GREEN);
        c.setBorderWidth(0.5f);
        c.setPadding(6);
        c.addElement(p);
        table.addCell(c);
    }

    private Paragraph sectionLabel(String text) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, SENA_DARK);
        Paragraph p = new Paragraph(text, f);
        p.setSpacingBefore(2);
        p.setSpacingAfter(3);
        return p;
    }

    private byte[] generarQr(String contenido) throws WriterException, IOException {
        BitMatrix matrix = new QRCodeWriter()
                .encode(contenido, BarcodeFormat.QR_CODE, 280, 280);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        return out.toByteArray();
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "No registrado" : value.trim();
    }

    private String trunc(String value, int max) {
        if (value == null) return "No registrado";
        value = value.trim();
        if (value.length() <= max) return value;
        return value.substring(0, Math.max(0, max - 1)) + "…";
    }

    // -------------------------------------------------------------------------
    // Header / Footer barras institucionales
    // -------------------------------------------------------------------------

    private static class HeaderFooterEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Rectangle page = document.getPageSize();

            // Barra superior
            cb.setColorFill(SENA_GREEN);
            cb.rectangle(0, page.getTop() - 36, page.getWidth(), 36);
            cb.fill();
            cb.beginText();
            cb.setColorFill(Color.WHITE);
            cb.setFontAndSize(FontFactory.getFont(FontFactory.HELVETICA_BOLD).getBaseFont(), 8);
            cb.setTextMatrix(34, page.getTop() - 22);
            cb.showText("SISTEMA DE GESTIÓN DOCUMENTAL ODIN");
            cb.setFontAndSize(FontFactory.getFont(FontFactory.HELVETICA).getBaseFont(), 7.5f);
            String right = "SENA — Servicio Nacional de Aprendizaje";
            float rw = FontFactory.getFont(FontFactory.HELVETICA).getBaseFont()
                    .getWidthPoint(right, 7.5f);
            cb.setTextMatrix(page.getWidth() - 34 - rw, page.getTop() - 22);
            cb.showText(right);
            cb.endText();

            // Barra inferior
            cb.setColorFill(SENA_DARK);
            cb.rectangle(0, 0, page.getWidth(), 28);
            cb.fill();
            cb.beginText();
            cb.setColorFill(Color.WHITE);
            cb.setFontAndSize(FontFactory.getFont(FontFactory.HELVETICA).getBaseFont(), 6.5f);
            String foot = "Documento generado electrónicamente • No requiere firma manuscrita • ODIN v2.0";
            float fw = FontFactory.getFont(FontFactory.HELVETICA).getBaseFont()
                    .getWidthPoint(foot, 6.5f);
            cb.setTextMatrix((page.getWidth() - fw) / 2, 10);
            cb.showText(foot);
            cb.endText();
        }
    }
}
