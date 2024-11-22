import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


// Main Driver Class
public class  HospitalManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StaffManager staffManager = StaffManager.getInstance();
        AppointmentManager appointmentManager = AppointmentManager.getInstance();
        appointmentManager.setStaffManager(staffManager);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


        ExcelReader reader = new ExcelReader();
        List<String[]> doctorsData = reader.readDoctorsFromExcel("src/Doctor_staff.xlsx");
        List<String[]> patientsData = reader.readPatientsFromExcel("src/Patient_List.xlsx");
        List<String[]> pharmacistsData = reader.readPharmacistsFromExcel("src/Pharma_staff.xlsx");
        List<String[]> administratorsData = reader.readAdministratorsFromExcel("src/Admin_staff.xlsx");
        List<String[]> medicationsData = reader.readMedicationsFromExcel("src/Medicine_List.xlsx");


        // Creating Doctor objects

        for (String[] data : doctorsData) {
            Doctor doctor = new Doctor(data[0], data[1], data[2], data[3], data[4]);
            staffManager.hospitalStaff.put(doctor.id, doctor);
        }

        // Creating Patient objects
        for (String[] data : patientsData) {
            Patient patient = new Patient(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], AppointmentManager.getInstance());
            staffManager.hospitalStaff.put(patient.id, patient);
        }

        // Creating Pharmacist objects
        for (String[] data : pharmacistsData) {
            Pharmacist pharmacist = new Pharmacist(data[0], data[1], data[2], data[3], data[4]);
            staffManager.hospitalStaff.put(pharmacist.id, pharmacist);
        }


        // Creating Administrator objects
        for (String[] data : administratorsData) {
            Administrator administrator = new Administrator(data[0], data[1], data[2], data[3], data[4]);
            staffManager.hospitalStaff.put(administrator.id, administrator);
        }

        for (String[] data : medicationsData) {
            String medicationName = data[0];

            // Parse initial stock and low stock alert
            double initialStockDouble = Double.parseDouble(data[1].trim());
            double lowStockAlertDouble = Double.parseDouble(data[2].trim());

            // Round to the nearest integer
            int initialStock = (int) Math.round(initialStockDouble);
            int lowStockAlert = (int) Math.round(lowStockAlertDouble);

            // Validate negative values
            if (initialStock < 0 || lowStockAlert < 0) {
                throw new NumberFormatException("Negative stock value.");
            }

            // Add medication to inventory
            PrescribedMedication medication = new PrescribedMedication(medicationName, initialStock, lowStockAlert);
            CommonInventory.inventory.put(medicationName, medication);

        }

        appointmentManager.setDefaultWeeklyAvailabilityForAllDoctors();

        boolean system = true;

        while (system) {
            try {
                System.out.print("Enter User ID: ");
                String userId = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();

                User currentUser = staffManager.hospitalStaff.get(userId);
                if (currentUser != null && currentUser.login(userId, password)) {
                    System.out.println("Login successful. Welcome, " + currentUser.name + "!");
                    if (currentUser.isFirstLogin()) {
                        System.out.println("You need to change your password.");
                        boolean passwordChanged = false;
                        while (!passwordChanged) {
                            System.out.print("Enter new password: ");
                            String newPassword = scanner.nextLine();
                            System.out.print("Confirm new password: ");
                            String confirmPassword = scanner.nextLine();

                            if (newPassword.equals(confirmPassword)) {
                                currentUser.changePassword(newPassword);
                                passwordChanged = true;
                            } else {
                                System.out.println("Passwords do not match. Please try again.");
                            }
                        }
                    }
                    boolean loggedIn = true;
                    while (loggedIn) {
                        currentUser.displayMenu();
                        System.out.print("Enter your choice: ");
                        int choice;
                        try {
                            choice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number corresponding to your menu choice.");
                            scanner.nextLine(); // Consume invalid input
                            continue;
                        }

                        switch (currentUser) {
                            case Patient patient -> {
                                switch (choice) {
                                    case 1:
                                        patient.viewMedicalRecord();
                                        break;
                                    case 2:
                                        System.out.print("Enter new contact information: ");
                                        String newContactInfo = scanner.nextLine();
                                        patient.updateContactInfo(newContactInfo);
                                        break;
                                    case 3:
                                        LocalDate date = null;
                                        while (date == null) {
                                            System.out.print("Enter date to view available time slots for doctor (yyyy-MM-dd): ");
                                            String dateString = scanner.nextLine();

                                            try {
                                                date = LocalDate.parse(dateString, dateFormatter);
                                            } catch (DateTimeParseException e) {
                                                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                                            }
                                        }
                                        // Step 3: View Available Time Slots
                                        appointmentManager.showAvailableTimeslots(date);
                                        break;
                                    case 4:
                                        LocalDate appointmentDate = null;
                                        while (appointmentDate == null) {
                                            System.out.print("Enter appointment date (yyyy-MM-dd): ");
                                            String dateInput = scanner.nextLine();
                                            try {
                                                appointmentDate = LocalDate.parse(dateInput, dateFormatter);
                                            } catch (DateTimeParseException e) {
                                                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                                            }
                                        }

                                        // Step 2: Get Appointment Time Slot
                                        LocalTime timeSlot = null;
                                        while (timeSlot == null) {
                                            System.out.print("Enter appointment time slot (HH:mm): ");
                                            String timeInput = scanner.nextLine();
                                            try {
                                                timeSlot = LocalTime.parse(timeInput, timeFormatter);
                                            } catch (DateTimeParseException e) {
                                                System.out.println("Invalid time format. Please enter the time in HH:mm format.");
                                            }
                                        }

                                        // Step 3: Get Doctor ID
                                        System.out.print("Enter Doctor ID: ");
                                        String doctorId = scanner.nextLine();

                                        // Step 4: Schedule the Appointment
                                        patient.scheduleAppointment(appointmentDate, timeSlot, doctorId);
                                        break;
                                    case 5:
                                        System.out.print("Enter Appointment ID to reschedule: ");
                                        String appointmentId = scanner.nextLine();

                                        LocalDate newDate = null;

                                        // Step 1: Get and validate new date
                                        while (newDate == null) {
                                            System.out.print("Enter new date (yyyy-MM-dd): ");
                                            String newDateString = scanner.nextLine();

                                            try {
                                                newDate = LocalDate.parse(newDateString, dateFormatter);

                                                // Ensure the new date is not in the past
                                                if (newDate.isBefore(LocalDate.now())) {
                                                    System.out.println("The date entered is in the past. Please enter a valid future date.");
                                                    newDate = null;  // Reset newDate to null so loop continues
                                                }
                                            } catch (DateTimeParseException e) {
                                                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                                            }
                                        }

                                        // Step 2: Get and validate new time slot
                                        LocalTime newTimeSlot = null;
                                        while (newTimeSlot == null) {
                                            System.out.print("Enter new time slot (HH:mm): ");
                                            String newTimeSlotString = scanner.nextLine();

                                            try {
                                                newTimeSlot = LocalTime.parse(newTimeSlotString, timeFormatter);
                                            } catch (DateTimeParseException e) {
                                                System.out.println("Invalid time format. Please enter the time in HH:mm format.");
                                            }
                                        }

                                        // Step 3: Call reschedule method
                                        patient.rescheduleAppointment(appointmentId, newDate, newTimeSlot);
                                        break;
                                    case 6:
                                        System.out.print("Enter Appointment ID to cancel: ");
                                        String cancelAppointmentId = scanner.nextLine();
                                        patient.cancelAppointment(cancelAppointmentId);
                                        break;
                                    case 7:
                                        patient.viewMyAppointments();
                                        break;
                                    case 8:
                                        patient.viewPastAppointmentOutcomes();
                                        break;
                                    case 9:
                                        loggedIn = false;
                                        System.out.println("Logging out...");
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Please try again.");
                                        break;
                                }
                            }
                            case Doctor doctor -> {
                                switch (choice) {
                                    case 1:
                                        System.out.print("Enter Patient ID to view record: ");
                                        String patientId = scanner.nextLine();
                                        Patient patient = (Patient) staffManager.hospitalStaff.get(patientId);
                                        if (patient != null) {
                                            patient.viewMedicalRecord();
                                            AppointmentManager.getInstance().getPastAppointmentsForPatient(patientId);
                                        } else {
                                            System.out.println("Invalid Patient ID.");
                                        }
                                        break;
                                    case 2:
                                        System.out.print("Enter Patient ID to update record: ");
                                        patientId = scanner.nextLine();
                                        patient = (Patient) staffManager.hospitalStaff.get(patientId);
                                        if (patient != null) {
                                            System.out.print("Enter new diagnosis: ");
                                            String newDiagnosis = scanner.nextLine();
                                            System.out.print("Enter prescription: ");
                                            String prescription = scanner.nextLine();
                                            System.out.print("Enter treatment plan: ");
                                            String treatmentPlan = scanner.nextLine();
                                            appointmentManager.updateMedicalRecord(patient, newDiagnosis, prescription, treatmentPlan);
                                        } else {
                                            System.out.println("Invalid Patient ID.");
                                        }
                                        break;
                                    case 3:
                                        System.out.println("All scheduled appointments: ");
                                        AppointmentManager.getInstance().getAppointmentsForDoctor(userId);
                                        System.out.print("Available time slots: ");
                                        System.out.print("Enter the date (YYYY-MM-DD) to view availability: ");
                                        String dateStr = scanner.nextLine();
                                        try {
                                            LocalDate date = LocalDate.parse(dateStr);
                                            doctor.showMyAvailableSlots(date);
                                        } catch (Exception e) {
                                            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                                        }
                                        break;
                                    case 4:
                                        System.out.print("Enter the date (YYYY-MM-DD): ");
                                        String dateStr1 = scanner.nextLine();
                                        try {
                                            LocalDate date = LocalDate.parse(dateStr1);
                                            doctor.inputAvailableTimeSlots(date);
                                        } catch (Exception e) {
                                            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                                        }
                                        break;
                                    case 5:
                                        System.out.print("Enter Appointment ID to view: ");
                                        String appointmentId = scanner.nextLine();
                                        Appointment appointment = appointmentManager.getAppointmentById(appointmentId);
                                        if (appointment != null) {
                                            System.out.print("Enter 1 to accept, 0 to decline: ");
                                            int acceptDecline;
                                            try {
                                                acceptDecline = scanner.nextInt();
                                                scanner.nextLine();
                                            } catch (InputMismatchException e) {
                                                System.out.println("Invalid input. Please enter 1 to accept or 0 to decline.");
                                                scanner.nextLine();
                                                break;
                                            }
                                            if (acceptDecline == 0) {
                                                doctor.declineAppointment(appointment);
                                            } else if (acceptDecline == 1) {
                                                doctor.acceptAppointment(appointment);
                                            } else {
                                                System.out.println("Invalid choice. Please enter 1 or 0.");
                                            }
                                        } else {
                                            System.out.println("Invalid Appointment ID.");
                                        }
                                        break;
                                    case 6:
                                        doctor.printAcceptedAppointments();
                                        break;
                                    case 7:
                                        System.out.print("Enter Appointment ID to record outcome: ");
                                        appointmentId = scanner.nextLine();
                                        appointment = appointmentManager.getAppointmentById(appointmentId);
                                        if (appointment != null) {
                                            System.out.print("Enter diagnosis: ");
                                            String diagnosis = scanner.nextLine();
                                            ArrayList<PrescribedMedication> prescribedMedications = new ArrayList<>();
                                            System.out.print("How many types of medications prescribed?: ");
                                            int numMedications;
                                            try {
                                                numMedications = scanner.nextInt();
                                                scanner.nextLine();
                                            } catch (InputMismatchException e) {
                                                System.out.println("Invalid input. Please enter a valid number.");
                                                scanner.nextLine();
                                                break;
                                            }

                                            for (int i = 0; i < numMedications; i++) {
                                                System.out.print("Enter medication name: ");
                                                String medicationName = scanner.nextLine();

                                                System.out.print("Enter the number of units for " + medicationName + ": ");
                                                int units;
                                                try {
                                                    units = scanner.nextInt();
                                                    scanner.nextLine();
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Invalid input. Please enter a valid number of units.");
                                                    scanner.nextLine();
                                                    break;
                                                }

                                                prescribedMedications.add(new PrescribedMedication(medicationName, units));
                                            }
                                            System.out.print("Enter type of treatment: ");
                                            String treatment = scanner.nextLine();
                                            AppointmentManager.getInstance().recordAppointmentOutcome(appointmentId, prescribedMedications, diagnosis, treatment);
                                        } else {
                                            System.out.println("Invalid Appointment ID.");
                                        }
                                        break;
                                    case 8:
                                        loggedIn = false;
                                        System.out.println("Logging out...");
                                        break;

                                    default:
                                        System.out.println("Invalid choice. Please try again.");
                                        break;
                                }
                            }
                            case Pharmacist pharmacist -> {
                                switch (choice) {
                                    case 1:
                                        System.out.print("All Completed Appointments: ");
                                        pharmacist.showAllcompletedAppointmentsId();
                                        System.out.print("Enter Appointment ID to view details: ");
                                        String appointmentOutcomeId = scanner.nextLine();
                                        pharmacist.showAppointmentDetails(appointmentOutcomeId);
                                        pharmacist.viewAppointmentOutcomeByAppointmentId(appointmentOutcomeId);
                                        break;
                                    case 2:
                                        System.out.print("Enter Appointment ID: ");
                                        String appointmentId = scanner.nextLine();
                                        System.out.print("Enter Medication Name to Update Status: ");
                                        String medicationName = scanner.nextLine();
                                        System.out.print("Enter New Status: ");
                                        String newStatus = scanner.nextLine();
                                        pharmacist.updatePrescriptionStatus(appointmentId, medicationName, newStatus, appointmentManager);
                                        break;
                                    case 3:
                                        pharmacist.viewMedicationInventory();
                                        break;
                                    case 4:
                                        String requestId = UUID.randomUUID().toString();
                                        System.out.println("Generated Replenish Request ID: " + requestId);

                                        System.out.println("Enter Item to Replenish:");
                                        String item = scanner.nextLine();

                                        System.out.println("Enter Quantity:");
                                        int quantity = scanner.nextInt();
                                        scanner.nextLine(); // Consume newline

                                        System.out.println("Enter Request Date (yyyy-MM-dd):");
                                        String dateString = scanner.nextLine();
                                        Date requestDate = null;
                                        try {
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            requestDate = dateFormat.parse(dateString);
                                        } catch (ParseException e) {
                                            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                                            return;
                                        }
                                        System.out.println("Request Description: Request ID: " + requestId + ", Pharmacist ID: " + userId + ", Item: " + item + ", Quantity: " + quantity + ", Date: " + new SimpleDateFormat("yyyy-MM-dd").format(requestDate));

                                        pharmacist.submitReplenishRq(requestId, userId, item, quantity, requestDate);
                                        break;
                                    case 5:
                                        loggedIn = false;
                                        System.out.println("Logging out...");
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Please try again.");
                                        break;
                                }
                            }
                            case Administrator adminUser -> {
                                switch (choice) {
                                    case 1:
                                        System.out.print("Enter staff ID: ");
                                        String staffId = scanner.nextLine();
                                        System.out.print("Enter staff name: ");
                                        String staffName = scanner.nextLine();
                                        System.out.print("Enter staff role (Doctor/Pharmacist): ");
                                        String staffRole = scanner.nextLine();
                                        System.out.print("Enter staff gender: ");
                                        String staffGender = scanner.nextLine();
                                        System.out.print("Enter staff age: ");
                                        String age = scanner.nextLine();
                                        User newUser;
                                        if (staffRole.equalsIgnoreCase("Doctor")) {
                                            newUser = new Doctor(staffId, staffName, staffRole, staffGender, age);
                                        } else if (staffRole.equalsIgnoreCase("Pharmacist")) {
                                            newUser = new Pharmacist(staffId, staffName, staffRole, staffGender, age);
                                        } else {
                                            System.out.println("Invalid role.");
                                            break;
                                        }
                                        adminUser.addHospitalStaff(newUser);
                                        break;
                                    case 2:
                                        System.out.println("Enter staff ID to update: ");
                                        staffId = scanner.nextLine();
                                        adminUser.updateHospitalStaff(staffId);
                                        break;
                                    case 3:
                                        System.out.print("Enter staff ID to remove: ");
                                        staffId = scanner.nextLine();
                                        adminUser.removeHospitalStaff(staffId);
                                        break;
                                    case 4:
                                        System.out.print("Enter roles to view staff: ");
                                        String inputRole = scanner.nextLine();
                                        adminUser.displayStaffByRole(inputRole);
                                        break;
                                    case 5:
                                        appointmentManager.viewAllAppointments();
                                        System.out.println("Enter appointment id to view outcome: ");
                                        String appointmentId2 = scanner.nextLine();
                                        adminUser.viewAppointmentsDetails(appointmentId2);
                                        break;
                                    case 6:
                                        adminUser.viewMedicationInventory();
                                        System.out.println("Manage inventory? (y/n): ");
                                        String input = scanner.nextLine();
                                        if (input.equalsIgnoreCase("y")) {
                                            System.out.println("Enter medication name: ");
                                            String medicationName = scanner.nextLine();
                                            System.out.println("Enter number of units: ");
                                            int units = scanner.nextInt();
                                            adminUser.addMedicationToInventory(medicationName, units);
                                        }
                                        break;
                                    case 7:
                                        adminUser.approveReplenishmentRequests();
                                        break;
                                    case 8:
                                        loggedIn = false;
                                        System.out.println("Logging out...");
                                        break;
                                    case 9:
                                        loggedIn = false;
                                        system = false;
                                        break;

                                    default:
                                        System.out.println("Invalid choice. Please try again.");
                                        break;
                                }
                            }
                            default -> {
                                System.out.println("Unknown user role.");
                            }
                        }
                    }
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }

        ExcelWriter writer = new ExcelWriter();

        ArrayList<Patient> updatedPatientsList = new ArrayList<>();
        for (User user : staffManager.hospitalStaff.values()) {
            if (user instanceof Patient) {
                updatedPatientsList.add((Patient) user);
            }
        }
        writer.writePatientsToExcel("src/Patient_List.xlsx", updatedPatientsList);

        ArrayList<Doctor> updatedDoctorsList = new ArrayList<>();
        for (User user : staffManager.hospitalStaff.values()) {
            if (user instanceof Doctor) {
                updatedDoctorsList.add((Doctor) user);
            }
        }
        writer.writeDoctorsToExcel("src/Doctor_staff.xlsx", updatedDoctorsList);

        ArrayList<Pharmacist> updatedPharmacistsList = new ArrayList<>();
        for (User user : staffManager.hospitalStaff.values()) {
            if (user instanceof Pharmacist) {
                updatedPharmacistsList.add((Pharmacist) user);
            }
        }
        writer.writePharmacistsToExcel("src/Pharma_staff.xlsx", updatedPharmacistsList);

        writer.writeMedicationsToExcel("src/Medicine_List.xlsx");

        System.out.println("Program executed successfully. All Updates saved to Excel.");
    }


}