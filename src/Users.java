import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


import java.util.*;

// Base User Class
abstract class User {
    protected String id;
    protected String name;
    protected String password;
    protected boolean firstLogin;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.password = "password"; // default password for all users
        this.firstLogin = true;
    }

    public boolean login(String id, String password) {
        return this.id.equals(id) && this.password.equals(password);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.firstLogin = false;
        System.out.println("Password changed successfully.");
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public abstract void displayMenu();
}

// Doctor Class
class Doctor extends User {
    protected ArrayList<Appointment> acceptedAppointments;
    private final ArrayList<AppointmentOutcome> appointmentOutcomes;
    private AppointmentManager appointmentManager;
    public String role;
    public String gender;
    public String age;

    public Doctor(String id, String name, String role, String gender, String age) {
        super(id, name);
        this.role = role;
        this.gender = gender;
        this.age = age;
        this.appointmentManager = AppointmentManager.getInstance();
        this.acceptedAppointments = new ArrayList<>();
        this.appointmentOutcomes = new ArrayList<>();
    }

    public void showMyAvailableSlots(LocalDate date){
        boolean hasAvailableSlots = false;
        if (appointmentManager.doctorAvailability.get(this.id).containsKey(date)) {
            Set<LocalTime> availableSlots = appointmentManager.doctorAvailability.get(this.id).get(date);

            // If there are available slots, print them in sorted order
            if (!availableSlots.isEmpty()) {
                hasAvailableSlots = true;
                System.out.println("\nDr. " + this.name + " available slots on " + date + ":");
                availableSlots.stream()
                        .sorted() // Sort the available slots to ensure order (not really needed if using TreeSet)
                        .forEach(slot -> System.out.println(" - " + slot));
            }
        }
    }


    // Method for manually inputting available timeslots for a specific date
    public void inputAvailableTimeSlots(LocalDate date) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");  // Specify the exact format

        System.out.println("Enter available time slots for Dr. " + getName() + " on " + date + " (type 'done' to finish):");

        while (true) {
            System.out.print("Enter time slot (HH:MM): ");
            String timeSlotStr = scanner.nextLine().trim();  // Remove any leading/trailing spaces

            // Log the received input for debugging purposes
            System.out.println("DEBUG: Received input -> '" + timeSlotStr + "'");

            if (timeSlotStr.equalsIgnoreCase("done")) {
                System.out.println("DEBUG: Finished input of available time slots.");
                break;
            }

            // Parse the time slot
            try {
                LocalTime timeSlot = LocalTime.parse(timeSlotStr, timeFormatter);
                System.out.println("DEBUG: Parsed time slot -> " + timeSlot);  // Log parsed time for debugging
                appointmentManager.addAvailableTimeslot(this.id, date, timeSlot);  // Add slot to AppointmentManager
                System.out.println("Time slot " + timeSlot + " added.");
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time slot format. Please use HH:MM format (e.g., 14:30).");
                System.out.println("DEBUG: Error message -> " + e.getMessage());  // Print error message for debugging
            } catch (Exception e) {
                // Catch-all for unexpected exceptions, including those from appointmentManager
                System.out.println("DEBUG: Unexpected error occurred -> " + e.getMessage());
                e.printStackTrace();  // Print stack trace for deep debugging
            }
        }
    }


    // Accept an appointment
    public void acceptAppointment(Appointment appointment) {
        if (appointment.getDoctorId().equals(this.id)) {
            LocalDate appointmentDate = appointment.getAppointmentDate();
            LocalTime requestedSlot = appointment.getTimeSlot();

            // Check availability from AppointmentManager
            if (appointmentManager.doctorAvailability.containsKey(this.id) &&
                    appointmentManager.doctorAvailability.get(this.id).containsKey(appointmentDate) &&
                    appointmentManager.doctorAvailability.get(this.id).get(appointmentDate).contains(requestedSlot)) {

                appointment.setStatus("confirmed");
                acceptedAppointments.add(appointment);
                appointmentManager.doctorAvailability.get(this.id).get(appointmentDate).remove(requestedSlot);

                System.out.println("Appointment accepted: " + appointment.getAppointmentId() + " at " + requestedSlot);
            } else {
                System.out.println("Time slot " + requestedSlot + " is not available. Please choose another time.");
            }
        } else {
            System.out.println("Doctor ID mismatch. Cannot accept this appointment.");
        }
    }

    // Method to print accepted appointments
    public void printAcceptedAppointments() {
        if (acceptedAppointments.isEmpty()) {
            System.out.println("No accepted appointments.");
        } else {
            System.out.println("Accepted Appointments:");
            for (Appointment appointment : acceptedAppointments) {
                System.out.println(appointment);
            }
        }
    }

    public void declineAppointment(Appointment appointment) {
        appointmentManager.declineAppointment(appointment.getAppointmentId());
    }


    @Override
    public void displayMenu() {
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Update Patient Medical Records");
        System.out.println("3. View Personal Schedule for all upcoming Appointments");
        System.out.println("4. Set Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View confirmed Appointments");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Logout");
    }

}

