/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package javaproj.GUI.Common;

import javaproj.Methods.User.SuperManagerService;
import javaproj.Methods.User.CustomerService;
import javaproj.Methods.User.CheckDuplicates;
import javaproj.Methods.User.StaffService;
import javaproj.Methods.User.DoctorService;
import javaproj.Methods.User.ManagerService;
import java.util.List;
import javaproj.GUI.Credentials.Session;
import javaproj.Model.Role.*;
import javaproj.Utils.ReturnList;
import javaproj.Utils.Utils;

import javax.swing.*;
//import javaproj.Services.UserService;

/**
 *
 * @author mayvi
 */
public class EditProfile extends javax.swing.JFrame {
    
    private final CustomerService customerSvc = new CustomerService();
    private final StaffService staffSvc    = new StaffService();
    private final ManagerService managerSvc  = new ManagerService();
    private final SuperManagerService superSvc    = new SuperManagerService();
    private final DoctorService doctorSvc   = new DoctorService();
    CheckDuplicates checker = new CheckDuplicates();
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EditProfile.class.getName());
    User userinfo = Session.getCurrentUser();
    /**
     * Creates new form editUser
     */
    public EditProfile() {
        initComponents();
        boolean isDoctor = (userinfo instanceof Doctor);
        specialtyLabel.setVisible(isDoctor);
        specialtyBox.setVisible(isDoctor);

        loadForm();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    // Setup panel title and specialty visibility
    

    private void loadForm() {
        nameField.setText(userinfo.getName());
        ICField.setText(userinfo.getIdentityNumber());
        addressArea.setText(userinfo.getAddress());
        phoneField.setText(userinfo.getPhone());
        emailField.setText(userinfo.getEmail());
        updateSpecialtyBox();
        if(userinfo instanceof Doctor d){
            specialtyBox.setSelectedItem(d.getSpecialty());
        }
        
    }
    
    private void updateSpecialtyBox(){
        List<String> specialty = ReturnList.get("specialty");
        Utils.updateComboBox(specialtyBox,specialty);
    }
    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
    
    private void updateAndSaveUser() {
        if (userinfo == null) {
            JOptionPane.showMessageDialog(this, "No user is logged in.");
            return;
        }

        final String id      = userinfo.getSystemId();
        final String name    = nameField.getText().trim();
        final String ic      = ICField.getText().trim();
        final String address = addressArea.getText().trim();
        final String phone   = phoneField.getText().trim();
        final String email   = emailField.getText().trim();

        boolean ok = false;

        try {
            if (userinfo instanceof Doctor) {
                String specialty = specialtyBox.getSelectedItem().toString().trim();
                ok = doctorSvc.update(id, name, ic, address, phone, email, specialty);
                if (ok) Session.setCurrentUser(doctorSvc.getById(id));
            } 
            else if (userinfo instanceof Staff) {
                ok = staffSvc.update(id, name, ic, address, phone, email);
                if (ok) Session.setCurrentUser(staffSvc.getById(id));
            } 
            else if (userinfo instanceof Supermanager sm) {  // <-- moved ABOVE Manager
                String preservedPwd = sm.getPassword();
                Supermanager updated = new Supermanager(id, name, ic, address, phone, email, preservedPwd);
                superSvc.save(updated);
                ok = true;
                Session.setCurrentUser(superSvc.getById(id));
            }
            else if (userinfo instanceof Manager) {
                ok = managerSvc.updateSelfRole(id, name, ic, address, phone, email);
                if (ok) Session.setCurrentUser(managerSvc.getById(id));
            }
            else if (userinfo instanceof Customer) {
                ok = customerSvc.update(id, name, ic, address, phone, email);
                if (ok) Session.setCurrentUser(customerSvc.getById(id));
            }
            
        } catch (Exception e) {
            ok = false;
            JOptionPane.showMessageDialog(this, "Error saving user: " + e.getMessage());
        }

        if (!ok) {
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    }

     
    private boolean validateForm() {
        String id = Session.getCurrentUser().getSystemId();
        String name    = nameField.getText().trim();
        String ic      = ICField.getText().trim();
        String address = addressArea.getText().trim();
        String phone   = phoneField.getText().trim();
        String email   = emailField.getText().trim();
        
        String error = null;
        if (userinfo instanceof Doctor) {
            String specialty = specialtyBox.getSelectedItem().toString().trim();
            error = doctorSvc.validateForm(name, ic, address, phone, email, specialty);
        } 
        else if (userinfo instanceof Staff) {
            error = staffSvc.validateBasicFields(name, ic, address, phone, email);
        }
        else if (userinfo instanceof Supermanager) {
            error = managerSvc.validateBasicFields(name, ic, address, phone, email);
        }
        else if (userinfo instanceof Manager) {
            error = superSvc.validateBasicFields(name, ic, address, phone, email);
        }
        else if (userinfo instanceof Customer) {
            error = customerSvc.validateBasicFields(name, ic, address, phone, email);
        }
        if (error == null) error = checker.checkDuplicates(id, ic, email);
        
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
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
        titleLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        ICField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        phoneField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        editBtn = new javax.swing.JButton();
        specialtyLabel = new javax.swing.JLabel();
        addressArea = new javax.swing.JTextArea();
        profilePic = new javax.swing.JButton();
        specialtyBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 243, 238));

        titleLabel.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        titleLabel.setText("Edit Profile");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Name:");

        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("IC Number:");

        ICField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ICFieldActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Address:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Phone Number:");

        phoneField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneFieldActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Email:");

        emailField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailFieldActionPerformed(evt);
            }
        });

        editBtn.setBackground(new java.awt.Color(0, 102, 153));
        editBtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        editBtn.setForeground(new java.awt.Color(255, 255, 255));
        editBtn.setText("Done");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        specialtyLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        specialtyLabel.setText("Specialty:");

        addressArea.setColumns(20);
        addressArea.setRows(5);

        profilePic.setText("Change Profile Picture");
        profilePic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilePicActionPerformed(evt);
            }
        });

        specialtyBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(221, 221, 221))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(specialtyLabel)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addGap(59, 59, 59)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(profilePic)
                    .addComponent(emailField)
                    .addComponent(ICField)
                    .addComponent(nameField)
                    .addComponent(phoneField)
                    .addComponent(addressArea, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                    .addComponent(specialtyBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(46, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(231, 231, 231))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(ICField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addressArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(specialtyLabel)
                            .addComponent(specialtyBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addComponent(profilePic)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 36, Short.MAX_VALUE))))
        );

        editBtn.setName("contentSecondary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.setName("contentSecondary");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameFieldActionPerformed

    private void ICFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ICFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ICFieldActionPerformed

    private void phoneFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneFieldActionPerformed

    private void emailFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailFieldActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
         // Always validate first
        if (!validateForm()) {
            return; // Stop if validation fails
        }
        
        updateAndSaveUser();
        JOptionPane.showMessageDialog(this, capitalize(Session.getUserRole()) + " updated successfully!");
        this.dispose();
    }//GEN-LAST:event_editBtnActionPerformed

    private void profilePicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilePicActionPerformed
        // TODO add your handling code here:
        UploadProfilePic.upload(userinfo.getSystemId());
    }//GEN-LAST:event_profilePicActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new EditProfile().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ICField;
    private javax.swing.JTextArea addressArea;
    private javax.swing.JButton editBtn;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField phoneField;
    private javax.swing.JButton profilePic;
    private javax.swing.JComboBox<String> specialtyBox;
    private javax.swing.JLabel specialtyLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
