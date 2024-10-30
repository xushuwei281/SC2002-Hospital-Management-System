import java.util.*;

// AppointmentManager Class
public class AppointmentManager {
    private static AppointmentManager instance = null;
    private Map<String, Appointment> appointments;
    public Set<String> availableTimeSlots;  // Use Set to store available slots
    public Map<String, User> hospitalStaff;


    // Private constructor for Singleton pattern
    public AppointmentManager() {
        appointments = new HashMap<>();
        availableTimeSlots = new HashSet<>();
        hospitalStaff = new HashMap<>();
    }

    // Singleton instance getter
    public static AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    // Set available time slots from another class
    public void setAvailableTimeSlots(Set<String> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    // Set hospital staff map from another class
    public void setHospitalStaff(Map<String, User> hospitalStaff) {
        this.hospitalStaff = hospitalStaff;
    }

    public void scheduleAppointment(Appointment appointment) {
        appointments.put(appointment.getAppointmentId(), appointment);
        availableTimeSlots.remove(appointment.getTimeSlot());  // Update available time slots
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
        if (appointment != null && "confirmed".equals(appointment.getStatus())) {
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
                    "confirmed".equals(appointment.getStatus())) {
                return false;
            }
        }
        return true;
    }

    public static List<Appointment> getAppointmentsForDoctor(String doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : AppointmentManager.getInstance().appointments.values()) {
            if (appointment.getDoctorId().equals(doctorId)) {
                result.add(appointment);
                System.out.println("Appointment ID: " + appointment.getAppointmentId() +
                            ", Patient: " + appointment.getPatientId() +
                            ", Date: " + appointment.getAppointmentDate() +
                            ", Time Slot: " + appointment.getTimeSlot());

            }
        }
        return result;
    }

    public Appointment getAppointmentById(String appointmentId) {
        return appointments.get(appointmentId);
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
        hospitalStaff.put(user.getId(), user);

    }
}
