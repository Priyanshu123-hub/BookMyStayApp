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
        private Queue<Reservation> requestQueue = new LinkedList<>();

        public void addRequest(Reservation r) {
            requestQueue.offer(r);
        }

        public Reservation getNextRequest() {
            return requestQueue.poll();
        }

        public boolean hasPendingRequests() {
            return !requestQueue.isEmpty();
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

        public void updateAvailability(String type, int count) {
            availability.put(type, count);
        }
    }

    /* ================= ALLOCATION SERVICE ================= */
    static class RoomAllocationService {

        private Set<String> allocatedRoomIds;
        private Map<String, Set<String>> assignedRoomsByType;
        private Map<String, Integer> counters;

        public RoomAllocationService() {
            allocatedRoomIds = new HashSet<>();
            assignedRoomsByType = new HashMap<>();
            counters = new HashMap<>();
        }

        public void allocateRoom(Reservation reservation, RoomInventory inventory) {

            String type = reservation.getRoomType();

            if (inventory.getAvailable(type) <= 0) {
                System.out.println("No rooms available for " + type);
                return;
            }

            String roomId = generateRoomId(type);

            allocatedRoomIds.add(roomId);

            assignedRoomsByType
                    .computeIfAbsent(type, k -> new HashSet<>())
                    .add(roomId);

            inventory.updateAvailability(type, inventory.getAvailable(type) - 1);

            System.out.println("Booking confirmed for Guest: "
                    + reservation.getGuestName()
                    + ", Room ID: " + roomId);
        }

        private String generateRoomId(String roomType) {
            int next = counters.getOrDefault(roomType, 0) + 1;
            counters.put(roomType, next);
            return roomType + "-" + next;
        }
    }

    /* ================= MAIN ================= */
    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocator = new RoomAllocationService();

        // Requests (FIFO)
        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));

        // Process queue
        while (queue.hasPendingRequests()) {
            Reservation r = queue.getNextRequest();
            allocator.allocateRoom(r, inventory);
        }
    }



}
