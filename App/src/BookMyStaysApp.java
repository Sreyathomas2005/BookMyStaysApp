import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return reservationId + " (" + roomType + ")";
    }
}

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public boolean bookRoom(Reservation res) {
        String type = res.getRoomType();
        int available = inventory.getOrDefault(type, 0);
        if (available <= 0) return false;
        inventory.put(type, available - 1);
        return true;
    }

    public void cancelRoom(Reservation res) {
        String type = res.getRoomType();
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void printInventory() {
        System.out.println("\n=== Inventory Status ===");
        inventory.forEach((type, count) -> System.out.println(type + ": " + count + " room(s) available"));
    }
}

class PersistenceService {
    private static final String INVENTORY_FILE = "inventory.ser";
    private static final String HISTORY_FILE = "booking_history.ser";

    public static void save(Object obj, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(obj);
            System.out.println("Saved state to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    public static Object load(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            System.out.println("Loaded state from " + filename);
            return in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing file: " + filename + ". Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state: " + e.getMessage());
        }
        return null;
    }
}
public class BookMyStaysApp {

    public static void main(String[] args) {
        System.out.println("=== Book My Stay App (Version 12.0) ===");

        RoomInventory inventory = (RoomInventory) PersistenceService.load("inventory.ser");
        if (inventory == null) inventory = new RoomInventory();

        List<Reservation> bookingHistory = (List<Reservation>) PersistenceService.load("booking_history.ser");
        if (bookingHistory == null) bookingHistory = new ArrayList<>();
        // Simulate new bookings
        Reservation r1 = new Reservation("RES301", "Single Room");
        Reservation r2 = new Reservation("RES302", "Double Room");

        if (inventory.bookRoom(r1)) bookingHistory.add(r1);
        if (inventory.bookRoom(r2)) bookingHistory.add(r2);
        // Print current state
        inventory.printInventory();
        System.out.println("\n=== Booking History ===");
        bookingHistory.forEach(System.out::println);

        PersistenceService.save(inventory, "inventory.ser");
        PersistenceService.save(bookingHistory, "booking_history.ser");

        System.out.println("\nSystem state persisted successfully. Restart to recover state.");
    }
}