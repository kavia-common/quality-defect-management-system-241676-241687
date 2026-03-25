package com.example.backendapi.service;

import com.example.backendapi.domain.CorrectiveAction;
import com.example.backendapi.domain.Defect;
import com.lowagie.text.Chunk;
import java.awt.Color;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    public byte[] exportDefectsPdf(List<Defect> defects, List<CorrectiveAction> actions) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16, com.lowagie.text.Font.BOLD, Color.BLACK);
            doc.add(new Paragraph("Quality Defect Report", titleFont));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Defects"));
            doc.add(Chunk.NEWLINE);

            PdfPTable defectsTable = new PdfPTable(new float[]{1.2f, 2.8f, 1.6f, 1.8f, 2.0f});
            defectsTable.setWidthPercentage(100);

            addHeader(defectsTable, "ID");
            addHeader(defectsTable, "Title");
            addHeader(defectsTable, "Severity");
            addHeader(defectsTable, "Status");
            addHeader(defectsTable, "Reported Date");

            DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;
            for (Defect d : defects) {
                defectsTable.addCell(cell(String.valueOf(d.getId())));
                defectsTable.addCell(cell(d.getTitle()));
                defectsTable.addCell(cell(d.getSeverity() == null ? "" : d.getSeverity().name()));
                defectsTable.addCell(cell(d.getStatus() == null ? "" : d.getStatus().name()));
                defectsTable.addCell(cell(d.getReportedDate() == null ? "" : d.getReportedDate().format(df)));
            }
            doc.add(defectsTable);

            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("Corrective Actions"));
            doc.add(Chunk.NEWLINE);

            PdfPTable actionsTable = new PdfPTable(new float[]{1.2f, 1.2f, 3.2f, 1.6f, 1.8f});
            actionsTable.setWidthPercentage(100);

            addHeader(actionsTable, "ID");
            addHeader(actionsTable, "Defect");
            addHeader(actionsTable, "Action");
            addHeader(actionsTable, "Status");
            addHeader(actionsTable, "Due Date");

            for (CorrectiveAction a : actions) {
                actionsTable.addCell(cell(String.valueOf(a.getId())));
                actionsTable.addCell(cell(a.getDefect() == null ? "" : String.valueOf(a.getDefect().getId())));
                actionsTable.addCell(cell(a.getAction()));
                actionsTable.addCell(cell(a.getStatus() == null ? "" : a.getStatus().name()));
                actionsTable.addCell(cell(a.getDueDate() == null ? "" : a.getDueDate().format(df)));
            }
            doc.add(actionsTable);

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF export", e);
        }
    }

    private void addHeader(PdfPTable table, String text) {
        PdfPCell c = new PdfPCell(new Phrase(text));
        c.setBackgroundColor(new Color(37, 99, 235)); // primary-ish
        c.setPhrase(new Phrase(text, new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, Color.WHITE)));
        c.setPadding(6);
        table.addCell(c);
    }

    private PdfPCell cell(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text == null ? "" : text));
        c.setPadding(5);
        return c;
    }
}
