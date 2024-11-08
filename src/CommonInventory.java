import java.util.HashMap;


public class CommonInventory {
    public static HashMap<String, PrescribedMedication> inventory = new HashMap<>();

    // Add or update an item in the inventory with the specified units
    public static void addItem(String medicationName, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }

        if (inventory.containsKey(medicationName)) {
            // If the medication already exists, update the count
            PrescribedMedication medication = inventory.get(medicationName);
            medication.setUnits(medication.getUnits() + count);
            System.out.println(medicationName + "stock has been updated to: " + medication.getUnits() + " units");
        } else {
            // Otherwise, add a new entry for the medication
            inventory.put(medicationName, new PrescribedMedication(medicationName, count));
        }
    }

    // Remove a specified number of units from the inventory
    public static void removeItem(String medicationName, int count) {
        if (!inventory.containsKey(medicationName)) {
            System.out.println("Item not found in inventory.");
            return;
        }

        PrescribedMedication medication = inventory.get(medicationName);
        int currentUnits = medication.getUnits();

        if (count > currentUnits) {
            throw new IllegalArgumentException("Cannot remove more units than available");
        } else if (count == currentUnits) {
            // Remove the item completely if all units are removed
            inventory.remove(medicationName);
        } else {
            // Otherwise, update the units count
            medication.setUnits(currentUnits - count);
        }
    }

    // Print the entire inventory
    public static void printInventory() {
        inventory.forEach((item, medication) ->
                System.out.println(item + ": " + medication.getUnits() + " units"));
    }
}

