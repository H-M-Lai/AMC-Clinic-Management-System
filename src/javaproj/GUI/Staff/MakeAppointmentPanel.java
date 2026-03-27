/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package javaproj.GUI.Staff;

import java.util.*;
import javaproj.Utils.*;

import javax.swing.*;
import java.time.*;
import javaproj.Repository.AppointmentRepository;
import javaproj.Methods.Appointment.StaffAppointment;
import javaproj.Methods.User.DoctorService;
import javaproj.Methods.User.CustomerService;
/**
 *
 * @author NICK
 */
public class MakeAppointmentPanel extends javax.swing.JPanel {
    private String ic;
    private PanelController controller;
    private LocalDateTime today = DateOptions.roundUpTime(LocalDateTime.now(),15);
    private LocalDateTime selectedDateTime = today;
    
    private final AppointmentRepository apptRepo   = new AppointmentRepository();
    private final DoctorService doctorService = new DoctorService();
    private final StaffAppointment staffAppt = new StaffAppointment(apptRepo, doctorService);
    private final CustomerService customerService = new CustomerService();
    
    List<String> specialityItems = new ArrayList<>();
    List<String> doctorItems = new ArrayList<>();
    List<String> typeItems = new ArrayList<>();
    
    private void addAppointment(){
        ic = icInput.getText().trim();
        String customerId = customerService.idFromIc(ic);
        if (customerId == null){
            JOptionPane.showMessageDialog(this, "Customer Not Found.");
            return;
        } else {
            String customerName = customerService.nameFromId(customerId);
            int option = JOptionPane.showConfirmDialog(this, customerName, "Confirm Appointment", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.NO_OPTION) return;
        }

        if (doctorComboBox.getSelectedItem() == null
                || typeComboBox.getSelectedItem() == null
                || specialtyComboBox.getSelectedItem() == null
                || "- - - Choose - - -".equals(typeComboBox.getSelectedItem())
                || "- - - Choose - - -".equals(specialtyComboBox.getSelectedItem())) {
            JOptionPane.showMessageDialog(this, "Please complete all fields.");
            return;
        }

        String doctorId = doctorComboBox.getSelectedItem().toString().split(" - ")[0];
        String type     = typeComboBox.getSelectedItem().toString();

        int year = Integer.parseInt(yearComboBox.getSelectedItem().toString());
        int month = Integer.parseInt(monthComboBox.getSelectedItem().toString());
        int day = Integer.parseInt(dayComboBox.getSelectedItem().toString());
        int hour = Integer.parseInt(hourComboBox.getSelectedItem().toString());
        int minute = Integer.parseInt(minuteComboBox.getSelectedItem().toString());

        LocalDateTime datetime = LocalDateTime.of(year, month, day, hour, minute);
        
        if (!staffAppt.validateTime(null, datetime)) {  
            JOptionPane.showMessageDialog(this, "Invalid date/time.");
            return;
        }

        String note = noteInput.getText().trim();
        try {
            staffAppt.addAppointment(customerId, doctorId,
                    String.format("%04d-%02d-%02d", year, month, day),
                    String.format("%02d:%02d", hour, minute),
                    note, type);

            JOptionPane.showMessageDialog(this, "Appointment created.");
            resetForm();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            //catch invalid value and the write file function is working
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateAllDateTimeComboBox(LocalDateTime selected){
        LocalDateTime today = DateOptions.roundUpTime(LocalDateTime.now(),15);
        if(selected.isBefore(today)){
            selected = today;
        }
        //year
        Utils.updateComboBox(yearComboBox, DateOptions.yearList(today.toLocalDate()));
        yearComboBox.setSelectedItem(String.valueOf(selected.getYear()));
        
        //month
        Utils.updateComboBox(monthComboBox, DateOptions.monthList(today.toLocalDate(),selected.getYear()));
        monthComboBox.setSelectedItem(String.valueOf(selected.getMonthValue()));
        
        //day
        Utils.updateComboBox(dayComboBox, DateOptions.dayList(today.toLocalDate(), selected.getYear(), selected.getMonthValue()));
        dayComboBox.setSelectedItem(String.valueOf(selected.getDayOfMonth()));
        
        // hour
        Utils.updateComboBox(hourComboBox, DateOptions.hourList(today, selected.getYear(), selected.getMonthValue(), selected.getDayOfMonth()));
        hourComboBox.setSelectedItem(String.format("%02d", selected.getHour()));

        // minute
        Utils.updateComboBox(minuteComboBox, DateOptions.minuteList(today,selected.getYear(), selected.getMonthValue(),selected.getDayOfMonth(), selected.getHour()));
        minuteComboBox.setSelectedItem(String.format("%02d", selected.getMinute()));

    }
    private void updateDoctorBox(LocalDateTime selected){
        doctorItems.clear();
        String specialty = (String) specialtyComboBox.getSelectedItem();
        doctorItems.addAll(staffAppt.doctorBox(specialty, selected, null)); 
        if (doctorItems.isEmpty()){
            doctorItems.add("- - - No Doctor Found - - -");
        }
        Utils.updateComboBox(doctorComboBox, doctorItems);
    }
    //refresh
    private void resetForm() {
        
        // reset text field input
        icInput.setText("");
        noteInput.setText("");
        
        //refresh date and time
        selectedDateTime = LocalDateTime.now();
        
        // reset specialty        
        List<String> specialty = ReturnList.get("specialty");
        specialityItems.clear();
        specialityItems.add("- - - Choose - - -");
        specialityItems.addAll(specialty);
        Utils.updateComboBox(specialtyComboBox,specialityItems);
        
        //reset doctor
        doctorItems.clear();
        doctorItems.add("- - - Choose - - -");
        Utils.updateComboBox(doctorComboBox,doctorItems);
        
        //reset type
        typeItems.clear();
        typeItems.add("- - - Choose - - -");
        typeItems.addAll(staffAppt.typeBox());
        Utils.updateComboBox(typeComboBox, typeItems);
        
        //reset all date and time
        updateAllDateTimeComboBox(today);
    }
    
    public MakeAppointmentPanel(PanelController controller) {
        this.controller = controller;
        initComponents();
    }
    @Override
    public void setVisible(boolean flag) {
        super.setVisible(flag);
        if (flag) {
            resetForm();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        confirmationBtn = new javax.swing.JButton();
        icInput = new javax.swing.JTextField();
        patientic = new javax.swing.JLabel();
        doctor = new javax.swing.JLabel();
        specialty = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        time = new javax.swing.JLabel();
        note = new javax.swing.JLabel();
        noteInput = new javax.swing.JTextField();
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
        backBtn = new javax.swing.JButton();
        recordBtn = new javax.swing.JButton();

        jPanel1.setBackground(java.awt.Color.white);

        jPanel2.setBackground(new java.awt.Color(245, 243, 238));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(java.awt.Color.black);
        jLabel1.setText("Make Appointment");

        confirmationBtn.setText("Make Appointment");
        confirmationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmationBtnActionPerformed(evt);
            }
        });

        icInput.setBackground(java.awt.Color.white);
        icInput.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        icInput.setForeground(java.awt.Color.black);

        patientic.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        patientic.setForeground(java.awt.Color.black);
        patientic.setText("Patient IC Number:");

        doctor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        doctor.setForeground(java.awt.Color.black);
        doctor.setText("Doctor:");

        specialty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        specialty.setForeground(java.awt.Color.black);
        specialty.setText("Specialty:");

        date.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        date.setForeground(java.awt.Color.black);
        date.setText("Date:");

        time.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        time.setForeground(java.awt.Color.black);
        time.setText("Time:");

        note.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        note.setForeground(java.awt.Color.black);
        note.setText("Note:");

        noteInput.setBackground(java.awt.Color.white);
        noteInput.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        noteInput.setForeground(java.awt.Color.black);

        specialtyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                specialtyComboBoxActionPerformed(evt);
            }
        });

        dayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        dayComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dayComboBoxActionPerformed(evt);
            }
        });

        monthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        monthComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthComboBoxActionPerformed(evt);
            }
        });

        yearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2026", "2", "27" }));
        yearComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearComboBoxActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("/");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("/");

        hourComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hourComboBoxActionPerformed(evt);
            }
        });

        minuteComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minuteComboBoxActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText(":");

        doctor1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        doctor1.setForeground(java.awt.Color.black);
        doctor1.setText("Type:");

        typeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(noteInput)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(specialtyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(dayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(jLabel2)
                                            .addGap(0, 0, 0)
                                            .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(jLabel3)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(date)
                                        .addComponent(note, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(icInput, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(specialty))
                                    .addGap(65, 65, 65))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addGap(124, 124, 124)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(doctor, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(doctorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(7, 7, 7)
                                    .addComponent(jLabel4)
                                    .addGap(7, 7, 7)
                                    .addComponent(minuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(time, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(confirmationBtn)
                                    .addComponent(doctor1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(patientic, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmationBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(patientic)
                    .addComponent(doctor1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(icInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(specialty)
                    .addComponent(doctor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(specialtyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(doctorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(date)
                    .addComponent(time))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(note)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noteInput, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        confirmationBtn.setName("contentSecondary");

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        recordBtn.setText("View Appointment Record");
        recordBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recordBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(backBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(194, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(recordBtn)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(195, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(backBtn)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recordBtn)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel2.setName("contentSecondary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 932, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 471, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void confirmationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmationBtnActionPerformed
        // TODO add your handling code here:
        addAppointment();
    }//GEN-LAST:event_confirmationBtnActionPerformed

    private void specialtyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_specialtyComboBoxActionPerformed
        // TODO add your handling code here:
        String selected = (String) specialtyComboBox.getSelectedItem();
        if ("- - - Choose - - -".equals(selected)) {
            return;
        }
        if (specialityItems.contains("- - - Choose - - -")) {
            specialityItems.remove("- - - Choose - - -");
            specialtyComboBox.removeItem("- - - Choose - - -"); // safer than rebuilding model
        }
        
        updateDoctorBox(selectedDateTime);
    }//GEN-LAST:event_specialtyComboBoxActionPerformed

    private void monthComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthComboBoxActionPerformed
        // TODO add your handling code here:
        int month = Integer.parseInt((String) monthComboBox.getSelectedItem());
        selectedDateTime = selectedDateTime.withMonth(month);
        updateAllDateTimeComboBox(selectedDateTime);
        updateDoctorBox(selectedDateTime);
    }//GEN-LAST:event_monthComboBoxActionPerformed

    private void yearComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearComboBoxActionPerformed
        // TODO add your handling code here:
        int year = Integer.parseInt((String) yearComboBox.getSelectedItem());
        selectedDateTime = selectedDateTime.withYear(year);
        updateAllDateTimeComboBox(selectedDateTime);
        updateDoctorBox(selectedDateTime);
    }//GEN-LAST:event_yearComboBoxActionPerformed

    private void hourComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hourComboBoxActionPerformed
        // TODO add your handling code here:
        int hour = Integer.parseInt((String) hourComboBox.getSelectedItem());
        selectedDateTime = selectedDateTime.withHour(hour);
        updateAllDateTimeComboBox(selectedDateTime);
        updateDoctorBox(selectedDateTime);
    }//GEN-LAST:event_hourComboBoxActionPerformed

    private void minuteComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minuteComboBoxActionPerformed
        // TODO add your handling code here:
        int minute = Integer.parseInt((String) minuteComboBox.getSelectedItem());
        selectedDateTime = selectedDateTime.withMinute(minute);
        updateAllDateTimeComboBox(selectedDateTime);
        updateDoctorBox(selectedDateTime);
    }//GEN-LAST:event_minuteComboBoxActionPerformed

    private void dayComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dayComboBoxActionPerformed
        // TODO add your handling code here:
        int day = Integer.parseInt((String) dayComboBox.getSelectedItem());
        selectedDateTime = selectedDateTime.withDayOfMonth(day);
        updateAllDateTimeComboBox(selectedDateTime);
        updateDoctorBox(selectedDateTime);
    }//GEN-LAST:event_dayComboBoxActionPerformed

    private void typeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboBoxActionPerformed
        // TODO add your handling code here:
        String selected = (String) typeComboBox.getSelectedItem();
        if ("- - - Choose - - -".equals(selected)) {
            return;
        }
        if (typeItems.contains("- - - Choose - - -")) {
            typeItems.remove("- - - Choose - - -");
            typeComboBox.removeItem("- - - Choose - - -");
        }
    }//GEN-LAST:event_typeComboBoxActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("home");
    }//GEN-LAST:event_backBtnActionPerformed

    private void recordBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordBtnActionPerformed
        // TODO add your handling code here:
        controller.show("appointmentRecord");
    }//GEN-LAST:event_recordBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JButton confirmationBtn;
    private javax.swing.JLabel date;
    private javax.swing.JComboBox<String> dayComboBox;
    private javax.swing.JLabel doctor;
    private javax.swing.JLabel doctor1;
    private javax.swing.JComboBox<String> doctorComboBox;
    private javax.swing.JComboBox<String> hourComboBox;
    private javax.swing.JTextField icInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox<String> minuteComboBox;
    private javax.swing.JComboBox<String> monthComboBox;
    private javax.swing.JLabel note;
    private javax.swing.JTextField noteInput;
    private javax.swing.JLabel patientic;
    private javax.swing.JButton recordBtn;
    private javax.swing.JLabel specialty;
    private javax.swing.JComboBox<String> specialtyComboBox;
    private javax.swing.JLabel time;
    private javax.swing.JComboBox<String> typeComboBox;
    private javax.swing.JComboBox<String> yearComboBox;
    // End of variables declaration//GEN-END:variables
}
