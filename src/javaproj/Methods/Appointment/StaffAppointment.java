package javaproj.Methods.Appointment;

import javaproj.Methods.IdGenerator.IdGenerator;
import javaproj.Methods.IdGenerator.FileIdGenerator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import javaproj.GUI.Credentials.Session;
import javaproj.Model.Appointment;
import javaproj.Model.Role.Doctor;
import javaproj.Repository.AppointmentRepository;
import java.time.format.DateTimeFormatter;
import javaproj.Methods.User.DoctorService;

public class StaffAppointment extends AppointmentBase{
    private final IdGenerator generator = new FileIdGenerator();
    private static final DateTimeFormatter HHmm = DateTimeFormatter.ofPattern("HH:mm");
    private final DoctorService doctorService;
    
    public StaffAppointment(AppointmentRepository repo, DoctorService doctorService){ 
        super(repo); 
        this.doctorService = doctorService;
    }
    public List<String> listAppointmentIds() {
        List<String> ids = new ArrayList<>();
        for (Appointment a : repo.findAll()) {
            ids.add(a.getAppointmentId());
        }
        return ids;
    }
    
    public void addAppointment(String customerId, String doctorId, String date, String time, String note, String type) {
        List<Appointment> all = new ArrayList<>(repo.findAll());
        List<String> ids = new ArrayList<>();
        for (Appointment a : all) ids.add(a.getAppointmentId());

        LocalDateTime candidate = LocalDate.parse(date)
            .atTime(java.time.LocalTime.parse(time));

        if (!validateTime(null, candidate)) {
            throw new IllegalArgumentException("Appointment time must be now/future.");
        }
        if (!doctorHasCapacity(candidate, doctorId, null, 3)) {
            throw new IllegalStateException("Selected slot is full for this doctor.");
        }

        String appointmentId = generator.nextId(ids, "A");
        String staffId = javaproj.GUI.Credentials.Session.getCurrentUser().getSystemId();

        Appointment appointment = new Appointment(
            appointmentId,
            staffId,
            customerId,
            doctorId,
            date,
            time,
            note,
            Appointment.Status.SCHEDULED,
            Appointment.Type.valueOf(type)
        );

        all.add(appointment);
        saveAll(all);
    }
    
    public void editAppointment(String id, String doctorId, String date, String time, String note, String type) {
        List<Appointment> all = new ArrayList<>(repo.findAll());
        int idx = indexOfAppointment(all, id);
        if (idx < 0) throw new NoSuchElementException("Appointment not found: " + id);

        // 1) Build the candidate datetime
        LocalDateTime candidate = LocalDate.parse(date)
            .atTime(java.time.LocalTime.parse(time));

        // 2) Validate time (unchanged past is allowed)
        if (!validateTime(id, candidate)) {
            throw new IllegalArgumentException("Appointment time must be now/future (unless unchanged).");
        }

        // 3) Enforce capacity, skipping this appointment’s own ID
        if (!doctorHasCapacity(candidate, doctorId, id, 3)) {
            throw new IllegalStateException("Selected slot is full for this doctor.");
        }

        // 4) Save the update
        Appointment a = all.get(idx);
        Appointment updated = new Appointment(
            a.getAppointmentId(),
            Session.getCurrentUser().getSystemId(),
            a.getCustomerId(),
            doctorId,
            date,
            time,
            note,
            a.getStatus(), // keep current status
            Appointment.Type.valueOf(type)
        );

        all.set(idx, updated);
        saveAll(all);
    }
    
    public void deleteAppointment(String appointmentId) {
        List<Appointment> all = new ArrayList<>(repo.findAll());
        boolean removed = all.removeIf(a ->
            appointmentId.equals(a.getAppointmentId()) &&
            a.getStatus() != Appointment.Status.SCHEDULED
        );
        if (!removed) {
            throw new IllegalStateException("Cannot delete: not found or still SCHEDULED: " + appointmentId);
        }
        saveAll(all);
    }
    //shared
    public Appointment getAppointment(String id){
            return getById(id);
        }
    
    public List<Appointment> filter(List<Appointment> appointments,
             String status, String type, LocalDate date, String query,
             Map<String,String> staffMap, Map<String,String> customerMap, Map<String,String> doctorMap) {
        return filterAppointment(appointments, status, type, date, query, staffMap, customerMap, doctorMap);
    }
    public void cancelAppointment(String appointmentId){
        cancelById(appointmentId);
    }
    public List<String> statusBox(){
        return allStatus();
    }
    public List<String> typeBox(){
        return allType();
    }
    
    public boolean validateTime(String id, LocalDateTime input) {
        return super.validateTime(id, input);
    }
    
    public List<String> doctorBox(String specialty, LocalDateTime selected, String skipAppointmentId) {
            return doctorBox(specialty, selected, skipAppointmentId, 3);
        }
        public List<String> doctorBox(String specialty, LocalDateTime selected, String skipAppointmentId, int capacityPerSlot) {
        List<String> items = new ArrayList<>();
        if (specialty == null || specialty.isBlank()) return items;

        // pull doctors and filter by specialty
        for (Doctor d : doctorService.doctors()) {
            if (d.getSpecialty() == null) continue;
            if (!d.getSpecialty().equalsIgnoreCase(specialty)) continue;

            if (doctorHasCapacity(selected, d.getSystemId(), skipAppointmentId, capacityPerSlot)) {
                items.add(d.getSystemId() + " - " + d.getName());
            }
        }
        return items;
    }
        
        protected boolean doctorHasCapacity(LocalDateTime selected, String doctorId, String skipAppointmentId, int capacityPerSlot) {
        String date = selected.toLocalDate().toString();                    // yyyy-MM-dd
        String time = selected.toLocalTime().withSecond(0).withNano(0).format(HHmm); // HH:mm

        int count = 0;
        for (Appointment a : repo.findAll()) {
            if (skipAppointmentId != null && skipAppointmentId.equals(a.getAppointmentId())) continue;
            if (!doctorId.equals(a.getDoctorId())) continue;
            if (!date.equals(a.getDate())) continue;
            if (!time.equals(a.getTime())) continue;

            count++;
            if (count >= capacityPerSlot) return false;
        }
        return true;
    }
}
