package javaproj.Methods.Report;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javaproj.GUI.Manager.PDFReportGenerator;
import javaproj.Model.Payment;
import javaproj.Repository.PaymentRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class FinancialReportService {
    public FinancialReportService(){}
    private final PaymentRepository paymentRepo = new PaymentRepository();
    
    // Inner class to hold summary statistics for financial reports
    public class Summary {
        public final long totalPayments;    // number of payments recorded
        public final double totalRevenue;   // total revenue from payments
        public final double avgPayment;     // average payment amount

        public Summary(long totalPayments, double totalRevenue, double avgPayment) {
            this.totalPayments = totalPayments;
            this.totalRevenue = totalRevenue;
            this.avgPayment = avgPayment;
        }
    }
    
    // Build summary (total payments, total revenue, avg payment) from a list of payments
    public Summary buildSummary(List<Payment> payments) {
        long total = payments.size();   // count total number of payments
        double revenue = payments.stream().mapToDouble(Payment::getAmount).sum();   // sum all amounts
        double avg = total > 0 ? revenue / total : 0.0; // calculate average
        avg = Math.round(avg * 100.0) / 100.0; // round to 2 decimals
        return new Summary(total, revenue, avg);
    }
    
    //Financial report
     // Pie chart: distribution of payments by status (COMPLETED, FAILED, PENDING)
    public JFreeChart createStatusChart() {
        List<Payment> list = paymentRepo.findAll();
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Group payments by status and count them
        Map<String, Long> count = list.stream()
                .collect(Collectors.groupingBy(p -> p.getStatus().toString(), Collectors.counting()));
        count.forEach(dataset::setValue);

        // Create pie chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Payments by Status", dataset, true, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));

        // Custom colors for each status
        plot.setSectionPaint("COMPLETED", new Color(76,175,80));
        plot.setSectionPaint("FAILED", new Color(244,67,54));
        plot.setSectionPaint("PENDING", new Color(255,152,0));

        // Improve label style
        plot.setSimpleLabels(true);
        plot.setLabelBackgroundPaint(new Color(255,255,255,150));
        plot.setLabelShadowPaint(null);
        plot.setLabelOutlinePaint(null);

        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));

        return chart;
    }
    
    // Pie chart: revenue distribution by payment method (CASH, CREDIT_CARD,null)
    public JFreeChart createMethodChart() {
        List<Payment> list = paymentRepo.findAll();
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Group by method and sum amounts
        Map<String, Double> sums = list.stream()
                .collect(Collectors.groupingBy(
                    p -> p.getPaymentMethod().toString(),
                    Collectors.summingDouble(Payment::getAmount)
                ));
        sums.forEach(dataset::setValue);    // Add results to dataset

        // Create pie chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Payments by Method", dataset, true, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));

        // Custom colors for methods
        plot.setSectionPaint("CREDIT_CARD", new Color(33,150,243));
        plot.setSectionPaint("CASH", new Color(96,125,139));
        plot.setSectionPaint("E_WALLET", new Color(156,39,176));
        plot.setSectionPaint("ONLINE_BANKING", new Color(0,150,136));

        // Improve label style
        plot.setSimpleLabels(true);
        plot.setLabelBackgroundPaint(new Color(255,255,255,150));
        plot.setLabelShadowPaint(null);
        plot.setLabelOutlinePaint(null);

        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));

        return chart;
    }
    
    // Line chart: daily revenue trend (sum of payments by date)
    public JFreeChart createDailyRevenueChart() {
        List<Payment> list = paymentRepo.findAll();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> daily = new TreeMap<>();
        
        // Accumulate revenue per date
        for (Payment p : list) {
            daily.put(p.getDate(), daily.getOrDefault(p.getDate(), 0.0) + p.getAmount());
        }
        // Add to dataset
        daily.forEach((d, sum) -> dataset.addValue(sum, "Revenue", d));

         // Create line chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Daily Revenue Trend", "Date", "Revenue (RM)",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(200,200,200));
        plot.setRangeGridlinePaint(new Color(200,200,200));

        // Line style customization
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(2.5f)); // thicker line
        renderer.setSeriesShapesVisible(0, true);   // show data points

        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));

        return chart;
    }
    
    // Total number of payments recorded
    public int totalPayment(){
        List<Payment> list = paymentRepo.findAll();
        return list.size();
    }
    
    // Total revenue from all payments
    public double totalRevenue(){
        List<Payment> payments = paymentRepo.findAll();
        return payments.stream().mapToDouble(Payment::getAmount).sum();
    }
    
    // Average payment amount (rounded to 2 decimals)
    public double avgPayment(){
        double avg = totalPayment() > 0 ? totalRevenue() / totalPayment() : 0.0;
        return Math.round(avg * 100.0) / 100.0; // round to 2 decimals
    }
    
    // Generate full PDF report with all financial charts
    public String generateReportFile() throws Exception{
             List<Payment> payments = paymentRepo.findAll();
            // Always save inside project folder "reports"
            String projectDir = System.getProperty("user.dir");  // root of your project
            File reportsDir = new File(projectDir, "reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();    // create folder if not exists
            }

            // Collect all charts into a map for PDF generation
            Map<String, JFreeChart> charts = new LinkedHashMap<>();
            charts.put("paymentStatus", createStatusChart());
            charts.put("paymentMethod", createMethodChart());
            charts.put("dailyRevenue", createDailyRevenueChart());

            // Generate PDF with charts + data table
            return PDFReportGenerator.generateReport(reportsDir,"FinancialReport","Payment Overview",payments,charts);
    }
}
