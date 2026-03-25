import java.util.*;

// Represents a Reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double bookingAmount;

    public Reservation(String reservationId, String guestName, String roomType, double bookingAmount) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.bookingAmount = bookingAmount;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getBookingAmount() {
        return bookingAmount;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
               ", Guest: " + guestName +
               ", Room Type: " + roomType +
               ", Amount: ₹" + bookingAmount;
    }
}

// Maintains booking history
class BookingHistory {
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        System.out.println("Booking stored in history: " + reservation.getReservationId());
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations); // return copy to prevent modification
    }
}

// Generates reports
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummaryReport(List<Reservation> reservations) {
        int totalBookings = reservations.size();
        double totalRevenue = 0.0;

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            totalRevenue += r.getBookingAmount();

            roomTypeCount.put(
                r.getRoomType(),
                roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);

        System.out.println("\nRoom Type Distribution:");
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("RES101", "Arun", "Deluxe", 3000);
        Reservation r2 = new Reservation("RES102", "Meena", "Suite", 5000);
        Reservation r3 = new Reservation("RES103", "Rahul", "Deluxe", 3200);

        // Store in history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin retrieves data
        List<Reservation> storedReservations = history.getAllReservations();

        // Display all bookings
        reportService.displayAllBookings(storedReservations);

        // Generate summary report
        reportService.generateSummaryReport(storedReservations);
    }
}