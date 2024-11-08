import java.util.ArrayList;
import java.util.HashMap;

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
    private final ArrayList<String> notifications;
    protected ArrayList<Appointment> acceptedAppointments;
    private final ArrayList<AppointmentOutcome> appointmentOutcomes;
    private Set<String> availableTimeslots;
    public String role;
    public String gender;
    public String age;

    public Doctor(String id, String name, String role, String gender, String age) {
        super(id, name);
        this.role = role;
        this.gender = gender;
        this.age = age;
        this.notifications = new ArrayList<>();
        this.acceptedAppointments = new ArrayList<>();
        this.appointmentOutcomes = new ArrayList<>();
        this.availableTimeslots = generateAvailableTimeslots();
    }


    public void viewPatientRecord(Patient patient) {
        patient.viewMedicalRecord();
    }

    public void updateMedicalRecord(Patient patient, String newDiagnosis, String prescription, String treatmentPlan) {
        patient.pastDiagnoses.add(newDiagnosis);
        patient.Prescription.add(prescription);
        patient.TreatmentPlan.add(treatmentPlan);
        System.out.println("Patient's medical record updated successfully.");
    }

    public void addNotification(String notification) {
        notifications.add(notification);
    }

    public void viewNotifications() {
        System.out.println("Notifications for Dr. " + this.name + ":");
        if (notifications.isEmpty()) {
            System.out.println("No new notifications.");
        } else {
            for (String notification : notifications) {
                System.out.println(notification);
            }
            notifications.clear();
        }
    }

    private Set<String> generateAvailableTimeslots() {
        Set<String> timeslots = new HashSet<>();
        String[] morningSlots = {"09:00", "09:30", "10:00", "10:30", "11:00", "11:30"};
        String[] afternoonSlots = {"14:00", "14:30", "15:00", "15:30", "16:00", "16:30"};
        timeslots.addAll(Arrays.asList(morningSlots));
        timeslots.addAll(Arrays.asList(afternoonSlots));
        return timeslots;
    }

    public Set<String> getAvailableTimeslots() { return new HashSet<>(availableTimeslots); }

    public void inputAvailableTimeSlots() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter available time slots for Doctor ID " + id + " (type 'done' to finish):");

        while (true) {
            System.out.print("Enter time slot (HH:MM): ");
            String timeSlot = scanner.nextLine();
            if (timeSlot.equalsIgnoreCase("done")) {
                break;
            }
            // Validate the time slot format (basic validation for HH:MM)
            if (timeSlot.matches("^[0-2][0-9]:[0-5][0-9]$")) {
                availableTimeslots.add(timeSlot);
                System.out.println("Time slot " + timeSlot + " added.");
            } else {
                System.out.println("Invalid time slot format. Please use HH:MM format.");
            }
        }
    }

    public void acceptAppointment(Appointment appointment) {
        if (appointment.getDoctorId().equals(this.id)) {
            String requestedSlot = appointment.getTimeSlot();
            if (availableTimeslots.contains(requestedSlot)) {
                appointment.setStatus("accepted");
                acceptedAppointments.add(appointment);
                availableTimeslots.remove(requestedSlot);  // Remove the accepted time slot
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
        appointment.setStatus("declined");
        System.out.println("Appointment declined: " + appointment.getAppointmentId());
    }

    public void recordAppointmentOutcome(String appointmentId, ArrayList<PrescribedMedication> prescribedMedications, String diagnosis, String treatment) {
        Appointment appointment = AppointmentManager.getInstance().getAppointmentById(appointmentId);
        if (appointment != null && "confirmed".equals(appointment.getStatus())) {
            AppointmentOutcome outcome = new AppointmentOutcome(appointmentId, prescribedMedications, diagnosis, treatment);
            appointmentOutcomes.add(outcome);
            System.out.println("Appointment outcome recorded successfully for appointment ID: " + appointmentId);
        } else {
            System.out.println("No confirmed appointment found with the given ID.");
        }
    }

    public AppointmentOutcome getOutcomeById(String appointmentId) {
        for (AppointmentOutcome outcome : appointmentOutcomes) {
            if (outcome.appointmentId.equals(appointmentId)) {
                return outcome;
            }
        }
        return null;  // Return null if no outcome found
    }


    @Override
    public void displayMenu() {
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Update Patient Medical Records");
        System.out.println("3. View Personal Schedule for accepted Appointments");
        System.out.println("4. Set Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View Pending Appointments");
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

class PrescribedMedication {
    public String medicationName;
    public int units;
    public String status;

    public PrescribedMedication(String medicationName,int units) {
        this.medicationName = medicationName;
        this.units = units;
        this.status = "pending";
    }

    public String getMedicationName() {
        return this.medicationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        if (units < 0) {
            throw new IllegalArgumentException("Units cannot be negative");
        }
        this.units = units;

}



    @Override
    public String toString() {
        return "PrescribedMedication [name=" + medicationName + ", units=" + units + "]";
    }
}


// Pharmacist Class
class Pharmacist extends User {
    private final ArrayList<String> notifications;
    public AppointmentManager appointmentManager;
    public String role;
    public String gender;
    public String age;


    public Pharmacist(String id, String name, String role, String gender, String age) {
        super(id, name);
        this.role = role;
        this.gender = gender;
        this.age = age;
        this.notifications = new ArrayList<>();
    }


    // Method to display appointment details
    public void showAppointmentDetails(String appointmentId) {
        Appointment appointment = AppointmentManager.getInstance().getAppointmentById(appointmentId);
        if (appointment != null) {
            System.out.println("Appointment Details:");
            System.out.println("Appointment ID: " + appointment.getAppointmentId());
            System.out.println("Patient ID: " + appointment.getPatientId());
            System.out.println("Doctor ID: " + appointment.getDoctorId());
            System.out.println("Date: " + appointment.getAppointmentDate());
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
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                ArrayList<PrescribedMedication> medications = AppointmentOutcome.getPrescribedMedications();
                for (PrescribedMedication medication : medications) {
                    if (medication.getMedicationName().equals(medicationName)) {
                        medication.setStatus(newStatus);
                        System.out.println("Prescription status updated successfully for " + medication.getMedicationName() + " to " + newStatus);
                    }
                    else{
                        System.out.println("Medication not found in the appointment.");
                    }
                    return;
                }
            }
        }
        System.out.println("Appointment not found.");
    }

    public void viewMedicationInventory() {
        System.out.println("Medication Inventory:");
        CommonInventory.printInventory();
    }

    public void submitReplenishmentRequest(String medicationName) {
        System.out.println("Replenishment request submitted for: " + medicationName);
    }

    public void addNotification(String notification) {
        notifications.add(notification);
    }

    public void viewNotifications() {
        System.out.println("Notifications for Pharmacist " + this.name + ":");
        if (notifications.isEmpty()) {
            System.out.println("No new notifications.");
        } else {
            for (String notification : notifications) {
                System.out.println(notification);
            }
            notifications.clear();
        }
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

// Administrator Class
class Administrator extends User {
    public HashMap<String, User> hospitalStaff;
    public AppointmentManager appointmentManager;

    public Administrator(String id, String name, String role, String gender, String age) {
        super(id, name);
        this.hospitalStaff = new HashMap<>();
    }

    public void addStaff(User user) {
        hospitalStaff.put(user.id, user);
        System.out.println("Staff member added: " + user.name);
    }

    public void updateStaff(String staffId) {
        User user = hospitalStaff.get(staffId);

        if (user == null) {
            System.out.println("Staff member not found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean updating = true;

        while (updating) {
            System.out.println("\nUpdating details for: " + user.name);
            System.out.println("Enter the attribute you want to update (name, role, gender, age) or type 'exit' to finish:");

            String attribute = scanner.nextLine();

            switch (attribute.toLowerCase()) {
                case "name":
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    user.name = newName;
                    System.out.println("Name updated to: " + user.name);
                    break;

                case "role":
                    if (user instanceof Doctor) {
                        System.out.print("Enter new role: ");
                        String newRole = scanner.nextLine();
                        ((Doctor) user).role = newRole;
                        System.out.println("Role updated to: " + newRole);
                    } else {
                        System.out.println("This user is not a doctor. Cannot update role.");
                    }
                    break;

                case "gender":
                    if (user instanceof Doctor) {
                        System.out.print("Enter new gender: ");
                        String newGender = scanner.nextLine();
                        ((Doctor) user).gender = newGender;
                        System.out.println("Gender updated to: " + newGender);
                    } else {
                        System.out.println("This user is not a doctor. Cannot update gender.");
                    }
                    break;

                case "age":
                    if (user instanceof Doctor) {
                        System.out.print("Enter new age: ");
                        String newAge = scanner.nextLine();
                        ((Doctor) user).age = newAge;
                        System.out.println("Age updated to: " + newAge);
                    } else {
                        System.out.println("This user is not a doctor. Cannot update age.");
                    }
                    break;

                case "exit":
                    updating = false;
                    System.out.println("Finished updating staff member.");
                    break;

                default:
                    System.out.println("Invalid attribute. Please enter a valid attribute name.");
                    break;
            }
        }
    }


    public void removeStaff(String userId) {
        User user = hospitalStaff.remove(userId);
        if (user != null) {
            System.out.println("Staff member removed: " + user.name);
        } else {
            System.out.println("Staff member not found.");
        }
    }

    public void displayStaffByRole(String role) {
        System.out.println("Staff members with role: " + role);
        for (User user : hospitalStaff.values()) {
            if (role.equalsIgnoreCase("doctor") && user instanceof Doctor) {
                System.out.println(user.name);
            } else if (role.equalsIgnoreCase("pharmacist") && user instanceof Pharmacist) {
                System.out.println(user.name);
            }
        }
    }

    public void viewAllAppointments() {
        List<Appointment> appointments = appointmentManager.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments available.");
        } else {
            System.out.println("All Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println(appointment); // This uses the toString() method in the Appointment class
            }
        }
    }

    public void viewAppointmentOutcomeByAppointmentId(String appointmentId) {
        appointmentManager.viewAppointmentOutcomeByAppointmentId(appointmentId);
    }


    public static void addMedicationToInventory(String medication, int count) {
        CommonInventory.addItem(medication, count);

    }


    public void approveReplenishmentRequest(String medicationName) {
        System.out.println("Replenishment request approved for: " + medicationName);
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Add Hospital Staff");
        System.out.println("2. Update Hospital Staff");
        System.out.println("3. Remove Hospital Staff");
        System.out.println("4. Display Staff by Role");
        System.out.println("5. View Appointments Details");
        System.out.println("6. View and Manage Medication Inventory");
        System.out.println("7. Approve Replenishment Requests");
        System.out.println("8. Logout");
    }
}