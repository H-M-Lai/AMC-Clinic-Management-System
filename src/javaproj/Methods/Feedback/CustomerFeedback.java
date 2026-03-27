package javaproj.Methods.Feedback;

import java.util.*;
import javaproj.Model.Appointment;
import javaproj.Model.Feedback;
import javaproj.Repository.*;
import javaproj.Methods.User.CustomerService;
import javaproj.Methods.User.DoctorService;

public class CustomerFeedback extends FeedbackBase {
    private DoctorService doctorService = new DoctorService();
    public static final String ROLE = "CUSTOMER";

    public CustomerFeedback(FeedbackRepository repo, AppointmentRepository apptRepo, CustomerService custService) {
        super(repo, apptRepo, custService);
    }

    public List<String> pendingAppointmentIds(String customerId) {
        List<Appointment> appts = new AppointmentRepository().findAll();

            List<String> out = new ArrayList<>();
            for (Appointment a : appts) {
                if (!Objects.equals(a.getCustomerId(), customerId)) continue;
                if (a.getStatus() != Appointment.Status.COMPLETED) continue;
                if (feedbackExists(ROLE, a.getAppointmentId())) continue;
                String doctor = doctorService.getById(a.getDoctorId()).getName();
                out.add(a.getAppointmentId()+"-"+doctor+"-"+a.getDate()+" "+a.getTime());
            }
            return out;
    }

    public void submit(String appointmentId, String rating, String comment1, String comment2) {
        if (feedbackExists(ROLE, appointmentId)) return;
        Feedback f = new Feedback(nextFeedbackId(), appointmentId, ROLE, rating, comment1, comment2);
        feedbackRepo.add(f);
    }

    public List<Feedback> feedbackByCustomer(String customerId) {
        Set<String> ids = new HashSet<>();
        for (Appointment a : allAppointments()) {
            if (Objects.equals(a.getCustomerId(), customerId)) ids.add(a.getAppointmentId());
        }
        return feedbackRepo.findAll().stream()
            .filter(f -> ids.contains(f.getAppointmentId()) && ROLE.equalsIgnoreCase(f.getUserType()))
            .toList();
    }
}
