import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Patient Class
public class Patient extends User {
    private String contactInfo;
    private final String bloodType;
    public String gender;
    public String age;
    public String dob;
    ArrayList<String> pastDiagnoses;
    ArrayList<String> TreatmentPlan;
    private final AppointmentManager appointmentManager;

    

    public Patient(String id, String name, String contact, String dob, String gender, String bloodType, String pastDiagnoses, String treatmentPlan, AppointmentManager appointmentManager) {
        super(id, name);
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
        System.out.println("Age: " + age);
        System.out.println("Date of Birth: " + dob);
        System.out.println("Past Diagnoses: " + pastDiagnoses);
        System.out.println("TreatmentPlan: " + TreatmentPlan);
    }

    public void viewMyInfo() {
        System.out.println("Patient ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Gender: " + gender);
        System.out.println("Age: " + age);
        System.out.println("Date of Birth: " + dob);
        System.out.println("Contact Info: " + contactInfo);
        System.out.println("Blood Type: " + bloodType);
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