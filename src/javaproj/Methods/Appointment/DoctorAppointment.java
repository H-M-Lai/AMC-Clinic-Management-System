/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import javaproj.Model.Appointment;
import javaproj.Repository.AppointmentRepository;

/**
 *
 * @author NICK
 */
public class DoctorAppointment extends AppointmentBase {
    public DoctorAppointment(AppointmentRepository repo){ 
        super(repo); 
    }
    
        public List<Appointment> appointmentList(String doctorId){
            List<Appointment> result = new ArrayList<>();
            for(Appointment a: repo.findAll()){
                if(doctorId.equals(a.getDoctorId())){
                    result.add(a);
                }
            }
            return result;
        }
        
        public List<String> listAppointmentIds(String doctorId, boolean skipStatus) {
            List<String> ids = new ArrayList<>();
            for (Appointment a : repo.findByDoctorId(doctorId)) {
                if (skipStatus || a.getStatus() == Appointment.Status.SCHEDULED) {
                    ids.add(a.getAppointmentId());
                }
            }
            return ids;
        }
        
        public void appointmentCompleted(String appointmentId){
            List<Appointment> all = repo.findAll();
            for (int i = 0; i < all.size(); i++) {
                Appointment a = all.get(i);
                if (a.getAppointmentId().equals(appointmentId)) {
                    a.complete();
                    all.set(i, a);
                    repo.saveAll(all);
                    return;
                }
            }
            throw new NoSuchElementException("Appointment not found: " + appointmentId);
        }
        
        public Appointment getAppointment(String id){
            return getById(id);
        }
        
        public List<Appointment> filter(List<Appointment> appointments,
             String status, String type, LocalDate date, String query,
             Map<String,String> staffMap, Map<String,String> customerMap, Map<String,String> doctorMap) {
        return filterAppointment(appointments, status, type, date, query, staffMap, customerMap, doctorMap);
    }
}
