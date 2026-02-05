package com.farmermarket.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerificationRequest {
	
	private Long orderId;

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}
