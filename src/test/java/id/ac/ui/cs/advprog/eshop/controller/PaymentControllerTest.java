package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private OrderService orderService;

    private Payment payment;
    private List<Payment> payments;
    private Order order;

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

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        payment = new Payment("pay-001", order,
                PaymentMethod.VOUCHER.getValue(), paymentData);

        payments = new ArrayList<>();
        payments.add(payment);
    }

    @Test
    void testGetPaymentDetailPage() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/detail"));
    }

    @Test
    void testGetPaymentDetailById() throws Exception {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/pay-001"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/detailResult"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testGetAdminPaymentList() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/adminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testGetAdminPaymentDetail() throws Exception {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/pay-001"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/adminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testPostSetStatus() throws Exception {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);
        when(paymentService.setStatus(any(Payment.class), anyString())).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/pay-001")
                        .param("status", PaymentStatus.SUCCESS.getValue()))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/adminDetail"))
                .andExpect(model().attributeExists("payment"));
    }
}