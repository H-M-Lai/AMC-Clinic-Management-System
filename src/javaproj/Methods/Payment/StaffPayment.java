/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import javaproj.Model.Payment;
import javaproj.Repository.AppointmentRepository;
import javaproj.Repository.PaymentRepository;

/**
 *
 * @author NICK
 */
public class StaffPayment {
    private final PaymentRepository payRepo;
    private final AppointmentRepository apptRepo;
    
    public StaffPayment(PaymentRepository payRepo){
        this.payRepo = payRepo;
        this.apptRepo = null;
    }
    public StaffPayment(PaymentRepository payRepo, AppointmentRepository apptRepo) {
        this.payRepo = payRepo;
        this.apptRepo = apptRepo;
    }
    
    public List<Payment> listAll() {
        return payRepo.findAll();
    }
    
    //the combobox item in the table for filter
    public List<String> statusBox() {
        List<String> out = new ArrayList<>();
        for (Payment.Status s : Payment.Status.values()) out.add(s.toString());
        return out;
    }
    public List<String> methodBox() {
        List<String> out = new ArrayList<>();
        for (Payment.PaymentMethod m : Payment.PaymentMethod.values()) out.add(m.toString());
        return out;
    }
    
    //get the pending payment appointment id, use map so that can trace back the payment id also
    public Map<String, String> pendingPaymentAppointmentMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (Payment p : payRepo.findAll()) {
            if (p.getStatus() == Payment.Status.PENDING) {
                map.put(p.getAppointmentId(), p.getPaymentId());
            }
        }
        return map;
    }
    
    //flilter the payment list
    public List<Payment> filter(List<Payment> all, String query, String status, String method, LocalDate date) {
        String q = (query == null ? "" : query.trim().toLowerCase());
        List<Payment> out = new ArrayList<>();

        for (Payment p : all) {
            boolean statusOk = "All".equals(status) || p.getStatus().toString().equals(status);
            boolean methodOk = "All".equals(method) || p.getPaymentMethod().toString().equals(method);
            boolean dateOk   = (date == null) || Objects.equals(p.getDate(), date.toString());
            if (!statusOk || !methodOk || !dateOk) continue;

            boolean matches = q.isEmpty() || p.getPaymentId().toLowerCase().contains(q);
            if (matches) out.add(p);
        }
        return out;
    }
    
    
    public List<String> filterPendingsId(String type, String customerId) {
        
        //check if want to get the pending by appointment or payment
        boolean wantAppt  = "appointment".equalsIgnoreCase(type);
        boolean wantPayId = "payment".equalsIgnoreCase(type);
        if (!wantAppt && !wantPayId) return Collections.emptyList();

        //load all the founded payment id with appointment id with payment status pending
        Map<String, String> pending = pendingPaymentAppointmentMap();
        
        if (customerId == null) { 
            return new ArrayList<>(wantAppt ? pending.keySet() : pending.values());
        }
        if (apptRepo == null) {
            return Collections.emptyList();
        }
        
        //convert the appointment list to map that stores appointment id and customer id only
        Map<String, String> apptToCust = new LinkedHashMap<>();
        apptRepo.findAll().forEach(a -> apptToCust.put(a.getAppointmentId(), a.getCustomerId()));

        
        List<String> out = new ArrayList<>();
        //loop through the pending map and get the appointment id first
        for (Map.Entry<String, String> e : pending.entrySet()) {
            String apptId = e.getKey();
            //use the appointment id and map equals function to get the customer id
            if (Objects.equals(apptToCust.get(apptId), customerId)) {
                out.add(wantAppt ? apptId : e.getValue());
            }
        }
        return out;
    }
    
    public boolean makePayment(String paymentId, double amount, String method) {
        List<Payment> all = payRepo.findAll();
        boolean found = false;

        for (Payment p : all) {
            if (Objects.equals(p.getPaymentId(), paymentId)) {
                found = true;
                p.setAmount(amount);
                p.setStatus(Payment.Status.PAID);
                p.setPaymentMethod(Payment.PaymentMethod.valueOf(method));

                LocalDateTime now = LocalDateTime.now();
                p.setDate(String.format("%04d-%02d-%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth()));
                p.setTime(String.format("%02d:%02d", now.getHour(), now.getMinute()));
                break;
            }
        }
        if (found) payRepo.saveAll(all);
        return found;
    }
    
    public double getAmount(String appointmentId) {
        double amt = 0;
        for (Payment p : payRepo.findAll()) {
            if (Objects.equals(appointmentId, p.getAppointmentId())) {
                amt = p.getAmount();
            }
        }
        return amt;
    }
}
