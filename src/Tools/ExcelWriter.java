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

    public static int save(ArrayList<Transaction> list) { return save(list, "output.xls"); }
    public static int save(ArrayList<Transaction> list, String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("TKO-äly");

        int rownum = 0;
        for(int i=0; i<list.size(); i++) {
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(1);
            cell.setCellValue((String) list.get(i).date);
            cell = row.createCell(2);
            cell.setCellValue((String) list.get(i).getMessage() + " " + list.get(i).name.toUpperCase());
            if(list.get(i).getSum() > 0) {
                cell = row.createCell(11);
                cell.setCellValue(Math.abs(list.get(i).getSum()));
            } else {
                cell = row.createCell(12);
                cell.setCellValue(Math.abs(list.get(i).getSum()));
            }
            int a = getCell(list.get(i).type, list.get(i).getSum());
            if(a>0) {
                cell = row.createCell(a);
                cell.setCellValue(Math.abs(list.get(i).getSum()));
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int getCell(String account, double sum) {
        ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Account WHERE name='"+account+"';");
        try {
            if(rs.next()) {
                if(sum < 0) return rs.getInt("excel_column+");
                if(sum >= 0) return rs.getInt("excel_column-");
            } else {
                System.out.println("No such event");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
        /*
        if(a== Transaction.Type.SAADUT_ENNAKOT && sum < 0) return 15;
        if(a== Transaction.Type.SAADUT_ENNAKOT && sum >= 0) return 16;
        if(a== Transaction.Type.MAKSETUT_ENNAKOT && sum < 0) return 17;
        if(a== Transaction.Type.MAKSETUT_ENNAKOT && sum >= 0) return 18;
        if(a== Transaction.Type.SIIRTOVELAT && sum < 0) return 19;
        if(a== Transaction.Type.SIIRTOVELAT && sum >= 0) return 20;
        if(a== Transaction.Type.SIIRTOSAAMISET && sum < 0) return 21;
        if(a== Transaction.Type.SIIRTOSAAMISET && sum >= 0) return 22;
        if(a== Transaction.Type.RAHANSIIRROT_JA_TÄSMÄYKSET && sum < 0) return 23;
        if(a== Transaction.Type.RAHANSIIRROT_JA_TÄSMÄYKSET && sum >= 0) return 24;
        if(a== Transaction.Type.RUOKAVÄLITYKSEN_VARASTO && sum < 0) return 25;
        if(a== Transaction.Type.RUOKAVÄLITYKSEN_VARASTO && sum >= 0) return 26;
        if(a== Transaction.Type.TKOÄLYN_TUOTTEIDEN_VARASTO && sum < 0) return 27;
        if(a== Transaction.Type.TKOÄLYN_TUOTTEIDEN_VARASTO && sum >= 0) return 28;
        if(a== Transaction.Type.KULTTUURITOIMINTA && sum >= 0) return 30;
        if(a== Transaction.Type.LIIKUNTATOIMINTA && sum >= 0) return 32;
        if(a== Transaction.Type.RUOKAVÄLITYS && sum >= 0) return 34;
        if(a== Transaction.Type.README && sum >= 0) return 36;
        if(a== Transaction.Type.RISTEILYT && sum >= 0) return 38;
        if(a== Transaction.Type.SAUNAILLAT && sum >= 0) return 40;
        if(a== Transaction.Type.SITSIT && sum >= 0) return 42;
        if(a== Transaction.Type.VUOSIJUHLA && sum >= 0) return 44;
        if(a== Transaction.Type.KERHOTOIMINTA && sum >= 0) return 46;
        if(a== Transaction.Type.TEEMAILLAT && sum >= 0) return 48;
        if(a== Transaction.Type.KALUSTEHANKINNAT && sum >= 0) return 50;
        if(a== Transaction.Type.KOULUTUS && sum >= 0) return 52;
        if(a== Transaction.Type.MUU_YHDISTYSTOIMINTA && sum >= 0) return 54;
        if(a== Transaction.Type.ATKYTP && sum >= 0) return 56;
        if(a== Transaction.Type.MUU_HUVITOIMINTA && sum >= 0) return 58;
        if(a== Transaction.Type.JÄSENMAKSUT && sum >= 0) return 60;
        if(a== Transaction.Type.TKOÄLYTUOTTEET && sum >= 0) return 62;
        if(a== Transaction.Type.MUU_VARAINHANKINTA && sum >= 0) return 64;
        if(a== Transaction.Type.LEHTIAVUSTUS && sum >= 0) return 66;
        if(a== Transaction.Type.HYYN_TOIMINTAAVUSTUS && sum >= 0) return 68;
        if(a== Transaction.Type.HAALARIMAINOKSET && sum >= 0) return 70;
        if(a== Transaction.Type.ACCENTUREN_YHTEISTYÖSOPIMUS && sum >= 0) return 72;
        if(a== Transaction.Type.YHTEISTYÖSOPIMUKSET && sum >= 0) return 74;
        if(a== Transaction.Type.APURAHAT && sum >= 0) return 76;
        if(a== Transaction.Type.KORKOTUOTOT && sum >= 0) return 78;
        if(a== Transaction.Type.FUKSITOIMINTA && sum < 0) return 79;
        if(a== Transaction.Type.TEEMAILLAT && sum < 0) return 81;
        if(a== Transaction.Type.KERHOTOIMINTA && sum < 0) return 83;
        if(a== Transaction.Type.KULTTUURITOIMINTA && sum < 0) return 85;
        if(a== Transaction.Type.LEHTITILAUKSET && sum < 0) return 87;
        if(a== Transaction.Type.LIIKUNTATOIMINTA && sum < 0) return 89;
        if(a== Transaction.Type.RUOKAVÄLITYS && sum < 0) return 91;
        if(a== Transaction.Type.README && sum < 0) return 93;
        if(a== Transaction.Type.MUU_JÄSENTOIMINTA && sum < 0) return 95;
        if(a== Transaction.Type.RISTEILYT && sum < 0) return 97;
        if(a== Transaction.Type.SAUNAILLAT && sum < 0) return 99;
        if(a== Transaction.Type.SITSIT && sum < 0) return 101;
        if(a== Transaction.Type.VUOSIJUHLA && sum < 0) return 103;
        if(a== Transaction.Type.ATKYTP && sum < 0) return 105;
        if(a== Transaction.Type.MUU_HUVITOIMINTA && sum < 0) return 107;
        if(a== Transaction.Type.KALUSTEHANKINNAT && sum < 0) return 109;
        if(a== Transaction.Type.LAITEHANKINNAT && sum < 0) return 111;
        if(a== Transaction.Type.MUUT_HANKINNAT && sum < 0) return 113;
        if(a== Transaction.Type.EDUSTUS && sum < 0) return 115;
        if(a== Transaction.Type.HALLINTO && sum < 0) return 117;
        if(a== Transaction.Type.KOULUTUS && sum < 0) return 119;
        if(a== Transaction.Type.PALVELUMAKSUT && sum < 0) return 121;
        if(a== Transaction.Type.TIEDOTUS && sum < 0) return 123;
        if(a== Transaction.Type.MUU_YHDISTYSTOIMINTA && sum < 0) return 125;
        if(a== Transaction.Type.TKOÄLYTUOTTEET && sum < 0) return 127;
        if(a== Transaction.Type.MUU_VARAINHANKINTA && sum < 0) return 129;
        return -1;*/
    }
}
