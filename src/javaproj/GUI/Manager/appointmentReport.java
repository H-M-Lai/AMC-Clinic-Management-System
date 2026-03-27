/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package javaproj.GUI.Manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import javaproj.Utils.PanelController;

import org.jfree.chart.*;

import javax.swing.*;
import javaproj.Methods.Report.AppointmentReportService;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
/**
 *
 * @author mayvi
 */
public class appointmentReport extends javax.swing.JPanel {
    
    private PanelController controller;
    private JPanel chartContainer;  
    private JPanel summaryPanel;
    private JToolBar toolBar;
    private JPanel descriptionPanel;
    private JLabel descriptionTitle;
    private JTextArea descriptionText;
    private JSplitPane splitPane;
    private JButton printBtn;

    AppointmentReportService appointmentReportService = new AppointmentReportService();
    
    public appointmentReport(PanelController controller) {
        this.controller = controller;
        initComponents();
        
        setLayout(new BorderLayout());   // whole panel
        jPanel1.setLayout(new BorderLayout()); // inner chart area
        
        // Toolbar for chart buttons
        toolBar = new JToolBar();
        toolBar.setFloatable(false); // fixed toolbar
    
        addToolbarButton("By Doctor", () -> {
            showChart(appointmentReportService.createDoctorChart());
            updateDescription("This chart shows doctor workload distribution, identifying who handles the most appointments. This is crucial to help balance doctors' workloads.");
        });
        addToolbarButton("By Status", () -> {
            showChart(appointmentReportService.createStatusChart());
            updateDescription("This chart is used to check appointment statuses (Completed, Scheduled, Cancelled). It can shows workflow efficiency.");
        });
        
        addToolbarButton("By Appointment Type", () -> {
            showChart(appointmentReportService.createTypeChart());
            updateDescription("This chart shows the distribution of appointment types (Consultation, Check-up, Follow-up, Preventative Care). It helps managers see what kinds of services are most common.");
        });

        addToolbarButton("Appointment Over Time", () -> {
            showChart(appointmentReportService.createDateChart());
            updateDescription("This chart is often to used to view the busiest days for appointments over time.");
        });
        
        // Left side panel: summary + description
        descriptionPanel = new JPanel(new BorderLayout(10, 10));
        descriptionPanel.setPreferredSize(new Dimension(300, 600));
        descriptionPanel.setBackground(Color.WHITE);
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Summary Section =====
        summaryPanel = new JPanel(new GridLayout(0, 2, 10, 8));
        summaryPanel.setBackground(Color.WHITE);

        JLabel summaryLabel = new JLabel("Summary Overview");
        summaryLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel summaryBox = new JPanel(new BorderLayout());
        summaryBox.setBackground(Color.WHITE);
        summaryBox.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        summaryBox.add(summaryLabel, BorderLayout.NORTH);
        summaryBox.add(summaryPanel, BorderLayout.CENTER);

        
        // ===== Description Section =====
        descriptionText = new JTextArea();
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setEditable(false);
        descriptionText.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13));
        descriptionText.setBackground(new Color(250, 250, 255));

        descriptionTitle = new JLabel("Chart Details");
        descriptionTitle.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        descriptionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JScrollPane descScroll = new JScrollPane(descriptionText);
        descScroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel descBox = new JPanel(new BorderLayout());
        descBox.setBackground(Color.WHITE);
        descBox.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        descBox.add(descriptionTitle, BorderLayout.NORTH);
        descBox.add(descScroll, BorderLayout.CENTER);
        
        // ===== Stack both sections nicely =====   
        JPanel leftContent = new JPanel(new BorderLayout(0, 12)); // 12px vertical gap
        leftContent.setBackground(Color.WHITE);
        leftContent.add(summaryBox, BorderLayout.NORTH);
        leftContent.add(descBox, BorderLayout.CENTER);

        descriptionPanel.removeAll();
        descriptionPanel.add(leftContent, BorderLayout.CENTER);
        descriptionPanel.revalidate();
        descriptionPanel.repaint();

        // ===== Chart container (right side) =====
        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setPreferredSize(new Dimension(800, 600));

        // SplitPane: left = description, right = chart
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, descriptionPanel, chartContainer);
        splitPane.setDividerLocation(330);
        splitPane.setResizeWeight(0.2);
        
        // Button panel
        printBtn = new JButton("Print Report");
        printBtn.addActionListener(evt -> printReport());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(printBtn);
        buttonPanel.add(backBtn); // from initComponents()

        // Default chart
        showChart(appointmentReportService.createDoctorChart());
        updateDescription("This chart shows doctor workload distribution, identifying who handles the most appointments. This is crucial to help balance doctors' workloads.");

        // Controls panel (toolbar + back button)
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.add(toolBar, BorderLayout.CENTER);
        controlsPanel.add(buttonPanel, BorderLayout.EAST);

        // Add everything to main layout
        add(splitPane, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.SOUTH);
    }
    
    private void addToolbarButton(String title, Runnable action) {
        JButton btn = new JButton(title);
        btn.addActionListener(e -> action.run());
        toolBar.add(btn);
    }
    
    private void showChart(JFreeChart chart) {
        chartContainer.removeAll();
        chartContainer.add(new ChartPanel(chart), BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }
    
    private void addSummaryItem(String key, String value, Color color, boolean bold) {
        JLabel keyLabel = new JLabel(key + ":");
        keyLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new java.awt.Font("SansSerif", bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN, 13));
        if (color != null) {
            keyLabel.setForeground(color);
            valueLabel.setForeground(color);
        }

        summaryPanel.add(keyLabel);
        summaryPanel.add(valueLabel);
    }
    
    private void updateDescription(String desc) {
        descriptionText.setText(desc);

        // Clear old summary
        summaryPanel.removeAll();
        // --- Add summary items (always balanced) ---
        addSummaryItem("Total Appointments", String.valueOf(appointmentReportService.total()), null, true);
        addSummaryItem("✔ Completed", String.valueOf(appointmentReportService.completed()), new Color(0, 153, 76), false);
        addSummaryItem("📅 Scheduled", String.valueOf(appointmentReportService.scheduled()), new Color(0, 102, 204), false);
        addSummaryItem("❌ Cancelled", String.valueOf(appointmentReportService.cancelled()), new Color(204, 0, 0), false);

        summaryPanel.revalidate();
        summaryPanel.repaint();
    }

    private void printReport() {
        try {
            // Generate PDF with all charts
            String filePath = appointmentReportService.generateReportFile();

            JOptionPane.showMessageDialog(this,
                    "Report generated successfully!\nSaved at: " + filePath,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            Desktop.getDesktop().open(new File(filePath));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage());
            ex.printStackTrace();
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
        backBtn = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(245, 243, 238));

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(802, Short.MAX_VALUE)
                .addComponent(backBtn)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(345, Short.MAX_VALUE)
                .addComponent(backBtn)
                .addContainerGap())
        );

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

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        controller.show("report");
    }//GEN-LAST:event_backBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
