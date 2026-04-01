import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }

    @Override
    public String toString() {
        return guestName + " | " + roomType + " | Room ID: " + roomId;
    }
}

class BookingHistory {
    private List<Reservation> confirmedBookings = new ArrayList<>();

    // Add a reservation to history
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedBookings);
    }
}

class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    public void printBookingHistory() {
        System.out.println("\n=== Confirmed Booking History ===");
        List<Reservation> bookings = history.getAllReservations();
        if (bookings.isEmpty()) {
            System.out.println("No confirmed bookings yet.");
            return;
        }
        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }

    public void printSummaryByRoomType() {
        System.out.println("\n=== Booking Summary by Room Type ===");
        Map<String, Integer> roomTypeCount = new HashMap<>();
        for (Reservation r : history.getAllReservations()) {
            roomTypeCount.put(r.getRoomType(), roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1);
        }
        for (String roomType : roomTypeCount.keySet()) {
            System.out.println(roomType + ": " + roomTypeCount.get(roomType) + " booking(s)");
        }
    }
}
public class BookMyStaysApp {

    public static void main(String[] args) {
        System.out.println("=== Book My Stay App (Version 8.0) ===");

        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("Alice", "Single Room", "SI123");
        Reservation r2 = new Reservation("Bob", "Double Room", "DO456");
        Reservation r3 = new Reservation("Charlie", "Suite Room", "SU789");
        Reservation r4 = new Reservation("Diana", "Single Room", "SI124");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);
        history.addReservation(r4);

        BookingReportService reportService = new BookingReportService(history);

        reportService.printBookingHistory();

        reportService.printSummaryByRoomType();

        System.out.println("\nBooking History & Reporting completed.");
    }
}