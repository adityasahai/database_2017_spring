package com.databasefrontend;

/**
 * Created by adityasahai on 17/03/17.
 */
public class TableData {
    private String[] columnNames;
    private Object[][] data;
    private int numRows;
    private int numCols;

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
        this.numCols = columnNames.length;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }
    public int getNumRows() {
        return this.numRows;
    }

    public void addColumn(String eachRowData, String newColName) {
        String[] newColList = new String[this.numCols + 1];
        System.arraycopy(this.columnNames, 0, newColList, 0, this.numCols);
        newColList[this.numCols] = newColName;
        this.columnNames = newColList;
        Object[][] newRows = new Object[this.numRows][this.numCols + 1];
        for (int i = 0; i < this.numRows; i++) {
            System.arraycopy(this.data[i], 0, newRows[i], 0, this.numCols);
            newRows[i][this.numCols] = eachRowData;
        }
        this.data = newRows;
    }
}
