package com.fss.core.fssCalculation.modal.generic;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DownloadReportElement {

    String sheetName;
    List<Object> objectList;

    public DownloadReportElement(String sheetName) {
        this.sheetName = sheetName;
        this.objectList = new ArrayList<>();
    }
}
