package javaproj.Methods.Appointment;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javaproj.Model.Appointment;
import javaproj.Repository.AppointmentRepository;

abstract class AppointmentBase {
    protected final AppointmentRepository repo;
    
    protected AppointmentBase(AppointmentRepository repo){
        this.repo = repo;
    }
    
    //return the appoitment by using the id
    protected Appointment getById(String id){
        for(Appointment a : repo.findAll()){
            if(a.getAppointmentId().equals(id)) return a;
        }
        return null;
    }
    
    //return the arraylist of the appointment for the searchbar and filter
    protected List<Appointment> filterAppointment(List<Appointment> appointments,String status, String type, LocalDate date, 
            String query,Map<String,String> staffMap, Map<String,String> customerMap, Map<String,String> doctorMap){
        
        //get the appointment list and initialize blank array to store the filtered record
        List<Appointment> all = (appointments != null) ? appointments : repo.findAll();
        List<Appointment> out = new ArrayList<>();
        
        String q = (query == null ? "" : query.trim().toLowerCase());
        
        for (Appointment a : all) {
            
            //filter the combobox and date first
            boolean statusOk = "All".equals(status) || a.getStatus().toString().equals(status); //check the status
            boolean typeOk   = "All".equals(type) || a.getType().toString().equals(type);   //check the appointment type
            boolean dateOk   = (date == null) || a.getDate().equals(date.toString());   //check the date
            if (!statusOk || !typeOk || !dateOk) continue;  //only pass the record that match all three requirement

            //convert the id into with name preview
            String staffDisplay = (staffMap == null) ? "" :
                a.getStaffId() + " - " + staffMap.getOrDefault(a.getStaffId(), "(unknown)");
            String customerDisplay =
                a.getCustomerId() + " - " + customerMap.getOrDefault(a.getCustomerId(), "(unknown)");
            String doctorDisplay = (doctorMap == null) ? "" :
                a.getDoctorId() + " - " + doctorMap.getOrDefault(a.getDoctorId(), "(unknown)");

            //filter the search bar
            boolean match = q.isEmpty()
                || staffDisplay.toLowerCase().contains(q)
                || customerDisplay.toLowerCase().contains(q)
                || doctorDisplay.toLowerCase().contains(q)
                || a.getAppointmentId().toLowerCase().contains(q);

            if (match) out.add(a);
        }
        return out;
    }
    
    //set the status of the appointment to cancel by comparing the id
    protected void cancelById(String appointmentId){
        List<Appointment> all = new ArrayList<>(repo.findAll());
        for(int i = 0; i < all.size(); i++){
            Appointment a = all.get(i);
            if (a.getAppointmentId().equals(appointmentId)) {
                a.cancel();
                all.set(i, a);
                repo.saveAll(all);
                return;
            }
        }
        throw new java.util.NoSuchElementException("Appointment not found: " + appointmentId);
    }
    
    //return the enum status for the combobox to use
    protected List<String> allStatus() {
        List<String> list = new ArrayList<>();
        for (Appointment.Status s : Appointment.Status.values()){
            list.add(s.toString());
        }
        return list;
    }

    //return the enum type for the combobox to use
    protected List<String> allType() {
        List<String> list = new ArrayList<>();
        for (Appointment.Type t : Appointment.Type.values()){
            list.add(t.toString());
        }
        return list;
    }
    
    //get the index of that selected appointment id
    protected int indexOfAppointment(List<Appointment> all, String id) {
        for (int i = 0; i < all.size(); i++) {
            if (id.equals(all.get(i).getAppointmentId())) return i;
        }
        return -1;
    }

    //save appointment list back to txt
    protected void saveAll(List<Appointment> all) {
        repo.saveAll(all);
    }
    
    //use when editing appointment record 
    protected boolean validateTimeUnchangedOrFuture(String appointmentId, LocalDateTime candidate) {
        //convert time to string
        DateTimeFormatter HHmm = DateTimeFormatter.ofPattern("HH:mm"); //the time is in hour:minute format
        String candDate = candidate.toLocalDate().toString(); // yyyy-MM-dd
        String candTime = candidate.toLocalTime().withSecond(0).withNano(0).format(HHmm);

        if (appointmentId != null) {
            //ignore the appointmentid that the date time is not changed
            return repo.findById(appointmentId)
                       .map(orig -> orig.getDate().equals(candDate) && orig.getTime().equals(candTime))
                       .orElseGet(() -> !candidate.isBefore(LocalDateTime.now()));
        }
        // if is before then return false
        return !candidate.isBefore(LocalDateTime.now());
    }
    
    //use when making appointment
    protected boolean validateTime(String id, LocalDateTime input) {
        // normalize time by remove the second
        LocalDateTime candidate = input.withSecond(0).withNano(0);

        
        Optional<Appointment> origOpt = repo.findById(id);
        if (origOpt.isPresent()) {
            Appointment orig = origOpt.get();
            LocalDateTime originalDT = LocalDateTime.of(
                LocalDate.parse(orig.getDate()),   // yyyy-MM-dd
                LocalTime.parse(orig.getTime())    // HH:mm
            );
            if (originalDT.equals(candidate)) return true;
        }

        // if is before then return false
        return !candidate.isBefore(LocalDateTime.now());
    }
}
