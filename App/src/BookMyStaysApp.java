import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public void validateAndBook(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Error: Invalid room type '" + roomType + "'");
        }
        int available = inventory.get(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("Error: No available rooms for '" + roomType + "'");
        }
        inventory.put(roomType, available - 1);
        System.out.println("Booking successful for: " + roomType + " | Remaining: " + (available - 1));
    }

    public void printInventory() {
        System.out.println("\n=== Current Room Inventory ===");
        inventory.forEach((type, count) -> System.out.println(type + ": " + count + " room(s) available"));
    }
}

public class BookMyStaysApp {

    public static void main(String[] args) {
        System.out.println("=== Book My Stay App (Version 9.0) ===");

        RoomInventory inventory = new RoomInventory();
        inventory.printInventory();

        String[] bookingRequests = {
                "Single Room",
                "Double Room",
                "Penthouse",
                "Suite Room",
                "Single Room",
                "Suite Room",
                "Suite Room"
        };

        for (String request : bookingRequests) {
            try {
                inventory.validateAndBook(request);
            } catch (InvalidBookingException e) {
                System.out.println(e.getMessage());
            }
        }
        inventory.printInventory();

        System.out.println("\nError Handling & Validation completed.");
    }
}