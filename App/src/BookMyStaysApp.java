import java.util.*;


class Reservation {
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

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();
    private Stack<String> rollbackStack = new Stack<>();

    public RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public void bookRoom(Reservation res) throws Exception {
        String roomType = res.getRoomType();
        if (!inventory.containsKey(roomType)) {
            throw new Exception("Invalid room type: " + roomType);
        }
        int available = inventory.get(roomType);
        if (available <= 0) {
            throw new Exception("No available rooms for: " + roomType);
        }
        inventory.put(roomType, available - 1);
        rollbackStack.push(res.getReservationId());
        System.out.println("Booked: " + res);
    }

    public void cancelBooking(Reservation res) throws Exception {
        if (!rollbackStack.contains(res.getReservationId())) {
            throw new Exception("Cannot cancel: Reservation not found or already cancelled: " + res.getReservationId());
        }
        rollbackStack.remove(res.getReservationId());

        String roomType = res.getRoomType();
        inventory.put(roomType, inventory.get(roomType) + 1);
        System.out.println("Cancelled: " + res + " | Inventory restored.");
    }

    public void printInventory() {
        System.out.println("\n=== Current Room Inventory ===");
        inventory.forEach((type, count) -> System.out.println(type + ": " + count + " room(s) available"));
    }
}
public class BookMyStaysApp {

    public static void main(String[] args) {
        System.out.println("=== Book My Stay App (Version 10.0) ===");

        RoomInventory inventory = new RoomInventory();

        Reservation r1 = new Reservation("RES101", "Single Room");
        Reservation r2 = new Reservation("RES102", "Double Room");
        Reservation r3 = new Reservation("RES103", "Suite Room");

        try {

            inventory.bookRoom(r1);
            inventory.bookRoom(r2);
            inventory.bookRoom(r3);

            inventory.cancelBooking(r2);

            Reservation r4 = new Reservation("RES999", "Suite Room");
            inventory.cancelBooking(r4); // Should raise error
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        inventory.printInventory();
        System.out.println("\nBooking Cancellation & Inventory Rollback completed.");
    }
}