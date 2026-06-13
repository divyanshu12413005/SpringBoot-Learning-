package com.divyanshu.youtube.InternalWorkingSpringBoot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="payment.provider",havingValue = "razorpay")
public class RazorpayPaymentService implements PaymentService {

    @Override
    public String pay(){
        String payment="Rozer Payment";
        System.out.println("Payment From:" + payment);
        return payment;

    }
}
