package com.fss.core.fssCalculation.service.utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.IOException;
import java.util.*;

@Component
public class ExcelCellMap {

    private static final String RESOURCE_PATH = "excelTemplate/ElementMapping.xlsx"; // classpath path

    public static Map<String, List<String>> loadMappings() throws IOException {
        Map<String, List<String>> cellMap = new LinkedHashMap<>();

        // Load file from classpath instead of file system
        try (InputStream is = ExcelCellMap.class.getClassLoader().getResourceAsStream(RESOURCE_PATH)) {
            if (is == null) {
                throw new IOException("Resource not found: " + RESOURCE_PATH);
            }

            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheet("Sheet1"); // adjust if sheet name is different

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // skip header

                    if (row.getCell(1) != null && row.getCell(2) != null) {
                        String element = row.getCell(1).getStringCellValue();
                        String locations = row.getCell(2).getStringCellValue();

                        List<String> cellLocations = Arrays.asList(locations.split(","));
                        cellMap.put(element, cellLocations);
                    }
                }
            }
        }
        return cellMap;
    }
}
