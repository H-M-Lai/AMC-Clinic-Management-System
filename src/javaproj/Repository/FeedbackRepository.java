/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Repository;

import java.util.*;

import javaproj.Model.Feedback;
import javaproj.Methods.Parser.FeedbackParser;
import javaproj.Utils.*;

/**
 *
 * @author NICK
 */
public class FeedbackRepository implements Repository<Feedback>{
    private final String filename;
    private final FeedbackParser parser;
    
        public FeedbackRepository(){
        this("data/feedback.txt");
    }
    public FeedbackRepository(String filename) {
        this.filename = filename;
        this.parser = new FeedbackParser();
    }
    
    @Override
    public List<Feedback> findAll(){
        List<Feedback> list = (List<Feedback>) Utils.readFile(filename, parser);
        return list;
    }
    public Optional<Feedback> findById(String id){
         return findAll().stream().filter(u -> u.getFeedbackId().equals(id)).findFirst();
    }
    public void saveAll(List<Feedback> items){
        Utils.writeFile(filename, items);
    }
    public void add(Feedback item){
        List<Feedback> all = findAll();
        all.add(item);
        saveAll(all);
    }
    public Optional<Feedback> findByRoleAndAppointment(String role, String appointmentId) {
        return findAll().stream()
            .filter(f -> role.equalsIgnoreCase(f.getUserType()) 
                      && appointmentId.equals(f.getAppointmentId()))
            .findFirst();
    }
    public List<Feedback> findByAppointmentId(String appointmentId) {
        return findAll().stream()
            .filter(f -> appointmentId.equals(f.getAppointmentId()))
            .toList();
    }

}
