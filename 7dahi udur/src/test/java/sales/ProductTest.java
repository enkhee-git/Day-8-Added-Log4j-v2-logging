package sales;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {
    @Test
    public void testAllProductScenarios() {
        // 1. Test valid product creation
        Product laptop = new Product("Laptop", 999.99, 10);
        assertEquals("Laptop", laptop.getName());
        assertEquals(999.99, laptop.getPrice(), 0.001);
        assertEquals(10, laptop.getStockQuantity());

        // 2. Test invalid product name
        try {
            new Product("!@#$", 100, 5);
            fail("Should throw IllegalArgumentException for invalid name");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // 3. Test negative price
        try {
            new Product("Phone", -50, 5);
            fail("Should throw IllegalArgumentException for negative price");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // 4. Test negative stock quantity
        try {
            new Product("Phone", 100, -1);
            fail("Should throw IllegalArgumentException for negative stock");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // 5. Test reduceStock beyond available
        Product tablet = new Product("Tablet", 200, 3);
        try {
            tablet.reduceStock(5);
            fail("Should throw IllegalArgumentException when reducing beyond stock");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // 6. Test increaseStock
        Product camera = new Product("Camera", 300, 2);
        camera.increaseStock(5);
        assertEquals(7, camera.getStockQuantity());
    }
}
