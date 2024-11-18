import java.util.Date;
import java.util.Scanner;

// Administrator Class
public class Administrator extends User {
    private StaffManager staffManager;
    private AppointmentManager appointmentManager;
    private replenishRequestManager replenishRequestManager;

    public Administrator(String id, String name, String role, String gender, String age) {
        super(id, name);
        this.staffManager = new StaffManager();
        this.appointmentManager = new AppointmentManager();
        this.replenishRequestManager = new replenishRequestManager();
    }

    // Function to add hospital staff
    public void addHospitalStaff(User newUser) {
        staffManager.addStaff(newUser);
    }

    // Function to update hospital staff
    public void updateHospitalStaff(String staffId) {
        staffManager.updateStaff(staffId);
    }

    // Function to remove hospital staff
    public void removeHospitalStaff(String userId) {
        staffManager.removeStaff(userId);
    }

    // Function to display staff by role
    public void displayStaffByRole(String role) {
        staffManager.displayStaffByRole(role);
    }

    public void viewMedicationInventory() {
        System.out.println("Medication Inventory:");
        CommonInventory.printInventory();
    }

    public void addMedicationToInventory(String medicationName,int units){
        CommonInventory.addItem(medicationName, units);
        System.out.println("Medication added to inventory: " + medicationName + " (" + units + ")");
    }

    // Function to approve replenishment requests
    public void approveReplenishmentRequests() {
        replenishRequestManager.viewReplenishRequests();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Replenish Request ID to approve: ");
        String requestId = scanner.nextLine();
        replenishRequestManager.acceptReplenishRequest(requestId);
        System.out.println("Replenish request approved.");
    }

    // Function to view appointments details (Placeholder for now)
    public void viewAppointmentsDetails(String appointmentId2) {
        appointmentManager.viewAppointmentOutcomeByAppointmentId(appointmentId2);
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
