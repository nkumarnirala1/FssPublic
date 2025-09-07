package com.fss.core.fssCalculation.service.ReportGen;


import com.fss.core.fssCalculation.modal.ExcelElement;
import com.fss.core.fssCalculation.persistance.S3Service;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ExcelSheetGenerator {

    @Autowired
    private ExcelCellMap excelCellMap;

    @Autowired
    private S3Service s3Service;


    private static final String S3_mullion_KEY = "mullion_design.xlsx"; // path inside bucket


    public ByteArrayOutputStream generateExcelReport(String sheetName, List<ExcelElement> excelElementList) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // Load file from classpath
        try (InputStream is = s3Service.downloadFile(S3_mullion_KEY)) {
            if (is == null) {
                throw new IOException("Resource not found in classpath: " + S3_mullion_KEY);
            }

            try (Workbook workbook = new XSSFWorkbook(is)) {

                // Get or create sheet
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    sheet = workbook.createSheet(sheetName);
                }

                for (ExcelElement excelElement : excelElementList) {
                    int rowIndex = excelElement.getRow();
                    int colIndex = excelElement.getCol();
                    String newValue = excelElement.getValue();

                    // Get or create row
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) {
                        row = sheet.createRow(rowIndex);
                    }

                    // Get or create cell
                    Cell cell = row.getCell(colIndex);
                    if (cell == null) {
                        cell = row.createCell(colIndex);
                    }

                    // Set new value
                    cell.setCellValue(newValue);
                }

                // Write changes to output stream
                workbook.write(bos);
            }
        }
        return bos;
    }

    public ArrayList<ExcelElement> enrichElements(HttpSession session) throws IOException {

        ArrayList<ExcelElement> excelElementArrayList = new ArrayList<>();

        Map<String, List<String>> cellMap = excelCellMap.loadMappings();


        for (Map.Entry<String, List<String>> entry : cellMap.entrySet()) {

            String name = entry.getKey();
            List<String> valueList = entry.getValue();

            double attributeValue = (session.getAttribute(name) instanceof Double)
                    ? (Double) session.getAttribute(name) : 0.0;


            for (String value : valueList) {
                int[] rc = getRowAndColumn(value);
                ExcelElement excelElement = new ExcelElement(name, Double.toString(attributeValue), rc[0], rc[1]);

                excelElementArrayList.add(excelElement);
            }
        }
        return excelElementArrayList;
    }

    public static int[] getRowAndColumn(String cell) {
        // Separate column letters and row numbers
        String columnPart = cell.replaceAll("[0-9]", "");
        String rowPart = cell.replaceAll("[A-Z]", "");

        // Convert column letters to number
        int columnNumber = 0;
        for (int i = 0; i < columnPart.length(); i++) {
            columnNumber = columnNumber * 26 + (columnPart.charAt(i) - 'A' + 1);
        }

        // Convert row part to integer
        int rowNumber = Integer.parseInt(rowPart);

        return new int[]{rowNumber > 0 ? rowNumber - 1 : 0, columnNumber > 0 ? columnNumber - 1 : 0};
    }


}
