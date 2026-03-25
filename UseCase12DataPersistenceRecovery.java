import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
               ", Guest: " + guestName +
               ", Room Type: " + roomType;
    }
}

// Wrapper class to persist system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> reservations;
    Map<String, Integer> inventory;

    public SystemState(List<Reservation> reservations, Map<String, Integer> inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public void saveState(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState loadState() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }
        return null;
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();

        // Attempt to restore state
        SystemState state = persistenceService.loadState();

        List<Reservation> reservations;
        Map<String, Integer> inventory;

        if (state != null) {
            // Restore previous state
            reservations = state.reservations;
            inventory = state.inventory;

            System.out.println("\nRecovered Reservations:");
            for (Reservation r : reservations) {
                System.out.println(r);
            }

        } else {
            // Initialize fresh state
            reservations = new ArrayList<>();
            inventory = new HashMap<>();

            inventory.put("Standard", 2);
            inventory.put("Deluxe", 1);

            // Simulate new bookings
            reservations.add(new Reservation("RES401", "Arun", "Standard"));
            reservations.add(new Reservation("RES402", "Meena", "Deluxe"));

            inventory.put("Standard", 1);
            inventory.put("Deluxe", 0);

            System.out.println("\nNew system initialized with sample data.");
        }

        // Display inventory
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Save state before shutdown
        SystemState newState = new SystemState(reservations, inventory);
        persistenceService.saveState(newState);
    }
}