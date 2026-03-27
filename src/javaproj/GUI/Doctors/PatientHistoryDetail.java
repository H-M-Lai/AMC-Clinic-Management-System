package javaproj.GUI.Doctors;

import java.util.ArrayList;
import javaproj.Model.*;
import javaproj.Model.Role.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javaproj.Repository.AppointmentRepository;
import javaproj.Repository.FeedbackRepository;
import javaproj.Methods.Appointment.DoctorAppointment;
import javaproj.Methods.Items.ChargeItemService;

import javaproj.Methods.User.CustomerService;
import javaproj.Utils.*;

public class PatientHistoryDetail extends javax.swing.JFrame {
    
    private Doctor doctor;
    private String customerId;


    private final AppointmentRepository apptRepo = new AppointmentRepository();
    private final DoctorAppointment doctorAppt   = new DoctorAppointment(apptRepo);
    
    private final ChargeItemService chargeItemService = new ChargeItemService();

    private final FeedbackRepository feedbackRepo = new FeedbackRepository();
    private final CustomerService customerService  = new CustomerService();
    
    private List<Appointment> appointments;
    private List<ChargeItems> charges;
    private List<Feedback> feedbacks;
    
    String[] columnNames = {"Item", "Quantity", "Unit Price"};
    private DefaultTableModel model = new DefaultTableModel(columnNames, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // all cells uneditable
        }
    };

    public PatientHistoryDetail(Doctor doctor, String customerId) {
        this.doctor = doctor;
        this.customerId = customerId;

        initComponents();
        
        lblCustHistory.setText("Patient History - " + customerId);
        tbCharges.setModel(model);
        cmbAppt.addActionListener(e -> showAppointmentDetails());
        
        setSize(715, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        txtName.setEditable(false);
        txtDate.setEditable(false);
        txtTime.setEditable(false);
        txtType.setEditable(false);
        txtStatus.setEditable(false);
        txtCustRating.setEditable(false);
        txtCustCmmt.setEditable(false);
        txtDocCmmt.setEditable(false);
        
        loadData();
    }

    private void loadData() {
        appointments = doctorAppt.appointmentList(doctor.getSystemId());
        charges = chargeItemService.chargeItemsList();
        feedbacks = feedbackRepo.findAll();


        List<String> apptIds = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getCustomerId().equals(customerId)) {
                apptIds.add(a.getAppointmentId());
            }
        }
        Utils.updateComboBox(cmbAppt, apptIds);


        if (cmbAppt.getItemCount() > 0) {
            cmbAppt.setSelectedIndex(0);
            showAppointmentDetails();
        }
    }

    private void showAppointmentDetails() {
        String selected = (String) cmbAppt.getSelectedItem();
        if (selected == null) {
            return;
        }

        // since only add the appointmentId into cmbAppt, no need to split
        String apptId = selected;
        
//        // use appointmentMap instead of getAppointment, if not changing getAppointment method
//        Appointment appt = AppointmentService.appointmentMap().get(apptId);
        
        // if change the get Appointment method
        Appointment appt = doctorAppt.getAppointment(apptId);


        if (appt != null) {
//            txtApptId.setText(appt.getAppointmentId());
            String cid = appt.getCustomerId();
            String name = customerService.nameMap().getOrDefault(cid, "(unknown)");
            txtName.setText(name);
            txtDate.setText(appt.getDate());
            txtTime.setText(appt.getTime());
            txtType.setText(appt.getType().toString());
            txtStatus.setText(appt.getStatus().toString());
        }

        // Charges
        model.setRowCount(0);
        for (ChargeItems c : charges) {
            if (c.getAppointmentId().equals(apptId)) {
                model.addRow(new Object[]{c.getChargeItemId(), c.getQuantity(), "RM " + c.getUnitPrice()});
            }
        }

        // Customer feedback
        txtCustRating.setText("");
        txtCustCmmt.setText("");
        for (Feedback f : feedbacks) {
            if (f.getAppointmentId().equals(apptId) && "CUSTOMER".equalsIgnoreCase(f.getUserType())) {
                txtCustRating.setText(f.getRating());
                txtCustCmmt.setText(f.getComment1());
                break;
            }
        }

        // Doctor feedback
        txtDocCmmt.setText("");
        for (Feedback f : feedbacks) {
            if (f.getAppointmentId().equals(apptId) && "DOCTOR".equalsIgnoreCase(f.getUserType())) {
                txtDocCmmt.setText(f.getComment1());
                break;
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
        txtType = new javax.swing.JTextField();
        txtStatus = new javax.swing.JTextField();
        lblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCustRating = new javax.swing.JTextField();
        txtCustCmmt = new javax.swing.JTextField();
        cmbAppt = new javax.swing.JComboBox<>();
        txtTime = new javax.swing.JTextField();
        txtDocCmmt = new javax.swing.JTextField();
        txtDate = new javax.swing.JTextField();
        lblCustRating = new javax.swing.JLabel();
        lblCustCmmt = new javax.swing.JLabel();
        lblDocCmmt = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCharges = new javax.swing.JTable();
        lblCharges = new javax.swing.JLabel();
        lblCustHistory = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblDate.setText("Date:");

        lblTime.setText("Time:");

        lblType.setText("Type:");

        lblStatus.setText("Status:");

        jLabel5.setText("Appointment ID:");

        cmbAppt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblCustRating.setText("Customer Rating:");

        lblCustCmmt.setText("Customer Comment:");

        lblDocCmmt.setText("Doctor Feedback:");

        tbCharges.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Item", "Quantity", "Unit Price"
            }
        ));
        jScrollPane1.setViewportView(tbCharges);

        lblCharges.setText("Charges:");

        lblCustHistory.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCustHistory.setText(" ");

        lblName.setText("Patient Name:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblCustHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblType, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblName))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtType, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTime)
                            .addComponent(txtDate)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbAppt, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblDocCmmt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCustCmmt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCustRating, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDocCmmt)
                            .addComponent(txtCustCmmt)
                            .addComponent(txtStatus, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCustRating, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(33, 33, 33))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(lblCustHistory)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbAppt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblStatus)
                        .addComponent(lblName))
                    .addComponent(txtName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCustRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCustRating))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCustCmmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCustCmmt))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDocCmmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDocCmmt)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDate))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTime))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblType))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCharges)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel1.setName("contentSecondary");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbAppt;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCharges;
    private javax.swing.JLabel lblCustCmmt;
    private javax.swing.JLabel lblCustHistory;
    private javax.swing.JLabel lblCustRating;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDocCmmt;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblType;
    private javax.swing.JTable tbCharges;
    private javax.swing.JTextField txtCustCmmt;
    private javax.swing.JTextField txtCustRating;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtDocCmmt;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtTime;
    private javax.swing.JTextField txtType;
    // End of variables declaration//GEN-END:variables
}
