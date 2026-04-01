import java.util.HashMap;
import java.util.Map;

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
    public void updateAvailability(String roomType, int countChange) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + countChange);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

public class BookMyStaysApp {

    public static void main(String[] args) {

        System.out.println("=== Book My Stay App (Version 3.1) ===");

        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();

        System.out.println("\nAvailable Single Rooms: " +
                inventory.getAvailability("Single Room"));

        inventory.updateAvailability("Single Room", -1);
        System.out.println("\nAfter Booking 1 Single Room:");
        inventory.displayInventory();

        System.out.println("\nApplication Finished.");
    }
}