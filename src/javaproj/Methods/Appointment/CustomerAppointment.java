/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Appointment;

import java.util.*;
import javaproj.Model.Appointment;
import javaproj.Repository.AppointmentRepository;

/**
 *
 * @author NICK
 */
public class CustomerAppointment extends AppointmentBase {
    public CustomerAppointment(AppointmentRepository repo){ 
        super(repo); 
    }
    //return the list of appointment of the current customer
    public List<Appointment> myAppointments(String customerId) {
        return repo.findByCustomerId(customerId);
    }
    
    public Appointment getAppointment(String id){
        return getById(id);
    }
    
    public void cancelAppointment(String appointmentId){
        cancelById(appointmentId);
    }
}
