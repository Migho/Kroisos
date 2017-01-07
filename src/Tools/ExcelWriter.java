package Tools;

import Bookkeeping.SQLManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExcelWriter {

    /**
     * This method will create excel file "output.xls" containing given
     * transactions (ready to be pasted to bookkeeping).
     * @param list List of transactions.
     * @return Will return 0 if successful, -1 for file writing error, and -2 for IOException.
     */
    public static int save(ArrayList<Transaction> list) { return save(list, "output.xls"); }

    /**
     * This method will create excel file "filename.xls" containing given
     * transactions (ready to be pasted to bookkeeping).
     * @param list List of transactions.
     * @param fileName name of the file.
     * @return Will return 0 if successful, -1 for file writing error, and -2 for IOException.
     */
    public static int save(ArrayList<Transaction> list, String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("TKO-äly");

        int rownum = 0;
        for (Transaction t : list) {
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(1);
            cell.setCellValue((String) t.getDate());
            cell = row.createCell(2);
            cell.setCellValue((String) t.getMessage() + " " + t.getName().toUpperCase());
            if(t.getSum() > 0) {
                cell = row.createCell(11);
                cell.setCellValue(Math.abs(t.getSum()));
            } else {
                cell = row.createCell(12);
                cell.setCellValue(Math.abs(t.getSum()));
            }
            int a = getCell(t.getType(), t.getSum());
            if(a>0) {
                cell = row.createCell(a);
                cell.setCellValue(Math.abs(t.getSum()));
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
            //System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }
        return 0;
    }

    private static int getCell(String account, double sum) {
        ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Account WHERE name=?", new Object[] {account});
        try {
            if(rs.next()) {
                if(sum < 0) return rs.getInt("excel_column-");
                if(sum >= 0) return rs.getInt("excel_column+");
            } else {
                //System.out.println("No such event");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
