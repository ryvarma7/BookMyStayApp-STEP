import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Represents Reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
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
        return "Reservation ID: " + reservationId +
               ", Guest: " + guestName +
               ", Room Type: " + roomType;
    }
}

// Inventory Manager (with validation)
class InventoryManager {
    private Map<String, Integer> roomInventory;

    public InventoryManager() {
        roomInventory = new HashMap<>();
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 1);
        roomInventory.put("Suite", 0);
    }

    // Validate and allocate room
    public void allocateRoom(String roomType) throws InvalidBookingException {

        // Validate room type
        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int available = roomInventory.get(roomType);

        // Prevent negative inventory
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        // Safe allocation
        roomInventory.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Booking Service with validation (Fail-Fast)
class BookingService {

    private InventoryManager inventoryManager;

    public BookingService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public Reservation createBooking(String reservationId, String guestName, String roomType)
            throws InvalidBookingException {

        // Input validation (Fail-Fast)
        if (reservationId == null || reservationId.isEmpty()) {
            throw new InvalidBookingException("Reservation ID cannot be empty.");
        }

        if (guestName == null || guestName.isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (roomType == null || roomType.isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        // Validate and allocate room
        inventoryManager.allocateRoom(roomType);

        // Create reservation only if validation succeeds
        return new Reservation(reservationId, guestName, roomType);
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        BookingService bookingService = new BookingService(inventoryManager);

        // Test scenarios
        String[][] testInputs = {
            {"RES201", "Arun", "Deluxe"},     // valid
            {"RES202", "Meena", "Suite"},     // no availability
            {"RES203", "Rahul", "Premium"},   // invalid room type
            {"", "John", "Standard"},         // invalid ID
            {"RES205", "", "Standard"}        // invalid guest name
        };

        for (String[] input : testInputs) {
            try {
                System.out.println("\nAttempting Booking...");

                Reservation r = bookingService.createBooking(
                        input[0], input[1], input[2]
                );

                System.out.println("Booking Successful: " + r);

            } catch (InvalidBookingException e) {
                // Graceful failure handling
                System.out.println("Booking Failed: " + e.getMessage());
            }
        }

        // Show final inventory state
        inventoryManager.displayInventory();
    }
}