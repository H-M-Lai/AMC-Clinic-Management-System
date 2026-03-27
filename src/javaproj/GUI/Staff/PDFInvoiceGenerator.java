package javaproj.GUI.Staff;

import javaproj.Model.ChargeItems;
import javaproj.Model.Medication;
import javaproj.Model.Service;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import javaproj.Methods.Items.ChargeItemService;
import javaproj.Methods.Items.MedicationService;
import javaproj.Methods.Items.ServiceService;
import javaproj.Model.Appointment;

import javaproj.Methods.User.CustomerService;
import javaproj.Model.Role.Customer;
import javaproj.Repository.AppointmentRepository;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


public class PDFInvoiceGenerator {
    private final AppointmentRepository appointmentRepo = new AppointmentRepository();
    private final CustomerService customerService = new CustomerService();
    private final MedicationService  medicationService  = new MedicationService();
    private final ServiceService serviceService = new ServiceService();
    private final ChargeItemService chargeItemsService = new ChargeItemService();
    
    private String[][] invoiceItems(String appointmentId){
        
        List<String[]> rows = new ArrayList<>();
        List<ChargeItems> chargeItems = chargeItemsService.chargeItemsList();
        List<Service> services = serviceService.serviceList();
        List<Medication> medications = medicationService.medicationList();
    
        Map<String, Service> serviceMap = new HashMap<>();
        for (Service s : services) {
            serviceMap.put(s.getId(), s);
        }

        Map<String, Medication> medMap = new HashMap<>();
        for (Medication m : medications) {
            medMap.put(m.getId(), m);
        }

        for (ChargeItems i : chargeItems){
            if (!appointmentId.equals(i.getAppointmentId())) continue;
            String name = "";
            double price = i.getUnitPrice();
            int quantity = i.getQuantity();
                
            String svcId = i.getServiceId();
            String medId = i.getMedicationId();
            if (svcId != null) { // service row
            Service s = serviceMap.get(svcId);
            if (s != null) {
                name = s.getName();
                price = s.getUnitPrice();
            } else {
                name = "Service " + svcId;
            }
        } else if (medId != null) { // medication row
            Medication m = medMap.get(medId);
            if (m != null) {
                name = m.getName();
                price = m.getUnitPrice();
            } else {
                name = "Medication " + medId;
            }
        }
            double lineTotal = price * quantity;
            rows.add(new String[]{
                    name,
                    String.valueOf(quantity),
                    String.format("%.2f",price),
                    String.format("%.2f", lineTotal)   
                });
        }
        return rows.toArray(new String[0][]) ;
    }
    
    public void generateInvoice(String appointmentId, String paymentId, double subTotal, String method, double totalPaid) throws IOException {
        
        LocalDate today = LocalDate.now();

        // Format it (e.g., 21/08/2025)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);
        
        PDDocument document = new PDDocument();

        // Create a landscape page by swapping width and height
        PDRectangle landscape = new PDRectangle(PDRectangle.LETTER.getWidth(), PDRectangle.LETTER.getHeight());
        PDPage page = new PDPage(landscape);
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);
        
        //logo
        PDImageXObject image = PDImageXObject.createFromFile("src/javaproj/image/logo.png", document); // replace with your image path
        content.drawImage(image, 30,680, 200, 100); // x, y, width, height
        
        //address
        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN, 10);
        content.newLineAtOffset(230, 750);
        String[] address = {"Jalan Teknologi 5,","Taman Teknologi Malaysia,"," 57000 Kuala Lumpur,","Wilayah Persekutuan Kuala Lumpur"};
        for (String line : address){
            content.showText(line);
            content.newLineAtOffset(0, -15);
        }
        content.endText();
        
        //receipt detail
        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN, 10);
        content.newLineAtOffset(460, 720);
        content.showText("Receipt ID: ");
        content.newLineAtOffset(0, -15);
        content.showText("Date: ");
        
        content.newLineAtOffset(60, 15);
        content.showText(paymentId);
        content.newLineAtOffset(0, -15);
        content.showText(formattedDate);
        content.endText();
        
        //patient information
        content.beginText();
        content.newLineAtOffset(50, 650);
        content.setFont(PDType1Font.TIMES_BOLD, 12);
        content.showText("Patient Information");
        content.newLineAtOffset(0, -20);
        content.setFont(PDType1Font.TIMES_ROMAN, 10);
        content.showText("Patient Name: ");
        content.newLineAtOffset(0, -20);
        content.showText("Patient IC: ");
        content.newLineAtOffset(0, -20);
        content.showText("Phone: ");
        
        Appointment appt = appointmentRepo.getById(appointmentId);
        Customer customer = (appt != null) ? customerService.getById(appt.getCustomerId()) : null;
        
        content.newLineAtOffset(100, 40);
        content.showText(customer.getName());
        content.newLineAtOffset(0, -20);
        content.showText(customer.getIdentityNumber());
        content.newLineAtOffset(0, -20);
        content.showText(customer.getPhone());
        content.endText();

        //line
        content.setStrokingColor(Color.BLACK);
        content.setLineWidth(1f);
        content.moveTo(30, 570);
        content.lineTo(582, 570);
        content.stroke();
        
        //table
        float margin = 50;
        float yStart = 550;
        float rowHeight = 20;
        
        //x-coordinate for all column
        float descriptionX = margin;
        float qtyX = margin + 370;
        float priceX  = margin + 400;
        float totalX = margin + 470;
        
        
        
        //header
        content.setFont(PDType1Font.TIMES_BOLD,10);
        
        content.beginText();
        content.newLineAtOffset(descriptionX, yStart);
        content.showText("Description");
        content.endText();

        content.beginText();
        content.newLineAtOffset(qtyX, yStart);
        content.showText("Qty");
        content.endText();
        
        content.beginText();
        content.newLineAtOffset(priceX, yStart);
        content.showText("Unit Price");
        content.endText();
        
        content.beginText();
        content.newLineAtOffset(totalX, yStart);
        content.showText("Total");
        content.endText();

        // Table rows
        content.setFont(PDType1Font.TIMES_ROMAN,10);
        String[][] rows = invoiceItems(appointmentId);

        for (int i = 0; i < rows.length; i++) {
            yStart -= rowHeight;

            // Description
            content.beginText();
            content.newLineAtOffset(descriptionX, yStart);
            content.showText(rows[i][0]);
            content.endText();

            // Qty
            content.beginText();
            content.newLineAtOffset(qtyX, yStart);
            content.showText(rows[i][1]);
            content.endText();

            // Price
            content.beginText();
            content.newLineAtOffset(priceX, yStart);
            content.showText(rows[i][2]);
            content.endText();

            // Total
            content.beginText();
            content.newLineAtOffset(totalX, yStart);
            content.showText(rows[i][3]);
            content.endText();
        }

        //summary
        
        
        content.moveTo(30, yStart-20);
        content.lineTo(582, yStart-20);
        content.stroke();
        content.beginText();
        content.newLineAtOffset(420,yStart-40);
        content.setFont(PDType1Font.TIMES_ROMAN, 10);
        content.showText("Subtotal: ");
        content.newLineAtOffset(0, -15);
        content.showText("Payment Method: ");
        content.newLineAtOffset(0, -15);
        content.showText("Total Paid: ");
        
        content.newLineAtOffset(100, 30);
        content.showText(String.format("%.2f",subTotal));
        content.newLineAtOffset(0, -15);
        content.showText(method);
        content.newLineAtOffset(0, -15);
        content.showText(String.format("%.2f",totalPaid));
        content.endText();
        content.close();
        
        document.save(new File("invoices/" + paymentId +".pdf"));
        document.close();
    }
}