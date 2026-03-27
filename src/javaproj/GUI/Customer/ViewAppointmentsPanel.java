package javaproj.GUI.Customer;

import javax.swing.*;
import java.time.LocalDate;
import java.util.*;
import javaproj.GUI.Credentials.Session;
import javaproj.Model.Appointment;
import javaproj.Repository.AppointmentRepository;
import javaproj.Methods.Appointment.CustomerAppointment;
import javaproj.Utils.PanelController;
import javaproj.Utils.Utils;
public class ViewAppointmentsPanel extends javax.swing.JPanel {
    private final PanelController controller;
    private final AppointmentRepository apptRepo = new AppointmentRepository();
    private final CustomerAppointment customerAppt = new CustomerAppointment(apptRepo);
    
    public ViewAppointmentsPanel(PanelController controller) {
        this.controller = controller;
        initComponents();
        loadDoctorIds();
        setupUI();
    }
    @Override
    public void setVisible(boolean flag) {
        super.setVisible(flag);
            if (flag) {
                loadDoctorIds();
                applyFilter();
            }
        }

    private void setupUI(){
        DoctorIDSelection.setSelectedIndex(0);
        StatusSelection.setSelectedIndex(0);
        
        List<Appointment> appointments = customerAppt.myAppointments(Session.getCurrentUser().getSystemId());
        Utils.refreshTable(AppoinmentsRecordTable, appointments, a -> new Object[] {
            a.getAppointmentId(),
            a.getDate(),
            a.getTime(),
            a.getDoctorId(),   // show ID; avoids DoctorService
            a.getStatus()
        });
        Utils.resizeColumnWidth(AppoinmentsRecordTable);
    }
    
