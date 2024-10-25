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

    public String getDoctorId() {
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

// AppointmentManager Class
class AppointmentManager {
    private static AppointmentManager instance = null;
    private Map<String, Appointment> appointments;

    public AppointmentManager() {
        appointments = new HashMap<>();
    }

    public static AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    public void scheduleAppointment(Appointment appointment) {
        appointments.put(appointment.getAppointmentId(), appointment);
        System.out.println("Appointment scheduled successfully: " + appointment.getAppointmentId());
    }

    public void rescheduleAppointment(String appointmentId, Date newDate, String newTimeSlot) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null) {
            if (isTimeSlotAvailable(appointment.getDoctorId(), newDate, newTimeSlot)) {
                availableTimeSlots.add(appointment.getTimeSlot());
                appointment.setAppointmentDate(newDate);
                appointment.setTimeSlot(newTimeSlot);
                availableTimeSlots.remove(newTimeSlot);
                System.out.println("Appointment rescheduled successfully.");
            } else {
                System.out.println("The selected time slot is not available for this doctor. Please choose another slot.");
            }
        } else {
            System.out.println("Appointment not found.");
        }
    }

    public void cancelAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null && appointment.getStatus().equals("confirmed")) {
            appointment.setStatus("canceled");
            availableTimeSlots.add(appointment.getTimeSlot());
            Doctor doctor = (Doctor) hospitalStaff.get(appointment.getDoctorId());
            if (doctor != null) {
                doctor.addNotification("Appointment on " + appointment.getAppointmentDate() + " at " + appointment.getTimeSlot() + " with Patient ID: " + appointment.getPatientId() + " has been canceled.");
            }
            Pharmacist pharmacist = (Pharmacist) hospitalStaff.get("P001"); // Assuming one pharmacist for notifications
            if (pharmacist != null) {
                pharmacist.addNotification("Appointment involving prescription for Patient ID: " + appointment.getPatientId() + " on " + appointment.getAppointmentDate() + " at " + appointment.getTimeSlot() + " has been canceled.");
            }
            System.out.println("Appointment canceled successfully.");
        } else if (appointment == null) {
            System.out.println("Appointment not found.");
        } else {
            System.out.println("Appointment cannot be canceled, either it's already canceled or completed.");
        }
    }

    private boolean isTimeSlotAvailable(String doctorId, Date date, String timeSlot) {
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorId().equals(doctorId) &&
                    appointment.getAppointmentDate().equals(date) &&
                    appointment.getTimeSlot().equals(timeSlot) &&
                    appointment.getStatus().equals("confirmed")) {
                return false;
            }
        }
        return true;
    }

    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorId().equals(doctorId)) {
                result.add(appointment);
            }
        }
        return result;
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }

    public List<Appointment> getAppointmentsForPatient(String patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getPatientId().equals(patientId)) {
                result.add(appointment);
            }
        }
        return result;
    }

    public void addStaff(User user) {
        hospitalStaff.put(user.id, user);
    }


}