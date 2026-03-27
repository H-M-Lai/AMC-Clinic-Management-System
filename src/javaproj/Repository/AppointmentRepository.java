package javaproj.Repository;

import java.util.*;

import javaproj.Model.Appointment;
import javaproj.Methods.Parser.AppointmentParser;
import javaproj.Utils.*;

public class AppointmentRepository implements Repository<Appointment>{
    private final String filePath;
    private final AppointmentParser parser;
    
    public AppointmentRepository(){
        this("data/appointment.txt");
    }
    public AppointmentRepository(String filePath) {
        this.filePath = filePath;
        this.parser = new AppointmentParser();
    }
    @Override
    public List<Appointment> findAll(){
        List<Appointment> list = (List<Appointment>) Utils.readFile(filePath, parser);
        return list;
    }
    public Optional<Appointment> findById(String id){
         return findAll().stream().filter(u -> u.getAppointmentId().equals(id)).findFirst();
    }
    public void saveAll(List<Appointment> items){
        Utils.writeFile(filePath, items);
    }

    public List<Appointment> findByDoctorId(String doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : findAll()) {
            if (doctorId.equals(a.getDoctorId())) {
                result.add(a);
            }
        }
        return result;
    }
    public List<String> findIdsByCustomerId(String customerId) {
        List<String> result = new ArrayList<>();
        for (Appointment a : findAll()) {
            if (customerId.equals(a.getCustomerId())) {
                result.add(a.getAppointmentId());
            }
        }
        return result;
    }
    public List<Appointment> findByCustomerId(String customerId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : findAll()) {
            if (customerId.equals(a.getCustomerId())) {
                result.add(a);
            }
        }
        return result;
    }
    public Appointment getById(String id) {
    // adjust method used to load all appointments to whatever your repo already has
    for (Appointment a : findAll()) {            // e.g. readAll(), list(), etc.
        if (a.getAppointmentId().equals(id)) {
            return a;
        }
    }
    return null;
}
}