    private void applyFilter(){
        String doctor = DoctorIDSelection.getSelectedItem().toString().split(" - ")[0];
        String status = StatusSelection.getSelectedItem().toString();
        LocalDate date = datePicker.getDate();
        String time = hourComboBox.getSelectedItem()  + ":" + minuteComboBox.getSelectedItem();
        
        List<Appointment> appointments = customerAppt.myAppointments(Session.getCurrentUser().getSystemId());
        List<Appointment> filter = new ArrayList<>();
        for (Appointment a : appointments){
            boolean match = true;

            if ((date != null) && !a.getDate().equalsIgnoreCase(date.toString())) match = false;
            if (!time.contains("--")&& !a.getTime().equals(time)) match = false;
            if (!doctor.equals("All") && !a.getDoctorId().equals(doctor)) match = false;
            if (!status.equals("All") && !a.getStatus().toString().equalsIgnoreCase(status)) match = false;
            
            if(match) filter.add(a);
        }
        
        Utils.refreshTable(AppoinmentsRecordTable,filter,a -> new Object[] {
                a.getAppointmentId(),
                a.getDate(),
                a.getTime(),
                a.getDoctorId(),
                a.getStatus()
            }
        );
        Utils.resizeColumnWidth(AppoinmentsRecordTable);
    }
    private void loadDoctorIds() {
        List<String> doctorIdList = new ArrayList<>();
        doctorIdList.add("All");

        List<Appointment> mine = customerAppt.myAppointments(Session.getCurrentUser().getSystemId());
        Set<String> uniq = new LinkedHashSet<>();
        for (Appointment a : mine) {
            uniq.add(a.getDoctorId());
        }
        doctorIdList.addAll(uniq);  

        Utils.updateComboBox(DoctorIDSelection, doctorIdList);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        To2 = new javax.swing.JPanel();
        Date = new javax.swing.JLabel();
        DoctorID = new javax.swing.JLabel();
        ViewDetails = new javax.swing.JButton();
        Status = new javax.swing.JLabel();
        DoctorIDSelection = new javax.swing.JComboBox<>();
        StatusSelection = new javax.swing.JComboBox<>();
        Time = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AppoinmentsRecordTable = new javax.swing.JTable();
        Search = new javax.swing.JButton();
        ResetButton = new javax.swing.JButton();
        datePicker = new com.github.lgooddatepicker.components.DatePicker();
        hourComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        minuteComboBox = new javax.swing.JComboBox<>();
        AppointmentRecords = new javax.swing.JLabel();
        backBtn = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        To2.setBackground(new java.awt.Color(245, 243, 238));

        Date.setText("Date:");

        DoctorID.setText("Doctor:");

        ViewDetails.setBackground(new java.awt.Color(102, 204, 255));
        ViewDetails.setText("View Details");
        ViewDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewDetailsActionPerformed(evt);
            }
        });

        Status.setText("Status:");

        DoctorIDSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "doctors" }));
        DoctorIDSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DoctorIDSelectionActionPerformed(evt);
            }
        });

        StatusSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Scheduled", "Completed", "Cancelled" }));
        StatusSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusSelectionActionPerformed(evt);
            }
        });

        Time.setText("Time:");

        AppoinmentsRecordTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Appointment ID", "Date", "Time", "Doctor ID", "Status"
            }
        ));
        jScrollPane1.setViewportView(AppoinmentsRecordTable);

        Search.setBackground(new java.awt.Color(153, 255, 255));
        Search.setText("Search");
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        ResetButton.setBackground(new java.awt.Color(153, 255, 255));
        ResetButton.setText("Reset");
        ResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetButtonActionPerformed(evt);
            }
        });

        hourComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        hourComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hourComboBoxActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText(":");

        minuteComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "00", "15", "30", "45" }));
        minuteComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minuteComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout To2Layout = new javax.swing.GroupLayout(To2);
        To2.setLayout(To2Layout);
        To2Layout.setHorizontalGroup(
            To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(To2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(To2Layout.createSequentialGroup()
                        .addGroup(To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Status)
                            .addComponent(Date)
                            .addComponent(DoctorID, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DoctorIDSelection, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, To2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                .addGap(20, 20, 20)
                                .addComponent(Time)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(minuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(StatusSelection, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(30, 30, 30))
            .addGroup(To2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ViewDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(To2Layout.createSequentialGroup()
                        .addComponent(ResetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        To2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {hourComboBox, minuteComboBox});

        To2Layout.setVerticalGroup(
            To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(To2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DoctorIDSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DoctorID))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(minuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Time))
                    .addGroup(To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Date)
                        .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StatusSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Status))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(To2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ResetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ViewDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        ViewDetails.setName("contentSecondary");
        Search.setName("contentSecondary");
        ResetButton.setName("contentSecondary");

        AppointmentRecords.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        AppointmentRecords.setText("Appointment Records");

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(backBtn))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(To2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(40, 40, 40))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(AppointmentRecords)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(backBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AppointmentRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(To2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        To2.setName("contentSecondary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ViewDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewDetailsActionPerformed
        // TODO add your handling code here:
        int row = AppoinmentsRecordTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.");
            return;
        }

        String appointmentID = AppoinmentsRecordTable.getValueAt(row, 0).toString();
        String date = AppoinmentsRecordTable.getValueAt(row, 1).toString();
        String time = AppoinmentsRecordTable.getValueAt(row, 2).toString();
        String doctor = AppoinmentsRecordTable.getValueAt(row, 3).toString();
        String status = AppoinmentsRecordTable.getValueAt(row, 4).toString();

        String message = "Appointment Details:\n"
                + "Appointment ID: " + appointmentID + "\n"
                + "Date: " + date + "\n"
                + "Time: " + time + "\n"
                + "Doctor: " + doctor + "\n"
                + "Status: " + status;

        JOptionPane.showMessageDialog(this, message, "Appointment Details", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_ViewDetailsActionPerformed
    
    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        // TODO add your handling code here:
        applyFilter();
    }//GEN-LAST:event_SearchActionPerformed

    private void ResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetButtonActionPerformed
        // TODO add your handling code here:
        datePicker.clear();
        DoctorIDSelection.setSelectedIndex(0);
        StatusSelection.setSelectedIndex(0);
        hourComboBox.setSelectedIndex(0);
        minuteComboBox.setSelectedIndex(0);
        applyFilter();
    }//GEN-LAST:event_ResetButtonActionPerformed

    private void StatusSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StatusSelectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusSelectionActionPerformed

    private void DoctorIDSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DoctorIDSelectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DoctorIDSelectionActionPerformed

    private void hourComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hourComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hourComboBoxActionPerformed

    private void minuteComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minuteComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_minuteComboBoxActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("home");
    }//GEN-LAST:event_backBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AppoinmentsRecordTable;
    private javax.swing.JLabel AppointmentRecords;
    private javax.swing.JLabel Date;
    private javax.swing.JLabel DoctorID;
    private javax.swing.JComboBox<String> DoctorIDSelection;
    private javax.swing.JButton ResetButton;
    private javax.swing.JButton Search;
    private javax.swing.JLabel Status;
    private javax.swing.JComboBox<String> StatusSelection;
    private javax.swing.JLabel Time;
    private javax.swing.JPanel To2;
    private javax.swing.JButton ViewDetails;
    private javax.swing.JButton backBtn;
    private com.github.lgooddatepicker.components.DatePicker datePicker;
    private javax.swing.JComboBox<String> hourComboBox;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> minuteComboBox;
    // End of variables declaration//GEN-END:variables
}
