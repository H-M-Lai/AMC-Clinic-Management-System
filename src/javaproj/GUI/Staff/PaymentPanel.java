/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package javaproj.GUI.Staff;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.io.IOException;
import javaproj.Utils.Utils;

import java.util.*;
import javaproj.Methods.Items.ChargeItemService;
import javaproj.Methods.Items.MedicationService;
import javaproj.Methods.Items.ServiceService;
import javaproj.Model.ChargeItems;
import javaproj.Model.Medication;
import javaproj.Model.Role.Customer;
import javaproj.Model.Service;

import javaproj.Methods.Payment.StaffPayment;
import javaproj.Repository.PaymentRepository;
import javaproj.Repository.AppointmentRepository;
import javaproj.Methods.User.CustomerService;
import javaproj.Utils.PanelController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author NICK
 */
public class PaymentPanel extends javax.swing.JPanel {
    
    PDFInvoiceGenerator pdf = new PDFInvoiceGenerator();
    private final PaymentRepository paymentRepo = new PaymentRepository();
    private final AppointmentRepository apptRepo = new AppointmentRepository();
    private final StaffPayment staffPayment = new StaffPayment(paymentRepo, apptRepo);
    private final CustomerService customerService = new CustomerService();
    private final ChargeItemService chargeItemsService = new ChargeItemService();
    private final MedicationService  medicationService  = new MedicationService();
    private final ServiceService     serviceService     = new ServiceService();
            
