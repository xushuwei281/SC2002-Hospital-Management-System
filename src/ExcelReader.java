import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    public List<String[]> rowsData;

    public ExcelReader() {
        rowsData = new ArrayList<>();
    }
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();  // You can format the date here if needed
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();  // If you want to evaluate the formula, use a FormulaEvaluator
            case BLANK:
                return "";
            default:
                return "";
        }
    }


    // Method to read Doctor data from an Excel sheet
    public List<String[]> readDoctorsFromExcel(String filePath) {
        List<String[]> doctorsData = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                System.out.println("Sheet 'Doctors' does not exist.");
                return doctorsData;
            }
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip the header row
                Row row = sheet.getRow(i);
                if (row != null) {
                    String[] doctorData = new String[6];
                    for (int j = 0; j < 6; j++) {
                        doctorData[j] = getCellValueAsString(row.getCell(j));
                    }
                    doctorsData.add(doctorData);
                }
            }
        } catch (IOException e) {}
        return doctorsData;
    }

    // Method to read Patient data from an Excel sheet
    public List<String[]> readPatientsFromExcel(String filePath) {
        List<String[]> patientsData = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                System.out.println("Sheet 'Patients' does not exist.");
                return patientsData;
            }
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip the header row
                Row row = sheet.getRow(i);
                if (row != null) {
                    String[] patientData = new String[8];
                    for (int j = 0; j < 6; j++) {
                        patientData[j] = getCellValueAsString(row.getCell(j));
                    }
                    patientsData.add(patientData);
                }
            }
        } catch (IOException e){}
        return patientsData;
    }

    // Method to read Pharmacist data from an Excel sheet
    public List<String[]> readPharmacistsFromExcel(String filePath) {
        List<String[]> pharmacistsData = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                System.out.println("Sheet 'Pharmacists' does not exist.");
                return pharmacistsData;
            }
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip the header row
                Row row = sheet.getRow(i);
                if (row != null) {
                    String[] pharmacistData = new String[8];
                    for (int j = 0; j < 6; j++) {
                        pharmacistData[j] = getCellValueAsString(row.getCell(j));
                    }
                    pharmacistsData.add(pharmacistData);
                }
            }
        } catch (IOException e) {}
        return pharmacistsData;
    }

    // Method to read Administrator data from an Excel sheet
    public List<String[]> readAdministratorsFromExcel(String filePath) {
        List<String[]> administratorsData = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                System.out.println("Sheet 'Administrators' does not exist.");
                return administratorsData;
            }
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip the header row
                Row row = sheet.getRow(i);
                if (row != null) {
                    String[] administratorData = new String[6];
                    for (int j = 0; j < 6; j++) {
                        administratorData[j] = getCellValueAsString(row.getCell(j));
                    }
                    administratorsData.add(administratorData);
                }
            }
        } catch (IOException e) {}
        return administratorsData;
    }

    public List<String[]> readMedicationsFromExcel(String filePath) {
        List<String[]> medicationsData = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                System.out.println("Sheet 'Sheet1' does not exist.");
                return medicationsData;
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip the header row (row 0)
                Row row = sheet.getRow(i);
                if (row != null) {
                    String medicationName = getCellValueAsString(row.getCell(0)); // Medication Name in column 0
                    String initialStockStr = getCellValueAsString(row.getCell(1)); // Initial Stock in column 1
                    String lowStockAlertStr = getCellValueAsString(row.getCell(2)); // Low Stock Alert in column 2
                    medicationsData.add(new String[]{medicationName, initialStockStr, lowStockAlertStr});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while reading medications from the Excel file.");
        }
        return medicationsData;
    }


}