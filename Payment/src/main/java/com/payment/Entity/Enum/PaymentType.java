package com.payment.Entity.Enum;

public enum PaymentType {
    CARD("card"),
    BANKTRANSFER("banktransfer");

    private final String paymentType;

    PaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    public String getPaymentType(){return paymentType;}
    public static PaymentType fromString(String paymentType) {
        for(PaymentType type : PaymentType.values()){
            if (type.getPaymentType().equalsIgnoreCase(paymentType)){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentType: " + paymentType);
    }
}

