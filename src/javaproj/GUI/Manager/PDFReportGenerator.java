package javaproj.GUI.Manager;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javaproj.Model.Appointment;
import javaproj.Model.Feedback;
import javaproj.Model.Payment;
import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.JFreeChart;

public class PDFReportGenerator {
    
    private static final Map<String, BiFunction<String, List<?>, String>> SUMMARY_GENERATORS = new HashMap<>();
    
    static {
        // Appointment report
        SUMMARY_GENERATORS.put("Doctor", PDFReportGenerator::summarizeAppointmentsByDoctor);
        SUMMARY_GENERATORS.put("Status", PDFReportGenerator::summarizeAppointmentsByStatus);
        SUMMARY_GENERATORS.put("Type", PDFReportGenerator::summarizeAppointmentsByType);
        SUMMARY_GENERATORS.put("Date", PDFReportGenerator::summarizeAppointmentsByDate);

        // Financial report
        SUMMARY_GENERATORS.put("paymentStatus", PDFReportGenerator::summarizePaymentStatus);
        SUMMARY_GENERATORS.put("paymentMethod", PDFReportGenerator::summarizePaymentMethod);
        SUMMARY_GENERATORS.put("dailyRevenue", PDFReportGenerator::summarizeDailyRevenue);

        // Feedback report
        SUMMARY_GENERATORS.put("ratingDistribution", PDFReportGenerator::summarizeRatingDistribution);
        SUMMARY_GENERATORS.put("averageRating", PDFReportGenerator::summarizeAverageRatingByDoctor);
    }
    
    private static void addHeader(PDDocument document, PDPage page, String reportType, String formattedDate) throws IOException {
        try (PDPageContentStream content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            // Logo
            PDImageXObject logo = PDImageXObject.createFromFile("src/javaproj/image/logo.png", document);
            content.drawImage(logo, 30, 680, 200, 100);

            // Address
            content.beginText();
            content.setFont(PDType1Font.TIMES_ROMAN, 10);
            content.newLineAtOffset(230, 750);
            String[] address = {"Jalan Teknologi 5,","Taman Teknologi Malaysia,","57000 Kuala Lumpur,","Wilayah Persekutuan Kuala Lumpur"};
            for (String line : address) {
                content.showText(line);
                content.newLineAtOffset(0, -15);
            }
            content.endText();

            // Report info
            content.beginText();
            content.setFont(PDType1Font.TIMES_ROMAN, 10);
            content.newLineAtOffset(395, 720);
            content.showText("Report Type: " + reportType);
            content.newLineAtOffset(32, -15);
            content.showText("Date: " + formattedDate);
            content.endText();
        }
    }

    public static String generateReport(File reportsDir, String baseName, String reportType, List<?> dataList, Map<String, JFreeChart> charts) throws IOException{
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);

