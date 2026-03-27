/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package javaproj.GUI.Manager;

import javaproj.Methods.User.CustomerService;
import javaproj.Methods.User.StaffService;
import javaproj.Methods.User.DoctorService;
import javaproj.Methods.User.ManagerService;
import javaproj.Model.Role.User;
import javaproj.Model.Role.Doctor;
import javaproj.Utils.*;

import java.util.*;
import javaproj.Model.Role.Manager;
import javaproj.Model.Role.Supermanager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
/**
 *
 * @author mayvi
 */
public class UserManagementPanel extends javax.swing.JPanel {
    
    private final CustomerService customerSvc = new CustomerService();
    private final DoctorService   doctorSvc   = new DoctorService();
    private final StaffService    staffSvc    = new StaffService();
    private final ManagerService  managerSvc  = new ManagerService();
    
    private JPanel contentPanel;
    private PanelController controller;
    private TableRowSorter<DefaultTableModel> sorter;
    
    private final User loggedInManager;
    private final boolean canManageManagers; 


    
    String[] columnNames = {"ID","Name","Ic Number","Address","Contact","Email","Password"};
    private DefaultTableModel model = new DefaultTableModel(columnNames, 0){
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    };

    public UserManagementPanel(PanelController controller, User loggedInManager) {
        this.controller = controller;
        this.loggedInManager = loggedInManager;
        this.canManageManagers = (loggedInManager instanceof Supermanager);
        initComponents();
        
        setupUI();
    }
    
    private void setupUI() {
        roleComboBox.removeAllItems();
        if (canManageManagers) roleComboBox.addItem("Manager"); // only for Supermanager
        roleComboBox.addItem("Staff");
        roleComboBox.addItem("Doctor");
        roleComboBox.addItem("Customer");
        
        if (roleComboBox.getItemCount() > 0) {
            roleComboBox.setSelectedIndex(0);
            updateTableForRole(roleComboBox.getSelectedItem().toString().toLowerCase());
        }
        
        sorter = new TableRowSorter<>(model);
        userTable.setRowSorter(sorter);
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }

