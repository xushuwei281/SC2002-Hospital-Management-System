import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class ExcelWriter {
    public void writePatientsToExcel(String filePath, ArrayList<Patient> patients) {
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                System.out.println("Sheet 'Sheet1' does not exist.");
                return;
            }

            for (int i = 0; i < patients.size(); i++) {
                Patient patient = patients.get(i);
                Row row = sheet.getRow(i + 1); // Assuming row 0 is the header
                if (row == null) {
                    row = sheet.createRow(i + 1);
                }

                // Write patient data to cells
                createOrUpdateCell(row, 0, patient.id);
                createOrUpdateCell(row, 1, patient.getName());
                createOrUpdateCell(row, 2, patient.contactInfo);
                createOrUpdateCell(row, 3, patient.dob);
                createOrUpdateCell(row, 4, patient.gender);
                createOrUpdateCell(row, 5, patient.bloodType);

                // Concatenate lists for pastDiagnoses, TreatmentPlan, and Prescription
                createOrUpdateCell(row, 6, String.join(", ", patient.pastDiagnoses));
                createOrUpdateCell(row, 7, String.join(", ", patient.TreatmentPlan));
                createOrUpdateCell(row, 8, String.join(", ", patient.Prescription));
            }

            // Write changes to the file
            try (FileOutputStream outputFile = new FileOutputStream(filePath)) {
                workbook.write(outputFile);
            }
            System.out.println("Patient data written to Excel successfully.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while writing to the Excel file.");
        }
    }

    private void createOrUpdateCell(Row row, int columnIndex, String value) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        cell.setCellValue(value);
    }

    public void writeDoctorsToExcel(String filePath, ArrayList<Doctor> doctors) {
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                System.out.println("Sheet 'Sheet1' does not exist.");
                return;
            }

            for (int i = 0; i < doctors.size(); i++) {
                Doctor doctor = doctors.get(i);
                Row row = sheet.getRow(i + 1); // Assuming row 0 is the header
                if (row == null) {
                    row = sheet.createRow(i + 1);
                }

                // Write doctor data to cells
                createOrUpdateCell(row, 0, doctor.id);
                createOrUpdateCell(row, 1, doctor.getName());
                createOrUpdateCell(row, 2, doctor.role);
                createOrUpdateCell(row, 3, doctor.gender);
                createOrUpdateCell(row, 4, doctor.age);

            }

            // Write changes to the file
            try (FileOutputStream outputFile = new FileOutputStream(filePath)) {
                workbook.write(outputFile);
            }
            System.out.println("Doctor data written to Excel successfully.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while writing doctors to the Excel file.");
        }
    }

    public void writePharmacistsToExcel(String filePath, ArrayList<Pharmacist> pharmacists) {
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                System.out.println("Sheet 'Sheet1' does not exist.");
                return;
            }

            for (int i = 0; i < pharmacists.size(); i++) {
                Pharmacist pharmacist = pharmacists.get(i);
                Row row = sheet.getRow(i + 1); // Assuming row 0 is the header
                if (row == null) {
                    row = sheet.createRow(i + 1);
                }

                // Write pharmacist data to cells
                createOrUpdateCell(row, 0, pharmacist.id);
                createOrUpdateCell(row, 1, pharmacist.getName());
                createOrUpdateCell(row, 2, pharmacist.role);
                createOrUpdateCell(row, 3, pharmacist.gender);
                createOrUpdateCell(row, 4, pharmacist.age);

            }

            // Write changes to the file
            try (FileOutputStream outputFile = new FileOutputStream(filePath)) {
                workbook.write(outputFile);
            }
            System.out.println("Pharmacist data written to Excel successfully.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while writing pharmacists to the Excel file.");
        }
    }

    public void writeMedicationsToExcel(String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Medications");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Medication Name");
            headerRow.createCell(1).setCellValue("Initial Stock");
            headerRow.createCell(2).setCellValue("Low Stock Level Alert");

            // Populate rows with inventory data
            int rowIndex = 1;
            for (PrescribedMedication medication : CommonInventory.inventory.values()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(medication.getMedicationName());
                row.createCell(1).setCellValue(medication.getUnits());
                row.createCell(2).setCellValue(medication.getLowStockAlert());
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("Updated medication data written to Excel: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while writing medications to Excel.");
        }
    }
}
