package _dashboard;

import java.util.HashMap;

public class Table {

    private final String table_name;
    private HashMap<String, String> columns;

    public Table(String table_name, String first_column, String first_type) {
        this.table_name = table_name;
        this.columns = new HashMap<String, String>();
        columns.put(first_column, first_type);
    }
    public String getTableName(){
        return this.table_name;
    }
    public void addColumn(String column_name, String column_type){
        this.columns.put(column_name, column_type);
    }
    public HashMap<String, String> getColumns(){
        return this.columns;
    }
}