    List<String> pendingPaymentAppointment = new ArrayList<>();
    private PanelController controller;
    private void checkCustomerAppointment(String customerId){
        pendingPaymentAppointment.clear();
        pendingPaymentAppointment.add("- - - Select Appointment - - -");
        pendingPaymentAppointment.addAll(staffPayment.filterPendingsId("appointment", customerId));
        Utils.updateComboBox(apppointmentComboBox, pendingPaymentAppointment);
    }
    private void addPayment(){
        try{
            String appointmentId = apppointmentComboBox.getSelectedItem().toString();
            if (appointmentId == null || appointmentId.startsWith("-")) {
                JOptionPane.showMessageDialog(this, "Please select a pending payment.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (methodComboBox.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Please select a payment method.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String paymentId = staffPayment.pendingPaymentAppointmentMap().get(appointmentId);
            double amount = Double.parseDouble(amountInput.getText().trim());
            String method = methodComboBox.getSelectedItem().toString().trim();

            boolean donePayment = staffPayment.makePayment(paymentId, amount, method);
            if (!donePayment){
                JOptionPane.showMessageDialog(this, "Payment record not found or already paid.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else{
                JOptionPane.showMessageDialog(this, "Payment completed.");
            }
            
            int option = JOptionPane.showConfirmDialog(this,"Receipt?", "Payment",JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                    try {
                        pdf.generateInvoice(appointmentId,paymentId,amount,method.toString(),amount);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    PaymentInvoice invoice = new PaymentInvoice("invoices/"+paymentId+".pdf");
                    invoice.setVisible(true);
                } else {
                    return;
                }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Payment failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void reset(){
        icInput.setText("");
        Utils.updateComboBox(apppointmentComboBox, Arrays.asList("- - -Input Customer IC - - -"));
        amountInput.setText("");
    }
    
    private void viewChargedItem(){
        String appointmentId = String.valueOf(apppointmentComboBox.getSelectedItem());
        if (appointmentId == null || appointmentId.startsWith("-")) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Pull the data we need
        List<ChargeItems> items = chargeItemsService.chargeItemsList(appointmentId);
        if (items == null || items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No charged items for this appointment.", "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Build quick lookup maps
        Map<String, Service>    serviceById = new HashMap<>();
        for (Service s : serviceService.serviceList()) serviceById.put(s.getId(), s);

        Map<String, Medication> medById = new HashMap<>();
        for (Medication m : medicationService.medicationList()) medById.put(m.getId(), m);

        // Table model
        String[] cols = {"ID","Name","Description","Quantity","Unit Price","Total Amount"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        double grandTotal = 0.0;
        for (ChargeItems c : items) {
            String name;
            if (c.getServiceId() != null) {
                Service s = serviceById.get(c.getServiceId());
                name = (s != null) ? s.getName() : "Service " + c.getServiceId();
            } else if (c.getMedicationId() != null) {
                Medication m = medById.get(c.getMedicationId());
                name = (m != null) ? m.getName() : "Medication " + c.getMedicationId();
            } else {
                name = "(Unknown)";
            }
            grandTotal += c.getTotalAmount();
            model.addRow(new Object[] {
                c.getChargeItemId(),
                name,
                c.getDescription(),
                c.getQuantity(),
                c.getUnitPrice(),
                c.getTotalAmount()
            });
        }
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setPreferredScrollableViewportSize(new Dimension(800, 260));
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(260);

        JLabel totalLbl = new JLabel(String.format("Grand Total: %.2f", grandTotal));
        totalLbl.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel south = new JPanel(new java.awt.BorderLayout());
        south.add(totalLbl, java.awt.BorderLayout.WEST);
        JButton close = new JButton("Close");
        close.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(close);
            if (w != null) w.dispose();
        });
        JPanel right = new JPanel();
        right.add(close);
        south.add(right, java.awt.BorderLayout.EAST);

        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Charged Items – " + appointmentId, Dialog.ModalityType.APPLICATION_MODAL);
        dlg.getContentPane().add(new JScrollPane(table), java.awt.BorderLayout.CENTER);
        dlg.getContentPane().add(south, java.awt.BorderLayout.SOUTH);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }
    
    public PaymentPanel(PanelController controller) {
        this.controller = controller;
        initComponents();
    }
    @Override
    public void setVisible(boolean flag) {
        super.setVisible(flag);
        if (flag) {
            reset();
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

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        confirmationBtn = new javax.swing.JButton();
        icInput = new javax.swing.JTextField();
        patientic = new javax.swing.JLabel();
        checkIcBtn = new javax.swing.JButton();
        appointmentIdLabel = new javax.swing.JLabel();
        amount = new javax.swing.JLabel();
        amountInput = new javax.swing.JTextField();
        methodInput = new javax.swing.JLabel();
        methodComboBox = new javax.swing.JComboBox<>();
        apppointmentComboBox = new javax.swing.JComboBox<>();
        chargedItemBtn = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();
        recordBtn = new javax.swing.JButton();

        setBackground(java.awt.Color.white);

        jPanel2.setBackground(new java.awt.Color(245, 243, 238));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(java.awt.Color.black);
        jLabel1.setText("Payment");

        confirmationBtn.setText("Make Payment");
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

        checkIcBtn.setText("Check");
        checkIcBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkIcBtnActionPerformed(evt);
            }
        });

        appointmentIdLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        appointmentIdLabel.setForeground(java.awt.Color.black);
        appointmentIdLabel.setText("Appointment ID:");

        amount.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        amount.setForeground(java.awt.Color.black);
        amount.setText("Amount:");

        amountInput.setBackground(java.awt.Color.white);
        amountInput.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        amountInput.setForeground(java.awt.Color.black);

        methodInput.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        methodInput.setForeground(java.awt.Color.black);
        methodInput.setText("Payment Method:");

        methodComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- - - Select - - -", "CARD", "CASH" }));

        apppointmentComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        apppointmentComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apppointmentComboBoxActionPerformed(evt);
            }
        });

        chargedItemBtn.setText("View Charged Item");
        chargedItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargedItemBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(methodInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(methodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(amountInput, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(patientic, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(icInput, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(checkIcBtn)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(66, 66, 66)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(appointmentIdLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                                            .addComponent(apppointmentComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(chargedItemBtn))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(262, 262, 262)
                        .addComponent(confirmationBtn)))
                .addGap(34, 86, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(patientic)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(icInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkIcBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(appointmentIdLabel)
                        .addGap(10, 10, 10)
                        .addComponent(apppointmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(amount)
                    .addComponent(chargedItemBtn))
                .addGap(0, 0, 0)
                .addComponent(amountInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(methodInput)
                .addGap(0, 0, 0)
                .addComponent(methodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(confirmationBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        confirmationBtn.setName("contentSecondary");

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        recordBtn.setText("View Payment Record");
        recordBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recordBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(backBtn)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(106, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recordBtn, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(backBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recordBtn)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jPanel2.setName("contentSecondary");
    }// </editor-fold>//GEN-END:initComponents

    private void confirmationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmationBtnActionPerformed
        // TODO add your handling code here:
        addPayment();
    }//GEN-LAST:event_confirmationBtnActionPerformed

    private void apppointmentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apppointmentComboBoxActionPerformed
        // TODO add your handling code here:
        String selected = (String) apppointmentComboBox.getSelectedItem();
        if ("- - - Select Appointment - - -".equals(selected)) {
            return;
        }
        
        if (pendingPaymentAppointment.contains("- - - Select Appointment - - -")) {
            pendingPaymentAppointment.remove("- - - Select Appointment - - -");
            apppointmentComboBox.removeItem("- - - Select Appointment - - -"); // safer than rebuilding model
        }
        
        amountInput.setText(String.valueOf(staffPayment.getAmount(selected)));
    }//GEN-LAST:event_apppointmentComboBoxActionPerformed

    private void checkIcBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkIcBtnActionPerformed
        // TODO add your handling code here:
        String ic = icInput.getText().trim();
        if (ic.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter patient IC", "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String customerId = getCustomerIdByIc(ic);
        if(customerId == null){
            JOptionPane.showMessageDialog(this, "No patient found", "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        checkCustomerAppointment(customerId);
    }//GEN-LAST:event_checkIcBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("home");
    }//GEN-LAST:event_backBtnActionPerformed

    private void recordBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordBtnActionPerformed
        // TODO add your handling code here:
        controller.show("paymentRecord");
    }//GEN-LAST:event_recordBtnActionPerformed

    private void chargedItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargedItemBtnActionPerformed
        // TODO add your handling code here:
        viewChargedItem();
    }//GEN-LAST:event_chargedItemBtnActionPerformed
    private String getCustomerIdByIc(String ic) {
        if (ic == null || ic.isBlank()) return null;
        for (Customer c : customerService.customers()) {
            if (ic.equals(c.getIdentityNumber())) return c.getSystemId();
        }
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel amount;
    private javax.swing.JTextField amountInput;
    private javax.swing.JLabel appointmentIdLabel;
    private javax.swing.JComboBox<String> apppointmentComboBox;
    private javax.swing.JButton backBtn;
    private javax.swing.JButton chargedItemBtn;
    private javax.swing.JButton checkIcBtn;
    private javax.swing.JButton confirmationBtn;
    private javax.swing.JTextField icInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox<String> methodComboBox;
    private javax.swing.JLabel methodInput;
    private javax.swing.JLabel patientic;
    private javax.swing.JButton recordBtn;
    // End of variables declaration//GEN-END:variables
}
