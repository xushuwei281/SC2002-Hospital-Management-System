import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private final String appointmentId;
    private final String patientId;
    private final String doctorId;
    private LocalDate appointmentDate;
    private LocalTime timeSlot;
    private String status;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");


    public Appointment(String appointmentId, String patientId, String doctorId, LocalDate appointmentDate, LocalTime timeSlot) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
        this.status = "pending";
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public String getFormattedApptDate() {
        return appointmentDate.format(DATE_FORMATTER);
    }

    public LocalTime getTimeSlot() {
        return timeSlot;
    }

    public String getFormattedTimeSlot() {
        return timeSlot.toString(); // Formats time slot as "HH:mm"
    }

    public String getStatus() {
        return status;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setTimeSlot(LocalTime timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment ID: " + appointmentId +
                ", Patient ID: " + patientId +
                ", Doctor ID: " + doctorId +
                ", Date: " + getFormattedApptDate() +
                ", Time Slot: " + getFormattedTimeSlot() +
                ", Status: " + status;
    }
}
