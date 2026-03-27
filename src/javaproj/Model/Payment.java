/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Model;


/**
 *
 * @author NICK
 */
public class Payment {
    private String paymentId,appointmentId,date,time;
    private PaymentMethod paymentMethod;
    private Status status;
    double amount;
    
    public enum PaymentMethod{CASH,CARD,NULL};
    public enum Status{PAID,PENDING};

    public Payment(String paymentId, String appointmentId, double amount, PaymentMethod paymentMethod, String date, String time,  Status status ) {
        this.paymentId = paymentId;
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.amount = amount;
    }
    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public String getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    @Override
    public String toString() {
        return paymentId + "," + appointmentId + "," + amount + "," +paymentMethod + "," + date + "," + time + "," + status;
    }
}
