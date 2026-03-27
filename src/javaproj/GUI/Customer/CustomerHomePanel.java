package javaproj.GUI.Customer;

import java.awt.*;
import javaproj.Model.Role.Customer;
import javaproj.Utils.PanelController;
import javaproj.Utils.Utils;
import static javaproj.Utils.Utils.setProfileIcon;
import javax.swing.*;

public class CustomerHomePanel extends javax.swing.JPanel {
    
    private JPanel contentPanel;
    private PanelController controller;
    private String picPath;
    private String id;
    
    private void loadPic(String id){
        setProfileIcon(pic, id);
        pic.setHorizontalAlignment(SwingConstants.CENTER);
        pic.setVerticalAlignment(SwingConstants.CENTER);
    }
    
    public CustomerHomePanel(PanelController controller, Customer userinfo) {
        this.id = userinfo.getSystemId();
        this.controller = controller;
        initComponents();
        
        Color buttonColor = new Color(245, 243, 238); 
        cancel_appointment.setBackground(buttonColor);
        view_appointments.setBackground(buttonColor);
        provide_comment.setBackground(buttonColor);
        view_history.setBackground(buttonColor);
        
        loadPic(id);
        nameLabel.setText("Name: " + userinfo.getName());
        roleLabel.setText("Role: Customer");
        icLabel.setText("IC: " + userinfo.getIdentityNumber());
    }
    
    @Override
    public void setVisible(boolean flag) {
        super.setVisible(flag);
            if (flag) {
                loadPic(id);
            }
        }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cancel_appointment = new javax.swing.JButton();
        view_appointments = new javax.swing.JButton();
        provide_comment = new javax.swing.JButton();
        view_history = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        roleLabel = new javax.swing.JLabel();
        icLabel = new javax.swing.JLabel();
        pic = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        cancel_appointment.setBackground(new java.awt.Color(245, 243, 238));
        cancel_appointment.setText("Cancel Appointment");
        cancel_appointment.setMaximumSize(new java.awt.Dimension(86, 27));
        cancel_appointment.setMinimumSize(new java.awt.Dimension(86, 27));
        cancel_appointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_appointmentActionPerformed(evt);
            }
        });

        view_appointments.setBackground(new java.awt.Color(245, 243, 238));
        view_appointments.setText("View Appointments");
        view_appointments.setMaximumSize(new java.awt.Dimension(86, 27));
        view_appointments.setMinimumSize(new java.awt.Dimension(86, 27));
        view_appointments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_appointmentsActionPerformed(evt);
            }
        });

        provide_comment.setBackground(new java.awt.Color(245, 243, 238));
        provide_comment.setText("Provide Comment");
        provide_comment.setMaximumSize(new java.awt.Dimension(86, 27));
        provide_comment.setMinimumSize(new java.awt.Dimension(86, 27));
        provide_comment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                provide_commentActionPerformed(evt);
            }
        });

        view_history.setBackground(new java.awt.Color(245, 243, 238));
        view_history.setText("View History");
        view_history.setMaximumSize(new java.awt.Dimension(86, 27));
        view_history.setMinimumSize(new java.awt.Dimension(86, 27));
        view_history.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_historyActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(245, 243, 238));

        nameLabel.setBackground(java.awt.Color.white);
        nameLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        nameLabel.setText("Name");
        nameLabel.setMinimumSize(new java.awt.Dimension(50, 10));
        nameLabel.setOpaque(true);
        nameLabel.setPreferredSize(new java.awt.Dimension(230, 30));

        roleLabel.setBackground(java.awt.Color.white);
        roleLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        roleLabel.setText("Role");
        roleLabel.setMinimumSize(new java.awt.Dimension(50, 10));
        roleLabel.setOpaque(true);
        roleLabel.setPreferredSize(new java.awt.Dimension(230, 30));

        icLabel.setBackground(java.awt.Color.white);
        icLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        icLabel.setText("IC");
        icLabel.setMinimumSize(new java.awt.Dimension(50, 10));
        icLabel.setOpaque(true);
        icLabel.setPreferredSize(new java.awt.Dimension(230, 30));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(icLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(roleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40))
                    .addComponent(pic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(pic)
                .addGap(18, 18, 18)
                .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(icLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancel_appointment, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(provide_comment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(view_appointments, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(view_history, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cancel_appointment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(view_appointments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(view_history, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(provide_comment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jPanel2.setName("contentSecondary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void view_historyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_historyActionPerformed
        // TODO add your handling code here:
        controller.show("viewhistory");
    }//GEN-LAST:event_view_historyActionPerformed

    private void provide_commentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_provide_commentActionPerformed
        // TODO add your handling code here:
        controller.show("providecomment");
    }//GEN-LAST:event_provide_commentActionPerformed

    private void view_appointmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_appointmentsActionPerformed
        // TODO add your handling code here:
        controller.show("viewappointments");
    }//GEN-LAST:event_view_appointmentsActionPerformed

    private void cancel_appointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_appointmentActionPerformed
        // TODO add your handling code here:
        controller.show("cancelappointment");
    }//GEN-LAST:event_cancel_appointmentActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel_appointment;
    private javax.swing.JLabel icLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel pic;
    private javax.swing.JButton provide_comment;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JButton view_appointments;
    private javax.swing.JButton view_history;
    // End of variables declaration//GEN-END:variables
}
