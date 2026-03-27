/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Report;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javaproj.GUI.Manager.PDFReportGenerator;
import javaproj.Model.Feedback;
import javaproj.Repository.FeedbackRepository;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class FeedbackReportService {
    public FeedbackReportService(){}
    private final FeedbackRepository feedbackRepo = new FeedbackRepository();

    
    // Safely converts a feedback rating (String) into an integer
    public static int safeRating(Feedback f) {
        String ratingStr = f.getRating();  // getRating returns String
        if (ratingStr == null) return 0;
        try {
            return Integer.parseInt(ratingStr);  // convert String to int
        } catch (NumberFormatException e) {
            return 0; // fallback if string is not a number (invalid)
        }
    }
    
    // Creates a pie chart showing how many feedbacks fall into each star rating (1–5)
    public JFreeChart createRatingChart() {
        List<Feedback> list = feedbackRepo.findAll();
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Count how many times each rating appears
        Map<Integer, Long> count = list.stream()
                .map(f -> safeRating(f))  // convert String -> int safely
                .filter(r -> r > 0)       // ignore invalid ratings
                .collect(Collectors.groupingBy(r -> r, Collectors.counting()));

        // Add to dataset with labels like "5 Stars", "4 Stars"
        count.forEach((rating, cnt) -> dataset.setValue(rating + " Stars", cnt));

        // Build pie chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Feedback Rating Distribution", dataset, true, true, false);

        // Style chart
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));

        // Custom colors for each rating
        plot.setSectionPaint("5 Stars", new Color(76,175,80));
        plot.setSectionPaint("4 Stars", new Color(33,150,243));
        plot.setSectionPaint("3 Stars", new Color(255,193,7));
        plot.setSectionPaint("2 Stars", new Color(255,87,34));
        plot.setSectionPaint("1 Stars", new Color(244,67,54));

        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));

        return chart;
    }
    
    // Link feedback to the correct doctor
    private static Map<String, String> loadAppointmentDoctorMap() {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/appointment.txt"))) {
            br.readLine(); // skip header line
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String appointmentId = parts[0].trim();
                    String doctorId = parts[3].trim();
                    map.put(appointmentId, doctorId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    // Creates a bar chart showing average feedback rating per doctor
    public JFreeChart createDoctorChart() {
        List<Feedback> list = feedbackRepo.findAll();
        Map<String,String> appointmentDoctorMap = loadAppointmentDoctorMap();
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    // Group feedback by doctorId and calculate average rating
    Map<String, Double> avgByDoctor = list.stream()
            .filter(f -> f.getRating() != null && !f.getRating().isEmpty())
            .filter(f -> f.getRating().matches("\\d+")) // only keep numeric ratings
            .collect(Collectors.groupingBy(
                    f -> appointmentDoctorMap.getOrDefault(f.getAppointmentId(), "UNKNOWN"),
                    java.util.stream.Collectors.averagingInt(f2 -> safeRating(f2))
            ));

    avgByDoctor.forEach((doctorId, avg) -> dataset.addValue(avg, "Average Rating", doctorId));

    // Create bar chart
    JFreeChart chart = ChartFactory.createBarChart(
            "Average Rating by Doctor",
            "Doctor ID",
            "Average Rating",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false);

    // Style chart background
    CategoryPlot plot = (CategoryPlot) chart.getPlot();
    plot.setBackgroundPaint(new Color(250, 250, 250)); // lighter bg
    plot.setDomainGridlinePaint(new Color(220, 220, 220));
    plot.setRangeGridlinePaint(new Color(200, 200, 200));

    // Assign custom colors
    final Paint[] colors = {
        new Color(33, 150, 243),  // bright blue
        new Color(244, 67, 54),   // bright red
        new Color(255, 193, 7),   // amber
        new Color(76, 175, 80),   // green
        new Color(156, 39, 176),  // purple
        new Color(0, 188, 212)    // cyan
    };

    BarRenderer renderer = new BarRenderer() {
        @Override
        public Paint getItemPaint(int row, int column) {
            return colors[column % colors.length];
        }
    };

    // Extra styling for bars
    renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
    renderer.setShadowVisible(false); // flat design
    renderer.setMaximumBarWidth(0.08); // thinner bars
    renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
    renderer.setBaseItemLabelsVisible(true);
    renderer.setBaseItemLabelFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));

    plot.setRenderer(renderer);

    // Title and axis styling
    chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
    chart.getCategoryPlot().getDomainAxis().setTickLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
    chart.getCategoryPlot().getRangeAxis().setTickLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));

    return chart;
}
    
    // Summary statistics
    // Returns total number of feedback records. 
    public long totalFeedbacks(){
        List<Feedback> list = feedbackRepo.findAll();
        return list.size();
    }
    
    // Calculates average rating across all feedbacks (ignores invalid)
    public double avgRating(){
        List<Feedback> list = feedbackRepo.findAll();
        return list.stream()
                .mapToInt(f -> safeRating(f))
                .filter(r -> r > 0)  // ignore nulls (0 means no rating)
                .average()
                .orElse(0.0);
    }
    
    //Finds the highest rating given in feedbacks
    public int highest(){
        List<Feedback> list = feedbackRepo.findAll();
        return list.stream().mapToInt(f -> safeRating(f)).max().orElse(0);
    }
    
    // Finds the lowest rating (ignores invalid ratings)
    public int lowest(){
        List<Feedback> list = feedbackRepo.findAll();
        return list.stream().mapToInt(f -> safeRating(f)).filter(r -> r > 0).min().orElse(0);
    }
    
    // Generates a PDF report containing: All feedback records, pie chart of rating distribution, bar chart of average ratings per doctor
    public String generateReportFile() throws Exception{
        List<Feedback> feedbacks = feedbackRepo.findAll();
        
        // Ensure "reports" folder exists
        String projectDir = System.getProperty("user.dir");  // root of your project
        File reportsDir = new File(projectDir, "reports");
        if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            // Collect charts into a map
            Map<String, JFreeChart> charts = new LinkedHashMap<>();
            charts.put("ratingDistribution", createRatingChart());
            charts.put("averageRating", createDoctorChart());

            // Generate PDF with all charts
            return PDFReportGenerator.generateReport(
                    reportsDir,
                    "FeedbackReport",
                    "Feedback Overview",
                    feedbacks,
                    charts
            );
    }
}
