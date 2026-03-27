package javaproj.GUI.Staff;

import javaproj.Model.Role.Doctor;
import javaproj.Model.Appointment;
import java.util.*;

import javaproj.Utils.*;

import java.time.*;
import javaproj.Methods.User.CustomerService;
import javaproj.Methods.User.DoctorService;
import javax.swing.JOptionPane;

import javaproj.Repository.AppointmentRepository;
import javaproj.Methods.Appointment.StaffAppointment;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EditAppointment extends javax.swing.JFrame {
    private boolean isLoading = false;
    private String currentId = null;
    
    //initialize list and map
    List<String> doctorItems = new ArrayList<>();
    private final CustomerService customerService = new CustomerService();
    private final DoctorService   doctorService   = new DoctorService();
    
    private final AppointmentRepository apptRepo = new AppointmentRepository();
    private Map<String, String> customerMap = customerService.nameMap(); 
    private Map<String, Doctor> doctorMap   = doctorService.mapById();
    private final StaffAppointment staffAppt     = new StaffAppointment(apptRepo, doctorService);
    
    private Map<String, Appointment> appointmentMap =
    apptRepo.findAll().stream().collect(Collectors.toMap(Appointment::getAppointmentId, Function.identity()));
    
    private void reloadAppointments() {
    appointmentMap = apptRepo.findAll().stream().collect(Collectors.toMap(Appointment::getAppointmentId, Function.identity()));
    }
    
    //setup frame
    private void loadField(String id){
        if (id == null) return;
        Appointment appointment = appointmentMap.get(id);
        if (appointment == null) return;
        
        isLoading = true;
        try{
            currentId = id;
            String patientName = customerMap.get(appointmentMap.get(id).getCustomerId());
        
            Doctor doctor =doctorMap.get(appointmentMap.get(id).getDoctorId());
            String currentSpecialty = doctor.getSpecialty();

            LocalDate date = LocalDate.parse(appointmentMap.get(id).getDate());
            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();
            LocalTime time = LocalTime.parse(appointmentMap.get(id).getTime()); 
            int hour = time.getHour();
            int minute = time.getMinute();
            
            LocalDateTime selectedTime = LocalDateTime.of(year, month, day, hour, minute);

            initializeComboBox(currentSpecialty, selectedTime);
            appointmentComboBox.setSelectedItem(id);
            patientInput.setText(patientName);
            typeComboBox.setSelectedItem(appointmentMap.get(id).getType().toString());
            specialtyComboBox.setSelectedItem(currentSpecialty);
            doctorComboBox.setSelectedItem(doctor.getSystemId() + " - " + doctor.getName());
            noteInput.setText(appointmentMap.get(id).getNote());
            setupDateTimeComboBox(LocalDateTime.of(year, month, day, hour, minute));
            
            if(appointmentMap.get(id).getStatus().equals(Appointment.Status.SCHEDULED)){
                cancelBtn.setVisible(true);
            }
            else{
                cancelBtn.setVisible(false);
            }
        }finally {
            isLoading = false;
        }
        
        
    }
    private void initializeComboBox(String currentSpecialty, LocalDateTime currentTime){
        List<String> apptIds = apptRepo.findAll().stream()
                .filter(a -> a.getStatus() == Appointment.Status.SCHEDULED) 
                .map(Appointment::getAppointmentId)
                .sorted()
                .toList();
        Utils.updateComboBox(appointmentComboBox, apptIds);

        Utils.updateComboBox(typeComboBox, staffAppt.typeBox());

        Utils.updateComboBox(specialtyComboBox, ReturnList.get("specialty"));

        Utils.updateComboBox(doctorComboBox,
                staffAppt.doctorBox(currentSpecialty, currentTime, currentId));  // capacity-aware
    }
    private static List<String> yearList(LocalDate today, int selectedYear) {
        int now = today.getYear();

        // Start from the earlier of now or the selected year
        int start = Math.min(selectedYear, now);

        // End at either selectedYear+2 or now+2 (whichever is smaller)
        int end   = Math.min(selectedYear + 2, now + 2);

        List<String> years = new ArrayList<>();
        for (int y = start; y <= end; y++) {
            years.add(String.valueOf(y));
        }

        // Ensure the selected year is in the list even if it’s beyond the capped end
        if (selectedYear > end && years.stream().noneMatch(s -> s.equals(String.valueOf(selectedYear)))) {
            years.add(String.valueOf(selectedYear));
        }

        return years;
    }
    private void setupDateTimeComboBox(LocalDateTime selected){
        LocalDate today = LocalDate.now();
        LocalDate anchorDate = LocalDate.of(1970, 1, 1);
        LocalDateTime anchorDateTime = LocalDateTime.of(1970, 1, 1, 0, 0);

        // year
        Utils.updateComboBox(yearComboBox, yearList(today, selected.getYear()));
        yearComboBox.setSelectedItem(String.valueOf(selected.getYear()));

        // month
        Utils.updateComboBox(monthComboBox, DateOptions.monthList(anchorDate, selected.getYear()));
        monthComboBox.setSelectedItem(String.valueOf(selected.getMonthValue()));

        // day
        Utils.updateComboBox(dayComboBox, DateOptions.dayList(anchorDate, selected.getYear(), selected.getMonthValue()));
        dayComboBox.setSelectedItem(String.valueOf(selected.getDayOfMonth()));

        // hour
        Utils.updateComboBox(hourComboBox, DateOptions.hourList(anchorDateTime,selected.getYear(), selected.getMonthValue(), selected.getDayOfMonth()));
        hourComboBox.setSelectedItem(String.format("%02d", selected.getHour()));

        // minute (15-min steps)
        Utils.updateComboBox(minuteComboBox, DateOptions.minuteList(anchorDateTime,selected.getYear(), selected.getMonthValue(), selected.getDayOfMonth(), selected.getHour()));
        minuteComboBox.setSelectedItem(String.format("%02d", selected.getMinute()));
    }
    private void updateDoctorBox(LocalDateTime selected){
        String specialty = (String) specialtyComboBox.getSelectedItem();
        List<String> doctorItems = staffAppt.doctorBox(specialty, selected, currentId);
        if (doctorItems.isEmpty()) {
            doctorItems = List.of("- - - No Doctor Found - - -");
        }
        Utils.updateComboBox(doctorComboBox, doctorItems);
    }
    
    private Appointment buildFromForm() {
        String type = typeComboBox.getSelectedItem().toString();
        String doctorId = ((String) doctorComboBox.getSelectedItem()).split(" - ")[0];
        String date = String.format("%04d-%02d-%02d",
                Integer.parseInt( yearComboBox.getSelectedItem().toString()),
                Integer.parseInt(monthComboBox.getSelectedItem().toString()),
                Integer.parseInt(dayComboBox.getSelectedItem().toString())
        );
        String time = String.format("%02d:%02d",
                Integer.parseInt(hourComboBox.getSelectedItem().toString()),
                Integer.parseInt(minuteComboBox.getSelectedItem().toString())
        );

        return new Appointment(
            currentId,
            appointmentMap.get(currentId).getStaffId(),
            appointmentMap.get(currentId).getCustomerId(),
            doctorId,
            date,
            time,
            noteInput.getText().trim(),
            appointmentMap.get(currentId).getStatus(),
            Appointment.Type.valueOf(type)
        );
    }
    private boolean isFormChanged() {       
        if (currentId == null) return false;
        Appointment original = appointmentMap.get(currentId);
        Appointment current  = buildFromForm();
        return current.isChanged(original);
    }
    private LocalDateTime getTime(){
        int year = Integer.parseInt(String.valueOf(yearComboBox.getSelectedItem()));
        int month = Integer.parseInt(String.valueOf(monthComboBox.getSelectedItem()));
        int day = Integer.parseInt(String.valueOf(dayComboBox.getSelectedItem()));
        int hour = Integer.parseInt(String.valueOf(hourComboBox.getSelectedItem()));
        int minute = Integer.parseInt(String.valueOf(minuteComboBox.getSelectedItem()));
        
        return LocalDateTime.of(year, month, day, hour, minute);
    }
    private void updateAppointment(){
        if (isLoading) return;
        if (currentId == null || currentId.isBlank()) {
            JOptionPane.showMessageDialog(this, "No appointment selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newType     = String.valueOf(typeComboBox.getSelectedItem());
        String newDoctorId = ((String) doctorComboBox.getSelectedItem()).split(" - ")[0];
        LocalDateTime dt   = getTime();
        String newDate     = String.format("%04d-%02d-%02d", dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth());
        String newTime     = String.format("%02d:%02d", dt.getHour(), dt.getMinute());
        String newNote     = noteInput.getText().trim();

        try {
            staffAppt.editAppointment(currentId, newDoctorId, newDate, newTime, newNote, newType);
            JOptionPane.showMessageDialog(this, "Appointment updated.");
            reloadAppointments();  // refresh cache
            dispose();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
        
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EditAppointment.class.getName());

    public EditAppointment() {
        initComponents();
        loadField("A0002");
    }
    public EditAppointment(String id){
        initComponents();
        loadField(id);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        patientInput = new javax.swing.JTextField();
        patientic = new javax.swing.JLabel();
        doctor = new javax.swing.JLabel();
        specialty = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        time = new javax.swing.JLabel();
        note = new javax.swing.JLabel();
        specialtyComboBox = new javax.swing.JComboBox<>();
        doctorComboBox = new javax.swing.JComboBox<>();
        dayComboBox = new javax.swing.JComboBox<>();
        monthComboBox = new javax.swing.JComboBox<>();
        yearComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        hourComboBox = new javax.swing.JComboBox<>();
        minuteComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        doctor1 = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        appointmentComboBox = new javax.swing.JComboBox<>();
        doneBtn = new javax.swing.JButton();
        noteInput = new javax.swing.JTextArea();
        cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(245, 243, 238));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(java.awt.Color.black);
        jLabel1.setText("Edit Appointment");

        patientInput.setEditable(false);
        patientInput.setBackground(java.awt.Color.white);
        patientInput.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        patientInput.setForeground(java.awt.Color.black);
        patientInput.setFocusable(false);

        patientic.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        patientic.setForeground(java.awt.Color.black);
        patientic.setText("Patient:");

        doctor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        doctor.setForeground(java.awt.Color.black);
        doctor.setText("Specialty:");

        specialty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        specialty.setForeground(java.awt.Color.black);
        specialty.setText("Doctor:");

        date.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        date.setForeground(java.awt.Color.black);
        date.setText("Date:");

        time.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        time.setForeground(java.awt.Color.black);
        time.setText("Time:");

        note.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        note.setForeground(java.awt.Color.black);
        note.setText("Note:");

        specialtyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                specialtyComboBoxActionPerformed(evt);
            }
        });

        dayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        monthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        yearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2026", "2", "27" }));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("/");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("/");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText(":");

        doctor1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        doctor1.setForeground(java.awt.Color.black);
        doctor1.setText("Type:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(java.awt.Color.black);
        jLabel5.setText("Appointment ID:");

        appointmentComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        appointmentComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appointmentComboBoxActionPerformed(evt);
            }
        });

        doneBtn.setText("Done");
        doneBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneBtnActionPerformed(evt);
            }
        });

        noteInput.setColumns(20);
        noteInput.setRows(5);

        cancelBtn.setText("Cancel Appointment");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(doctor1)
                                            .addComponent(patientic)
                                            .addComponent(jLabel5))
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(specialty)
                                            .addComponent(doctor)
                                            .addComponent(date)
                                            .addComponent(note))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(doctorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(patientInput, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(specialtyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(noteInput, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(dayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel2)
                                        .addGap(0, 0, 0)
                                        .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(time)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(minuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(appointmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(172, 172, 172)
                                        .addComponent(cancelBtn))))
                            .addComponent(jLabel1)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(doneBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(appointmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(patientic)
                    .addComponent(patientInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(doctor1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(doctor)
                    .addComponent(specialtyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(specialty)
                    .addComponent(doctorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(date)
                        .addComponent(time)
                        .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(minuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(note)
                    .addComponent(noteInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(doneBtn)
                .addGap(15, 15, 15))
        );

        doneBtn.setName("contentSecondary");
        cancelBtn.setName("contentSecondary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setName("contentSecondary");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void specialtyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_specialtyComboBoxActionPerformed
        // TODO add your handling code here:
        if (isLoading) return;  
        updateDoctorBox(getTime());
    }//GEN-LAST:event_specialtyComboBoxActionPerformed

    private void doneBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneBtnActionPerformed
        // TODO add your handling code here:
        LocalDateTime selectedTime = getTime();
        if(!staffAppt.validateTime(currentId, selectedTime)){
            JOptionPane.showMessageDialog(this,"Invalid date and time.");
        }
        else{
            updateAppointment();
        }
    }//GEN-LAST:event_doneBtnActionPerformed

    private void appointmentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appointmentComboBoxActionPerformed
        // TODO add your handling code here:
            if (isLoading) return;

        Object sel = appointmentComboBox.getSelectedItem();
        if (sel == null) return;

        if (isFormChanged()) {
            int option = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "You have unsaved changes. Do you want to discard them?",
                "Unsaved Changes",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (option == javax.swing.JOptionPane.NO_OPTION) {
                // revert selection to currentId without retriggering
                isLoading = true;
                try { appointmentComboBox.setSelectedItem(currentId); }
                finally { isLoading = false; }
                return;
            }
        }

        currentId = sel.toString();
        loadField(currentId);
        
    }//GEN-LAST:event_appointmentComboBoxActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        // TODO add your handling code here:
        int option = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Do you confirm to cancel the appointment?",
                "Cancel Appointment",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
        try {
            staffAppt.cancelAppointment(currentId);
            reloadAppointments();
            dispose();
        } catch (NoSuchElementException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cancelBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new EditAppointment().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> appointmentComboBox;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel date;
    private javax.swing.JComboBox<String> dayComboBox;
    private javax.swing.JLabel doctor;
    private javax.swing.JLabel doctor1;
    private javax.swing.JComboBox<String> doctorComboBox;
    private javax.swing.JButton doneBtn;
    private javax.swing.JComboBox<String> hourComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox<String> minuteComboBox;
    private javax.swing.JComboBox<String> monthComboBox;
    private javax.swing.JLabel note;
    private javax.swing.JTextArea noteInput;
    private javax.swing.JTextField patientInput;
    private javax.swing.JLabel patientic;
    private javax.swing.JLabel specialty;
    private javax.swing.JComboBox<String> specialtyComboBox;
    private javax.swing.JLabel time;
    private javax.swing.JComboBox<String> typeComboBox;
    private javax.swing.JComboBox<String> yearComboBox;
    // End of variables declaration//GEN-END:variables
}
