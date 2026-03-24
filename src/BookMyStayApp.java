import java.util.*;


public class BookMyStayApp {

    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
    }

    /* ================= QUEUE ================= */
    static class BookingRequestQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.offer(r);
        }

        public Reservation getNextRequest() {
            return queue.poll();
        }

        public boolean hasRequests() {
            return !queue.isEmpty();
        }
    }

    /* ================= INVENTORY ================= */
    static class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 5);
            availability.put("Double", 3);
            availability.put("Suite", 2);
        }

        public int getAvailable(String type) {
            return availability.getOrDefault(type, 0);
        }

        public void update(String type, int count) {
            availability.put(type, count);
        }

        public Map<String, Integer> getAll() {
            return availability;
        }
    }

    /* ================= ALLOCATION ================= */
    static class RoomAllocationService {
        private Map<String, Integer> counters = new HashMap<>();

        public void allocateRoom(Reservation r, RoomInventory inventory) {
            String type = r.getRoomType();

            if (inventory.getAvailable(type) <= 0) return;

            int id = counters.getOrDefault(type, 0) + 1;
            counters.put(type, id);

            inventory.update(type, inventory.getAvailable(type) - 1);

            System.out.println("Booking confirmed for Guest: "
                    + r.getGuestName() + ", Room ID: " + type + "-" + id);
        }
    }

    /* ================= THREAD PROCESSOR ================= */
    static class ConcurrentBookingProcessor implements Runnable {

        private BookingRequestQueue bookingQueue;
        private RoomInventory inventory;
        private RoomAllocationService allocationService;

        public ConcurrentBookingProcessor(
                BookingRequestQueue bookingQueue,
                RoomInventory inventory,
                RoomAllocationService allocationService) {
            this.bookingQueue = bookingQueue;
            this.inventory = inventory;
            this.allocationService = allocationService;
        }

        @Override
        public void run() {
            while (true) {

                Reservation reservation;

                synchronized (bookingQueue) {
                    if (!bookingQueue.hasRequests()) return;
                    reservation = bookingQueue.getNextRequest();
                }

                synchronized (inventory) {
                    allocationService.allocateRoom(reservation, inventory);
                }
            }
        }
    }

    /* ================= MAIN ================= */
    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Add requests
        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kunal", "Suite"));

        // Threads
        Thread t1 = new Thread(new ConcurrentBookingProcessor(
                bookingQueue, inventory, allocationService));

        Thread t2 = new Thread(new ConcurrentBookingProcessor(
                bookingQueue, inventory, allocationService));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        System.out.println("\nRemaining Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getAll().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

}
