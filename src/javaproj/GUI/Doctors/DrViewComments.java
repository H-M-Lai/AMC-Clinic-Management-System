package javaproj.GUI.Doctors;

import javaproj.Model.Role.*;
import javaproj.Model.*;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import javaproj.Repository.AppointmentRepository;
import javaproj.Repository.FeedbackRepository;
import javaproj.Methods.Appointment.DoctorAppointment;
import javaproj.Methods.User.CustomerService;
import javaproj.Utils.PanelController;

public class DrViewComments extends javax.swing.JPanel {

    private Doctor doctor;
    private PanelController controller;

    private final CustomerService customerService = new CustomerService();
    private final FeedbackRepository feedbackRepo = new FeedbackRepository();
    private final AppointmentRepository apptRepo = new AppointmentRepository();
    private final DoctorAppointment doctorAppt   = new DoctorAppointment(apptRepo);

    
    String[] columnNames = {"Feedback ID", "Appointment ID", "Patient Name", "Rating", "Comment"};
    private DefaultTableModel model = new DefaultTableModel(columnNames, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // all cells uneditable
        }
    };
    
    // Keep original data in memory for filtering
    private final List<Object[]> allRows = new ArrayList<>();
    
    public DrViewComments(Doctor doctor, PanelController controller) {
        this.controller = controller;
        this.doctor = doctor;
        initComponents();
        tbFeedback.setModel(model);
        setupActions();
        loadFeedback();
    }
    
    private void setupActions(){
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
        });
        
        cmbRating.addActionListener(e -> applyFilters());
        cmbDate.addActionListener(e -> applyFilters());

        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            cmbRating.setSelectedIndex(0);
            cmbDate.setSelectedIndex(0);
            applyFilters();
        });
    }
    
    private void loadFeedback(){
        List<Feedback> feedbacks = feedbackRepo.findAll();
        List<Appointment> appointments = doctorAppt.appointmentList(doctor.getSystemId());
        Map<String, String> customerNameMap = customerService.nameMap();

        
        // Map appointmentId -> patientName + date
        Set<String> doctorAppointmentIds = new HashSet<>();
        Map<String, String> apptToCustomer = new HashMap<>();
        Map<String, String> apptToDate = new HashMap<>();
        
        apptToDate.clear();
        for (Appointment appt : appointments) {
                    if (Objects.equals(appt.getDoctorId(), doctor.getSystemId())) {
                        doctorAppointmentIds.add(appt.getAppointmentId());
                    }
                    apptToDate.put(appt.getAppointmentId(), appt.getDate());
                    apptToCustomer.put(appt.getAppointmentId(),
                            customerNameMap.getOrDefault(appt.getCustomerId(), "Unknown"));
                }

//        // Get doctor’s appointments
//        List<String> doctorAppts = new ArrayList<>();
//        for (Appointment a : appointments) {
//            if (a.getDoctorId().equals(doctor.getSystemId())) {
//                doctorAppts.add(a.getAppointmentId());
//            }
//        }

        // Store rows
        allRows.clear();
        Set<String> dates = new LinkedHashSet<>();
        dates.add("All");
        
        for (Feedback f : feedbacks) {
            // Only customer feedback linked to this doctor's appointments
            if (!"CUSTOMER".equalsIgnoreCase(f.getUserType())) continue;
            if (!doctorAppointmentIds.contains(f.getAppointmentId())) continue;

            String date        = apptToDate.getOrDefault(f.getAppointmentId(), "Unknown");
            String patientName = apptToCustomer.getOrDefault(f.getAppointmentId(), "Unknown");
            dates.add(date);

            allRows.add(new Object[]{
                f.getFeedbackId(),
                f.getAppointmentId(),
                patientName,
                f.getRating(),      // may be null; handled in filter
                f.getComment1(),    // patient's comment
                date                // hidden column for filtering
            });
        }
        
        // Fill cmbDate
        cmbDate.setModel(new DefaultComboBoxModel<>(dates.toArray(new String[0])));

        applyFilters();
    }
    
    private void applyFilters() {
        String search = txtSearch.getText().trim().toLowerCase();
        String ratingFilter = (String) cmbRating.getSelectedItem();
        String dateFilter = (String) cmbDate.getSelectedItem();
        
        model.setRowCount(0);

        for (Object[] row : allRows) {
            String appointmentId = row[1].toString().toLowerCase();
            String patientName = row[2].toString().toLowerCase();
            String rating = row[3] == null ? "" : row[3].toString();
            String date = row[5] == null ? "" : row[5].toString();

            boolean matchesSearch = search.isEmpty()
                    || appointmentId.contains(search)
                    || patientName.contains(search);

            boolean matchesRating = ratingFilter.equals("All") || rating.equals(ratingFilter);
            boolean matchesDate = dateFilter.equals("All") || date.equals(dateFilter);

            if (matchesSearch && matchesRating && matchesDate) {
                // only show first 5 cols in table
                model.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4]});
            }
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
        tbFeedback = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        cmbRating = new javax.swing.JComboBox<>();
        btnReset = new javax.swing.JButton();
        lblSearch = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbDate = new javax.swing.JComboBox<>();
        lblDate = new javax.swing.JLabel();
        lblTitleCmmt = new javax.swing.JLabel();
        backBtn = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tbFeedback.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Feedback ID", "Appointment ID", "Patient Name", "Rating", "Comment"
            }
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // disable editing for all cells
            }
        });
        jScrollPane1.setViewportView(tbFeedback);

        cmbRating.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "5", "4", "3", "2", "1" }));

        btnReset.setText("Reset");

        lblSearch.setText("Search (Name/Appt ID):");

        jLabel1.setText("Rating:");

        cmbDate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblDate.setText("Date:");

        lblTitleCmmt.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitleCmmt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleCmmt.setText("View Comments");
        lblTitleCmmt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

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
                    .addComponent(backBtn)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(lblSearch)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                            .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbDate, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cmbRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)
                            .addComponent(btnReset))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblTitleCmmt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(backBtn)
                .addGap(7, 7, 7)
                .addComponent(lblTitleCmmt)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset)
                    .addComponent(lblSearch)
                    .addComponent(jLabel1)
                    .addComponent(cmbDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDate))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("home");
    }//GEN-LAST:event_backBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cmbDate;
    private javax.swing.JComboBox<String> cmbRating;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblTitleCmmt;
    private javax.swing.JTable tbFeedback;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
