package javaproj.GUI.Doctors;

import java.time.LocalDate;
import javaproj.Model.Role.*;
import javaproj.Model.*;
import javaproj.Utils.*;

import javax.swing.*;
import java.util.*;
import javaproj.Methods.User.CustomerService; 
import javaproj.Repository.AppointmentRepository;
import javaproj.Methods.Appointment.DoctorAppointment;

import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class DrAppointment extends javax.swing.JPanel {
    
    private PanelController controller;
    private Doctor doctor;
    
    private final CustomerService customerService = new CustomerService();
    private final AppointmentRepository apptRepo = new AppointmentRepository();
    private final DoctorAppointment doctorAppt = new DoctorAppointment(apptRepo);


    
    private List<Appointment> appointments = Collections.emptyList();
    private Map<String, String> customerMap = new LinkedHashMap<>();

    String[] columnNames = {"ID", "Time", "Patient", "Date", "Type", "Note", "Status"};
    private DefaultTableModel model = new DefaultTableModel(columnNames, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // all cells uneditable
        }
    };
    private DefaultComboBoxModel<String> typeModel = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<String> statusModel = new DefaultComboBoxModel<>();

    public DrAppointment(Doctor doctor, PanelController controller) {
        this.controller  = controller;
        this.doctor = doctor;
        initComponents();
        loadData();
        populateCmb();
        setupActions();
        loadAppointments();
    }

    public DrAppointment() {
        
    }

    private void loadData() {
        if (doctor != null) {
            appointments = doctorAppt.appointmentList(doctor.getSystemId());
        } else {
            appointments = Collections.emptyList();
        }
        // refresh patient map from per-role service
        customerMap = customerService.nameMap();
    }

    private void setupActions() {
        // Live search
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {applyFilters();}
            public void removeUpdate(javax.swing.event.DocumentEvent e) {applyFilters();}
            public void changedUpdate(javax.swing.event.DocumentEvent e) {applyFilters();}
        });

        // Reset
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            cmbType.setSelectedItem("All");
            cmbStatus.setSelectedItem("All");
            datePicker.clear();
            applyFilters();
        });

        // Type filter
        cmbType.addActionListener(e -> applyFilters());
        
        // Date filter
        datePicker.addDateChangeListener(e -> applyFilters());
        
        // Status filter
        cmbStatus.addActionListener(e -> applyFilters());
    }

    private void populateCmb() {
        // cmbType
        typeModel.addElement("All");
        for (Appointment.Type t : Appointment.Type.values()) {
            typeModel.addElement(t.name());
        }
        cmbType.setModel(typeModel);
        
        // cmbStatus
        statusModel.addElement("All"); // reset option
        for (Appointment.Status s : Appointment.Status.values()) {
            statusModel.addElement(s.name());
        }
        cmbStatus.setModel(statusModel);
    }
    
    // Load appointments belonging to this doctor
    private void loadAppointments() {
        Utils.refreshTable(AppointmentTable,appointments,a -> new Object[] {
                a.getAppointmentId(),
                a.getTime(),
                a.getCustomerId() + " - " + customerMap.getOrDefault(a.getCustomerId(), "(unknown)"),
                a.getDate(),
                a.getType(),
                a.getStatus(),
                a.getNote()
            }
        );
    }

    private void applyFilters() {
        if (appointments == null) return;
        String keyword = txtSearch.getText().trim().toLowerCase();
        String selectedType = cmbType.getSelectedItem().toString();
        String selectedStatus = cmbStatus.getSelectedItem().toString();
        LocalDate selectedDate = datePicker.getDate();

        
        List<Appointment> filtered = doctorAppt.filter( appointments, selectedStatus, selectedType, selectedDate, keyword, null, customerMap, null);
        //String[] columnNames = {"ID", "Time", "Patient", "Date", "Type", "Note", "Status"};
        Utils.refreshTable(AppointmentTable,filtered,a -> new Object[] {
                a.getAppointmentId(),
                a.getTime(),
                a.getCustomerId() + " - " + customerMap.getOrDefault(a.getCustomerId(), "(unknown)"),
                a.getDate(),
                a.getType(),
                a.getStatus(),
                a.getNote()
            });
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
        lblTitleAppt = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        cmbStatus = new javax.swing.JComboBox<>();
        lblSearch = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AppointmentTable = new javax.swing.JTable();
        lblType = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        cmbType = new javax.swing.JComboBox<>();
        datePicker = new com.github.lgooddatepicker.components.DatePicker();
        backBtn = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblTitleAppt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleAppt.setText("Appointments");
        lblTitleAppt.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitleAppt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        txtSearch.setToolTipText("");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblSearch.setText("Search (Name/ID):");

        lblStatus.setText("Status:");

        AppointmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Time", "Patient", "Date", "Type", "Note", "Status"
            }
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // disable editing for all cells
            }
        });
        jScrollPane1.setViewportView(AppointmentTable);

        lblType.setText("Type:");

        btnReset.setText("Reset");

        cmbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitleAppt, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(backBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(backBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitleAppt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblType)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSearch)
                    .addComponent(btnReset)
                    .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatus)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addGap(80, 80, 80))
        );

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

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("home");
    }//GEN-LAST:event_backBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AppointmentTable;
    private javax.swing.JButton backBtn;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JComboBox<String> cmbType;
    private com.github.lgooddatepicker.components.DatePicker datePicker;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitleAppt;
    private javax.swing.JLabel lblType;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
