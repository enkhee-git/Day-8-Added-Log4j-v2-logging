package sales;

class Customer {

    private String name, email, phone;

    public Customer(String name, String email, String phone) {
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!phone.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Phone number must be 8 digits.");
        }
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
}
