import java.util.*;


public class BookMyStayApp {

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

    /* ================= CANCELLATION SERVICE ================= */
    static class CancellationService {

        private Stack<String> releasedRoomIds;
        private Map<String, String> reservationRoomTypeMap;

        public CancellationService() {
            releasedRoomIds = new Stack<>();
            reservationRoomTypeMap = new HashMap<>();
        }

        public void registerBooking(String reservationId, String roomType) {
            reservationRoomTypeMap.put(reservationId, roomType);
        }

        public void cancelBooking(String reservationId, RoomInventory inventory) {

            if (!reservationRoomTypeMap.containsKey(reservationId)) {
                System.out.println("Invalid cancellation request.");
                return;
            }

            String roomType = reservationRoomTypeMap.get(reservationId);

            releasedRoomIds.push(reservationId);

            inventory.updateAvailability(
                    roomType,
                    inventory.getAvailable(roomType) + 1
            );

            reservationRoomTypeMap.remove(reservationId);

            System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        }

        public void showRollbackHistory() {

            System.out.println("\nRollback History (Most Recent First):");

            while (!releasedRoomIds.isEmpty()) {
                System.out.println("Released Reservation ID: " + releasedRoomIds.pop());
            }
        }
    }

    /* ================= MAIN ================= */
    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        String reservationId = "Single-1";

        // Simulate a confirmed booking
        service.registerBooking(reservationId, "Single");

        // Cancel booking
        service.cancelBooking(reservationId, inventory);

        // Show rollback history
        service.showRollbackHistory();

        System.out.println("\nUpdated Single Room Availability: "
                + inventory.getAvailable("Single"));
    }

}