class AppointmentOutcome {
    public String appointmentId;
    public String typeOfService;
    public static ArrayList<PrescribedMedication> prescribedMedications;
    public String diagnosis;
    public String treatment;

    public AppointmentOutcome(String appointmentId, ArrayList<PrescribedMedication> prescribedMedications, String diagnosis, String treatment) {
        this.appointmentId = appointmentId;
        AppointmentOutcome.prescribedMedications = prescribedMedications;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    public static ArrayList<PrescribedMedication> getPrescribedMedications() {

        return prescribedMedications;
    }
}


// Pharmacist Class
class Pharmacist extends User {

    public AppointmentManager appointmentManager;
    public replenishRequestManager replenishRequestManager;
    public String role;
    public String gender;
    public String age;


    public Pharmacist(String id, String name, String role, String gender, String age) {
        super(id, name);
        this.role = role;
        this.gender = gender;
        this.age = age;
        this.appointmentManager = AppointmentManager.getInstance();
        this.replenishRequestManager = replenishRequestManager.getInstance();
    }

    public void showAllcompletedAppointmentsId() {
        appointmentManager.getAllCompletedAppointments();
    }


    // Method to display appointment details
    public void showAppointmentDetails(String appointmentId) {
        Appointment appointment = AppointmentManager.getInstance().getAppointmentById(appointmentId);
        if (appointment != null) {
            System.out.println("Appointment Details:");
            System.out.println("Appointment ID: " + appointment.getAppointmentId());
            System.out.println("Patient ID: " + appointment.getPatientId());
            System.out.println("Doctor ID: " + appointment.getDoctorId());
            System.out.println("Date: " + appointment.getFormattedApptDate());
            System.out.println("Time Slot: " + appointment.getTimeSlot());
            System.out.println("Status: " + appointment.getStatus());
        } else {
            System.out.println("No appointment found with the given ID.");
        }
    }

    public void viewAppointmentOutcomeByAppointmentId(String appointmentId) {
        appointmentManager.viewAppointmentOutcomeByAppointmentId(appointmentId);
    }

    public void updatePrescriptionStatus(String appointmentId, String medicationName, String newStatus, AppointmentManager appointmentManager) {
        List<Appointment> appointments = appointmentManager.getAllAppointments(); // Assume this returns a List of appointments.
        boolean appointmentFound = false;
        boolean medicationFound = false;

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointmentFound = true;
                ArrayList<PrescribedMedication> medications = AppointmentOutcome.getPrescribedMedications();
                for (PrescribedMedication medication : medications) {
                    if (medication.getMedicationName().equals(medicationName)) {
                        medication.setStatus(newStatus);
                        CommonInventory.removeItem(medicationName, medication.getUnits());
                        System.out.println("Prescription status updated successfully for " + medication.getMedicationName() + " to " + newStatus);
                        medicationFound = true;
                        break; // Exit loop once the medication is found and updated
                    }
                }

                if (!medicationFound) {
                    System.out.println("Medication not found in the appointment.");
                }

                break; // Exit loop once the correct appointment is found and processed
            }
        }

        if (!appointmentFound) {
            System.out.println("Appointment not found.");
        }
    }


    public void viewMedicationInventory() {
        System.out.println("Medication Inventory:");
        CommonInventory.printInventory();
    }

    public void submitReplenishRq(String requestId,String userId,String item, int quantity, Date requestDate){
        replenishRequestManager.submitReplenishRequest(requestId, userId, item, quantity, requestDate);
    }


    @Override
    public void displayMenu() {
        System.out.println("1. View Appointment Outcome Record");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View Medication Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
    }
}

