public class BookMyStayApp {

        static abstract class Room {

            protected int numberOfBeds;
            protected int squareFeet;
            protected double pricePerNight;

            public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
                this.numberOfBeds = numberOfBeds;
                this.squareFeet = squareFeet;
                this.pricePerNight = pricePerNight;
            }

            public void displayRoomDetails() {
                System.out.println("Beds: " + numberOfBeds);
                System.out.println("Size: " + squareFeet + " sqft");
                System.out.println("Price per night: " + pricePerNight);
            }
        }

        /* ================= SINGLE ROOM ================= */
        static class SingleRoom extends Room {
            public SingleRoom() {
                super(1, 250, 1500.0);
            }
        }

        /* ================= DOUBLE ROOM ================= */
        static class DoubleRoom extends Room {
            public DoubleRoom() {
                super(2, 400, 2500.0);
            }
        }

        /* ================= SUITE ROOM ================= */
        static class SuiteRoom extends Room {
            public SuiteRoom() {
                super(3, 750, 5000.0);
            }
        }

        /* ================= MAIN METHOD ================= */
        public static void main(String[] args) {

            System.out.println("Hotel Room Initialization\n");

            Room single = new SingleRoom();
            Room doub = new DoubleRoom();
            Room suite = new SuiteRoom();

            int singleAvailable = 5;
            int doubleAvailable = 3;
            int suiteAvailable = 2;

            System.out.println("Single Room:");
            single.displayRoomDetails();
            System.out.println("Available: " + singleAvailable + "\n");

            System.out.println("Double Room:");
            doub.displayRoomDetails();
            System.out.println("Available: " + doubleAvailable + "\n");

            System.out.println("Suite Room:");
            suite.displayRoomDetails();
            System.out.println("Available: " + suiteAvailable);
        }





}
