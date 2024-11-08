import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;


// Main Driver Class
public class HospitalManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppointmentManager appointmentManager = AppointmentManager.getInstance();
        ExcelReader reader = new ExcelReader();
        List<String[]> doctorsData = reader.readDoctorsFromExcel("src/Doctor_staff.xlsx");
        List<String[]> patientsData = reader.readPatientsFromExcel("src/Patient_List.xlsx");
        List<String[]> pharmacistsData = reader.readPharmacistsFromExcel("src/Pharma_staff.xlsx");
        List<String[]> administratorsData = reader.readAdministratorsFromExcel("src/Admin_staff.xlsx");

        // Creating Doctor objects

        for (String[] data : doctorsData) {
            Doctor doctor = new Doctor(data[0], data[1], data[2], data[3], data[4]);
            appointmentManager.hospitalStaff.put(doctor.id, doctor);
        }

        // Creating Patient objects
        for (String[] data : patientsData) {
            Patient patient = new Patient(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], AppointmentManager.getInstance());
            appointmentManager.hospitalStaff.put(patient.id, patient);
        }

        // Creating Pharmacist objects
        for (String[] data : pharmacistsData) {
            Pharmacist pharmacist = new Pharmacist(data[0], data[1], data[2], data[3], data[4]);
            appointmentManager.hospitalStaff.put(pharmacist.id, pharmacist);
        }


        // Creating Administrator objects
        for (String[] data : administratorsData) {
            Administrator administrator = new Administrator(data[0], data[1], data[2], data[3], data[4]);
            appointmentManager.hospitalStaff.put(administrator.id, administrator);
        }



        // Adding sample medications
        PrescribedMedication med1 = new PrescribedMedication("Aspirin",500);
        PrescribedMedication med2 = new PrescribedMedication("Ibuprofen",500);
        CommonInventory.addItem("Aspirin", 500);
        CommonInventory.addItem("Ibuprofen", 500);


        while (true) {
            try {
                System.out.print("Enter User ID: ");
                String userId = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();

                User currentUser = appointmentManager.hospitalStaff.get(userId);
                if (currentUser != null && currentUser.login(userId, password)) {
                    System.out.println("Login successful. Welcome, " + currentUser.name + "!");
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
                                        patient.viewMyInfo();
                                        break;
                                    case 2:
                                        System.out.print("Enter new contact information: ");
                                        String newContactInfo = scanner.nextLine();
                                        patient.updateContactInfo(newContactInfo);
                                        break;
                                    case 3:
                                        System.out.println("Enter the Doctor's ID to view available appointment slots: ");
                                        String doctorId = scanner.nextLine();
                                        appointmentManager.showAvailableTimeslots(doctorId);
                                        break;
                                    case 4:
                                        System.out.print("Enter Doctor ID: ");
                                        String doctorId4 = scanner.nextLine();
                                        Doctor doctor = appointmentManager.getDoctorById(doctorId4);
                                        if (doctor != null) {
                                            System.out.println("Enter appointment date (yyyy-MM-dd): ");
                                            String dateString = scanner.nextLine();
                                            Date appointmentDate;

                                            try {
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                dateFormat.setLenient(false);
                                                appointmentDate = dateFormat.parse(dateString);

                                                if (appointmentDate.before(new Date())) {
                                                    System.out.println("The date entered is in the past. Please enter a valid future date.");
                                                    break;
                                                }

                                                System.out.print("Enter time slot: ");
                                                String timeSlot = scanner.nextLine();
                                                patient.scheduleAppointment(appointmentDate, timeSlot, doctorId4);
                                            } catch (ParseException e) {
                                                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                                            }
                                        } else {
                                            System.out.println("Invalid Doctor ID.");
                                        }
                                        break;
                                    case 5:
                                        System.out.print("Enter Appointment ID to reschedule: ");
                                        String appointmentId = scanner.nextLine();
                                        System.out.print("Enter new date (yyyy-MM-dd): ");
                                        String newDateString = scanner.nextLine();
                                        Date newDate;

                                        try {
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            dateFormat.setLenient(false);
                                            newDate = dateFormat.parse(newDateString);

                                            if (newDate.before(new Date())) {
                                                System.out.println("The date entered is in the past. Please enter a valid future date.");
                                                break;
                                            }

                                            System.out.print("Enter new time slot: ");
                                            String newTimeSlot = scanner.nextLine();
                                            patient.rescheduleAppointment(appointmentId, newDate, newTimeSlot);
                                        } catch (ParseException e) {
                                            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                                        }
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
                                        appointmentManager.getPastAppointmentsForPatient(userId);
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
                                        Patient patient = (Patient) appointmentManager.hospitalStaff.get(patientId);
                                        if (patient != null) {
                                            doctor.viewPatientRecord(patient);
                                        } else {
                                            System.out.println("Invalid Patient ID.");
                                        }
                                        break;
                                    case 2:
                                        System.out.print("Enter Patient ID to update record: ");
                                        patientId = scanner.nextLine();
                                        patient = (Patient) appointmentManager.hospitalStaff.get(patientId);
                                        if (patient != null) {
                                            System.out.print("Enter new diagnosis: ");
                                            String newDiagnosis = scanner.nextLine();
                                            System.out.print("Enter prescription: ");
                                            String prescription = scanner.nextLine();
                                            System.out.print("Enter treatment plan: ");
                                            String treatmentPlan = scanner.nextLine();
                                            doctor.updateMedicalRecord(patient, newDiagnosis, prescription, treatmentPlan);
                                        } else {
                                            System.out.println("Invalid Patient ID.");
                                        }
                                        break;

                                    case 3:
                                        doctor.printAcceptedAppointments();
                                        break;

                                    case 4:
                                        doctor.inputAvailableTimeSlots();
                                        break;

                                    case 5:
                                        System.out.print("Enter Appointment ID to accept: ");
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
                                        AppointmentManager.getAppointmentsForDoctor(userId);
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
                                            doctor.recordAppointmentOutcome(appointmentId, prescribedMedications, diagnosis, treatment);
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
                                        System.out.print("Enter Appointment Outcome ID: ");
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
                                        System.out.print("Enter medication name for replenishment request: ");
                                        String shortageMedName = scanner.nextLine();
                                        pharmacist.submitReplenishmentRequest(shortageMedName);
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
                                            newUser = new Doctor(staffId, staffGender, staffRole, staffGender, age);
                                        } else if (staffRole.equalsIgnoreCase("Pharmacist")) {
                                            newUser = new Pharmacist(staffId, staffGender, staffRole, staffGender, age);
                                        } else {
                                            System.out.println("Invalid role.");
                                            break;
                                        }
                                        adminUser.addStaff(newUser);
                                        appointmentManager.addStaff(newUser);
                                        break;
                                    case 2:
                                        System.out.println("Enter staff ID to update: ");
                                        staffId = scanner.nextLine();
                                        adminUser.updateStaff(staffId);
                                        break;

                                    case 3:
                                        System.out.print("Enter staff ID to remove: ");
                                        staffId = scanner.nextLine();
                                        adminUser.removeStaff(staffId);
                                        break;

                                    case 4:
                                        System.out.print("Enter roles to view staff: ");
                                        String inputRole = scanner.nextLine();
                                        adminUser.displayStaffByRole(inputRole);
                                        break;

                                    case 5:
                                        adminUser.viewAllAppointments();
                                        System.out.println("Enter appointment id to view outcome: ");
                                        String appointmentId2 = scanner.nextLine();
                                        adminUser.viewAppointmentOutcomeByAppointmentId(appointmentId2);
                                        break;

                                    case 6:
                                        CommonInventory.printInventory();
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
                                        adminUser.approveReplenishmentRequest("");
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
    }


}