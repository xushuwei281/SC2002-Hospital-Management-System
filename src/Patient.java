import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

// Patient Class
public class Patient extends User {
    public String contactInfo;
    public String bloodType;
    public String gender;
    public String dob;
    public ArrayList<String> pastDiagnoses;
    public ArrayList<String> TreatmentPlan;
    public ArrayList<String> Prescription = new ArrayList<>();
    public AppointmentManager appointmentManager;

    

    public Patient(String id, String name, String contact, String dob, String gender, String bloodType, String pastDiagnoses, String treatmentPlan, AppointmentManager appointmentManager) {
        super(id, name);
        this.dob = dob;
        this.gender = gender;
        this.contactInfo = contact;
        this.bloodType = bloodType;
        this.pastDiagnoses = new ArrayList<>();
        this.TreatmentPlan = new ArrayList<>();
        this.appointmentManager = appointmentManager;
    }


    public void viewMedicalRecord() {
        System.out.println("Patient ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Contact Info: " + contactInfo);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Gender: " + gender);
        System.out.println("Date of Birth: " + dob);
        System.out.println("Past Diagnoses: " + pastDiagnoses);
        System.out.println("Past Prescriptions: " + Prescription);
        System.out.println("TreatmentPlan: " + TreatmentPlan);
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
                    ", Date: " + appointment.getFormattedApptDate() +
                    ", Time Slot: " + appointment.getTimeSlot()+
                    ", Status:" + appointment.getStatus());
        }
    }

    public void viewPastAppointmentOutcomes() {
        appointmentManager.getPastAppointmentsForPatient(this.id);
    }

    public void scheduleAppointment(LocalDate date, LocalTime timeSlot, String doctorId) {
        Appointment appointment = new Appointment("A" + System.currentTimeMillis(), id, doctorId, date, timeSlot);
        AppointmentManager.getInstance().scheduleAppointment(appointment, doctorId);
    }

    public void rescheduleAppointment(String appointmentId, LocalDate date, LocalTime timeSlot) {
        appointmentManager.rescheduleAppointment(appointmentId, date, timeSlot);
    }

    public void cancelAppointment(String appointmentId) {
        appointmentManager.cancelAppointment(appointmentId);
    }

    @Override
    public void displayMenu() {
        System.out.println("1. View Personal Information");
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
