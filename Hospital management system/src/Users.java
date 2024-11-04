import java.util.ArrayList;
import java.util.Date;
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

// Patient Class
class Patient extends User {
    private String contactInfo;
    private final String bloodType;
    ArrayList<String> pastDiagnoses;
    private final AppointmentManager appointmentManager;

    public Patient(String id, String name, String contactInfo, String bloodType, AppointmentManager appointmentManager) {
        super(id, name);
        this.contactInfo = contactInfo;
        this.bloodType = bloodType;
        this.pastDiagnoses = new ArrayList<>();
        this.appointmentManager = appointmentManager;
    }

    public void viewMedicalRecord() {
        System.out.println("Patient ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Contact Info: " + contactInfo);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Past Diagnoses: " + pastDiagnoses);
    }


    public void updateContactInfo(String newContactInfo) {
        this.contactInfo = newContactInfo;
        System.out.println("Contact information updated successfully.");
    }
    public void viewMyAppointments() {
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsForPatient(this.id);
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getAppointmentId() +
                    ", Doctor: " + appointment.getDoctorId() +
                    ", Date: " + appointment.getAppointmentDate() +
                    ", Time Slot: " + appointment.getTimeSlot());
        }
    }
    public void scheduleAppointment(Date date, String timeSlot, String doctorId) {
        Appointment appointment = new Appointment("A" + System.currentTimeMillis(), id, doctorId, date, timeSlot);
        AppointmentManager.getInstance().scheduleAppointment(appointment, doctorId);
    }


    public void rescheduleAppointment(String appointmentId, Date newDate, String newTimeSlot) {
        appointmentManager.rescheduleAppointment(appointmentId, newDate, newTimeSlot);
    }

    public void cancelAppointment(String appointmentId) {
        appointmentManager.cancelAppointment(appointmentId);
    }

    @Override
    public void displayMenu() {
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Schedule an Appointment");
        System.out.println("5. Reschedule an Appointment");
        System.out.println("6. Cancel an Appointment");
        System.out.println("7. View Scheduled Appointments");
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Logout");
    }
}

// Doctor Class
class Doctor extends User {
    private final ArrayList<String> patientList;
    private final ArrayList<String> notifications;
    private final ArrayList<Appointment> upcomingAppointments;
    private final ArrayList<AppointmentOutcome> appointmentOutcomes;
    private Set<String> availableTimeslots;

    public Doctor(String id, String name) {
        super(id, name);
        this.patientList = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.upcomingAppointments = new ArrayList<>();
        this.appointmentOutcomes = new ArrayList<>();
        this.availableTimeslots = generateAvailableTimeslots();
    }


    public void viewPatientRecord(Patient patient) {
        patient.viewMedicalRecord();
    }

    public void updateMedicalRecord(Patient patient, String newDiagnosis, String prescription, String treatmentPlan) {
        patient.pastDiagnoses.add(newDiagnosis);
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
    public void setAvailableTimeSlots(Set<String> availableTimeSlots) { this.availableTimeslots = availableTimeSlots; }



    public void acceptAppointment(Appointment appointment) {
        appointment.setStatus("accepted");
        upcomingAppointments.add(appointment);
        System.out.println("Appointment accepted: " + appointment.getAppointmentId());
    }

    public void declineAppointment(Appointment appointment) {
        appointment.setStatus("declined");
        System.out.println("Appointment declined: " + appointment.getAppointmentId());
    }


    public void recordAppointmentOutcome(String appointmentId, String typeOfService, ArrayList<PrescribedMedication> prescribedMedications, String consultationNotes) {
        Appointment appointment = AppointmentManager.getInstance().getAppointmentById(appointmentId);
        if (appointment != null && "confirmed".equals(appointment.getStatus())) {
            AppointmentOutcome outcome = new AppointmentOutcome(appointmentId, typeOfService, prescribedMedications, consultationNotes);
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
        System.out.println("3. View Personal Schedule");
        System.out.println("4. Set Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View Upcoming Appointments");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Logout");
    }
}

class AppointmentOutcome {
    public String appointmentId;
    public String typeOfService;
    public static ArrayList<PrescribedMedication> prescribedMedications;
    public String consultationNotes;

    public AppointmentOutcome(String appointmentId, String typeOfService, ArrayList<PrescribedMedication> prescribedMedications, String consultationNotes) {
        this.appointmentId = appointmentId;
        this.typeOfService = typeOfService;
        this.prescribedMedications = prescribedMedications;
        this.consultationNotes = consultationNotes;
    }

    public static ArrayList<PrescribedMedication> getPrescribedMedications() {
        return prescribedMedications;
    }

}

class PrescribedMedication {
    public String medicationName;
    public String status;

    public PrescribedMedication(String medicationName) {
        this.medicationName = medicationName;
        this.status = "pending";
    }

    public String getMedicationName() {
        String name = this.medicationName;
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

// Pharmacist Class
class Pharmacist extends User {
    private final ArrayList<String> notifications;


    public Pharmacist(String id, String name) {
        super(id, name);
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
        Appointment appointment = AppointmentManager.getInstance().getAppointmentById(appointmentId);
        if (appointment != null) {
            String doctorId = appointment.getDoctorId();  // Get the doctor ID from the appointment
            Doctor doctor = AppointmentManager.getInstance().getDoctorById(doctorId);

            if (doctor != null) {
                AppointmentOutcome outcome = doctor.getOutcomeById(appointmentId);
                if (outcome != null) {
                    System.out.println("Appointment Outcome Details:");
                    System.out.println("Type of Service: " + outcome.typeOfService);
                    System.out.println("Consultation Notes: " + outcome.consultationNotes);
                    System.out.println("Prescribed Medications:");
                    for (PrescribedMedication medication : outcome.getPrescribedMedications()) {
                        System.out.println("Medication: " + medication.getMedicationName() + ", Status: " + medication.getStatus());
                    }
                } else {
                    System.out.println("No outcome found for the provided appointment ID.");
                }
            } else {
                System.out.println("Doctor not found for the given appointment ID.");
            }
        } else {
            System.out.println("No appointment found with the given ID.");
        }
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
                        return;
                    }
                    else{
                        System.out.println("Medication not found in the appointment.");
                        return;
                    }
                }
            }
        }
        System.out.println("Appointment not found.");
    }

    public void viewMedicationInventory() {
        System.out.println("Medication Inventory:");
        for (Map.Entry<String, Integer> entry : CommonInventory.inventory.entrySet()) {
            String medicationName = entry.getKey();
            Integer count = entry.getValue();
            System.out.println("Medication: " + medicationName + ", Count: " + count);
        }
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

    public Administrator(String id, String name) {
        super(id, name);
        this.hospitalStaff = new HashMap<>();
    }

    public void addStaff(User user) {
        hospitalStaff.put(user.id, user);
        System.out.println("Staff member added: " + user.name);
    }

    public void updateStaff(String userId, String newName) {
        User user = hospitalStaff.get(userId);
        if (user != null) {
            user.name = newName;
            System.out.println("Staff member updated: " + user.name);
        } else {
            System.out.println("Staff member not found.");
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

    public static void addMedicationToInventory(String medication, int count) {
        CommonInventory.addItem(medication, count);
    }

    public void viewMedicationInventory() {
        System.out.println("Medication Inventory:");
        CommonInventory.printInventory();
    }

    public void updateStockLevel(String medicationName, int newStockLevel) {
        System.out.println("Stock level updated for: " + medicationName + " to " + newStockLevel);
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