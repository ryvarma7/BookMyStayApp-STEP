import java.util.LinkedList;
import java.util.Queue;

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

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " requested " + roomType);
    }
}

class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
    }

    public Reservation pollRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }

    public void displayAllRequests() {
        System.out.println("Current Booking Requests:");
        for (Reservation r : requestQueue) {
            r.displayRequest();
        }
    }
}

public class UseCase5BookingRequestQueue {
    public static void main(String[] args) {
        BookingRequestQueue queue = new BookingRequestQueue();

        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        queue.addRequest(r1);
        queue.addRequest(r2);
        queue.addRequest(r3);

        queue.displayAllRequests();
    }
}