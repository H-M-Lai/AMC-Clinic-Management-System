package javaproj.Model;

import java.util.Objects;

public class ChargeItems {
    private String chargeItemId, appointmentId, serviceId, medicationId, description;
    private int quantity;
    private double unitPrice, totalAmount;

    public ChargeItems(String chargeItemId, String appointmentId, String serviceId, String medicationId,
                       int quantity, double unitPrice, double totalAmount,
                      String description) {
        this.chargeItemId = chargeItemId;
        this.appointmentId = appointmentId;
        this.serviceId = (serviceId == null || Objects.equals(serviceId, "NULL")) ? null : serviceId;
        this.medicationId = (medicationId == null || Objects.equals(medicationId, "NULL")) ? null : medicationId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.description = description;
    }

    public String getChargeItemId() {
        return chargeItemId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getMedicationId() {
        return medicationId;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return String.join(",",
                chargeItemId,
                appointmentId,
                serviceId == null ? "NULL" : serviceId,
                medicationId == null ? "NULL" : medicationId,
                String.valueOf(quantity),
                String.format("%.2f", unitPrice),
                String.format("%.2f", totalAmount),
                description == null || description.isEmpty() ? "N/A" : description
        );
    }
}
