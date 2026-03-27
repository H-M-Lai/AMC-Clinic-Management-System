package javaproj.GUI.Staff;

import java.time.LocalDate;
import javaproj.Model.Appointment;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

import javaproj.Repository.AppointmentRepository;
import javaproj.Methods.Appointment.StaffAppointment;
import javaproj.Methods.User.CustomerService;
import javaproj.Methods.User.DoctorService;
import javaproj.Methods.User.StaffService;

import javaproj.Utils.PanelController;
import javaproj.Utils.Utils;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class AppointmentRecordPanel extends javax.swing.JPanel {
    
    private final CustomerService customerService = new CustomerService();
    private final StaffService staffService = new StaffService();
    private final DoctorService doctorService = new DoctorService();
    
    private final AppointmentRepository apptRepo = new AppointmentRepository();
    private final StaffAppointment staffAppt = new StaffAppointment(apptRepo, doctorService);

    private PanelController controller;
    
    //initialize list and map
    Map<String, String> staffMap = staffService.nameMap();
    Map<String, String> customerMap = customerService.nameMap();
    Map<String, String> doctorMap = doctorService.nameMap();
    List<Appointment> appointments;
    
    //initialize table format
    String[] columnNames = {"ID","Staff","Customer","Doctor","Date","Time","Status","Type"};
    private DefaultTableModel model = new DefaultTableModel(columnNames, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    //setup filter combobox    
    private String status = "All";
    private String type = "All";
    private void setupFilter(){
        List<String> statusBox = new ArrayList<>();
        statusBox.add(status);
        statusBox.addAll(staffAppt.statusBox());

        List<String> typeBox = new ArrayList<>();
        typeBox.add(type);
        typeBox.addAll(staffAppt.typeBox());

        statusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(statusBox.toArray(new String[0])));
        typeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(typeBox.toArray(new String[0])));
    }
    
    private void applyFilter(){
        if (appointments == null) return;
        LocalDate date = dateFilter.getDate();

        List<Appointment> filtered = staffAppt.filter(appointments,status,type,date,searchField.getText(),staffMap,customerMap,doctorMap
        );

        Utils.refreshTable(jTable1, filtered, a -> new Object[] {
            a.getAppointmentId(),
            a.getStaffId()    + " - " + staffMap.getOrDefault(a.getStaffId(),    "(unknown)"),
            a.getCustomerId() + " - " + customerMap.getOrDefault(a.getCustomerId(), "(unknown)"),
            a.getDoctorId()   + " - " + doctorMap.getOrDefault(a.getDoctorId(),   "(unknown)"),
            a.getDate(),
            a.getTime(),
            a.getStatus(),
            a.getType()
        });
        Utils.resizeColumnWidth(jTable1);
    }
    private void addLiveSearch() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilter(); }
            public void removeUpdate(DocumentEvent e) { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });
    }
    
    //refresh
    private void reloadTxt(){
        appointments = apptRepo.findAll();
    }
    private void reloadAndRefresh() {
    // Make sure the maps and list are up to date
        reloadTxt(); 
        statusFilter.setSelectedIndex(0);
        typeFilter.setSelectedIndex(0);
        
        // Render using refreshTable
        Utils.refreshTable(jTable1,appointments,a -> new Object[] {
                a.getAppointmentId(),
                a.getStaffId()    + " - " + staffMap.getOrDefault(a.getStaffId(),    "(unknown)"),
                a.getCustomerId() + " - " + customerMap.getOrDefault(a.getCustomerId(), "(unknown)"),
                a.getDoctorId()   + " - " + doctorMap.getOrDefault(a.getDoctorId(),   "(unknown)"),
                a.getDate(),
                a.getTime(),
                a.getStatus(),
                a.getType()
            }
        );
        Utils.resizeColumnWidth(jTable1);
    }
    
    public AppointmentRecordPanel(PanelController controller) {
        this.controller = controller;
        model.setColumnIdentifiers(columnNames);
       
        initComponents();
        setupFilter();
        reloadAndRefresh();
        addLiveSearch();
    }
    @Override
    public void setVisible(boolean flag) {
        super.setVisible(flag);
        if (flag) {
            reloadAndRefresh();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        searchField = new javax.swing.JTextField();
        resetBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();
        typeFilter = new javax.swing.JComboBox<>();
        statusFilter = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dateFilter = new com.github.lgooddatepicker.components.DatePicker();
        jLabel3 = new javax.swing.JLabel();

        jPanel1.setBackground(java.awt.Color.white);

        jTable1.setModel(model);
        jTable1.setAutoscrolls(false);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(50); // Appointment ID
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(130); // Staff
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(130); // Customer
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(160); // Doctor
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(80); // Date
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(45); // Time
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(100); // Status
        jTable1.getColumnModel().getColumn(7).setPreferredWidth(100); // Type

        resetBtn.setText("Reset");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        editBtn.setText("Edit");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        typeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        typeFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeFilterActionPerformed(evt);
            }
        });

        statusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        statusFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusFilterActionPerformed(evt);
            }
        });

        jLabel1.setText("Status:");
        jLabel1.setForeground(java.awt.Color.black);

        jLabel2.setText("Type:");
        jLabel2.setForeground(java.awt.Color.black);

        jLabel3.setText("Appointment Record");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(backBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 387, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(typeFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetBtn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(36, 36, 36))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(backBtn)
                .addGap(20, 20, 20)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(typeFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editBtn)
                        .addComponent(deleteBtn)
                        .addComponent(statusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(dateFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        dateFilter.addDateChangeListener(e -> {applyFilter();});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 932, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 496, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(this,"Please select a record to delete.");
            return;
        }
        String selectedId = jTable1.getValueAt(selectedRow,0).toString();

        int option = JOptionPane.showConfirmDialog(
                this,"Are you sure to delete this record?",
                "Delete Appointment", JOptionPane.YES_NO_OPTION);
        if(option != JOptionPane.YES_OPTION) return;

        try {
            staffAppt.deleteAppointment(selectedId);
            reloadAndRefresh();
        } catch (IllegalStateException ex) {
            // thrown when not found or status == SCHEDULED (per StaffAppointment)
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to delete: " + ex.getMessage());
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
        
        int selectedRow = jTable1.getSelectedRow();
        String id = jTable1.getValueAt(0,0).toString();
        String currentStatus = jTable1.getValueAt(0,6).toString();
        if (selectedRow != -1){
            id = jTable1.getValueAt(selectedRow,0).toString();
            currentStatus = jTable1.getValueAt(selectedRow,6).toString();
        }
        
        if(!currentStatus.equalsIgnoreCase("SCHEDULED")){
            JOptionPane.showMessageDialog(this,"Non-schedulled appointment can't be edited.");
            return;
        }
        
        EditAppointment edit = new EditAppointment(id);
        edit.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                reloadAndRefresh();
            }
        });
        edit.setVisible(true);
    }//GEN-LAST:event_editBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("appointment");
    }//GEN-LAST:event_backBtnActionPerformed

    private void statusFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusFilterActionPerformed
        // TODO add your handling code here:
        status = statusFilter.getSelectedItem().toString();
        applyFilter();
    }//GEN-LAST:event_statusFilterActionPerformed

    private void typeFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeFilterActionPerformed
        // TODO add your handling code here:
        type = typeFilter.getSelectedItem().toString();
        applyFilter();
    }//GEN-LAST:event_typeFilterActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        // TODO add your handling code here:
        searchField.setText("");
        statusFilter.setSelectedIndex(0);
        typeFilter.setSelectedIndex(0);
        dateFilter.clear();
        reloadAndRefresh();
    }//GEN-LAST:event_resetBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private com.github.lgooddatepicker.components.DatePicker dateFilter;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton resetBtn;
    private javax.swing.JTextField searchField;
    private javax.swing.JComboBox<String> statusFilter;
    private javax.swing.JComboBox<String> typeFilter;
    // End of variables declaration//GEN-END:variables
}
