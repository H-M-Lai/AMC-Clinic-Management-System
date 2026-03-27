/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package javaproj.GUI.Manager;

import java.util.List;
import javaproj.Model.Role.Doctor;
import javaproj.Utils.*;


import javax.swing.*;
import javaproj.Model.Role.User;

import javaproj.Methods.User.CustomerService;
import javaproj.Methods.User.DoctorService;
import javaproj.Methods.User.ManagerService;
import javaproj.Methods.User.StaffService;
/**
 *
 * @author mayvi
 */
public class deleteUser extends javax.swing.JFrame {
    
    private String selectedRole;
    private String currentUserId;
    private String selectedId;
    
    private final CustomerService customerService = new CustomerService();
    private final DoctorService   doctorService   = new DoctorService();
    private final ManagerService  managerService  = new ManagerService();
    private final StaffService    staffService    = new StaffService();
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(deleteUser.class.getName());

    public deleteUser() {
        initComponents();
    }
    
    public deleteUser(String selectedRole, String selectedId) {
        this.selectedRole = selectedRole;
        this.selectedId = selectedId;
        initComponents();
        setupUI();
        loadUserIDs();
        if (selectedId != null) {
            IDComboBox.setSelectedItem(selectedId);
        }
        loadUserData((String) IDComboBox.getSelectedItem());
        currentUserId = (String) IDComboBox.getSelectedItem();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void setupUI() {
        titleLabel.setText("Delete " + selectedRole);
        
        // Show/Hide specialty row based on type
        boolean isDoctor = "doctor".equalsIgnoreCase(selectedRole);
        specialtyLabel.setVisible(isDoctor);
        specialtyField.setVisible(isDoctor);
    }
    
    private void loadUserIDs() {
        IDComboBox.removeAllItems();
        try {
            List<String> ids = switch (selectedRole.toLowerCase()) {
                case "customer" -> customerService.userIdList();
                case "doctor"   -> doctorService.userIdList();
                case "manager"  -> managerService.userIdList();
                case "staff"    -> staffService.userIdList();
                default         -> java.util.Collections.emptyList();
            };
            Utils.updateComboBox(IDComboBox, ids);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading user IDs: " + e.getMessage());
        }
    }
    
    private void loadUserData(String id) {
        if (id == null) return;
        try {
            User user = switch (selectedRole.toLowerCase()) {
                case "customer" -> customerService.getById(id);
                case "doctor"   -> doctorService.getById(id);
                case "manager"  -> managerService.getById(id);
                case "staff"    -> staffService.getById(id);
                default         -> null;
            };
            if (user == null) return;

            nameField.setText(user.getName());
            ICField.setText(user.getIdentityNumber());
            phoneField.setText(user.getPhone());
            emailField.setText(user.getEmail());
            addressField.setText(user.getAddress());
            if (user instanceof Doctor d) {
                specialtyField.setText(d.getSpecialty());
            } else {
                specialtyField.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage());
        }
    }
    
    private void deleteAndSaveUser() {
        String id = (String) IDComboBox.getSelectedItem();
        if (id == null) return;

        User toRemove = switch (selectedRole.toLowerCase()) {
            case "customer" -> customerService.getById(id);
            case "doctor" -> doctorService.getById(id);
            case "manager" -> managerService.getById(id);
            case "staff" -> staffService.getById(id);
            default -> null;
        };
        if (toRemove == null) {
            JOptionPane.showMessageDialog(this, "User not found!");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + selectedRole + ": " + toRemove.getName() + "?",
                "Confirm Delete", JOptionPane.OK_CANCEL_OPTION);

        if (choice != JOptionPane.OK_OPTION) return;

        boolean ok = switch (selectedRole.toLowerCase()) {
            case "customer" -> customerService.deleteCustomer(id);
            case "doctor" -> doctorService.deleteDoctor(id);
            case "manager" -> managerService.deleteManager(id);
            case "staff" -> staffService.delete(id);
            default -> false;
        };
        if (ok) {
            JOptionPane.showMessageDialog(this, selectedRole + " deleted successfully!");
            loadUserIDs();
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed.");
        }   
    }
    
    // i think this not need, you see see delete maa
//    private void clearForm() {
//        nameLabel.setText("");
//        ICLabel.setText("");
//        addressLabel.setText("");
//        phoneLabel.setText("");
//        emailLabel.setText("");
//        specialtyField.setText("");
//    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        deleteBtn = new javax.swing.JButton();
        IDComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        specialtyLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        addressField = new javax.swing.JTextArea();
        nameField = new javax.swing.JTextField();
        ICField = new javax.swing.JTextField();
        phoneField = new javax.swing.JTextField();
        emailField = new javax.swing.JTextField();
        specialtyField = new javax.swing.JTextField();

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Name:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("IC Number:");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(245, 243, 238));

        titleLabel.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        titleLabel.setText("Delete Manager");

        deleteBtn.setBackground(new java.awt.Color(0, 102, 153));
        deleteBtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setText("Done");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        IDComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        IDComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDComboBoxActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("System ID:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Name:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("IC Number:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Address:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Phone Number:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Email:");

        specialtyLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        specialtyLabel.setText("Specialty:");

        addressField.setEditable(false);
        addressField.setColumns(20);
        addressField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addressField.setLineWrap(true);
        addressField.setRows(5);
        addressField.setText("Example address");
        addressField.setWrapStyleWord(true);
        addressField.setOpaque(false);
        jScrollPane1.setViewportView(addressField);

        nameField.setEditable(false);

        ICField.setEditable(false);

        phoneField.setEditable(false);

        emailField.setEditable(false);

        specialtyField.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(titleLabel))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(specialtyLabel)
                            .addComponent(jLabel8))
                        .addGap(56, 56, 56)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(IDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(phoneField)
                                    .addGap(185, 185, 185)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(ICField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                .addComponent(nameField, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(specialtyField, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(264, 264, 264)
                        .addComponent(deleteBtn)))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(IDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(ICField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(specialtyLabel)
                    .addComponent(specialtyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        deleteBtn.setName("contentSecondary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel2.setName("contentSecondary");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        deleteAndSaveUser();
        this.dispose();
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void IDComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDComboBoxActionPerformed
        // TODO add your handling code here:
        String Id = (String) IDComboBox.getSelectedItem();
        loadUserData(Id);
    }//GEN-LAST:event_IDComboBoxActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new deleteUser().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ICField;
    private javax.swing.JComboBox<String> IDComboBox;
    private javax.swing.JTextArea addressField;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField phoneField;
    private javax.swing.JTextField specialtyField;
    private javax.swing.JLabel specialtyLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
