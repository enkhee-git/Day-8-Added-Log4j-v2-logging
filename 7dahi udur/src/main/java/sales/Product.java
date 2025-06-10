package sales;

public class Product {
    private String name;
    private double price;
    private int stockQuantity;

    public Product(String name, double price, int stockQuantity) {
        if (name == null || name.trim().isEmpty() || !name.matches("[a-zA-Z0-9\\s]+")) {
            throw new IllegalArgumentException(
                    "Product name must not be empty and only contain letters, numbers, and spaces.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be a positive number.");
        }
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }

        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void reduceStock(int qty) {
        if (qty < 0 || qty > stockQuantity) {
            throw new IllegalArgumentException("Invalid quantity.");
        }
        stockQuantity -= qty;
    }

    public void increaseStock(int qty) {
        if (qty < 0) {
            throw new IllegalArgumentException("Quantity to add must be positive.");
        }
        stockQuantity += qty;
    }
}
