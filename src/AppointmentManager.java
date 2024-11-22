import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;


// AppointmentManager Class
public class AppointmentManager {
    private static AppointmentManager instance = null;
    public Map<String, Appointment> appointments;
    public Map<String, AppointmentOutcome> appointmentOutcomes;
    public Map<String, Map<LocalDate, Set<LocalTime>>> doctorAvailability;
    private StaffManager staffManager;

    public AppointmentManager() {
        appointments = new HashMap<>();
        doctorAvailability = new HashMap<>();
        appointmentOutcomes = new HashMap<>();
    }

    public static AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    // Method to set StaffManager instance after creating AppointmentManager
    public void setStaffManager(StaffManager staffManager) {
        this.staffManager = staffManager;
    }

    public Doctor getDoctorById(String doctorId) {
        User user = staffManager.hospitalStaff.get(doctorId);
        return (user instanceof Doctor) ? (Doctor) user : null;
    }

    private String getDoctorNameById(String doctorId) {
        User user = staffManager.hospitalStaff.get(doctorId);
        if (user instanceof Doctor) {
            return ((Doctor) user).getName();
        }
        return "Unknown Doctor";
    }

    public void generateDefaultAvailableTimeslots(String doctorId, LocalDate date) {
        doctorAvailability.computeIfAbsent(doctorId, k -> new HashMap<>());
        doctorAvailability.get(doctorId).put(date, generateAvailableTimeslots());
    }

    public void setDefaultWeeklyAvailabilityForAllDoctors() {
        LocalDate today = LocalDate.now();
        for (User user : staffManager.hospitalStaff.values()) {
            if (user instanceof Doctor) {
                Doctor doctor = (Doctor) user;
                for (int i = 0; i < 7; i++) {
                    LocalDate date = today.plusDays(i);
                    generateDefaultAvailableTimeslots(doctor.getId(), date);
                }
            }
        }
    }

    // Method to generate default available timeslots
    private Set<LocalTime> generateAvailableTimeslots() {
        Set<LocalTime> timeslots = new TreeSet<>(); // Use TreeSet to maintain sorted order
        LocalTime[] morningSlots = {
                LocalTime.of(9, 0), LocalTime.of(9, 30), LocalTime.of(10, 0),
                LocalTime.of(10, 30), LocalTime.of(11, 0), LocalTime.of(11, 30)
        };
        LocalTime[] afternoonSlots = {
                LocalTime.of(14, 0), LocalTime.of(14, 30), LocalTime.of(15, 0),
                LocalTime.of(15, 30), LocalTime.of(16, 0), LocalTime.of(16, 30)
        };
        timeslots.addAll(Arrays.asList(morningSlots));
        timeslots.addAll(Arrays.asList(afternoonSlots));
        return timeslots;
    }

    public void addAvailableTimeslot(String doctorId, LocalDate date, LocalTime time) {
        // Ensure that we have an entry for the doctor
        doctorAvailability.computeIfAbsent(doctorId, k -> new HashMap<>());

        // Ensure that we have an entry for the specific date
        doctorAvailability.get(doctorId).computeIfAbsent(date, k -> new HashSet<>());

        // Add the time slot back to the available slots for that date
        doctorAvailability.get(doctorId).get(date).add(time);
    }


    // Display available timeslots for a specific date
    public void showAvailableTimeslots(LocalDate date) {
        System.out.println("Available timeslots for all doctors on " + date + ":");

        boolean hasAvailableSlots = false;

        // Iterate through all doctors in doctorAvailability
        for (String doctorId : doctorAvailability.keySet()) {
            String doctorName = getDoctorNameById(doctorId);

            // Check if this doctor has availability for the given date
            if (doctorAvailability.get(doctorId).containsKey(date)) {
                Set<LocalTime> availableSlots = doctorAvailability.get(doctorId).get(date);

                // If there are available slots, print them in sorted order
                if (!availableSlots.isEmpty()) {
                    hasAvailableSlots = true;
                    System.out.println("\nDr. " + doctorName + " / Dr Id:"+doctorId+" available slots on " + date + ":");
                    availableSlots.stream()
                            .sorted() // Sort the available slots to ensure order (not really needed if using TreeSet)
                            .forEach(slot -> System.out.println(" - " + slot));
                }
            }
        }

        // If no doctors have available slots for the given date
        if (!hasAvailableSlots) {
            System.out.println("No available time slots found for any doctor on " + date + ".");
        }
    }

    public void scheduleAppointment(Appointment appointment, String doctorId) {
        String doctorName = getDoctorNameById(doctorId);
        LocalDate date = appointment.getAppointmentDate();
        LocalTime timeSlot = appointment.getTimeSlot();

        if (doctorAvailability.containsKey(doctorId) && doctorAvailability.get(doctorId).containsKey(date) &&
                doctorAvailability.get(doctorId).get(date).contains(timeSlot)) {

            // Schedule the appointment
            appointments.put(appointment.getAppointmentId(), appointment);

            System.out.println("Appointment scheduled successfully with Dr. " + doctorName + ": " + appointment.getAppointmentId());
        } else {
            System.out.println("The selected time slot is not available for Dr. " + doctorName + ".");
        }
    }


