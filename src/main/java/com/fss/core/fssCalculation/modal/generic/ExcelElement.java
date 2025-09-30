package com.fss.core.fssCalculation.modal.generic;


import lombok.Data;

@Data
public class ExcelElement {

    int row;
    int col;
    String value;
    String name;

    public ExcelElement(String name, String value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExcelElement{" +
                "name='" + name +
                "value='" + value +
                "row=" + row +
                ", col=" + col +
                +'\'' +
                '}';
    }
}
