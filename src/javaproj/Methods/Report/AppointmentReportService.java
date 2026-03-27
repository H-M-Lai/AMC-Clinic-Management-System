/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Report;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javaproj.GUI.Manager.PDFReportGenerator;
import javaproj.Model.Appointment;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javaproj.Repository.AppointmentRepository;


public class AppointmentReportService {
    private final AppointmentRepository apptRepo;
    public AppointmentReportService(){
    this(new AppointmentRepository());
    }
    public AppointmentReportService(AppointmentRepository apptRepo) {
        this.apptRepo = apptRepo;
    }
    private List<Appointment> loadAll() {
        // e.g. replace with apptRepo.list(), apptRepo.findAll(), or apptRepo.appointmentList()
        return apptRepo.findAll();
    }
    
    //  Generate a bar chart showing how many appointments each doctor has
    public JFreeChart createDoctorChart() {
        List<Appointment> list = loadAll();
        
        // Count appointments per doctor
        Map<String, Long> count = list.stream()
                .collect(Collectors.groupingBy(Appointment::getDoctorId, Collectors.counting()));

        // Sort doctors by number of appointments (descending)
        LinkedHashMap<String, Long> sorted = count.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        // Build dataset for chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        sorted.forEach((doctor, num) -> dataset.addValue(num, "Appointments", doctor));

        // Create bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Appointments per Doctor", "Doctor", "Count", dataset,
                PlotOrientation.VERTICAL, false, true, false);

        // Customize look
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // Apply custom colors for bars
        final Paint[] colors = {
            new Color(66, 133, 244),  // Blue
            new Color(219, 68, 55),   // Red
            new Color(244, 180, 0),   // Yellow
            new Color(15, 157, 88),   // Green
            new Color(171, 71, 188),  // Purple
            new Color(0, 172, 193)    // Teal
        };

        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                return colors[column % colors.length];
            }
        };
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        plot.setRenderer(renderer);

        chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        return chart;
    }
    
    // Generate a pie chart showing distribution of appointments by their status (Completed, Scheduled, Cancelled)
    public JFreeChart createStatusChart() {
        List<Appointment> list = loadAll();
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Count appointments by status
        Map<String, Long> count = list.stream()
                .collect(Collectors.groupingBy(a -> a.getStatus().toString(), Collectors.counting()));
        count.forEach(dataset::setValue);

        // Create pie chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Appointments by Status", dataset, true, true, false);

        // Customize pie chart
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Completed", new java.awt.Color(0, 153, 76));  // Green
        plot.setSectionPaint("Scheduled", new Color(0, 102, 204));  // Blue
        plot.setSectionPaint("Cancelled", new Color(204, 0, 0));    // Red
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));

        chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        return chart;
    }
    
    // Generate a bar chart showing appointments grouped by type
    public JFreeChart createTypeChart() {
        List<Appointment> list = loadAll();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Count appointments by type
        Map<String, Long> count = list.stream()
                .collect(Collectors.groupingBy(a -> a.getType().toString(), Collectors.counting()));

         // Sort types by count
        LinkedHashMap<String, Long> sorted = count.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        // Add data, wrapping long labels
        sorted.forEach((type, num) -> {
            String wrappedLabel = type.replaceAll("(.{8})", "$1\n");
            dataset.addValue(num, "Appointments", wrappedLabel);
        });

        // Create bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Appointments by Type", "Type", "Count", dataset,
                PlotOrientation.VERTICAL, false, true, false);

        // Customize look
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // Fix label display
        CategoryAxis domainAxis = plot.getDomainAxis();

        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        domainAxis.setMaximumCategoryLabelWidthRatio(1.0f);
        domainAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 10));

        // Assign bar colors
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                Color[] colors = {
                    new Color(66, 133, 244),   // Blue
                    new Color(219, 68, 55),    // Red
                    new Color(244, 180, 0),    // Yellow
                    new Color(15, 157, 88)     // Green
                };
                return colors[column % colors.length];
            }
        };
        
        plot.setRenderer(renderer);

        chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        return chart;
    }
    
    // Generate a line chart showing number of appointments per day
    public JFreeChart createDateChart() {
        List<Appointment> list = loadAll();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Count appointments by date (TreeMap = sorted by date)
        Map<String, Long> count = new TreeMap<>();
        for (Appointment a : list)
            count.put(a.getDate(), count.getOrDefault(a.getDate(), 0L) + 1);
        count.forEach((d, num) -> dataset.addValue(num, "Appointments", d));

        // Create line chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Appointments per Day", "Date", "Count", dataset,
                PlotOrientation.VERTICAL, false, true, false);
        
        // Style chart
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        return chart;
    }
    
    // Summary Counters
    public int total(){
        List<Appointment> appointments = loadAll();
        return appointments.size();
    }
    public long completed(){
        List<Appointment> appointments = loadAll();
        return appointments.stream().filter(a -> a.getStatus().toString().equalsIgnoreCase("Completed")).count();
    }
    public long scheduled(){
        List<Appointment> appointments = loadAll();
        return appointments.stream().filter(a -> a.getStatus().toString().equalsIgnoreCase("Scheduled")).count();
    }
    public long cancelled(){
        List<Appointment> appointments = loadAll();
        return appointments.stream().filter(a -> a.getStatus().toString().equalsIgnoreCase("Cancelled")).count();
    }
       
    // Generate a PDF file containing all charts and appointment details
    public String generateReportFile() throws Exception{
        List<Appointment> appointments = loadAll();
        
        // Ensure reports directory exists
        String projectDir = System.getProperty("user.dir");  // root of your project
        File reportsDir = new File(projectDir, "reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
        
        // Collect charts into a map
        Map<String, JFreeChart> charts = new LinkedHashMap<>();
        charts.put("Doctor", createDoctorChart());
        charts.put("Status", createStatusChart());
        charts.put("Type", createTypeChart());
        charts.put("Date", createDateChart());
            
        // Generate PDF with all charts
        return PDFReportGenerator.generateReport(
                reportsDir,
                "AppointmentReport",
                "Appointments Overview",
                appointments,
                charts
        );

    }
}
