package javaproj.GUI.Manager;

import java.util.List;
import javaproj.Model.Role.Doctor;
import javaproj.Model.Role.User;
import javaproj.Model.Role.Customer;
import javaproj.Model.Role.Staff;
import javaproj.Model.Role.Manager;
import javaproj.Methods.User.CheckDuplicates;
import javaproj.Methods.User.CustomerService;
import javaproj.Methods.User.DoctorService;
import javaproj.Methods.User.ManagerService;
import javaproj.Methods.User.StaffService;
import javaproj.Utils.*;


import javax.swing.*;

public class editUser extends javax.swing.JFrame {
    
    private final CustomerService customerService = new CustomerService();
    private final DoctorService doctorService = new DoctorService();
    private final ManagerService managerService = new ManagerService();
    private final StaffService staffService = new StaffService();
    
    private final CheckDuplicates checker = new CheckDuplicates();
    private String selectedRole;
    private String selectedId;
    private String currentUserId; 
    private boolean isLoading = false;
    private User currentUserSnapshot;
    private Manager loggedInManager;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(editUser.class.getName());


    public editUser() {
        initComponents();
    }
    
    public editUser(String selectedRole, String selectedId) {
        this.selectedRole = selectedRole;
        this.selectedId = selectedId;
        initComponents();
        setupRoleField();
        // load all user IDs into ComboBox
        loadUserIDs();
        updateSpecialtyBox();
        
        // ensure combo box points to the selected ID
        if (selectedId != null) {
            IDComboBox.setSelectedItem(selectedId);
        }
        
        loadUserData(selectedId);
        currentUserId = selectedId;  // set current user ID here
            
//        // load first user automatically
//        if (IDComboBox.getItemCount() > 0) {
//            String firstId = (String) IDComboBox.getItemAt(0);
//            loadUserData(firstId);
//            currentUserId = firstId;  // set current user ID here
//        }
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
    }
    public void setLoggedInManager(Manager manager) {
        this.loggedInManager = manager;
    }
    // Setup panel title and specialty visibility
    private void setupRoleField() {
        titleLabel.setText("Edit " + capitalize(selectedRole));

        // Show/Hide specialty row based on type
        boolean isDoctor = "doctor".equalsIgnoreCase(selectedRole);
        specialtyLabel.setVisible(isDoctor);
        specialtyBox.setVisible(isDoctor);
    }
    private void updateSpecialtyBox(){
        List<String> specialty = ReturnList.get("specialty");
        Utils.updateComboBox(specialtyBox,specialty);
    }
    private void loadUserIDs() {
 IDComboBox.removeAllItems();
        try {
            java.util.List<String> ids = switch (selectedRole.toLowerCase()) {
                case "customer" -> customerService.userIdList();
                case "doctor" -> doctorService.userIdList();
                case "manager" -> managerService.userIdList();
                case "staff" -> staffService.userIdList();
                default -> java.util.Collections.emptyList();
            };
            Utils.updateComboBox(IDComboBox, ids);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading user IDs: " + e.getMessage());
        }
    }
    
