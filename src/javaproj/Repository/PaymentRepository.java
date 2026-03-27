package javaproj.Repository;

import java.util.*;
import java.util.stream.Collectors;

import javaproj.Model.Payment;
import javaproj.Methods.Parser.PaymentParser;
import javaproj.Utils.*;

public class PaymentRepository implements Repository<Payment>{
    private final String filePath;
    private final PaymentParser parser;
    
    public PaymentRepository() {
        this("data/payment.txt");
    }
    
    public PaymentRepository(String filePath) {
        this.filePath = filePath;
        this.parser = new PaymentParser();
    }
    
    @Override
    public List<Payment> findAll(){
        List<Payment> list = (List<Payment>) Utils.readFile(filePath, parser);
        return list;
    }
    public Optional<Payment> findById(String id){
         return findAll().stream().filter(u -> u.getPaymentId().equals(id)).findFirst();
    }
    public List<Payment> findByAppointmentId(String appointmentId) {
        return findAll().stream()
                .filter(p -> Objects.equals(p.getAppointmentId(), appointmentId))
                .collect(Collectors.toList());
    }
    public void saveAll(List<Payment> items){
        Utils.writeFile(filePath, items);
    }
    public void add(Payment item){
        List<Payment> all = findAll();
        all.add(item);
        saveAll(all);
    }
    public void update(Payment item){
        List<Payment> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getAppointmentId().equals(item.getPaymentId())) {
                all.set(i, item);
                saveAll(all);
                return;
            }
        }
        throw new IllegalArgumentException("Payment not found: " + item.getPaymentId());
    }
    public void delete(String id){
        List<Payment> all = findAll();
        List<Payment> filtered = all.stream().filter(u -> !u.getPaymentId().equals(id)).collect(Collectors.toList());
        saveAll(filtered);
    }

}
