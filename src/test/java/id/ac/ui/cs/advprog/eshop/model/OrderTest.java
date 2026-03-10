package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    // 11 usages
    private List<Product> products;

    @BeforeEach
    void setUp() {
        this.products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4664-83c7-f32db8620155");
        product2.setProductName("Sabun Cap Usep");
        product2.setProductQuantity(1);
        this.products.add(product1);
        this.products.add(product2);
    }

    @Test
    void testCreateOrderEmptyProduct() {
        this.products.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            Order order = new Order(id: "13652556-012a-4c07-b546-54eb1396d79b",
                    this.products, orderTime: 1708560000L, author: "Safira Sudrajat");
        });
    }

    @Test
    void testCreateOrderDefaultStatus() {
        Order order = new Order(id: "13652556-012a-4c07-b546-54eb1396d79b",
                this.products, orderTime: 1708560000L, author: "Safira Sudrajat");

        assertSame(this.products, order.getProducts());
        assertEquals(expected: 2, order.getProducts().size());
        assertEquals(expected: "Sampo Cap Bambang", order.getProducts().get(0).getProductName());
        assertEquals(expected: "Sabun Cap Usep", order.getProducts().get(1).getProductName());

        assertEquals(expected: "13652556-012a-4c07-b546-54eb1396d79b", order.getId());
        assertEquals(expected: 1708560000L, order.getOrderTime());
        assertEquals(expected: "Safira Sudrajat", order.getAuthor());
        assertEquals(expected: "WAITING_PAYMENT", order.getStatus());
    }

    @Test
    void testCreateOrderSuccessStatus() {
        Order order = new Order(id: "13652556-012a-4c07-b546-54eb1396d79b",
                this.products, orderTime: 1708560000L, author: "Safira Sudrajat", status: "SUCCESS");
        assertEquals(expected: "SUCCESS", order.getStatus());
    }

    @Test
    void testCreateOrderInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            Order order = new Order(id: "13652556-012a-4c07-b546-54eb1396d79b",
                    this.products, orderTime: 1708560000L, author: "Safira Sudrajat", status: "MEOW");
        });
    }

    @Test
    void testSetStatusToCancelled() {
        Order order = new Order(id: "13652556-012a-4c07-b546-54eb1396d79b",
                this.products, orderTime: 1708560000L, author: "Safira Sudrajat");
        order.setStatus("CANCELLED");
        assertEquals(expected: "CANCELLED", order.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        Order order = new Order(id: "13652556-012a-4c07-b546-54eb1396d79b",
                this.products, orderTime: 1708560000L, author: "Safira Sudrajat");
        assertThrows(IllegalArgumentException.class, () -> order.setStatus("MEOW"));
    }
}