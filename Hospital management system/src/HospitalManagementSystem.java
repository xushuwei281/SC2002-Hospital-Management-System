import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

// Main Driver Class
public class HospitalManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppointmentManager appointmentManager = AppointmentManager.getInstance();

        // Adding sample doctors
        Doctor doctor1 = new Doctor("D001", "Dr. John Smith");
        Doctor doctor2 = new Doctor("D002", "Dr. Emily Davis");
        appointmentManager.addStaff(doctor1);
        appointmentManager.addStaff(doctor2);

        // Adding sample pharmacists
        Pharmacist pharmacist1 = new Pharmacist("P001", "Pharma Lee");
        Pharmacist pharmacist2 = new Pharmacist("P002", "Pharma Kim");
        appointmentManager.addStaff(pharmacist1);
        appointmentManager.addStaff(pharmacist2);

        // Adding sample patients
        Patient patient1 = new Patient("PT001", "Alice Johnson", "alice.johnson@example.com", "A+", appointmentManager);
        Patient patient2 = new Patient("PT002", "Bob Brown", "bob.brown@example.com", "B+", appointmentManager);
        appointmentManager.addStaff(patient1);
        appointmentManager.addStaff(patient2);

        // Adding sample medications
        PrescribedMedication med1 = new PrescribedMedication("Aspirin");
        PrescribedMedication med2 = new PrescribedMedication("Ibuprofen");
        CommonInventory.addItem("Aspirin", 500);
        CommonInventory.addItem("Ibuprofen", 500);

        Administrator admin = new Administrator("A001", "Admin Jane");
        appointmentManager.addStaff(admin);

        while(true){
        // Example: Login and menu navigation
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            User currentUser = appointmentManager.hospitalStaff.get(userId);
            if (currentUser != null && currentUser.login(userId, password)) {
                System.out.println("Login successful. Welcome, " + currentUser.name + "!");
                boolean loggedIn = true;
                while (loggedIn) {// Consume newline

                    switch (currentUser) {
                        case Patient patient -> {
                            currentUser.displayMenu();
                            System.out.print("Enter your choice: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine();
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
                                    System.out.println("Available Appointment Slots: " + appointmentManager.availableTimeSlots);
                                    break;
                                case 4:
                                    System.out.print("Enter Doctor ID: ");
                                    String doctorId = scanner.nextLine();
                                    Doctor doctor = (Doctor) appointmentManager.hospitalStaff.get(doctorId);
                                    if (doctor != null) {
                                        System.out.print("Enter appointment date (yyyy-MM-dd): ");
                                        Date date = new Date(); // Simplified for demonstration
                                        System.out.print("Enter time slot: ");
                                        String timeSlot = scanner.nextLine();
                                        patient.scheduleAppointment(date, timeSlot, doctorId);
                                    } else {
                                        System.out.println("Invalid Doctor ID.");
                                    }
                                    break;
                                case 5:
                                    System.out.print("Enter Appointment ID to reschedule: ");
                                    String appointmentId = scanner.nextLine();
                                    System.out.print("Enter new date (yyyy-MM-dd): ");
                                    Date newDate = new Date(); // Simplified for demonstration
                                    System.out.print("Enter new time slot: ");
                                    String newTimeSlot = scanner.nextLine();
                                    patient.rescheduleAppointment(appointmentId, newDate, newTimeSlot);
                                    break;
                                case 6:
                                    System.out.print("Enter Appointment ID to cancel: ");
                                    String cancelAppointmentId = scanner.nextLine();
                                    patient.cancelAppointment(cancelAppointmentId);
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
                            currentUser.displayMenu();
                            System.out.print("Enter your choice: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine();
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
                                case 5:
                                    System.out.print("Enter Appointment ID to accept: ");
                                    String appointmentId = scanner.nextLine();
                                    Appointment appointment = appointmentManager.getAppointmentById(appointmentId);
                                    if (appointment != null) {
                                        System.out.print("Enter 1 to accept, 0 to decline: ");
                                        int acceptDecline = scanner.nextInt();
                                        if (acceptDecline == 0) {
                                            doctor.declineAppointment(appointment);
                                        }
                                        else if (acceptDecline == 1) {
                                            doctor.acceptAppointment(appointment);
                                        }

                                    } else {
                                        System.out.println("Invalid Appointment ID.");
                                    }
                                    break;


                                case 4:
                                    break;

                                case 7:
                                    System.out.print("Enter Appointment ID to record outcome: ");
                                    appointmentId = scanner.nextLine();
                                    appointment = appointmentManager.getAppointmentById(appointmentId);
                                    if (appointment != null) {
                                        System.out.print("Enter type of service: ");
                                        String typeOfService = scanner.nextLine();
                                        ArrayList<PrescribedMedication> prescribedMedications = new ArrayList<>();
                                        System.out.print("Enter number of prescribed medications: ");
                                        int numMedications = scanner.nextInt();
                                        scanner.nextLine(); // Consume newline
                                        for (int i = 0; i < numMedications; i++) {
                                            System.out.print("Enter medication name: ");
                                            String medicationName = scanner.nextLine();
                                            prescribedMedications.add(new PrescribedMedication(medicationName));
                                        }
                                        System.out.print("Enter consultation notes: ");
                                        String consultationNotes = scanner.nextLine();
                                        doctor.recordAppointmentOutcome(appointment, typeOfService, prescribedMedications, consultationNotes);
                                    } else {
                                        System.out.println("Invalid Appointment ID.");
                                    }
                                    break;
                                case 6:
                                    AppointmentManager.getAppointmentsForDoctor(userId);
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
                        case Pharmacist pharmacist -> {
                            currentUser.displayMenu();
                            System.out.print("Enter your choice: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine();
                            switch (choice) {
                                case 1:
                                    System.out.print("Enter Appointment Outcome ID: ");
                                    String appointmentOutcomeId = scanner.nextLine();
                                    AppointmentOutcome outcome = new AppointmentOutcome(new Date(), "General Checkup", new ArrayList<>(), "All good"); // Example outcome
                                    pharmacist.viewAppointmentOutcome(outcome);
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
                                    String ShortageMedName = scanner.nextLine();
                                    pharmacist.submitReplenishmentRequest(ShortageMedName);
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
                            currentUser.displayMenu();
                            System.out.print("Enter your choice: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine();
                            switch (choice) {
                                case 1:
                                    System.out.print("Enter staff ID: ");
                                    String staffId = scanner.nextLine();
                                    System.out.print("Enter staff name: ");
                                    String staffName = scanner.nextLine();
                                    System.out.print("Enter staff role (Doctor/Pharmacist): ");
                                    String staffRole = scanner.nextLine();
                                    User newUser;
                                    if (staffRole.equalsIgnoreCase("Doctor")) {
                                        newUser = new Doctor(staffId, staffName);
                                    } else if (staffRole.equalsIgnoreCase("Pharmacist")) {
                                        newUser = new Pharmacist(staffId, staffName);
                                    } else {
                                        System.out.println("Invalid role.");
                                        break;
                                    }
                                    adminUser.addStaff(newUser);
                                    appointmentManager.addStaff(newUser);
                                    break;
                                case 3:
                                    System.out.print("Enter staff ID to remove: ");
                                    staffId = scanner.nextLine();
                                    adminUser.removeStaff(staffId);
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
                        default -> System.out.println("Unknown user role.");
                    }
                }
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }
}