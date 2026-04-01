import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseAvailability(String roomType) {
        inventory.put(roomType, getAvailability(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String key : inventory.keySet()) {
            System.out.println(key + " : " + inventory.get(key));
        }
    }
}

class BookingService {

    private Queue<Reservation> queue;
    private RoomInventory inventory;

    private Set<String> allocatedRoomIds = new HashSet<>();

    private Map<String, Set<String>> allocationMap = new HashMap<>();

    public BookingService(Queue<Reservation> queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    private String generateRoomId(String roomType) {
        String id;
        do {
            id = roomType.substring(0, 2).toUpperCase() + new Random().nextInt(1000);
        } while (allocatedRoomIds.contains(id));

        allocatedRoomIds.add(id);
        return id;
    }

    public void processBookings() {

        System.out.println("\n--- Processing Bookings ---");

        while (!queue.isEmpty()) {

            Reservation request = queue.poll();

            String roomType = request.getRoomType();

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = generateRoomId(roomType);

                allocationMap.putIfAbsent(roomType, new HashSet<>());
                allocationMap.get(roomType).add(roomId);

                inventory.decreaseAvailability(roomType);

                System.out.println("Booking Confirmed for " + request.getGuestName()
                        + " | Room Type: " + roomType
                        + " | Room ID: " + roomId);

            } else {
                System.out.println("Booking Failed for " + request.getGuestName()
                        + " | No rooms available for " + roomType);
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\n--- Room Allocations ---");
        for (String type : allocationMap.keySet()) {
            System.out.println(type + " → " + allocationMap.get(type));
        }
    }
}
public class BookMyStaysApp {

    public static void main(String[] args) {

        System.out.println("=== Book My Stay App (Version 6.0) ===");

        Queue<Reservation> queue = new LinkedList<>();

        queue.offer(new Reservation("Alice", "Single Room"));
        queue.offer(new Reservation("Bob", "Double Room"));
        queue.offer(new Reservation("Charlie", "Single Room"));
        queue.offer(new Reservation("David", "Suite Room"));
        queue.offer(new Reservation("Eve", "Single Room")); // may fail

        RoomInventory inventory = new RoomInventory();

        BookingService service = new BookingService(queue, inventory);

        service.processBookings();

        service.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nProcessing completed.");
    }
}