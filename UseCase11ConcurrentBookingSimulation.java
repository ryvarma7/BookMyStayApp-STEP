import java.util.*;

// Booking Request
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Thread-safe Inventory Manager
class InventoryManager {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryManager() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // Critical Section (synchronized)
    public synchronized boolean allocateRoom(String roomType, String guestName) {

        if (!inventory.containsKey(roomType)) {
            System.out.println("Invalid room type for " + guestName);
            return false;
        }

        int available = inventory.get(roomType);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " allocating " + roomType + " to " + guestName);

            // Simulate delay (to expose race condition if not synchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            inventory.put(roomType, available - 1);

            System.out.println("Booking SUCCESS for " + guestName);
            return true;
        } else {
            System.out.println("Booking FAILED for " + guestName + " (No rooms)");
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Shared Booking Queue
class BookingQueue {

    private Queue<BookingRequest> queue = new LinkedList<>();

    // synchronized add
    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    // synchronized retrieval
    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Worker Thread
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryManager inventoryManager;

    public BookingProcessor(String name, BookingQueue queue, InventoryManager inventoryManager) {
        super(name);
        this.queue = queue;
        this.inventoryManager = inventoryManager;
    }

    @Override
    public void run() {

        while (true) {

            BookingRequest request;

            // Critical section for queue access
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) {
                break; // no more requests
            }

            // Process booking (thread-safe)
            inventoryManager.allocateRoom(request.roomType, request.guestName);
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate multiple guest requests
        bookingQueue.addRequest(new BookingRequest("Arun", "Standard"));
        bookingQueue.addRequest(new BookingRequest("Meena", "Standard"));
        bookingQueue.addRequest(new BookingRequest("Rahul", "Standard")); // should fail
        bookingQueue.addRequest(new BookingRequest("John", "Deluxe"));
        bookingQueue.addRequest(new BookingRequest("Anita", "Deluxe"));   // should fail

        // Create multiple threads (simulating concurrent users)
        BookingProcessor t1 = new BookingProcessor("Thread-1", bookingQueue, inventoryManager);
        BookingProcessor t2 = new BookingProcessor("Thread-2", bookingQueue, inventoryManager);
        BookingProcessor t3 = new BookingProcessor("Thread-3", bookingQueue, inventoryManager);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventoryManager.displayInventory();
    }
}