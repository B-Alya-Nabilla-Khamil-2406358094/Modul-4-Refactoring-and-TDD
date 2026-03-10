package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private PaymentService paymentService;

    private Order order;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        orders = new ArrayList<>();
        orders.add(order);
    }

    @Test
    void testGetCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/createOrder"));
    }

    @Test
    void testGetOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/history"));
    }

    @Test
    void testPostOrderHistory() throws Exception {
        when(orderService.findAllByAuthor("Safira Sudrajat")).thenReturn(orders);

        mockMvc.perform(post("/order/history")
                        .param("author", "Safira Sudrajat"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/historyList"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void testGetPayOrderPage() throws Exception {
        when(orderService.findById("13652556-012a-4c07-b546-54eb1396d79b"))
                .thenReturn(order);

        mockMvc.perform(get("/order/pay/13652556-012a-4c07-b546-54eb1396d79b"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/pay"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testPostPayOrder() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("pay-001", order,
                PaymentMethod.VOUCHER.getValue(), paymentData);

        when(orderService.findById(anyString())).thenReturn(order);
        when(paymentService.addPayment(any(), anyString(), any())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/13652556-012a-4c07-b546-54eb1396d79b")
                        .param("paymentMethod", "VOUCHER")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/payResult"))
                .andExpect(model().attributeExists("paymentId"))
                .andExpect(model().attributeExists("status"));
    }
}