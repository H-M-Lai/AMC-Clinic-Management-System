/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package javaproj.GUI.Staff;

import java.util.*;
import javaproj.Methods.Items.MedicationService;
import javaproj.Methods.Items.ServiceService;
import javaproj.Model.Item;
import javaproj.Model.Medication;
import javaproj.Model.Service;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author NICK
 */
public class EditItem extends javax.swing.JFrame {
    private String type, currentId;
    private boolean isLoading = false;
    private Item original;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EditItem.class.getName());
    
    private final MedicationService medicationService = new MedicationService();
    private final ServiceService    serviceService    = new ServiceService();
    
    private boolean isMedication() { return "Medication".equalsIgnoreCase(type); }
    private boolean isService()    { return "Service".equalsIgnoreCase(type); }
    
    private void updateForm(String selectedItem){
        isLoading = true;
        try {
            this.type = selectedItem; // normalize incoming type
            List<String> ids;
            List<String> categories;

            if (isMedication()) {
                ids = medicationService.ids();
                categories = medicationService.categories(); // e.g. therapeutic classes
            } else {
                ids = serviceService.ids();
                categories = serviceService.categories();    // e.g. service types
            }

            IDComboBox.setModel(new DefaultComboBoxModel<>(ids.toArray(new String[0])));
            categoryComboBox.setModel(new DefaultComboBoxModel<>(categories.toArray(new String[0])));

            if (!ids.isEmpty()) {
                IDComboBox.setSelectedIndex(0);
                currentId = ids.get(0);
            }
            if (!categories.isEmpty()) {
                categoryComboBox.setSelectedIndex(0);
            }

            titleLabel.setText("Edit " + (isMedication() ? "Medication" : "Service"));
        } finally {
            isLoading = false;
        }
    }
    private void updateAndSaveItem(String selectedItem) {
        if (isLoading) return;
        String selectedId = (String) IDComboBox.getSelectedItem();
        if (selectedId == null) return;

        if (!validateInput()) return;

        String name  = nameInput.getText().trim();
        double price = Double.parseDouble(unitPriceInput.getText().trim());
        String category   = String.valueOf(categoryComboBox.getSelectedItem());
        String desc  = descriptionInput.getText().trim();

        boolean ok = false;
        if (isMedication()) {
            ok = medicationService.updateMedication(selectedId, name, desc, price, category);
        } else {
            ok = serviceService.updateService(selectedId, name, desc, price, category);
        }

        if (ok) {
            JOptionPane.showMessageDialog(this, "Saved successfully.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save (ID not found).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadData(String type, String id){
        if (id == null || id.isEmpty()) return;
        isLoading = true;
        try {
            currentId = id;
            if ("Medication".equalsIgnoreCase(type)) {
                Medication m = medicationService.loadMedication(id);
                original = m;
                if (m == null) return;
                IDComboBox.setSelectedItem(m.getId());
                nameInput.setText(m.getName());
                unitPriceInput.setText(String.format(Locale.ROOT, "%.2f", m.getUnitPrice()));
                categoryComboBox.setSelectedItem(m.getCategory());
                descriptionInput.setText(m.getDescription());
            } else if ("Service".equalsIgnoreCase(type)) {
                Service s = serviceService.loadService(id);
                original = s;
                if (s == null) return;
                IDComboBox.setSelectedItem(s.getId());
                nameInput.setText(s.getName());
                unitPriceInput.setText(String.format(Locale.ROOT, "%.2f", s.getUnitPrice()));
                categoryComboBox.setSelectedItem(s.getCategory());
                descriptionInput.setText(s.getDescription());
            }
        } finally {
            isLoading = false;
        }
    }
    private boolean isFormChanged(){
        if (original == null) return false;
        String name = nameInput.getText().trim();
        String priceTxt = unitPriceInput.getText().trim();
        String category = String.valueOf(categoryComboBox.getSelectedItem());
        String desc = descriptionInput.getText().trim();

        double price;
        try { price = Double.parseDouble(priceTxt); } catch (Exception e) { price = original.getUnitPrice(); }

        if (!Objects.equals(original.getName(), name)) return true;
        if (Double.compare(original.getUnitPrice(), price) != 0) return true;
        if (!Objects.equals(original.getDescription(), desc)) return true;

        if (original instanceof Medication m) {
            return !Objects.equals(m.getCategory(), category);
        } else if (original instanceof Service s) {
            return !Objects.equals(s.getCategory(), category);
        }
        return false;
    }
    private boolean validateInput(){
        String name = nameInput.getText().trim();
        String unitPrice = unitPriceInput.getText().trim();
        String category = categoryComboBox.getSelectedItem().toString();
        
        if (name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        double price;
        try {
            price = Double.parseDouble(unitPrice);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Unit Price must be a number.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (price < 0) {
            JOptionPane.showMessageDialog(this, "Unit Price cannot be negative.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (category == null || category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a category.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!isFormChanged()) {
            JOptionPane.showMessageDialog(this, "No changes to save.");
            return false;
        }
        return true;
    }
   
    public EditItem() {
        initComponents();
        updateForm("Medication");
        loadData("Medication","X0001");
    }
    public EditItem(String type, String id) {
        this.type = type;
        initComponents();
        updateForm(this.type);
        loadData(this.type,id);
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
        nameInput = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        unitPriceInput = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        editBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        IDComboBox = new javax.swing.JComboBox<>();
        descriptionInput = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        categoryComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 243, 238));

        titleLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        titleLabel.setText("Edit Item");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Name:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Unit Price:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Description:");

        editBtn.setBackground(new java.awt.Color(0, 102, 153));
        editBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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

        descriptionInput.setColumns(20);
        descriptionInput.setRows(5);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Category:");

        categoryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1))
                        .addGap(59, 59, 59)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(unitPriceInput, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                            .addComponent(nameInput, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(categoryComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(descriptionInput, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(45, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(217, 217, 217))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(227, 227, 227))))
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
                    .addComponent(nameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(unitPriceInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(descriptionInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
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

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
        updateAndSaveItem(this.type);
    }//GEN-LAST:event_editBtnActionPerformed

    private void IDComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDComboBoxActionPerformed
        // TODO add your handling code here:
         if (isLoading) return;
         
        Object sel = IDComboBox.getSelectedItem();
        if (sel == null) return;

        if (isFormChanged()) {
            int option = JOptionPane.showConfirmDialog(
                this,
                "You have unsaved changes. Do you want to discard them?",
                "Unsaved Changes",
                JOptionPane.YES_NO_OPTION
            );
            if (option == JOptionPane.NO_OPTION) {
                // Revert selection (wrap with guard so this revert doesn’t fire again)
                isLoading = true;
                try { IDComboBox.setSelectedItem(currentId); }
                finally { isLoading = false; }
                return;
            }
        }

        loadData(type, sel.toString());
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
        java.awt.EventQueue.invokeLater(() -> new EditItem("Medication","X0001").setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> IDComboBox;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JTextArea descriptionInput;
    private javax.swing.JButton editBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nameInput;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField unitPriceInput;
    // End of variables declaration//GEN-END:variables
}
