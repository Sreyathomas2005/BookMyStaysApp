import java.util.*;
import java.util.concurrent.*;


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

    public RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public synchronized boolean bookRoom(Reservation res) {
        String roomType = res.getRoomType();
        int available = inventory.getOrDefault(roomType, 0);
        if (available <= 0) {
            System.out.println("No available rooms for: " + roomType + " | Reservation failed: " + res.getReservationId());
            return false;
        }
        inventory.put(roomType, available - 1);
        System.out.println("Booked: " + res + " | Remaining " + roomType + ": " + (available - 1));
        return true;
    }

    public synchronized void printInventory() {
        System.out.println("\n=== Final Inventory Status ===");
        inventory.forEach((type, count) -> System.out.println(type + ": " + count + " room(s) available"));
    }
}

class BookingTask implements Runnable {
    private RoomInventory inventory;
    private Reservation reservation;

    public BookingTask(RoomInventory inventory, Reservation reservation) {
        this.inventory = inventory;
        this.reservation = reservation;
    }

    @Override
    public void run() {
        inventory.bookRoom(reservation);
    }
}

public class BookMyStaysApp {

    public static void main(String[] args) {
        System.out.println("=== Book My Stay App (Version 11.0) ===");
        RoomInventory inventory = new RoomInventory();

        List<Reservation> requests = Arrays.asList(
                new Reservation("RES201", "Single Room"),
                new Reservation("RES202", "Double Room"),
                new Reservation("RES203", "Single Room"),
                new Reservation("RES204", "Suite Room"),
                new Reservation("RES205", "Single Room"),
                new Reservation("RES206", "Double Room"),
                new Reservation("RES207", "Suite Room"),
                new Reservation("RES208", "Single Room")
        );

        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (Reservation res : requests) {
            executor.submit(new BookingTask(inventory, res));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.printInventory();
        System.out.println("\nConcurrent Booking Simulation completed.");
    }
}
