package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    private String id;
    private Order order;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        this.id = id;
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
        this.status = PaymentStatus.WAITING_PAYMENT.getValue();

        if (method.equals(PaymentMethod.VOUCHER.getValue())) {
            this.status = validateVoucher(paymentData.get("voucherCode"));
        } else if (method.equals(PaymentMethod.BANK_TRANSFER.getValue())) {
            this.status = validateBankTransfer(paymentData);
        }
    }

    public void setStatus(String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
        this.status = status;
    }

    private String validateVoucher(String voucherCode) {
        if (voucherCode == null) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (voucherCode.length() != 16) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (!voucherCode.startsWith("ESHOP")) {
            return PaymentStatus.REJECTED.getValue();
        }
        long digitCount = voucherCode.chars()
                .filter(Character::isDigit)
                .count();
        if (digitCount != 8) {
            return PaymentStatus.REJECTED.getValue();
        }
        return PaymentStatus.SUCCESS.getValue();
    }

    private String validateBankTransfer(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");

        if (bankName == null || bankName.isEmpty()) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (referenceCode == null || referenceCode.isEmpty()) {
            return PaymentStatus.REJECTED.getValue();
        }
        return PaymentStatus.SUCCESS.getValue();
    }
}