import java.util.*;

// Represents an Add-On Service
class Service {
    private String serviceId;
    private String serviceName;
    private double cost;

    public Service(String serviceId, String serviceName, double cost) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manages Add-On Services for Reservations
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<Service>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service added to Reservation " + reservationId + ": " + service.getServiceName());
    }

    // Get services for a reservation
    public List<Service> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalServiceCost(String reservationId) {
        double total = 0.0;

        List<Service> services = reservationServicesMap.get(reservationId);
        if (services != null) {
            for (Service service : services) {
                total += service.getCost();
            }
        }

        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<Service> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("\nAdd-On Services for Reservation " + reservationId + ":");
        for (Service s : services) {
            System.out.println("- " + s);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalServiceCost(reservationId));
    }
}

// Main class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        // Simulated Reservation ID (from previous use case)
        String reservationId = "RES123";

        AddOnServiceManager manager = new AddOnServiceManager();

        // Available services
        Service breakfast = new Service("S1", "Breakfast", 500);
        Service airportPickup = new Service("S2", "Airport Pickup", 1200);
        Service spa = new Service("S3", "Spa Access", 2000);

        // Guest selects services
        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, airportPickup);
        manager.addService(reservationId, spa);

        // Display selected services
        manager.displayServices(reservationId);
    }
}