package javaproj.Methods.Appointment;

import java.util.*;
import javaproj.Model.Appointment;
import javaproj.Repository.AppointmentRepository;

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
