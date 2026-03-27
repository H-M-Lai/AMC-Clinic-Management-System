package javaproj.Methods.Feedback;

import java.util.*;
import javaproj.Model.Appointment;
import javaproj.Model.Feedback;
import javaproj.Model.Role.Customer;
import javaproj.Repository.*;
import javaproj.Methods.User.CustomerService;

public class DoctorFeedback extends FeedbackBase {
    public static final String ROLE = "DOCTOR";

    public DoctorFeedback(FeedbackRepository repo, AppointmentRepository apptRepo, CustomerService custService) {
        super(repo, apptRepo, custService);
    }

    public List<String> pendingAppointments(String doctorId) {
        List<String> out = new ArrayList<>();
        Map<String, Customer> customers = customersById();
        for (Appointment a : allAppointments()) {
            if (Objects.equals(a.getDoctorId(), doctorId) && !feedbackExists(ROLE, a.getAppointmentId())) {
                String patientName = Optional.ofNullable(customers.get(a.getCustomerId())).map(Customer::getName).filter(n -> !n.isBlank()).orElse("Unknown");
                out.add(a.getAppointmentId() + " - " + patientName + " (" + a.getDate() + ")");
            }
        }
        return out;
    }

    public void submit(String appointmentId, String rating, String comment1, String comment2) {
        if (feedbackExists(ROLE, appointmentId)) return;    //check if there is any record of this customer first
        Feedback f = new Feedback(nextFeedbackId(), appointmentId, ROLE, rating, comment1, comment2);
        feedbackRepo.add(f);
    }
}

