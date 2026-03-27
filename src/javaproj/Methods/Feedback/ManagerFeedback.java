/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Feedback;

import java.util.*;
import javaproj.Model.Feedback;
import javaproj.Repository.*;
import javaproj.Methods.User.CustomerService;

public class ManagerFeedback extends FeedbackBase {
    public static final String ROLE = "MANAGER";

    public ManagerFeedback(FeedbackRepository repo, AppointmentRepository apptRepo, CustomerService custService) {
        super(repo, apptRepo, custService);
    }

    public List<Feedback> allFeedback() {
        return feedbackRepo.findAll();
    }

    public List<Feedback> feedbackForAppointment(String apptId) {
        return feedbackRepo.findByAppointmentId(apptId);
    }

    public boolean checkFeedbackGiven(String role, String appointmentId) {
        return feedbackExists(role, appointmentId);
    }
}