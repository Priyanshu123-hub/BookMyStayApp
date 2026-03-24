import java.util.*;


public class BookMyStayApp {

    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
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
    }

    /* ================= QUEUE ================= */
    static class BookingRequestQueue {
        private Queue<String> queue = new LinkedList<>();

        public void addRequest(String request) {
            queue.offer(request);
        }
    }

    /* ================= VALIDATOR ================= */
    static class ReservationValidator {

        public void validate(
                String guestName,
                String roomType,
                RoomInventory inventory
        ) throws InvalidBookingException {

            if (guestName == null || guestName.trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty.");
            }

            if (!(roomType.equals("Single") ||
                    roomType.equals("Double") ||
                    roomType.equals("Suite"))) {
                throw new InvalidBookingException("Invalid room type selected.");
            }

            if (inventory.getAvailable(roomType) <= 0) {
                throw new InvalidBookingException("No rooms available for selected type.");
            }
        }
    }

    /* ================= MAIN ================= */
    public static void main(String[] args) {

        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue queue = new BookingRequestQueue();

        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            validator.validate(guestName, roomType, inventory);

            queue.addRequest(guestName + "-" + roomType);

            System.out.println("Booking request accepted.");

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

}
