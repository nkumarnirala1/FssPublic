package com.fss.core.fssCalculation.service.utility;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

@Component
public class PdfGenerator {

    String libreOfficePath = "C:/Program Files/LibreOffice/program/soffice.exe";

    private static final String LIBRE_OFFICE_BIN = "soffice";


    public byte[] convertExcelToPdf(ByteArrayOutputStream excelStream) throws Exception {
        // Create temporary Excel file
        File tempExcel = File.createTempFile("excel_", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(tempExcel)) {
            excelStream.writeTo(fos);
        }

        // Create temporary PDF file
        File tempPdf = File.createTempFile("excel_", ".pdf");

        // Run LibreOffice command
        ProcessBuilder pb = new ProcessBuilder(
                libreOfficePath,
                "--headless",
                "--convert-to", "pdf",
                "--outdir", tempPdf.getParent(),
                tempExcel.getAbsolutePath()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();

        // Copy PDF content into byte array
        File generatedPdf = new File(tempPdf.getParent(),
                tempExcel.getName().replace(".xlsx", ".pdf"));
        byte[] pdfBytes = Files.readAllBytes(generatedPdf.toPath());

        // Cleanup temporary files
        tempExcel.delete();
        generatedPdf.delete();

        return pdfBytes;
    }


}
