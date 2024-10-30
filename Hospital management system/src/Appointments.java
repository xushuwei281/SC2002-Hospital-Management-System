import java.util.*;


// Appointment Class
class Appointment {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private Date appointmentDate;
    private String timeSlot;
    private String status;

    public Appointment(String appointmentId, String patientId, String doctorId, Date appointmentDate, String timeSlot) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
        this.status = "confirmed";
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public  String getDoctorId() {
        return doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

