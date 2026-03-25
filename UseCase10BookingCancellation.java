import java.util.*;

// Custom Exception
class InvalidCancellationException extends Exception {
    public InvalidCancellationException(String message) {
        super(message);
    }
}

// Reservation Model
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
               ", Guest: " + guestName +
               ", Room: " + roomType +
               ", Room ID: " + roomId +
               ", Status: " + (isCancelled ? "Cancelled" : "Active");
    }
}

// Inventory Manager
class InventoryManager {

    private Map<String, Integer> inventory;
    private Map<String, Stack<String>> roomPools;

    public InventoryManager() {
        inventory = new HashMap<>();
        roomPools = new HashMap<>();

        // Initialize inventory
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);

        // Initialize room IDs (Stack for rollback)
        roomPools.put("Standard", new Stack<>());
        roomPools.put("Deluxe", new Stack<>());

        roomPools.get("Standard").push("S1");
        roomPools.get("Standard").push("S2");
        roomPools.get("Deluxe").push("D1");
    }

    // Allocate room
    public String allocateRoom(String roomType) throws Exception {
        if (!inventory.containsKey(roomType)) {
            throw new Exception("Invalid room type");
        }

        if (inventory.get(roomType) <= 0) {
            throw new Exception("No rooms available");
        }

        String roomId = roomPools.get(roomType).pop();
        inventory.put(roomType, inventory.get(roomType) - 1);

        return roomId;
    }

    // Rollback (release room)
    public void releaseRoom(String roomType, String roomId) {
        roomPools.get(roomType).push(roomId);
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Booking Manager
class BookingManager {

    private Map<String, Reservation> reservations = new HashMap<>();
    private InventoryManager inventoryManager;

    public BookingManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public void createBooking(String id, String name, String roomType) {
        try {
            String roomId = inventoryManager.allocateRoom(roomType);
            Reservation r = new Reservation(id, name, roomType, roomId);
            reservations.put(id, r);

            System.out.println("Booking Confirmed: " + r);

        } catch (Exception e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }
}

// Cancellation Service
class CancellationService {

    private BookingManager bookingManager;
    private InventoryManager inventoryManager;

    public CancellationService(BookingManager bookingManager, InventoryManager inventoryManager) {
        this.bookingManager = bookingManager;
        this.inventoryManager = inventoryManager;
    }

    public void cancelBooking(String reservationId) throws InvalidCancellationException {

        Reservation r = bookingManager.getReservation(reservationId);

        // Validation
        if (r == null) {
            throw new InvalidCancellationException("Reservation does not exist.");
        }

        if (r.isCancelled()) {
            throw new InvalidCancellationException("Reservation already cancelled.");
        }

        // Rollback steps
        String roomType = r.getRoomType();
        String roomId = r.getRoomId();

        // Release room back to inventory (LIFO)
        inventoryManager.releaseRoom(roomType, roomId);

        // Mark as cancelled
        r.cancel();

        System.out.println("Cancellation Successful: " + reservationId);
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        BookingManager bookingManager = new BookingManager(inventoryManager);
        CancellationService cancellationService =
                new CancellationService(bookingManager, inventoryManager);

        // Create bookings
        bookingManager.createBooking("RES301", "Arun", "Standard");
        bookingManager.createBooking("RES302", "Meena", "Deluxe");

        inventoryManager.displayInventory();

        // Perform cancellations
        try {
            System.out.println("\nAttempting Cancellation...");
            cancellationService.cancelBooking("RES301");

            System.out.println("\nAttempting Duplicate Cancellation...");
            cancellationService.cancelBooking("RES301");

        } catch (InvalidCancellationException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }

        try {
            System.out.println("\nAttempting Invalid Cancellation...");
            cancellationService.cancelBooking("RES999");

        } catch (InvalidCancellationException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }

        // Final state
        inventoryManager.displayInventory();
    }
}