        try (PDDocument document = new PDDocument()) {
             PDRectangle landscape = new PDRectangle(PDRectangle.LETTER.getWidth(), PDRectangle.LETTER.getHeight());

            // Insert each chart on a new page
            if (charts != null && !charts.isEmpty()) {
                for (Map.Entry<String, JFreeChart> entry : charts.entrySet()) {
                    PDPage chartPage = new PDPage(landscape);
                    document.addPage(chartPage);
                    
                    // Header for chart page
                    addHeader(document, chartPage, reportType + " - " + entry.getKey(), formattedDate);
                    
                    // Chart image
                    BufferedImage chartImage = entry.getValue().createBufferedImage(600, 400);
                    File tempFile = File.createTempFile("chart", ".png");
                    ImageIO.write(chartImage, "png", tempFile);
                    PDImageXObject chartPdImage = PDImageXObject.createFromFileByContent(tempFile, document);

                    try (PDPageContentStream content = new PDPageContentStream(
                        document, chartPage, PDPageContentStream.AppendMode.APPEND, true)) {
                    content.drawImage(chartPdImage, 5, 240, 600, 400);
                }
                tempFile.delete();
                
                // Add summary under chart
                addSummary(document, chartPage, entry.getKey(), dataList);
                }
            }
            
            // Save PDF with baseName, chartType & timestamp
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = LocalDateTime.now().format(dtf);
            String filename = baseName + "_" + reportType + "_" + timestamp + ".pdf";
            File file = new File(reportsDir, filename);

            // Save PDF 
            document.save(file);
            return file.getAbsolutePath(); // return saved path
        }    
    }
    
    private static void addSummary(PDDocument document, PDPage page, String chartType, List<?> data) throws IOException {
        try (PDPageContentStream content = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true)) {

            float marginLeft = 60;
            float startY = 200;
            float leading = 15f;
            float currentY = startY - 25; // initial offset for summary

            // Section Title
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 14);
            content.newLineAtOffset(marginLeft, startY);
            content.showText("Summary");
            content.endText();

            // Select layout
            if (chartType.equals("paymentStatus") || chartType.equals("paymentMethod") || chartType.equals("dailyRevenue")) {
                currentY = drawTable(content, marginLeft, currentY, chartType, data);
                currentY -= 25; // remarks alignment for sumary table
            } else {
                // Bullet list
                String summaryText = generateSummary(chartType, data);
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 12);
                content.newLineAtOffset(marginLeft, currentY);
                for (String line : summaryText.split("\n")) {
                    if (!line.trim().isEmpty()) {
                        content.showText("• " + line.trim());
                        currentY -= (leading + 3); // update Y as you move down
                        content.newLineAtOffset(0, -(leading + 3));
                    }
                }
                content.endText();
                currentY -= 15; // remarks alignment for summary text
            }

            // Closing remark
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_OBLIQUE, 11);
            content.newLineAtOffset(marginLeft, currentY - 8);
            content.showText("This analysis provides insights to support managerial decision-making.");
            content.endText();
        }
    }
    
    private static float drawTable(PDPageContentStream content, float startX, float startY,
                                  String chartType, List<?> data) throws IOException {
        float rowHeight = 20f;
        float col1Width = 200f;
        float col2Width = 200f;
        float tableWidth = col1Width + col2Width;
        float textY = startY;

        Map<String, String> rows = generateTableSummary(chartType, data);

        // Header
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.newLineAtOffset(startX + 2, textY);
        content.showText("Category / Date");
        content.newLineAtOffset(col1Width + 5, 0);
        content.showText("Value");
        content.endText();
        
        // Horizontal line below header
        content.moveTo(startX, textY - 4);       
        content.lineTo(startX + tableWidth, textY - 4);
        content.setLineWidth(0.5f);
        content.stroke();

        textY -= rowHeight;

        for (Map.Entry<String, String> entry : rows.entrySet()) {
            // Left column
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(startX + 2, textY);
            content.showText(entry.getKey());
            content.endText();

            // Right column
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(startX + col1Width + 5, textY);
            content.showText(entry.getValue());
            content.endText();

            textY -= rowHeight;
        }
        
        float tableTopY = startY + 8; 
        float tableBottomY = textY + rowHeight - 8; 
        float separatorX = startX + col1Width;

        content.moveTo(separatorX, tableTopY);
        content.lineTo(separatorX, tableBottomY);
        content.stroke();

        // return the last Y position
        return textY;
    }
    
    private static String generateSummary(String chartType, List<?> dataList) {
        BiFunction<String, List<?>, String> generator = SUMMARY_GENERATORS.get(chartType);
        if (generator != null) {
            return generator.apply("", dataList);
        }
        return "No summary available for " + chartType;
    }
    
    private static String summarizeAppointmentsByDoctor(String baseName, List<?> list) {
        List<Appointment> appointments = (List<Appointment>) list;
        Map<String, Long> counts = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getDoctorId, Collectors.counting()));

        StringBuilder sb = new StringBuilder("Appointments by Doctor:\n");
        counts.forEach((doc, cnt) -> sb.append("Doctor ").append(doc).append(" handled ").append(cnt).append(" appointments.\n"));
        return sb.toString();
    }

    private static String summarizeAppointmentsByStatus(String baseName, List<?> list) {
        List<Appointment> appointments = (List<Appointment>) list;
        Map<String, Long> counts = appointments.stream()
                .collect(Collectors.groupingBy(a -> a.getStatus().toString(), Collectors.counting()));

        StringBuilder sb = new StringBuilder("Appointments by Status:\n");
        counts.forEach((status, cnt) -> sb.append(status).append(": ").append(cnt).append("\n"));
        return sb.toString();
    }
    
    private static String summarizeAppointmentsByType(String baseName, List<?> list) {
        List<Appointment> appointments = (List<Appointment>) list;
        Map<String, Long> counts = appointments.stream()
                .collect(Collectors.groupingBy(a -> a.getType().toString(), Collectors.counting()));

        StringBuilder sb = new StringBuilder("Appointments by Type:\n");
        counts.forEach((type, cnt) -> sb.append(type).append(": ").append(cnt).append("\n"));
        return sb.toString();
    }

    private static String summarizeAppointmentsByDate(String baseName, List<?> list) {
        List<Appointment> appointments = (List<Appointment>) list;
        Map<String, Long> counts = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getDate, Collectors.counting()));

        StringBuilder sb = new StringBuilder("Appointments by Date:\n");
        counts.forEach((date, cnt) -> sb.append(date).append(": ").append(cnt).append("\n"));
        return sb.toString();
    }
    
    private static Map<String, String> generateTableSummary(String chartType, List<?> list) {
        Map<String, String> result = new LinkedHashMap<>();

        if (chartType.equals("paymentStatus")) {
            List<Payment> payments = (List<Payment>) list;
            Map<String, Long> counts = payments.stream()
                    .collect(Collectors.groupingBy(p -> p.getStatus().toString(), Collectors.counting()));
            counts.forEach((status, cnt) -> result.put(status, cnt + " payments"));
        } else if (chartType.equals("paymentMethod")) {
            List<Payment> payments = (List<Payment>) list;
            Map<String, Double> totals = payments.stream()
                    .collect(Collectors.groupingBy(p -> p.getPaymentMethod().toString(), Collectors.summingDouble(Payment::getAmount)));
            totals.forEach((method, amt) -> result.put(method, "RM " + String.format("%.2f", amt)));
        } else if (chartType.equals("dailyRevenue")) {
            List<Payment> payments = (List<Payment>) list;
            Map<String, Double> daily = new TreeMap<>();
            for (Payment p : payments) {
                daily.put(p.getDate(), daily.getOrDefault(p.getDate(), 0.0) + p.getAmount());
            }
            daily.forEach((date, total) -> result.put(date, "RM " + String.format("%.2f", total)));
        }

        return result;
    }
    
    private static String summarizePaymentStatus(String baseName, List<?> list) {
        return "";
    }

    private static String summarizePaymentMethod(String baseName, List<?> list) {
        return "";
    }

    private static String summarizeDailyRevenue(String baseName, List<?> list) {
        return "";
    }

    private static String summarizeRatingDistribution(String baseName, List<?> list) {
        List<Feedback> feedbacks = (List<Feedback>) list;
        Map<Integer, Long> counts = feedbacks.stream()
            .map(f -> {
                try {
                    return Integer.valueOf(f.getRating());
                } catch (Exception e) {
                    return 0; // handle non-numeric ratings
                }
            })
            .filter(r -> r > 0)
            .collect(Collectors.groupingBy(r -> r, Collectors.counting()));

        StringBuilder sb = new StringBuilder("Rating Distribution:\n");
        counts.forEach((rating, cnt) -> sb.append("Rating ").append(rating).append(": ").append(cnt).append("\n"));
        return sb.toString();
    }
    
    private static double parseRatingSafe(String ratingStr) {
        if (ratingStr == null) return 0.0;
        ratingStr = ratingStr.trim();
        if (ratingStr.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(ratingStr);
        } catch (NumberFormatException e) {
            // rating may be "null" or non-numeric; ignore it
            return 0.0;
        }
    }
    
    private static Map<String, String> loadAppointmentDoctorMap(String filePath) {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine(); // skip header if present
            String line;
            while ((line = br.readLine()) != null) {
                // split by comma; simple CSV parsing (works for your text file format)
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String appointmentId = parts[0].trim();
                    String doctorId = parts[3].trim();
                    if (!appointmentId.isEmpty() && !doctorId.isEmpty()) {
                        map.put(appointmentId, doctorId);
                    }
                }
            }
        } catch (IOException e) {
            // if file missing or unreadable, map remains empty and summarizer will fall back to "UNKNOWN"
            e.printStackTrace();
        }
        return map;
    }

    private static String summarizeAverageRatingByDoctor(String baseName, List<?> list) {
        List<Feedback> feedbacks = (List<Feedback>) list;
        
        // load appointment -> doctor map 
        Map<String, String> doctorID = loadAppointmentDoctorMap("data/appointment.txt");
    
        Map<String, Double> avgByDoctor = feedbacks.stream()
            .filter(f -> f.getAppointmentId() != null && f.getRating() != null)
            .map(f -> {
                String doctorId = doctorID.getOrDefault(f.getAppointmentId(), "UNKNOWN");
                double rating = parseRatingSafe(f.getRating());
                return new AbstractMap.SimpleEntry<>(doctorId, rating);
            })
            .filter(e -> e.getValue() > 0) // discard non-numeric or zero
            .collect(Collectors.groupingBy(
                    Map.Entry::getKey,
                    Collectors.averagingDouble(Map.Entry::getValue)
            ));

        StringBuilder sb = new StringBuilder("Average Ratings by Doctor:\n");
        avgByDoctor.forEach((doc, score) -> sb.append("Doctor ").append(doc).append(": ").append(String.format("%.2f", score)).append("\n"));
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) reportsDir.mkdirs();
            
            List<Appointment> appointments = List.of(); 
            List<Payment> payments = List.of(); 
            List<Feedback> feedbacks = List.of(); 
            
            // Empty charts for now (replace with real charts in your panels)
            Map<String, JFreeChart> emptyCharts = new HashMap<>();

            // Generate different reports
            generateReport(reportsDir, "AppointmentReport", "Appointments", appointments, emptyCharts);
            generateReport(reportsDir, "FinancialReport", "Financial", payments, emptyCharts);
            generateReport(reportsDir, "FeedbackReport", "Feedback", feedbacks, emptyCharts);

            System.out.println("Reports generated successfully in " + reportsDir.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
