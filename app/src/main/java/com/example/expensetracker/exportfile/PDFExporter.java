package com.example.expensetracker.exportfile;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.example.expensetracker.model.TransactionExp;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class PDFExporter {
    public static void exportToPDF(Context context, List<TransactionExp> transactions) {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/transactions.pdf";
        File file = new File(filePath);
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            Paragraph title = new Paragraph("Giao dịch")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(18);


            document.add(title);

            float[] columnWidths = {5, 3, 3, 3};
            Table table = new Table(columnWidths);

           // table.addCell(new Cell().add(new Paragraph("ID")));
            table.addCell(new Cell().add(new Paragraph("Category")));
            table.addCell(new Cell().add(new Paragraph("Amount")));
            table.addCell(new Cell().add(new Paragraph("Date")));
            table.addCell(new Cell().add(new Paragraph("Description")));

            for (TransactionExp transaction : transactions) {
               // table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getId()))));
                table.addCell(new Cell().add(new Paragraph(transaction.getCategory().getName())));
                table.addCell(new Cell().add(new Paragraph(transaction.getSpend().toString())));
                table.addCell(new Cell().add(new Paragraph(transaction.getCreatedAt().toString())));
                table.addCell(new Cell().add(new Paragraph(transaction.getNote())));
            }

            document.add(table);
            document.close();
            Log.d("PDFExporter", "PDF created successfully");

        } catch (FileNotFoundException e) {
            Log.e("PDFExporter", "File not found", e);
        } catch (Exception e) {
            Log.e("PDFExporter", "Error creating PDF", e);
        }
    }
}