    private void loadUserData(String selectedId) {
        if (selectedId == null) return;
        isLoading = true;
        try {
            User user = switch (selectedRole.toLowerCase()) {
                case "customer" -> customerService.getById(selectedId);
                case "doctor" -> doctorService.getById(selectedId);
                case "manager" -> managerService.getById(selectedId);
                case "staff" -> staffService.getById(selectedId);
                default -> null;
            };
            if (user == null) return;

            nameField.setText(user.getName());
            ICField.setText(user.getIdentityNumber());
            phoneField.setText(user.getPhone());
            emailField.setText(user.getEmail());
            addressArea.setText(user.getAddress());
            if (user instanceof Doctor d) {
                specialtyBox.setSelectedItem(d.getSpecialty());
            } else {
                specialtyBox.setSelectedIndex(0);
            }
            currentUserSnapshot = user;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage());
        }
        isLoading = false;
    }
    
    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }


    private boolean isFormChanged() {
        if (isLoading || currentUserSnapshot == null) return false;

        if (currentUserSnapshot instanceof Manager m) {
            return !nameField.getText().trim().equals(m.getName().trim()) ||
                   !ICField.getText().trim().equals(m.getIdentityNumber().trim()) ||
                   !phoneField.getText().trim().equals(m.getPhone().trim()) ||
                   !emailField.getText().trim().equals(m.getEmail().trim()) ||
                   !addressArea.getText().trim().equals(m.getAddress().trim());
        } else if (currentUserSnapshot instanceof Staff s) {
            return !nameField.getText().trim().equals(s.getName().trim()) ||
                   !ICField.getText().trim().equals(s.getIdentityNumber().trim()) ||
                   !phoneField.getText().trim().equals(s.getPhone().trim()) ||
                   !emailField.getText().trim().equals(s.getEmail().trim()) ||
                   !addressArea.getText().trim().equals(s.getAddress().trim());
        } else if (currentUserSnapshot instanceof Doctor d) {
            return !nameField.getText().trim().equals(d.getName().trim()) ||
                   !ICField.getText().trim().equals(d.getIdentityNumber().trim()) ||
                   !phoneField.getText().trim().equals(d.getPhone().trim()) ||
                   !emailField.getText().trim().equals(d.getEmail().trim()) ||
                   !addressArea.getText().trim().equals(d.getAddress().trim()) ||
                   !specialtyBox.getSelectedItem().toString().trim().equals(d.getSpecialty().trim());
        } else if (currentUserSnapshot instanceof Customer c) {
            return !nameField.getText().trim().equals(c.getName().trim()) ||
                   !ICField.getText().trim().equals(c.getIdentityNumber().trim()) ||
                   !phoneField.getText().trim().equals(c.getPhone().trim()) ||
                   !emailField.getText().trim().equals(c.getEmail().trim()) ||
                   !addressArea.getText().trim().equals(c.getAddress().trim());
        }
        return false;
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
        jLabel7 = new javax.swing.JLabel();
        IDComboBox = new javax.swing.JComboBox<>();
        specialtyLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        addressArea = new javax.swing.JTextArea();
        specialtyBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 243, 238));

        titleLabel.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        titleLabel.setText("Edit Manager");

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

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("System ID:");

        IDComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "001", "002", "003" }));
        IDComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDComboBoxActionPerformed(evt);
            }
        });

        specialtyLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        specialtyLabel.setText("Specialty:");

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
                        .addGap(168, 168, 168)
                        .addComponent(IDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(specialtyLabel)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(59, 59, 59)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(emailField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phoneField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                            .addComponent(ICField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(specialtyBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(241, 241, 241)
                        .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(221, 221, 221))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(ICField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(specialtyLabel)
                    .addComponent(specialtyBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        editBtn.setName("contentSecondary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        String role = (selectedRole == null ? "" : selectedRole.trim().toLowerCase());

        // 1) Validate via the correct service (expect String error or null)
        
        String error = switch (role) {
            case "customer" -> customerService.validateForm(
                    nameField.getText().trim(),
                    ICField.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            case "doctor" -> doctorService.validateForm(
                    nameField.getText().trim(),
                    ICField.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    specialtyBox.getSelectedItem().toString().trim()
            );
            case "manager" -> managerService.validateForm(
                    loggedInManager,                 // actor (for permissions if needed)
                    currentUserId,                   // editing this ID (skip-self in uniqueness)
                    role,
                    nameField.getText().trim(),
                    ICField.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    specialtyBox.getSelectedItem().toString().trim()  // ignored for non-doctors inside service
            );
            case "staff" -> staffService.validateForm(
                    nameField.getText().trim(),
                    ICField.getText().trim(),
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
        
        String dup = checker.checkDuplicates(IDComboBox.getSelectedItem().toString(), ICField.getText().trim(), emailField.getText().trim());
        if (dup != null) {
            JOptionPane.showMessageDialog(this, dup, "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // 2) Update via role-specific service
        boolean ok = switch (role) {
            case "customer" -> customerService.update(
                    currentUserId,
                    nameField.getText().trim(),
                    ICField.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            case "doctor" -> doctorService.update(
                    currentUserId,
                    nameField.getText().trim(),
                    ICField.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    specialtyBox.getSelectedItem().toString().trim()
            );
            case "manager" -> managerService.updateSelfRole(
                    currentUserId,
                    nameField.getText().trim(),
                    ICField.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            case "staff" -> staffService.update(
                    currentUserId,
                    nameField.getText().trim(),
                    ICField.getText().trim(),
                    addressArea.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            default -> false;
        };

        if (ok) {
            JOptionPane.showMessageDialog(this, capitalize(role) + " updated successfully!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    }//GEN-LAST:event_editBtnActionPerformed

    private void IDComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDComboBoxActionPerformed
        // TODO add your handling code here:
        if (isLoading) return; // skip when loading data

        String newId = (String) IDComboBox.getSelectedItem();
        if (newId == null || newId.equals(currentUserId)) return;

        // Only check changes if we already had a loaded user
        if (currentUserId != null && isFormChanged()) {
            int option = JOptionPane.showConfirmDialog(
                this,
                "You have unsaved changes. Do you want to discard them?",
                "Unsaved Changes",
                JOptionPane.YES_NO_OPTION
            );
            if (option == JOptionPane.NO_OPTION) {
                // revert to previous selection
                IDComboBox.setSelectedItem(currentUserId);
                return;
            }
        }

        // load new user and update current ID
        loadUserData(newId);
        currentUserId = newId;
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
        java.awt.EventQueue.invokeLater(() -> new editUser().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ICField;
    private javax.swing.JComboBox<String> IDComboBox;
    private javax.swing.JTextArea addressArea;
    private javax.swing.JButton editBtn;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField phoneField;
    private javax.swing.JComboBox<String> specialtyBox;
    private javax.swing.JLabel specialtyLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
