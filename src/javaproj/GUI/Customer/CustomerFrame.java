package javaproj.GUI.Customer;

import java.awt.*;
import javaproj.GUI.Credentials.LoginFrame;
import javaproj.GUI.Credentials.Session;
import javaproj.GUI.Common.*;
import javaproj.Model.Role.Customer;
import javaproj.Utils.*;
import javax.swing.*;

public class CustomerFrame extends javax.swing.JFrame {
    
    
    private Customer loggedInCustomer;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerFrame.class.getName());
    
    //NavBar
    private CustomerHomePanel homePanel;
    private ProfilePanel profilePanel;
    private SettingPanel settingsPanel;
    private AboutUsPanel aboutUsPanel;
    //Home
    private CancelAppointmentPanel cancelAppointmentPanel;
    private ViewAppointmentsPanel viewappointmentsPanel;
    private ProvideCommentPanel providecommentPanel;
    private ViewHistoryPanel viewhistoryPanel;
    
    //String[] userinfo = {"C001","Mandev","123456781234","0182067277","Jalan Teknologi 5, Taman Teknologi Malaysia, 57000 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur","mandev@gmail.com","12345"};
    
    public CustomerFrame(Customer user) {
        this.loggedInCustomer = user;
        initComponents();
        
        getContentPane().setBackground(Color.WHITE);
        setSize(1024,768);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        PanelController controller = new PanelController(contentPanel);
        
        //NavBar Panel Button
        homePanel = new CustomerHomePanel(controller, loggedInCustomer);
        profilePanel = new ProfilePanel();
        settingsPanel = new SettingPanel();
        aboutUsPanel = new AboutUsPanel();
        
        contentPanel.add(homePanel,"home");
        contentPanel.add(profilePanel,"profile");
        contentPanel.add(settingsPanel,"settings");
        contentPanel.add(aboutUsPanel, "aboutUs");
        
        //Home Panel Button 
        cancelAppointmentPanel = new CancelAppointmentPanel(controller);
        viewappointmentsPanel = new ViewAppointmentsPanel(controller);
        providecommentPanel = new ProvideCommentPanel(controller);
        viewhistoryPanel = new ViewHistoryPanel(controller);
        
        contentPanel.add(cancelAppointmentPanel,"cancelappointment");
        contentPanel.add(viewappointmentsPanel,"viewappointments");
        contentPanel.add(providecommentPanel,"providecomment");
        contentPanel.add(viewhistoryPanel,"viewhistory");
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel1, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        
        cardLayout.show(contentPanel, "home");
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Home = new javax.swing.JButton();
        Profile = new javax.swing.JButton();
        Settings = new javax.swing.JButton();
        AboutUs = new javax.swing.JButton();
        Exit = new javax.swing.JButton();
        pictureFormat1 = new javaproj.Comp.PictureFormat();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 243, 238));

        Home.setText("Home");
        Home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomeActionPerformed(evt);
            }
        });

        Profile.setText("Profile");
        Profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProfileActionPerformed(evt);
            }
        });

        Settings.setText("Settings");
        Settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingsActionPerformed(evt);
            }
        });

        AboutUs.setText("About us");
        AboutUs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutUsActionPerformed(evt);
            }
        });

        Exit.setText("Exit");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });

        pictureFormat1.set$image(new javax.swing.ImageIcon(getClass().getResource("/javaproj/image/logo.png"))); // NOI18N
        pictureFormat1.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                pictureFormat1HierarchyChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(pictureFormat1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(Profile, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(Settings, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(AboutUs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {AboutUs, Home, Profile, Settings});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pictureFormat1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Profile, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Settings, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AboutUs, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {AboutUs, Exit, Home, Profile, Settings});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 463, Short.MAX_VALUE))
        );

        jPanel1.setName("contentSecondary");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomeActionPerformed
        // TODO add your handling code here:
        cardLayout.show(contentPanel, "home");
    }//GEN-LAST:event_HomeActionPerformed

    private void ProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProfileActionPerformed
        // TODO add your handling code here:
        cardLayout.show(contentPanel, "profile");
    }//GEN-LAST:event_ProfileActionPerformed

    private void SettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsActionPerformed
        // TODO add your handling code here:
        cardLayout.show(contentPanel, "settings");
    }//GEN-LAST:event_SettingsActionPerformed

    private void AboutUsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutUsActionPerformed
        // TODO add your handling code here:
        cardLayout.show(contentPanel, "aboutUs");
    }//GEN-LAST:event_AboutUsActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        // TODO add your handling code here:
        Session.clear();
        JFrame login = new LoginFrame();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ExitActionPerformed

    private void pictureFormat1HierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_pictureFormat1HierarchyChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_pictureFormat1HierarchyChanged

    public void showHomePanel() {
        cardLayout.show(contentPanel, "home");
    }
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
        //java.awt.EventQueue.invokeLater(() -> new CustomerFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AboutUs;
    private javax.swing.JButton Exit;
    private javax.swing.JButton Home;
    private javax.swing.JButton Profile;
    private javax.swing.JButton Settings;
    private javax.swing.JPanel jPanel1;
    private javaproj.Comp.PictureFormat pictureFormat1;
    // End of variables declaration//GEN-END:variables
}
