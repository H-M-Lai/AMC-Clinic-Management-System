package javaproj.Methods.Payment;

import java.time.LocalDateTime;
import java.util.*;
import javaproj.Model.Payment;
import javaproj.Repository.PaymentRepository;
import javaproj.Utils.Utils;

public class DoctorPayment {
    private final PaymentRepository payRepo;

    public DoctorPayment(PaymentRepository payRepo) {
        this.payRepo = payRepo;
    }
    
    //add pending payment for charges 
    public String addPendingPayment(String appointmentId, double amount) {
        List<Payment> all = payRepo.findAll();
        String paymentId = Utils.idIncrement(all, "P");

        LocalDateTime now = LocalDateTime.now();
        String date = String.format("%04d-%02d-%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        String time = String.format("%02d:%02d", now.getHour(), now.getMinute());

        Payment p = new Payment(
            paymentId,
            appointmentId,
            amount,
            Payment.PaymentMethod.NULL, 
            date,
            time,
            Payment.Status.PENDING
        );

        all.add(p);
        payRepo.saveAll(all);
        return paymentId;
    }
}