    public void rescheduleAppointment(String appointmentId, LocalDate newDate, LocalTime newTimeSlot) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null) {
            String doctorId = appointment.getDoctorId();
            String doctorName = getDoctorNameById(doctorId);

            // Retrieve the old date and old time slot directly
            LocalDate oldDate = appointment.getAppointmentDate();
            LocalTime oldTimeSlot = appointment.getTimeSlot();  // No need to parse, just use directly

            // Check if the new time slot is available for the selected doctor and date
            if (doctorAvailability.containsKey(doctorId) && doctorAvailability.get(doctorId).containsKey(newDate) &&
                    doctorAvailability.get(doctorId).get(newDate).contains(newTimeSlot)) {

                // Free the old time slot
                addAvailableTimeslot(doctorId, oldDate, oldTimeSlot);

                // Update appointment details
                appointment.setAppointmentDate(newDate); // Convert LocalDate to java.util.Date
                appointment.setTimeSlot(newTimeSlot);

                // Book the new time slot
                doctorAvailability.get(doctorId).get(newDate).remove(newTimeSlot);

                System.out.println("Appointment rescheduled successfully with Dr. " + doctorName + ".");
            } else {
                System.out.println("The selected new time slot is not available for Dr. " + doctorName + ". Please choose another slot.");
            }
        } else {
            System.out.println("Appointment not found.");
        }
    }


    public void cancelAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null && "pending".equals(appointment.getStatus())) {
            String doctorId = appointment.getDoctorId();
            LocalDate appointmentDate = appointment.getAppointmentDate();
            LocalTime timeSlot = appointment.getTimeSlot();

            // Set the appointment status to canceled
            appointment.setStatus("canceled");

            // Free the old time slot by adding it back to availability
            addAvailableTimeslot(doctorId, appointmentDate, timeSlot);

            System.out.println("Appointment with ID " + appointmentId + " has been canceled successfully.");

        } else if (appointment == null) {
            System.out.println("Appointment not found.");
        } else {
            System.out.println("Appointment cannot be canceled, either it's already canceled or completed.");
        }
    }

    public void declineAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null&& !Objects.equals(appointment.getStatus(), "completed")) {
            String doctorId = appointment.getDoctorId();
            LocalDate appointmentDate = appointment.getAppointmentDate();
            LocalTime timeSlot = appointment.getTimeSlot();

            // Set the appointment status to canceled
            appointment.setStatus("declined");

            // Free the old time slot by adding it back to availability
            addAvailableTimeslot(doctorId, appointmentDate, timeSlot);

            System.out.println("Appointment declined: " + appointment.getAppointmentId());

        } else if (appointment == null) {
            System.out.println("Appointment not found.");
        } else {
            System.out.println("Appointment cannot be declined, either it's already canceled or completed.");
        }
    }


    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : AppointmentManager.getInstance().appointments.values()) {
            if (appointment.getDoctorId().equals(doctorId)) {
                result.add(appointment);
                System.out.println("Appointment ID: " + appointment.getAppointmentId() +
                            ", Patient: " + appointment.getPatientId() +
                            ", Date: " + appointment.getFormattedApptDate() +
                            ", Time Slot: " + appointment.getTimeSlot() +
                            ", Status: " + appointment.getStatus());

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

    public List<Appointment> getAllCompletedAppointments() {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getStatus().equals("completed")) {
                result.add(appointment);
                System.out.println("Appointment Id: " + appointment.getAppointmentId() + " Date: " + appointment.getFormattedApptDate() + " TimeSlot: " + appointment.getTimeSlot());
            }
        }
        return result;
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
            if (appointment.getPatientId().equals(patientId)&& "completed".equals(appointment.getStatus())) {
                result.add(appointment);
                AppointmentOutcome outcome = appointmentOutcomes.get(appointment.getAppointmentId());
                if (outcome != null) {
                    System.out.println("Appointment ID: " + appointment.getAppointmentId() +
                            ", Date: " + appointment.getFormattedApptDate() +
                            ", Time Slot: " + appointment.getTimeSlot() +
                            ", Diagnosis: " + outcome.diagnosis +
                            ", Treatment: " + outcome.treatment);
                    System.out.println("Prescribed Medications:");
                    for (PrescribedMedication medication : outcome.getPrescribedMedications()) {
                        System.out.println("Medication: " + medication.getMedicationName() + ", Med Status: " + medication.getStatus());
                    }
                } else {
                    System.out.println("No outcome recorded for this appointment.");
                }
            }
        }
        if (result.isEmpty()) {
            System.out.println("No past appointments found for patient ID: " + patientId);
        }
        return result;
    }

    public void updateMedicalRecord(Patient patient, String newDiagnosis, String prescription, String treatmentPlan) {
        patient.pastDiagnoses.add(newDiagnosis);
        patient.Prescription.add(prescription);
        patient.TreatmentPlan.add(treatmentPlan);
        System.out.println("Patient's medical record updated successfully.");
    }

    public void recordAppointmentOutcome(String appointmentId, ArrayList<PrescribedMedication> prescribedMedications, String diagnosis, String treatment) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment != null && "confirmed".equals(appointment.getStatus())) {
            AppointmentOutcome outcome = new AppointmentOutcome(appointmentId, prescribedMedications, diagnosis, treatment);
            appointmentOutcomes.put(appointmentId, outcome);

            // Update patient's medical record
            Patient patient = (Patient) staffManager.hospitalStaff.get(appointment.getPatientId());
            if (patient != null) {
                updateMedicalRecord(patient, diagnosis, prescribedMedications.toString(), treatment);
            }
            appointment.setStatus("completed");

            System.out.println("Appointment outcome recorded successfully for appointment ID: " + appointmentId);
        } else {
            System.out.println("No confirmed appointment found with the given ID.");
        }
    }

    public void viewAppointmentOutcomeByAppointmentId(String appointmentId) {
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
                    for (PrescribedMedication medication : AppointmentOutcome.prescribedMedications) {
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

    public void viewAllAppointments() {
        List<Appointment> appointments = getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments available.");
        } else {
            System.out.println("All Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println(appointment); // This uses the toString() method in the Appointment class
            }
        }
    }

}
