/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package javaproj.GUI.Manager;

import java.util.List;
import javaproj.Model.Role.Manager;
import javaproj.Methods.User.CheckDuplicates;
import javaproj.Methods.User.CustomerService;
import javaproj.Methods.User.DoctorService;
import javaproj.Methods.User.ManagerService;
import javaproj.Methods.User.StaffService;
import javaproj.Utils.ReturnList;
import javaproj.Utils.Utils;
import javax.swing.*;
/**
 *
 * @author mayvi
 */
public class addUser extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(addUser.class.getName());
    private String selectedRole;
    private Manager loggedInManager;
    private final CustomerService customerService = new CustomerService();
    private final DoctorService doctorService = new DoctorService();
    private final ManagerService managerService = new ManagerService();
    private final StaffService staffService = new StaffService();
    private final CheckDuplicates checker = new CheckDuplicates();

    public addUser() {
        initComponents();
    }
    
    public addUser(String selectedRole) {
        initComponents();
        this.selectedRole = selectedRole;
        setupRoleField();
        updateSpecialtyBox();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public void setLoggedInManager(Manager manager) {
        this.loggedInManager = manager;
    }
    private void setupRoleField() {
        titleLabel.setText("Add New " + selectedRole);

        // Show/Hide specialty row based on type
        boolean isDoctor = "doctor".equalsIgnoreCase(selectedRole);
        specialtyLabel.setVisible(isDoctor);
        specialtyBox.setVisible(isDoctor);
    }
    private void updateSpecialtyBox(){
        List<String> specialty = ReturnList.get("specialty");
        Utils.updateComboBox(specialtyBox,specialty);
    }
    private void clearForm() {
        nameField.setText("");
        ICfield.setText("");
        addressArea.setText("");
        phoneField.setText("");
        emailField.setText("");
        specialtyBox.setSelectedIndex(0);
    }

    private void onAddUser() {
        String role = (selectedRole == null ? "" : selectedRole.trim().toLowerCase());

        // 1) validate using the correct service -> each returns String error or null if OK
        String error = switch (role) {
            case "customer" -> customerService.validateForm(
                    nameField.getText().trim(),
                    ICfield.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            case "doctor" -> doctorService.validateForm(
                    nameField.getText().trim(),
                    ICfield.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    specialtyBox.getSelectedItem().toString().trim()
            );
            case "manager" -> managerService.validateForm(
                    loggedInManager, /* currentUserId */ null, role,
                    nameField.getText().trim(),
                    ICfield.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    specialtyBox.getSelectedItem().toString().trim()
            );
            case "staff" -> staffService.validateForm(
                    nameField.getText().trim(),
                    ICfield.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            default -> "Please select a valid role.";
        };

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String dup = checker.checkDuplicates(null, ICfield.getText().trim(), emailField.getText().trim());
        if (dup != null) {
            JOptionPane.showMessageDialog(this, dup, "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2) create using the correct service
        switch (role) {
            case "customer" -> customerService.create(
                    nameField.getText().trim(),
                    ICfield.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            case "doctor" -> doctorService.create(
                    nameField.getText().trim(),
                    ICfield.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    specialtyBox.getSelectedItem().toString().trim()
            );
            case "manager" -> managerService.create(
                    nameField.getText().trim(),
                    ICfield.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            case "staff" -> staffService.create(
                    nameField.getText().trim(),
                    ICfield.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            default -> {
                JOptionPane.showMessageDialog(this, "Unknown role: " + selectedRole);
                return;
            }
        }

        JOptionPane.showMessageDialog(this,
                (selectedRole == null ? "User" : selectedRole) + " added successfully!");
        clearForm();
        this.dispose();
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
        NameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        ICLabel = new javax.swing.JLabel();
        ICfield = new javax.swing.JTextField();
        AddressLabel = new javax.swing.JLabel();
        PhoneLabel = new javax.swing.JLabel();
        phoneField = new javax.swing.JTextField();
        emailLabel = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        addBtn = new javax.swing.JButton();
        specialtyLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        addressArea = new javax.swing.JTextArea();
        specialtyBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 243, 238));

        NameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        NameLabel.setText("Name:");

        ICLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ICLabel.setText("IC Number:");

        AddressLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddressLabel.setText("Address:");

        PhoneLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PhoneLabel.setText("Phone Number:");

        emailLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        emailLabel.setText("Email:");

        addBtn.setBackground(new java.awt.Color(0, 102, 153));
        addBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        addBtn.setForeground(new java.awt.Color(255, 255, 255));
        addBtn.setText("Done");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        specialtyLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        specialtyLabel.setText("Specialty:");

        titleLabel.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        titleLabel.setText("Add New Manager");

        addressArea.setColumns(20);
        addressArea.setRows(5);
        jScrollPane1.setViewportView(addressArea);

        specialtyBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(NameLabel)
                            .addComponent(ICLabel)
                            .addComponent(AddressLabel)
                            .addComponent(PhoneLabel)
                            .addComponent(emailLabel)
                            .addComponent(specialtyLabel))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                            .addComponent(ICfield, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                            .addComponent(phoneField, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                            .addComponent(emailField, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                            .addComponent(specialtyBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(248, 248, 248)
                        .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addComponent(titleLabel)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(titleLabel)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ICfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ICLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AddressLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PhoneLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emailLabel)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(specialtyLabel)
                    .addComponent(specialtyBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        addBtn.setName("contentSecondary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setName("contentSecondary");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        onAddUser();
    }//GEN-LAST:event_addBtnActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new addUser().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AddressLabel;
    private javax.swing.JLabel ICLabel;
    private javax.swing.JTextField ICfield;
    private javax.swing.JLabel NameLabel;
    private javax.swing.JLabel PhoneLabel;
    private javax.swing.JButton addBtn;
    private javax.swing.JTextArea addressArea;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField phoneField;
    private javax.swing.JComboBox<String> specialtyBox;
    private javax.swing.JLabel specialtyLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
