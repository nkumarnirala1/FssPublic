package com.fss.core.fssCalculation.service.ReportGen;


import com.fss.core.fssCalculation.modal.generic.ExcelElement;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelSheetGenerator {

    @Autowired
    private ExcelCellMap excelCellMap;

    @Autowired
    private S3Service s3Service;


    private static final String S3_mullion_KEY = "mullion_design.xlsx"; // path inside bucket


    public ByteArrayOutputStream generateExcelReport(List<String> sheetNameList, Map<String, ArrayList<ExcelElement>> excelElementListSheetMap) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // Load file from S3
        try (InputStream is = s3Service.downloadFile(S3_mullion_KEY);
             Workbook workbook = new XSSFWorkbook(is)) {  // Create workbook once

            if (is == null) {
                throw new IOException("Resource not found in S3: " + S3_mullion_KEY);
            }

            for (String sheetName : sheetNameList) {
                // Get or create sheet

                ArrayList<ExcelElement> excelElementList = excelElementListSheetMap.get(sheetName);
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    sheet = workbook.createSheet(sheetName);
                }

                // Fill sheet with data
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
            }

            // Write all changes once
            workbook.write(bos);
        }

        return bos;
    }

    public Map<String, ArrayList<ExcelElement>> enrichElements(ArrayList<String> mullionDesignList, HttpSession session) throws IOException {

        Map<String, ArrayList<ExcelElement>> mapOfexcelElementArrayList = new HashMap<>();

        Map<String, Map<String, List<String>>> sheetWiseCellMap = new HashMap<>();
        // transformation
        for (String sheetName : mullionDesignList) {
            sheetWiseCellMap.put(sheetName, new HashMap<>());
            mapOfexcelElementArrayList.put(sheetName, new ArrayList<>());
        }

        sheetWiseCellMap = excelCellMap.loadMappings(mullionDesignList, sheetWiseCellMap);

        for (Map.Entry<String, ArrayList<ExcelElement>> singleSheet : mapOfexcelElementArrayList.entrySet()) {

            ArrayList<ExcelElement> excelElementArrayList = singleSheet.getValue();
            String sheetName = singleSheet.getKey();

            for (Map.Entry<String, List<String>> entry : sheetWiseCellMap.get(sheetName).entrySet()) {

                String name = entry.getKey();
                List<String> valueList = entry.getValue();

                double attributeValue = (session.getAttribute(name) instanceof Double)
                        ? (Double) session.getAttribute(name) : 0.0;


                for (String value : valueList) {
                    int[] rc = getRowAndColumn(value.strip());
                    ExcelElement excelElement = new ExcelElement(name, Double.toString(attributeValue), rc[0], rc[1]);

                    excelElementArrayList.add(excelElement);
                }
            }
        }
        return mapOfexcelElementArrayList;
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