        private void filter() {
            String keyword = searchField.getText().trim();
            if (keyword.length() == 0) {
                sorter.setRowFilter(null); // show all
            } else {
                // Search in Name (col 1) and IC (col 2)
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 1, 2));
            }
        }
        });
    }
    
    private void updateTableForRole(String role) {
        if (role == null) return;
        switch (role.toLowerCase()) {
            case "manager": {
                if (!canManageManagers) return;
                model.setColumnIdentifiers(new String[]{"ID","Name","IC","Address","Phone","Email","Password"});
                List<? extends User> list = managerSvc.list();
                Utils.refreshTable(userTable, list, u -> new Object[]{
                    u.getSystemId(), u.getName(), u.getIdentityNumber(),
                    u.getAddress(), u.getPhone(), u.getEmail(), u.getPassword()
                });
            break;
            }
            case "staff": {
                model.setColumnIdentifiers(new String[]{"ID","Name","IC","Address","Phone","Email","Password"});
                List<? extends User> list = staffSvc.list();
                Utils.refreshTable(userTable, list, u -> new Object[]{
                    u.getSystemId(), u.getName(), u.getIdentityNumber(),
                    u.getAddress(), u.getPhone(), u.getEmail(), u.getPassword()
                });
                break;

            }
            case "doctor": {
                model.setColumnIdentifiers(new String[]{"ID","Name","IC","Address","Phone","Email","Specialty","Password"});
                List<Doctor> list = doctorSvc.doctors(); // typed helper we added
                Utils.refreshTable(userTable, list, d -> new Object[]{
                    d.getSystemId(), d.getName(), d.getIdentityNumber(),
                    d.getAddress(), d.getPhone(), d.getEmail(), d.getSpecialty(), d.getPassword()
                });
                break;
            }
            case "customer": {
                model.setColumnIdentifiers(new String[]{"ID","Name","IC","Address","Phone","Email","Password"});
                List<? extends User> list = customerSvc.list();
                Utils.refreshTable(userTable, list, u -> new Object[]{
                    u.getSystemId(), u.getName(), u.getIdentityNumber(),
                    u.getAddress(), u.getPhone(), u.getEmail(), u.getPassword()
                });
                break;
            }
            default:
        }

        Utils.resizeColumnWidth(userTable);
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
        userTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        roleComboBox = new javax.swing.JComboBox<>();
        addBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        resetBtn = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        userTable.setModel(model);
        jScrollPane1.setViewportView(userTable);

        jLabel1.setBackground(new java.awt.Color(245, 243, 238));
        jLabel1.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("User Management System");

        roleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manager", "Staff", "Doctor", "Customer" }));
        roleComboBox.setOpaque(true);
        roleComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roleComboBoxActionPerformed(evt);
            }
        });

        addBtn.setBackground(new java.awt.Color(0, 102, 153));
        addBtn.setForeground(new java.awt.Color(255, 255, 255));
        addBtn.setText("Add");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        editBtn.setBackground(new java.awt.Color(0, 102, 153));
        editBtn.setForeground(new java.awt.Color(255, 255, 255));
        editBtn.setText("Edit");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        deleteBtn.setBackground(new java.awt.Color(0, 102, 153));
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("Search (Name/IC):");

        resetBtn.setText("Reset");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                            .addComponent(backBtn)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resetBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addBtn)
                        .addGap(90, 90, 90)
                        .addComponent(editBtn)
                        .addGap(90, 90, 90)
                        .addComponent(deleteBtn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(backBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resetBtn)
                    .addComponent(searchField)
                    .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBtn)
                    .addComponent(editBtn)
                    .addComponent(deleteBtn))
                .addGap(19, 19, 19))
        );

        addBtn.setName("contentSecondary");
        editBtn.setName("contentSecondary");
        deleteBtn.setName("contentSecondary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void roleComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roleComboBoxActionPerformed
        // TODO add your handling code here:
        Object selectedRole = roleComboBox.getSelectedItem();
        if (selectedRole != null) {
            String role = selectedRole.toString().toLowerCase();
            updateTableForRole(role);
        }
    }//GEN-LAST:event_roleComboBoxActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        // TODO add your handling code here:
        String roleChosen = roleComboBox.getSelectedItem().toString();
        addUser addFrame = new addUser(roleChosen);
        
        // Set logged-in manager
        addFrame.setLoggedInManager((Manager) loggedInManager);
        
        addFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Reload data and refresh table
                updateTableForRole(roleChosen.toLowerCase());
            }   
        });
        addFrame.setVisible(true);
    }//GEN-LAST:event_addBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
        String roleChosen = roleComboBox.getSelectedItem().toString();
        
        String selectedId = userTable.getValueAt(0,0).toString();
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1){
            selectedId = userTable.getValueAt(selectedRow,0).toString();
        }
        
        editUser editFrame = new editUser(roleChosen, selectedId);
        
        // Set logged-in manager
        editFrame.setLoggedInManager((Manager) loggedInManager);
        
        editFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Reload data and refresh table
                updateTableForRole(roleChosen.toLowerCase());
            }   
        });
        editFrame.setVisible(true);
    }//GEN-LAST:event_editBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        String roleChosen = roleComboBox.getSelectedItem().toString();
        String selectedId = userTable.getValueAt(0,0).toString();
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1){
            selectedId = userTable.getValueAt(selectedRow,0).toString();
        }
        deleteUser delete = new deleteUser(roleChosen,selectedId);
        
        delete.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Reload data and refresh table
                updateTableForRole(roleChosen.toLowerCase());
            }   
        });
        delete.setVisible(true);
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        // TODO add your handling code here:
        searchField.setText("");
        sorter.setRowFilter(null); // reset filter
    }//GEN-LAST:event_resetBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("home");
    }//GEN-LAST:event_backBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton backBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton resetBtn;
    private javax.swing.JComboBox<String> roleComboBox;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
