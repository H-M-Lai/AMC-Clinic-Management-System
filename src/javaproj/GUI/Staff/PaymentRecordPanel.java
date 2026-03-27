package javaproj.GUI.Staff;

import java.time.LocalDate;
import javaproj.Model.Appointment;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import javaproj.Model.Payment;
import javaproj.Repository.AppointmentRepository;
import javaproj.Methods.User.CustomerService;

import javaproj.Utils.PanelController;
import javaproj.Utils.Utils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import javaproj.Methods.Payment.StaffPayment;
import javaproj.Repository.PaymentRepository;

public class PaymentRecordPanel extends javax.swing.JPanel {

    private PanelController controller;
    
    List<Payment> payments;
    private final PaymentRepository paymentRepo = new PaymentRepository();
    private final AppointmentRepository apptRepo = new AppointmentRepository();
    private final CustomerService customerService = new CustomerService();
    private final StaffPayment staffPayment = new StaffPayment(paymentRepo, apptRepo);
    private Map<String, String> customerNameByAppt = Collections.emptyMap();
    
    //initialize table format
    String[] columnNames = {"ID","Customer","Amount","Payment Method","Date","Time","Status"};
    private DefaultTableModel model = new DefaultTableModel(columnNames, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    //setup filter combobox    
    private String status = "All";
    private String method = "All";
    private void setupFilter(){
        List<String> statusBox = new ArrayList<>();
        statusBox.add(status);
        statusBox.addAll(staffPayment.statusBox());
        List<String> methodBox = new ArrayList<>();
        methodBox.add(method);
        methodBox.addAll(staffPayment.methodBox());
        
        statusFilter.setModel( new javax.swing.DefaultComboBoxModel<>(statusBox.toArray(new String[0])));
        methodFilter.setModel( new javax.swing.DefaultComboBoxModel<>(methodBox.toArray(new String[0])));
    }
    private void rebuildApptCustomerMap() {
        List<Appointment> appts = apptRepo.findAll();
        Map<String, String> nameMap = customerService.nameMap();

        Map<String, String> m = new HashMap<>();
        for (Appointment a : appts) {
            String customerName = nameMap.getOrDefault(a.getCustomerId(), "(unknown)");
            m.put(a.getAppointmentId(), customerName);
        }
        customerNameByAppt = m;
    }
    private void applyFilter(){
        if (payments == null) return;
        LocalDate date = dateFilter.getDate();
        String q = (searchField.getText() == null) ? "" : searchField.getText().trim().toLowerCase();

        // 1) filter by status/method/date/paymentId
        List<Payment> base = staffPayment.filter(payments, "", status, method, date);
        
         List<Payment> filtered = new ArrayList<>();
        for (Payment p : base) {
            String name = customerNameByAppt.getOrDefault(p.getAppointmentId(), "(unknown)").toLowerCase();
            boolean matches = q.isEmpty()
                    || name.contains(q)
                    || p.getPaymentId().toLowerCase().contains(q);
            if (matches) filtered.add(p);
        }
        
        Utils.refreshTable(jTable1,filtered,p -> new Object[] {
            p.getPaymentId(),
            //p.getAppointmentId(),
            customerNameByAppt.getOrDefault(p.getAppointmentId(), "(unknown)"),
            p.getAmount(),
            p.getPaymentMethod().toString(),
            p.getDate(),
            p.getTime(),
            p.getStatus().toString()
            }
        );
        //tuneColumns();
    }
    private void addLiveSearch() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilter(); }
            public void removeUpdate(DocumentEvent e) { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });
    }

    //refresh
    private void reloadTxt(){
        payments = paymentRepo.findAll();
    }
    private void reloadAndRefresh() {
    // Make sure the maps and list are up to date
        reloadTxt(); 
        rebuildApptCustomerMap();
        statusFilter.setSelectedIndex(0);
        methodFilter.setSelectedIndex(0);
        // Render using refreshTable
        Utils.refreshTable(jTable1,payments,p -> new Object[] {
            p.getPaymentId(),
            //p.getAppointmentId(),
            customerNameByAppt.getOrDefault(p.getAppointmentId(), "(unknown)"),
            p.getAmount(),
            p.getPaymentMethod().toString(),
            p.getDate(),
            p.getTime(),
            p.getStatus().toString()
            }
        );
        //tuneColumns();
        }
    
    public PaymentRecordPanel(PanelController controller) {
        this.controller = controller;
        model.setColumnIdentifiers(columnNames);
       
        initComponents();
        rebuildApptCustomerMap();
        setupFilter();
        reloadAndRefresh();
        addLiveSearch();
    }
    @Override
    public void setVisible(boolean flag) {
        super.setVisible(flag);
        if (flag) {
            reloadAndRefresh();
            rebuildApptCustomerMap();
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        searchField = new javax.swing.JTextField();
        resetBtn = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();
        methodFilter = new javax.swing.JComboBox<>();
        statusFilter = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dateFilter = new com.github.lgooddatepicker.components.DatePicker();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        jPanel1.setBackground(java.awt.Color.white);

        jTable1.setModel(model);
        jTable1.setAutoscrolls(false);
        jScrollPane1.setViewportView(jTable1);

        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });

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

        methodFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        methodFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                methodFilterActionPerformed(evt);
            }
        });

        statusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        statusFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusFilterActionPerformed(evt);
            }
        });

        jLabel1.setText("Status:");
        jLabel1.setForeground(java.awt.Color.black);

        jLabel2.setText("Payment Method:");
        jLabel2.setForeground(java.awt.Color.black);

        jLabel3.setText("Payment Record");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        jLabel4.setText("Name:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resetBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(statusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(methodFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9))
                            .addComponent(jScrollPane1))
                        .addGap(36, 36, 36))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(backBtn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(backBtn)
                .addGap(11, 11, 11)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(methodFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(statusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(dateFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(resetBtn)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        dateFilter.addDateChangeListener(e -> {applyFilter();});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 864, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 496, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("payment");
    }//GEN-LAST:event_backBtnActionPerformed

    private void statusFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusFilterActionPerformed
        // TODO add your handling code here:
        status = statusFilter.getSelectedItem().toString();
        applyFilter();
    }//GEN-LAST:event_statusFilterActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        // TODO add your handling code here:
        searchField.setText("");
        statusFilter.setSelectedIndex(0);
        methodFilter.setSelectedIndex(0);
        dateFilter.clear();
        reloadAndRefresh();
    }//GEN-LAST:event_resetBtnActionPerformed

    private void methodFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_methodFilterActionPerformed
        // TODO add your handling code here:
        method = methodFilter.getSelectedItem().toString();
        applyFilter();
    }//GEN-LAST:event_methodFilterActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private com.github.lgooddatepicker.components.DatePicker dateFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> methodFilter;
    private javax.swing.JButton resetBtn;
    private javax.swing.JTextField searchField;
    private javax.swing.JComboBox<String> statusFilter;
    // End of variables declaration//GEN-END:variables
}
