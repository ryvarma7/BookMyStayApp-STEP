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
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementAvailability(String roomType) {
        if (isAvailable(roomType)) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class BookingService {
    private RoomInventory inventory;
    private Queue<Reservation> requestQueue;
    private HashMap<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory, Queue<Reservation> requestQueue) {
        this.inventory = inventory;
        this.requestQueue = requestQueue;
        allocatedRooms = new HashMap<>();
    }

    private String generateRoomID(String roomType) {
        return roomType.substring(0, 3).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            Reservation r = requestQueue.poll();
            String type = r.getRoomType();
            if (inventory.isAvailable(type)) {
                String roomID = generateRoomID(type);
                allocatedRooms.putIfAbsent(type, new HashSet<>());
                allocatedRooms.get(type).add(roomID);
                inventory.decrementAvailability(type);
                System.out.println("Reservation Confirmed: " + r.getGuestName() + " -> " + type + " (Room ID: " + roomID + ")");
            } else {
                System.out.println("Reservation Failed (No Availability): " + r.getGuestName() + " -> " + type);
            }
        }
    }

    public void displayAllocatedRooms() {
        System.out.println("\nAllocated Rooms:");
        for (String type : allocatedRooms.keySet()) {
            System.out.println(type + ": " + allocatedRooms.get(type));
        }
    }
}

public class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        Queue<Reservation> bookingQueue = new LinkedList<>();
        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Single Room"));
        bookingQueue.add(new Reservation("David", "Suite Room"));
        bookingQueue.add(new Reservation("Eve", "Double Room"));

        BookingService service = new BookingService(inventory, bookingQueue);
        service.processBookings();
        service.displayAllocatedRooms();
    }
}