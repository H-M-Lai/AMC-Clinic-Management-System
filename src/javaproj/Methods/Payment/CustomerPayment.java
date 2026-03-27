package javaproj.Methods.Payment;

import java.util.*;
import javaproj.Model.Appointment;
import javaproj.Model.Payment;
import javaproj.Repository.AppointmentRepository;
import javaproj.Repository.PaymentRepository;

public final class CustomerPayment {
    
    private final PaymentRepository payRepo;
    private final AppointmentRepository apptRepo;
    
    public CustomerPayment(PaymentRepository payRepo, AppointmentRepository apptRepo) {
        this.payRepo = payRepo;
        this.apptRepo = apptRepo;
    }
    
    //payment list for customer
    public List<Payment> myPayments(String customerId) {
        List<Payment> allPayments = payRepo.findAll();
        List<Appointment> allAppts = apptRepo.findAll();
        
        Map<String, String> apptToCustomer = new HashMap<>();
        for (Appointment a : allAppts) {
            apptToCustomer.put(a.getAppointmentId(), a.getCustomerId());
        }

        List<Payment> result = new ArrayList<>();
        for (Payment p : allPayments) {
            String custId = apptToCustomer.get(p.getAppointmentId());
            if (Objects.equals(customerId, custId)) {
                result.add(p);
            }
        }
        return result;
    }
}
 