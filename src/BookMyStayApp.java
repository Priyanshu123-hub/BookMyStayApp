import java.io.*;
import java.util.*;


public class BookMyStayApp {

    static class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 5);
            availability.put("Double", 3);
            availability.put("Suite", 2);
        }

        public Map<String, Integer> getAll() {
            return availability;
        }

        public void setAvailability(String type, int count) {
            availability.put(type, count);
        }
    }

    /* ================= FILE SERVICE ================= */
    static class FilePersistenceService {

        public void saveInventory(RoomInventory inventory, String filePath) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Map.Entry<String, Integer> entry : inventory.getAll().entrySet()) {
                    writer.write(entry.getKey() + "=" + entry.getValue());
                    writer.newLine();
                }
                System.out.println("Inventory saved successfully.");
            } catch (IOException e) {
                System.out.println("Error saving inventory.");
            }
        }

        public void loadInventory(RoomInventory inventory, String filePath) {
            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("No valid inventory data found. Starting fresh.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        inventory.setAvailability(parts[0], Integer.parseInt(parts[1]));
                    }
                }

                System.out.println("Inventory loaded successfully.");

            } catch (Exception e) {
                System.out.println("Error loading inventory. Starting fresh.");
            }
        }
    }

    /* ================= MAIN ================= */
    public static void main(String[] args) {

        System.out.println("System Recovery");

        String filePath = "inventory.txt";

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService service = new FilePersistenceService();

        // Load existing data
        service.loadInventory(inventory, filePath);

        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getAll().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Save current state
        service.saveInventory(inventory, filePath);
    }
}
