package com.fss.core.fssCalculation.service.utility;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

public class PdfGenerator {

    public static void generateResultPdf(HttpServletResponse response,
                                         double Ixx, double df, double bm, Double customDeflection, Double givenIxx) throws IOException {

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // ✅ Add Logo
        try {
            ClassPathResource imgFile = new ClassPathResource("static/images/fsslogo.jpeg");
            Image logo = Image.getInstance(imgFile.getURL());
            logo.scaleToFit(500, 100);
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            // logo optional
        }

        // ✅ Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
        Paragraph title = new Paragraph("Glazing Calculation Result", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // ✅ Table for Results
        PdfPTable table = new PdfPTable(2); // 2 columns
        table.setWidthPercentage(80);
        table.setSpacingBefore(10f);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        PdfPCell header1 = new PdfPCell(new Phrase("Metric", headerFont));
        header1.setBackgroundColor(new Color(0, 102, 204));
        PdfPCell header2 = new PdfPCell(new Phrase("Value", headerFont));
        header2.setBackgroundColor(new Color(0, 102, 204));
        table.addCell(header1);
        table.addCell(header2);

        // ✅ Data rows
        table.addCell(new Phrase("Required Ixx", cellFont));
        table.addCell(new Phrase(Ixx + " ×10¹⁰ mm⁴", cellFont));

        table.addCell(new Phrase("Allowable Deflection", cellFont));
        table.addCell(new Phrase(df + " mm", cellFont));

        table.addCell(new Phrase("Bending Moment", cellFont));
        table.addCell(new Phrase(bm + " kN·m", cellFont));

        if(customDeflection != null) {
            table.addCell(new Phrase("Calculated deflection as per given Ixx = " + givenIxx, cellFont));
            table.addCell(new Phrase(customDeflection + " mm", cellFont));
        }


        document.add(table);

        document.close();
    }
}
