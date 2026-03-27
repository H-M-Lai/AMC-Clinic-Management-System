/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package javaproj.GUI.Manager;

import javaproj.Model.Appointment;
import javaproj.Utils.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import javaproj.Repository.AppointmentRepository;
import javaproj.Methods.Appointment.ManagerAppointment;

import javaproj.Methods.User.CustomerService;
import javaproj.Methods.User.DoctorService;
import javaproj.Methods.User.StaffService;

/**
 *
 * @author mayvi
 */
public class appointmentMonitoring extends javax.swing.JPanel {
    private PanelController controller;
    
    private final AppointmentRepository apptRepo = new AppointmentRepository();
    private final ManagerAppointment managerAppt = new ManagerAppointment(apptRepo);
    private List<Appointment> appointments = Collections.emptyList();

    private final CustomerService customerService = new CustomerService();
    private final StaffService staffService = new StaffService();
    private final DoctorService doctorService = new DoctorService();

    private Map<String, String> customerNameMap = customerService.nameMap();
    private Map<String, String> staffNameMap    = staffService.nameMap();
    private Map<String, String> doctorNameMap   = doctorService.nameMap();
    
    private final String[] cols = {
            "Appointment ID", "Staff", "Customer", "Doctor",
            "Date", "Time", "Note", "Status", "Type"
    };
    private DefaultTableModel model = new DefaultTableModel(cols, 0){
        
    @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public appointmentMonitoring(PanelController controller) {
        this.controller = controller;
        initComponents();
        
        appointmentTable.setModel(model);
        
        reloadTxt();
        populateFilters();
        
        // Initial table load
        applyFilters();        // fills table according to default filters + empty search
        Utils.resizeColumnWidth(appointmentTable);

        // Listeners
        addLiveSearch();
        
         // Resize columns when the panel resizes
        this.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                Utils.resizeColumnWidth(appointmentTable);
            }
        });
    
    }
    
    //setup filter combobox    
    private String status = "All";
    private String type = "All";
    private void populateFilters() {        
        List<String> statusBox = new ArrayList<>();
        statusBox.add(status);
        statusBox.addAll(managerAppt.statusBox());

        List<String> typeBox = new ArrayList<>();
        typeBox.add(type);
        typeBox.addAll(managerAppt.typeBox());

        statusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(statusBox.toArray(new String[0])));
        typeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(typeBox.toArray(new String[0])));
    }
    private void applyFilters() {
        if (appointments == null) return;
            LocalDate date = dateFilter.getDate();

            String q = searchField.getText() == null ? "" : searchField.getText().trim();
            List<Appointment> filtered = managerAppt.filter(appointments,status,type,date,q,staffNameMap,customerNameMap,doctorNameMap
            );

            Utils.refreshTable(appointmentTable, filtered, a -> new Object[] {
                a.getAppointmentId(),
                a.getStaffId()    + " - " + staffNameMap.getOrDefault(a.getStaffId(),    "(unknown)"),
                a.getCustomerId() + " - " + customerNameMap.getOrDefault(a.getCustomerId(), "(unknown)"),
                a.getDoctorId()   + " - " + doctorNameMap.getOrDefault(a.getDoctorId(),   "(unknown)"),
                a.getDate(),
                a.getTime(),
                a.getNote(),
                a.getStatus().name(),
                a.getType().name()
            });

            Utils.resizeColumnWidth(appointmentTable);
    }

    private void addLiveSearch() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });
    }

    private void reloadTxt(){
        appointments = apptRepo.findAll();
        customerNameMap = customerService.nameMap();
        staffNameMap    = staffService.nameMap();
        doctorNameMap   = doctorService.nameMap();
    }
    private void reloadAndRefresh() {
    // Make sure the maps and list are up to date
        reloadTxt(); 
        statusFilter.setSelectedIndex(0);
        typeFilter.setSelectedIndex(0);
//        dateFilter.clear();
        
        // Render using refreshTable
        Utils.refreshTable(appointmentTable,appointments,a -> new Object[] {
                a.getAppointmentId(),
                a.getStaffId()    + " - " + staffNameMap.getOrDefault(a.getStaffId(),    "(unknown)"),
                a.getCustomerId() + " - " + customerNameMap.getOrDefault(a.getCustomerId(), "(unknown)"),
                a.getDoctorId()   + " - " + doctorNameMap.getOrDefault(a.getDoctorId(),   "(unknown)"),
                a.getDate(),
                a.getTime(),
                a.getNote(),
                a.getStatus(),
                a.getType()
            }
        );
    }

//    private void addResetBehavior() {
//        resetBtn.addActionListener(e -> {
//            searchField.setText("");
//            populateFilters();
//            applyFilters();
//        });
//    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        appointmentTable = new javax.swing.JTable();
        statusFilter = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        resetBtn = new javax.swing.JButton();
        typeFilter = new javax.swing.JComboBox<>();
        backBtn = new javax.swing.JButton();
        dateFilter = new com.github.lgooddatepicker.components.DatePicker();

        jPanel1.setBackground(new java.awt.Color(245, 243, 238));
        jPanel1.setRequestFocusEnabled(false);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Appointment Monitoring");
        jLabel1.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N

        appointmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                appointmentTableMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(appointmentTable);

        statusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        statusFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusFilterActionPerformed(evt);
            }
        });

        jLabel2.setText("Search (Name):");

        resetBtn.setText("Reset");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });

        typeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        typeFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeFilterActionPerformed(evt);
            }
        });

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(resetBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                                .addComponent(statusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(typeFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(backBtn)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addComponent(dateFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 762, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(backBtn)
                .addGap(3, 3, 3)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetBtn)
                    .addComponent(typeFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                .addContainerGap())
        );

        dateFilter.addDateChangeListener(e -> {applyFilters();});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 963, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void appointmentTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_appointmentTableMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_appointmentTableMouseReleased

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("home");
    }//GEN-LAST:event_backBtnActionPerformed

    private void statusFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusFilterActionPerformed
        // TODO add your handling code here:
        status = statusFilter.getSelectedItem().toString();
        applyFilters();
    }//GEN-LAST:event_statusFilterActionPerformed

    private void typeFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeFilterActionPerformed
        // TODO add your handling code here:
        type = typeFilter.getSelectedItem().toString();
        applyFilters();
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
    private javax.swing.JTable appointmentTable;
    private javax.swing.JButton backBtn;
    private com.github.lgooddatepicker.components.DatePicker dateFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton resetBtn;
    private javax.swing.JTextField searchField;
    private javax.swing.JComboBox<String> statusFilter;
    private javax.swing.JComboBox<String> typeFilter;
    // End of variables declaration//GEN-END:variables
}
