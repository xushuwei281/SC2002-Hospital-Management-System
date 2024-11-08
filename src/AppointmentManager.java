import java.util.*;


// AppointmentManager Class
public class AppointmentManager {
    private static AppointmentManager instance = null;
    private Map<String, Appointment> appointments;
    private Map<String, AppointmentOutcome> appointmentOutcomes;
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


    public Doctor getDoctorById(String doctorId) {
        User user = hospitalStaff.get(doctorId);
        return (user instanceof Doctor) ? (Doctor) user : null;
    }

    // Display available timeslots for a specific doctor
    public void showAvailableTimeslots(String doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        if (doctor != null) {
            Set<String> availableSlots = doctor.getAvailableTimeslots();
            if (availableSlots.isEmpty()) {
                System.out.println("No available timeslots for Dr. " + doctor.getName());
            } else {
                System.out.println("Available timeslots for Dr. " + doctor.getName() + ":");
                availableSlots.forEach(System.out::println);
            }
        } else {
            System.out.println("Doctor not found. Please check the Doctor ID and try again.");
        }
    }


    public void scheduleAppointment(Appointment appointment,String doctorId) {
        if (getDoctorById(doctorId).getAvailableTimeslots().contains(appointment.getTimeSlot())) {
            appointments.put(appointment.getAppointmentId(), appointment);
            availableTimeSlots.remove(appointment.getTimeSlot());  // Remove booked slot
            System.out.println("Appointment scheduled successfully: " + appointment.getAppointmentId());
        } else {
            System.out.println("The selected time slot is not available.");
        }
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
        if (result.isEmpty()) {
            System.out.println("No appointments found for Doctor ID: " + doctorId);
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
        if (result.isEmpty()) {
            System.out.println("No appointments found for patient ID: " + patientId);
        }
        return result;
    }

    public List<Appointment> getPastAppointmentsForPatient(String patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getPatientId().equals(patientId)) {
                result.add(appointment);
            }
        }
        if (result.isEmpty()) {
            System.out.println("No past appointments found for patient ID: " + patientId);
        }
        return result;
    }

    public AppointmentOutcome viewAppointmentOutcomeByAppointmentId(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null) {
            String doctorId = appointment.getDoctorId();  // Get the doctor ID from the appointment
            Doctor doctor = getDoctorById(doctorId);

            if (doctor != null) {
                AppointmentOutcome outcome = appointmentOutcomes.get(appointmentId);  // Get outcome from appointmentOutcomes map
                if (outcome != null) {
                    System.out.println("Appointment Outcome Details:");
                    System.out.println("Type of Service: " + outcome.typeOfService);
                    System.out.println("Consultation Notes: " + outcome.diagnosis);
                    System.out.println("Treatment Plan: " + outcome.treatment);
                    System.out.println("Prescribed Medications:");
                    for (PrescribedMedication medication : outcome.prescribedMedications) {
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
        return null;
    }


    public void addStaff(User user) {
        hospitalStaff.put(user.getId(), user);

    }
}
