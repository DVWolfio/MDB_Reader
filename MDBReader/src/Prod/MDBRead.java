package Prod;

import com.healthmarketscience.jackcess.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by s022890 on 07/09/2017.
 */
public class MDBRead {

//    public static String fileName = "C:\\!work_folder\\test.mdb";
    private static String fileName ; //= "C:\\!work_folder\\work_sch(28-08-2014)_p\\work_sch(28-08-2014)_p\\work_sch(28-08-2014) - Copy.mdb";

    public static void setFileName(String fileName) {
        MDBRead.fileName = fileName;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void main(String[] args) throws IOException {

        /*connect to .mdb
        вариант ucanaccess имеет минус в части скорости, и ограниченности по количеству чтений файла (не более 1 раза после открытия).
        https://stackoverflow.com/questions/22984438/java-lang-classnotfoundexception-sun-jdbc-odbc-jdbcodbcdriver-exception-occurri
        Для удобства, назовем таблицы MS Access'а - секциями, чтобы не путать с таблицами данных
        */

      /*  Connection con = DriverManager.getConnection("jdbc:ucanaccess://C:/!work_folder/NRD_LIC_IMP.mdb;memory=false");
//        Connection con = DriverManager.getConnection("jdbc:ucanaccess://C:\\!work_folder\\work_sch(28-08-2014)_p\\work_sch(28-08-2014)_p\\work_sch(28-08-2014) - Copy.mdb;memory=false");

        ResultSet AllSections = con.getMetaData().getTables(null, null, null, null);
        ResultSet ClmnsOfTbl;
        int TbCnt = 0;
        while (AllSections.next()) {

            if (AllSections.getString("TABLE_NAME").equals("CLASS_ATTRIBUTES") || AllSections.getString("TABLE_NAME").equals("CLASS_TABLES")) {
                ClmnsOfTbl = con.createStatement().executeQuery("SELECT * FROM [" + AllSections.getString("TABLE_NAME") + "]");
                //вывод списка таблиц в файле с количеством колонок (не записей)
                System.out.println(++TbCnt + ". " + AllSections.getString("TABLE_NAME") + " (" + ClmnsOfTbl.getMetaData().getColumnCount() + ")");
                while (ClmnsOfTbl.next()) {
                    //вывод
                    for (int i = 1; i <= ClmnsOfTbl.getMetaData().getColumnCount(); i++) {
                        System.out.print("\t" + ClmnsOfTbl.getMetaData().getColumnLabel(i));
                    }
                    System.out.println();

                    for (int i = 1; i <= ClmnsOfTbl.getMetaData().getColumnCount(); i++) {
                        if (ClmnsOfTbl.getString(ClmnsOfTbl.getMetaData().getColumnLabel(i)) != null) {
                            System.out.print("\t" + ClmnsOfTbl.getString(ClmnsOfTbl.getMetaData().getColumnLabel(i)));
                        }
                    }
                }
                System.out.println();
            }
        }
        System.out.println("-------------------------------");
*/
        System.out.println(LocalDateTime.now());
        //вариант jackcess
//        Database db = DatabaseBuilder.open(new File("C:\\!work_folder\\NRD_LIC_IMP.mdb"));
        Database db = DatabaseBuilder.open(new File(fileName));
        db.setCharset(Charset.forName("windows-1251"));
        Set<String> tableList = db.getTableNames();
        Iterator<String> tbls = tableList.iterator();
        String tblName = "";
        Table table;
//        Row row = null;
        while (tbls.hasNext()) {
            tblName = tbls.next();

            table = db.getTable(tblName);
            if (tblName.equals("METHODS") ) {
            System.out.println(tblName + " " + "(" + table.getRowCount() + ")");
            for (Column column : table.getColumns()) {

                String columnName = column.getName();
                System.out.print("\t<" + columnName + ">"); //+ "\t\t" + "(" + column.getType() + ")");
            }
            System.out.println();

                for (Column column : table.getColumns()) {
                    String columnName = column.getName();
                    for (Row row : table) {
                        System.out.print("\t<**" + row.get(columnName)+"**>");
                    }
                }
            System.out.println();
            }

        }
        System.out.println("----------------");

        System.out.println(LocalDateTime.now());
        //System.out.println(db.getTable("SOURCES_LONG").toString());

/*
        try {
            Table table = DatabaseBuilder.open(new File("C:\\Users\\Public\\Database1.accdb")).getTable("Inventory");
            int numRows = table.getRowCount();
            String[] strArray = new String[numRows];
            int index = 0;
            for (Row row : table) {
                strArray[index++] = row.get("SerialNumber").toString();
            }
            System.out.println("The first item in the array is: " + strArray[0]);
            System.out.println("The last item in the array is: " + strArray[numRows - 1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
    }

    public static ArrayList<String> getTableList() throws IOException {
        ArrayList<String> result = new ArrayList<String>();
        Database db = DatabaseBuilder.open(new File(fileName));
        db.setCharset(Charset.forName("windows-1251"));
        Set<String> tableList = db.getTableNames();
        Iterator<String> tbls = tableList.iterator();
        String tblName = "";
        Table table;
        while (tbls.hasNext()) {
            tblName = tbls.next();
            table = db.getTable(tblName);
            result.add(tblName + " " + "(" + table.getRowCount() + ")");
        }
        return result;
    }

    public static ArrayList<TableColumn<ObservableList<String>, String>> getHeadTable(String selectedTable) throws IOException {
        ArrayList<TableColumn<ObservableList<String>, String>> result = new ArrayList<TableColumn<ObservableList<String>, String>>();
        Database db = DatabaseBuilder.open(new File(fileName));
        Set<String> tableList = db.getTableNames();
        Iterator<String> tbls = tableList.iterator();
        String tblName = "";
        Table table;
        while (tbls.hasNext()) {
            tblName = tbls.next();
            table = db.getTable(tblName);
            if (tblName.equals(selectedTable)) {
                for (Column column : table.getColumns()) {
                    result.add(new TableColumn<>(column.getName()));
                }
            }
        }
        return result;
    }

    public static ArrayList<String> getBodyTable(String selectedTable, int columnsCount) throws IOException {
        ArrayList<String> rowsByColumns = new ArrayList<String>();
        ArrayList<String> result = new ArrayList<String>();
//        System.out.println(LocalDateTime.now());
        Database db = DatabaseBuilder.open(new File(fileName));
        db.setCharset(Charset.forName("windows-1251")); // FIXME: 10/10/2017 сделать какойто инициализатор файла и кодировки
        Set<String> tableList = db.getTableNames();
        Iterator<String> tbls = tableList.iterator();
        String tblName = "";
        int rowCount=-1;
        Table table;
        while (tbls.hasNext()) {
            tblName = tbls.next();

            table = db.getTable(tblName);
            if (tblName.equals(selectedTable)) {
                rowCount = table.getRowCount();

                for (Column column : table.getColumns()) {
                    String columnName = column.getName();
                    for (Row row : table) {
                        if (row.get(columnName) != null) {
                            rowsByColumns.add(row.get(columnName).toString());
                        }
                        else if (row.get(columnName) == null) {
                            rowsByColumns.add((String) row.get(columnName));
                        }
                    }
                }

            }
        }
        //т.к. данные вычитываются изначально в разрезе колонок, а нужно в разрезе строк - делаем реверс
        for (int i = 0; i < rowCount; i++) {
            for (int j = i; j < rowsByColumns.size(); ) {
                result.add(rowsByColumns.get(j));
                j = j + rowCount;
            }
        }
        return result;
    }
}

