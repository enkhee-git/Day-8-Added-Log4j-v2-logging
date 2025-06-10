package sales;

import java.util.*;

class Order {
    private Customer customer;
    private List<OrderItem> items;

    public Order(Customer customer) {
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    public boolean addItem(Product product, int quantity) {
        if (quantity > 100) {
            throw new IllegalArgumentException("Cannot order more than 100 items at once.");
        }
        if (quantity <= product.getStockQuantity()) {
            items.add(new OrderItem(product, quantity));
            product.reduceStock(quantity);
            return true;
        } else {
            return false;
        }
    }

    public String getOrderDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer: ").append(customer.getName()).append("\n");
        for (OrderItem item : items) {
            sb.append("  - ").append(item).append("\n");
        }
        return sb.toString();
    }
}
