package sales;
class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public String toString() {
        return product.getName() + " - " + quantity + " pcs";
    }
}

