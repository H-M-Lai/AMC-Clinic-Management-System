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