package javaproj.GUI.Doctors;

import javaproj.Model.Role.Doctor;
import javaproj.Model.Appointment;
import javaproj.Model.Service;
import javaproj.Model.Medication;


import javaproj.Utils.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import javaproj.Model.Role.Customer;
import javaproj.Repository.AppointmentRepository;
import javaproj.Methods.Appointment.DoctorAppointment;
import javaproj.Methods.Items.ChargeItemService;
import javaproj.Methods.Items.MedicationService;
import javaproj.Methods.Items.ServiceService;
import javaproj.Methods.Payment.DoctorPayment;      
import javaproj.Repository.PaymentRepository; 
import javaproj.Utils.PanelController;
import javaproj.Methods.User.CustomerService;


public class ChargePanel extends javax.swing.JPanel {

    private PanelController controller;
    
    private final MedicationService medicationService = new MedicationService();
    private final ServiceService serviceService = new ServiceService();
    private final ChargeItemService chargeItemService = new ChargeItemService();
    private final PaymentRepository paymentRepo = new PaymentRepository();
    private final DoctorPayment doctorPayment = new DoctorPayment(paymentRepo);
    private final CustomerService customerService = new CustomerService();
    private final AppointmentRepository apptRepo = new AppointmentRepository();
    private final DoctorAppointment doctorAppt = new DoctorAppointment(apptRepo);

    private final Doctor doctor;
        
    private final List<Service> services = serviceService.serviceList();
    private final List<Medication> medications = medicationService.medicationList();
    
    private int qty;
    private double unitPrice;
    private double totalamt;
    private String apptId;
    
