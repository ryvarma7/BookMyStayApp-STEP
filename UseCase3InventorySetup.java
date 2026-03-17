import java.util.HashMap;

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

    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type) + " available");
        }
    }
}

public class UseCase3InventorySetup {
    public static void main(String[] args) {
        SingleRoom single = new SingleRoom();
        DoubleRoom doubleRoom = new DoubleRoom();
        SuiteRoom suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom(single, 5);
        inventory.addRoom(doubleRoom, 3);
        inventory.addRoom(suite, 2);

        single.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(single.getType()) + "\n");

        doubleRoom.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(doubleRoom.getType()) + "\n");

        suite.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(suite.getType()) + "\n");

        inventory.displayInventory();
    }
}