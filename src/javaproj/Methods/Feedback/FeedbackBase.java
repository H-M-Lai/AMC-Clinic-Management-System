/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Feedback;

import java.util.*;
import javaproj.Model.Appointment;
import javaproj.Model.Role.Customer;
import javaproj.Repository.*;
import javaproj.Methods.User.CustomerService;
import javaproj.Utils.Utils;

public abstract class FeedbackBase {
    protected final FeedbackRepository feedbackRepo;
    protected final AppointmentRepository apptRepo;
    protected final CustomerService customerService;

    protected FeedbackBase(FeedbackRepository feedbackRepo, AppointmentRepository apptRepo, CustomerService customerService) {
        this.feedbackRepo = feedbackRepo;
        this.apptRepo = apptRepo;
        this.customerService = customerService;
    }

    protected List<Appointment> allAppointments() {
        return apptRepo.findAll();
    }

    protected Map<String, Customer> customersById() {
        return customerService.mapById();
    }

    protected boolean feedbackExists(String role, String appointmentId) {
        return feedbackRepo.findByRoleAndAppointment(role, appointmentId).isPresent();
    }

    protected String nextFeedbackId() {
        return Utils.idIncrement(feedbackRepo.findAll(), "F");
    }
}