    String[] columnNames = {"Type","Item Name","Quantity", "Unit Price", "Total Amount","Description"};
    private DefaultTableModel model = new DefaultTableModel(columnNames, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // all cells uneditable
        }
    };
    
    public ChargePanel(Doctor doctor, PanelController controller) {
        this.controller = controller;
        this.doctor = doctor;
        initComponents();
        loadData();
        setupTable();
        setupActions();
        comboUpdateDetails();
        
        // Initialize Appointment
        comboSelectAppointment.setSelectedIndex(-1);
        
        txtName.setEditable(false);
        txtICNum.setEditable(false);
        txtType.setEditable(false);
        
        // Initialize comboService for preview
        comboService.setSelectedItem("Service");
        comboService.setSelectedIndex(0);
        spnQuantity.setValue(1);
        spnQuantity.setEnabled(false);
        
        comboMedication.setSelectedIndex(-1);
    }
    public void setVisible(boolean flag) {
        super.setVisible(flag);
            if (flag) {
                refresh();
            }
        }

    private void loadData() {

        // filter only appointments belonging to this doctor
        Utils.updateComboBox(comboSelectAppointment, doctorAppt.listAppointmentIds(doctor.getSystemId(), false));
        
        // Fill services
        Utils.updateComboBox(comboService,serviceService.serviceNameList());

        // Fill medications
        Utils.updateComboBox(comboMedication,medicationService.medicationNameList());
    }
    
    private void setupTable() {
        TbChargeItems.setModel(model);
    }
    
    private void comboUpdateDetails(){
        comboSelectAppointment.addActionListener(e -> updateAppointmentDetails());
    }
    
    private void updateAppointmentDetails() {
        String selectedId = (String) comboSelectAppointment.getSelectedItem();
        if (selectedId == null) {
            return;
        }

        Appointment selectedAppt = doctorAppt.getAppointment(selectedId);
        if (selectedAppt == null) {
            return;
        }

        apptId = selectedId; // keep it in sync

        // Find customer info
        String customerId = selectedAppt.getCustomerId();
        Customer selectedCustomer = customerService.getById(customerId); 

        if (selectedCustomer != null) {
            txtName.setText(selectedCustomer.getName());
            txtICNum.setText(selectedCustomer.getIdentityNumber());
        } else {
            txtName.setText("Unknown");
            txtICNum.setText("-");
        }

        txtType.setText(selectedAppt.getType().toString());
    }

    
    private void setupActions() {
        // Calculate total automatically when quantity changes
        spnQuantity.addChangeListener(e -> updateTotal());

        comboService.addActionListener(e -> {
            if (comboService.getSelectedIndex() != -1) {
                // Clear medication if service chosen
                comboMedication.setSelectedIndex(-1);
                spnQuantity.setValue(1);
                spnQuantity.setEnabled(false);

                String selectedService = comboService.getSelectedItem().toString();
                for (Service s : services) {
                    if (s.getName().equals(selectedService.split(" - ")[1])) {
                        txtDescription.setText(s.getDescription()); // show service description
                        break;
                    }
                }
            }
            updateTotal();
        });

        comboMedication.addActionListener(e -> {
            if (comboMedication.getSelectedIndex() != -1) {
                // Clear service if medication chosen
                comboService.setSelectedIndex(-1);
                spnQuantity.setEnabled(true);

                String selectedMed = comboMedication.getSelectedItem().toString();
                for (Medication m : medications) {
                    if (m.getName().equals(selectedMed.split(" - ")[1])) {
                        txtDescription.setText(m.getDescription()); // show medication indication
                        break;
                    }
                }
            }
            updateTotal();
        });

        btnAdd.addActionListener(e -> addChargeItem());
        btnRemove.addActionListener(e -> removeSelectedItem());
        btnSave.addActionListener(e -> saveChargeItems());
    }
    
    private void updateTotal() {
        qty = (Integer) spnQuantity.getValue();
        unitPrice = 0;

        String selectedService = (String) comboService.getSelectedItem();
        String selectedMed = (String) comboMedication.getSelectedItem();

        if (selectedService != null && !selectedService.isEmpty()) {
            for (Service s : services) {
                if (s.getId().equals(selectedService.split(" - ")[0])) {
                    unitPrice = s.getUnitPrice();
                    break;
                }
            }
        }
        
        if (selectedMed != null && !selectedMed.isEmpty()) {
            for (Medication m : medications) {
                if (m.getId().equals(selectedMed.split(" - ")[0])) {
                    unitPrice = m.getUnitPrice();
                    break;
                }
            }
        }

        totalamt = qty * unitPrice;
        txtTotalAmount.setText(String.format("%.2f", totalamt));
    }
    
    private void addChargeItem() {
        apptId = (String) comboSelectAppointment.getSelectedItem();
        if (apptId == null) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.");
            return;
        }

        // Check service or medication
        String serviceId = null,
        serviceName = null,
        medId = null,
        medName = null;
        String type = "";
        
        if (comboService.getSelectedItem() != null && !comboService.getSelectedItem().toString().isEmpty()) { 
            serviceId = comboService.getSelectedItem().toString().split(" - ")[0];
            serviceName = comboService.getSelectedItem().toString().split(" - ")[1];
            unitPrice = serviceService.loadService(serviceId).getUnitPrice();
            qty = 1; // force service to 1
            type = "Service";
            
        } else if (comboMedication.getSelectedItem() != null && !comboMedication.getSelectedItem().toString().isEmpty()) {
            medId = comboMedication.getSelectedItem().toString().split(" - ")[0];
            medName = comboMedication.getSelectedItem().toString().split(" - ")[1];
            unitPrice = medicationService.loadMedication(medId).getUnitPrice();
            qty = (Integer) spnQuantity.getValue();
            type = "Medication";
        }
        else {
            JOptionPane.showMessageDialog(this, "Please select a service or medication.");
            return;
        }

        
        totalamt = qty * unitPrice;

        // Add to table (names only)
        String itemName = (serviceName != null) ? serviceName : medName;
        String description = txtDescription.getText().trim().isEmpty() ? "N/A" : txtDescription.getText().trim();
        
        // Check if already exists in table, merge if exists
        for (int i = 0; i < model.getRowCount(); i++) {
            String existingType = (String) model.getValueAt(i, 0);
            String existingName = (String) model.getValueAt(i, 1);

            if (existingType.equals(type) && existingName.equals(itemName)) {
                if (type.equals("Medication")) {
                    int oldQty = (int) model.getValueAt(i, 2);
                    int newQty = oldQty + qty;
                    model.setValueAt(newQty, i, 2);
                    model.setValueAt(unitPrice, i, 3);
                    model.setValueAt(unitPrice * newQty, i, 4);
                    model.setValueAt(description, i, 5);
                } else {
                    // Service: keep quantity = 1 (don’t duplicate)
                    JOptionPane.showMessageDialog(this, "This service is already added.");
                }
                return;
            }
        }
        
        // If not found, add new row
        model.addRow(new Object[]{type, itemName, qty, unitPrice, totalamt, description});
    }

    private void removeSelectedItem() {
        int row = TbChargeItems.getSelectedRow();
        if (row == -1){
            JOptionPane.showMessageDialog(this, "Please select an item to remove.");
        }
        if (row != -1) {
            model.removeRow(row);
        }
    }
    
    private void saveChargeItems() {
        // Check if the table has any rows
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No charge items to save.");
            return; // stop here, don’t save
        }
        
        
        // Build new items from table
        apptId = (String) comboSelectAppointment.getSelectedItem();

        double grandTotal = 0.0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String type = (String) model.getValueAt(i, 0);
            String itemName = (String) model.getValueAt(i, 1);
            int qty = (int) model.getValueAt(i, 2);
            double unitPrice = (double) model.getValueAt(i, 3);
            double totalamt = (double) model.getValueAt(i, 4);
            String description = (String) model.getValueAt(i, 5);

            String serviceId = null, medId = null;
            if (type.equals("Service")) {
                for (Service s : services) {
                    if (s.getName().equals(itemName)) {
                        serviceId = s.getId();
                        break;
                    }
                }
            } else {
                for (Medication m : medications) {
                    if (m.getName().equals(itemName)) {
                        medId = m.getId();
                        break;
                    }
                }
            }

            chargeItemService.createAndAdd( 
                    apptId, 
                    serviceId, 
                    medId,
                    qty, 
                    unitPrice, 
                    totalamt,
                    description
            );
            grandTotal += totalamt;
        }

        doctorAppt.appointmentCompleted(apptId);
        doctorPayment.addPendingPayment(apptId, grandTotal);
        JOptionPane.showMessageDialog(this, "Charge items saved successfully!");
        
        // Clear the table after save
        model.setRowCount(0);
        refresh();
    }
    private void refresh(){
        loadData();
        comboService.setSelectedItem("Service");
        comboService.setSelectedIndex(0);
        spnQuantity.setValue(1);
        spnQuantity.setEnabled(false);

        comboMedication.setSelectedIndex(-1);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblFeesCharging = new javax.swing.JLabel();
        lblSelectAppointment = new javax.swing.JLabel();
        comboSelectAppointment = new javax.swing.JComboBox<>();
        lblPatient = new javax.swing.JLabel();
        lblAppointmentSelection = new javax.swing.JLabel();
        lblICNumber = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        comboService = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        lblService = new javax.swing.JLabel();
        lblMedication = new javax.swing.JLabel();
        comboMedication = new javax.swing.JComboBox<>();
        lblQuantity = new javax.swing.JLabel();
        spnQuantity = new javax.swing.JSpinner();
        txtTotalAmount = new javax.swing.JTextField();
        lblTotalAmount = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TbChargeItems = new javax.swing.JTable();
        lblChargeItems = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        txtICNum = new javax.swing.JTextField();
        txtType = new javax.swing.JTextField();
        backBtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        lblFeesCharging.setText("Fees Charging");
        lblFeesCharging.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        lblSelectAppointment.setText("Select Appointment");
        lblSelectAppointment.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N

        comboSelectAppointment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblPatient.setText("Patient:");
        lblPatient.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblAppointmentSelection.setText("Appointment Selection");
        lblAppointmentSelection.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        lblICNumber.setText("IC Number:");
        lblICNumber.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblType.setText("Type:");
        lblType.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        comboService.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Add Charge Items");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        lblService.setText("Service");
        lblService.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N

        lblMedication.setText("Medication");
        lblMedication.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N

        comboMedication.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblQuantity.setText("Quantity");
        lblQuantity.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N

        txtTotalAmount.setText("Amount");

        lblTotalAmount.setText("Total Amount");
        lblTotalAmount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblDescription.setText("Description");
        lblDescription.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N

        txtDescription.setText("desc");

        TbChargeItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(TbChargeItems);

        lblChargeItems.setText("Charge Items");
        lblChargeItems.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        btnAdd.setText("Add");

        btnRemove.setText("Remove");

        btnSave.setText("Save");

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectAppointment)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(spnQuantity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblQuantity)
                                    .addGap(32, 32, 32)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblTotalAmount)
                                .addComponent(txtTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(comboSelectAppointment, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDescription)
                        .addComponent(lblDescription)
                        .addComponent(jLabel1)
                        .addComponent(lblAppointmentSelection)
                        .addComponent(lblFeesCharging)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(comboService, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblService))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(comboMedication, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblMedication)
                                    .addGap(0, 0, Short.MAX_VALUE))))
                        .addComponent(btnAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblPatient, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                                .addComponent(lblType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblICNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                .addComponent(txtICNum)
                                .addComponent(txtType)))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblChargeItems, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(393, 393, 393))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(backBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(backBtn)
                .addGap(7, 7, 7)
                .addComponent(lblFeesCharging)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAppointmentSelection)
                    .addComponent(lblChargeItems))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSelectAppointment)
                        .addGap(12, 12, 12)
                        .addComponent(comboSelectAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPatient)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblICNumber)
                            .addComponent(txtICNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblType)
                            .addComponent(txtType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblService)
                            .addComponent(lblMedication))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboMedication, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(lblDescription)
                        .addGap(13, 13, 13)
                        .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTotalAmount)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblQuantity)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spnQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnSave)
                    .addComponent(btnRemove))
                .addGap(0, 26, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("home");
    }//GEN-LAST:event_backBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TbChargeItems;
    private javax.swing.JButton backBtn;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> comboMedication;
    private javax.swing.JComboBox<String> comboSelectAppointment;
    private javax.swing.JComboBox<String> comboService;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAppointmentSelection;
    private javax.swing.JLabel lblChargeItems;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblFeesCharging;
    private javax.swing.JLabel lblICNumber;
    private javax.swing.JLabel lblMedication;
    private javax.swing.JLabel lblPatient;
    private javax.swing.JLabel lblQuantity;
    private javax.swing.JLabel lblSelectAppointment;
    private javax.swing.JLabel lblService;
    private javax.swing.JLabel lblTotalAmount;
    private javax.swing.JLabel lblType;
    private javax.swing.JSpinner spnQuantity;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtICNum;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTotalAmount;
    private javax.swing.JTextField txtType;
    // End of variables declaration//GEN-END:variables
}
