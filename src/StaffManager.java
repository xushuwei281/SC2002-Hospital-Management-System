import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public class StaffManager {
    public Map<String, User> hospitalStaff;

    public StaffManager() {
        this.hospitalStaff = new java.util.HashMap<>();
    }

    public void addStaff(User user) {
        hospitalStaff.put(user.id, user);
        System.out.println("Staff member added: " + user.name);
    }

    public void updateStaff(String staffId) {
        User user = hospitalStaff.get(staffId);

        if (user == null) {
            System.out.println("Staff member not found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean updating = true;

        while (updating) {
            System.out.println("\nUpdating details for: " + user.name);
            System.out.println("Enter the attribute you want to update (name, role, gender, age) or type 'exit' to finish:");

            String attribute = scanner.nextLine();

            switch (attribute.toLowerCase()) {
                case "name":
                    System.out.print("Enter new name: ");
                    user.name = scanner.nextLine();
                    System.out.println("Name updated to: " + user.name);
                    break;

                case "role":
                    if (user instanceof Doctor) {
                        System.out.print("Enter new role: ");
                        String newRole = scanner.nextLine();
                        ((Doctor) user).role = newRole;
                        System.out.println("Role updated to: " + newRole);
                    } else {
                        System.out.println("This user is not a doctor. Cannot update role.");
                    }
                    break;

                case "gender":
                    if (user instanceof Doctor) {
                        System.out.print("Enter new gender: ");
                        String newGender = scanner.nextLine();
                        ((Doctor) user).gender = newGender;
                        System.out.println("Gender updated to: " + newGender);
                    } else {
                        System.out.println("This user is not a doctor. Cannot update gender.");
                    }
                    break;

                case "age":
                    if (user instanceof Doctor) {
                        System.out.print("Enter new age: ");
                        String newAge = scanner.nextLine();
                        ((Doctor) user).age = newAge;
                        System.out.println("Age updated to: " + newAge);
                    } else {
                        System.out.println("This user is not a doctor. Cannot update age.");
                    }
                    break;

                case "exit":
                    updating = false;
                    System.out.println("Finished updating staff member.");
                    break;

                default:
                    System.out.println("Invalid attribute. Please enter a valid attribute name.");
                    break;
            }
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
            if (role.equalsIgnoreCase("Doctor") && user instanceof Doctor) {
                System.out.println(user.name);

            } else if (role.equalsIgnoreCase("Pharmacist") && user instanceof Pharmacist) {
                System.out.println(user.name);
            }
        }
    }
}
