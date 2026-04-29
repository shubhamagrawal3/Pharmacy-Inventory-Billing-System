import java.util.*;

// Custom Exceptions
class MedicineNotFoundException extends Exception {
    public MedicineNotFoundException(String message) {
        super(message);
    }
}

class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}

// Medicine Model Class
class Medicine {
    int id;
    String name;
    double price;
    int stock;
    String expiryDate;

    public Medicine(int id, String name, double price, int stock, String expiryDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.expiryDate = expiryDate;
    }

    public void showDetails() {
        System.out.println("ID: " + id + " | Name: " + name + " | Price: " + price + 
                           " | Stock: " + stock + " | Expiry: " + expiryDate);
    }
}

public class PharmacyManagement {
    private static HashMap<Integer, Medicine> inventory = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Initial dummy data for the project
        inventory.put(101, new Medicine(101, "Paracetamol", 20.0, 50, "12/2026"));
        inventory.put(102, new Medicine(102, "Amoxicillin", 150.0, 3, "05/2027"));

        while (true) {
            System.out.println("\n--- Pharmacy Inventory & Billing System ---");
            System.out.println("1. View Inventory");
            System.out.println("2. Add/Update Stock");
            System.out.println("3. Generate Bill");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewInventory();
                    break;
                case 2:
                    addOrUpdateStock();
                    break;
                case 3:
                    try {
                        generateBill();
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Exiting System...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static void viewInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Medicine m : inventory.values()) {
            m.showDetails();
            // Low Stock Alert check
            if (m.stock < 5) {
                System.out.println("   *** ALERT: Low Stock! Please restock " + m.name + " ***");
            }
        }
    }

    private static void addOrUpdateStock() {
        System.out.print("Enter Medicine ID: ");
        int id = sc.nextInt();
        System.out.print("Enter Name: ");
        String name = sc.next();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        System.out.print("Enter Expiry (MM/YYYY): ");
        String expiry = sc.next();

        inventory.put(id, new Medicine(id, name, price, qty, expiry));
        System.out.println("Inventory updated successfully!");
    }

    private static void generateBill() throws MedicineNotFoundException, OutOfStockException {
        System.out.print("Enter Medicine ID for Billing: ");
        int medId = sc.nextInt();

        if (!inventory.containsKey(medId)) {
            throw new MedicineNotFoundException("Medicine with ID " + medId + " not found!");
        }

        Medicine med = inventory.get(medId);

        System.out.print("Enter Quantity needed: ");
        int qtyNeeded = sc.nextInt();

        if (qtyNeeded > med.stock) {
            throw new OutOfStockException("Only " + med.stock + " units available for " + med.name);
        }

        // Logic for calculations
        double basePrice = med.price * qtyNeeded;
        double discount = basePrice * 0.05; // 5% Discount
        double gst = (basePrice - discount) * 0.10; // 10% GST on discounted price
        double finalPrice = (basePrice - discount) + gst;

        // Updating stock
        med.stock -= qtyNeeded;

        System.out.println("\n--- BILL RECEIPT ---");
        System.out.println("Medicine: " + med.name);
        System.out.println("Quantity: " + qtyNeeded);
        System.out.println("Base Price: " + basePrice);
        System.out.println("Discount (5%): -" + discount);
        System.out.println("GST (10%): +" + gst);
        System.out.println("Total Amount to Pay: " + finalPrice);
        System.out.println("--------------------");
    }
}
