package com.fss.core.fssCalculation.service.ReportGen;

import com.fss.core.fssCalculation.persistance.S3Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.IOException;
import java.util.*;

@Component
public class ExcelCellMap {

    @Autowired
    private S3Service s3Service;

    private static final String S3_KEY = "ElementMapping.xlsx"; // path inside bucket

    public  Map<String, Map<String, List<String>> >   loadMappings(ArrayList<String> mullionDesignList, Map<String, Map<String, List<String>> > sheetWiseCellMap) throws IOException {


        try (InputStream is = s3Service.downloadFile(S3_KEY)) {
            if (is == null) {
                throw new IOException("Resource not found: " + S3_KEY);
            }

            try (Workbook workbook = new XSSFWorkbook(is)) {
                // Sheet sheet = workbook.getSheet("Sheet1"); // adjust if sheet name is different

                for (String mullionSheetName : mullionDesignList) {

                    Map<String, List<String>> singleSheetCellMap = sheetWiseCellMap.get(mullionSheetName);

                    String mappingSheetName= mullionSheetName+"_mapping";
                    Sheet sheet = workbook.getSheet(mappingSheetName); // adjust if sheet name is different

                    for (Row row : sheet) {
                        if (row.getRowNum() == 0) continue; // skip header

                        if (row.getCell(2) != null && row.getCell(1) != null) {
                            String element = row.getCell(2).getStringCellValue();
                            String locations = row.getCell(1).getStringCellValue();

                            List<String> cellLocations = new ArrayList<>(Arrays.asList(locations.split(",")));

                            if (singleSheetCellMap.get(element) == null) {
                                singleSheetCellMap.put(element, cellLocations);
                            } else {
                                List<String> existingLoc = singleSheetCellMap.get(element);
                                existingLoc.addAll(cellLocations);
                                singleSheetCellMap.put(element, existingLoc);
                            }
                        }
                    }
                }
            }
        }
        return sheetWiseCellMap;
    }
}
