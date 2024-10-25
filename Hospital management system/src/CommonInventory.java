import java.util.HashMap;

public class CommonInventory {
    public static HashMap<String, Integer> inventory = new HashMap<>();

    // Optionally, you can add methods to manipulate the inventory if needed
    public static void addItem(String medicationName, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        inventory.put(medicationName, inventory.getOrDefault(medicationName, 0) + count);
    }

    public static void removeItem(String item) {
        if (inventory.containsKey(item)) {
            int count = inventory.get(item);
            if (count > 1) {
                inventory.put(item, count - 1);
            } else {
                inventory.remove(item);
            }
        }
    }

    public static void printInventory() {
        inventory.forEach((item, count) -> System.out.println(item + ": " + count));
    }
}

