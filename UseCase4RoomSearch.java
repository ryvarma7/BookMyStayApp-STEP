import java.util.HashMap;
import java.util.ArrayList;

abstract class Room {
    protected String type;
    protected int beds;
    protected double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Number of Beds: " + beds);
        System.out.println("Price per Night: $" + price);
    }

    public String getType() {
        return type;
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 50.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 80.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 150.0);
    }
}

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoom(Room room, int count) {
        inventory.put(room.getType(), count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public HashMap<String, Integer> getInventorySnapshot() {
        return new HashMap<>(inventory);
    }
}

class RoomSearchService {
    private RoomInventory inventory;
    private ArrayList<Room> rooms;

    public RoomSearchService(RoomInventory inventory, ArrayList<Room> rooms) {
        this.inventory = inventory;
        this.rooms = rooms;
    }

    public void displayAvailableRooms() {
        System.out.println("Available Rooms:");
        for (Room room : rooms) {
            if (inventory.getAvailability(room.getType()) > 0) {
                room.displayDetails();
                System.out.println("Available: " + inventory.getAvailability(room.getType()) + "\n");
            }
        }
    }
}

public class UseCase4RoomSearch {
    public static void main(String[] args) {
        SingleRoom single = new SingleRoom();
        DoubleRoom doubleRoom = new DoubleRoom();
        SuiteRoom suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom(single, 5);
        inventory.addRoom(doubleRoom, 0);
        inventory.addRoom(suite, 2);

        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(single);
        rooms.add(doubleRoom);
        rooms.add(suite);

        RoomSearchService searchService = new RoomSearchService(inventory, rooms);
        searchService.displayAvailableRooms();
    }
}