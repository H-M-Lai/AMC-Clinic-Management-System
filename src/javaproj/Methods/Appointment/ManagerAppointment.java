/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javaproj.Model.Appointment;
import javaproj.Repository.AppointmentRepository;

/**
 *
 * @author NICK
 */
public class ManagerAppointment extends AppointmentBase{
    public ManagerAppointment(AppointmentRepository repo){ 
        super(repo); 
    }
    
    public List<Appointment> filter(List<Appointment> appointments,
             String status, String type, LocalDate date, String query,
             Map<String,String> staffMap, Map<String,String> customerMap, Map<String,String> doctorMap) {
        return filterAppointment(appointments, status, type, date, query, staffMap, customerMap, doctorMap);
    }
    
    public List<String> statusBox(){
        return allStatus();
    }
    public List<String> typeBox(){
        return allType();
    }
}
