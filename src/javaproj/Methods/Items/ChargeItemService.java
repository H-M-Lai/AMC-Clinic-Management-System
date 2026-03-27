/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javaproj.Model.ChargeItems;
import javaproj.Model.Medication;
import javaproj.Model.Service;
import javaproj.Repository.ChargeItemsRepository;
import javaproj.Utils.Utils;

/**
 *
 * @author NICK
 */
public class ChargeItemService {

    private final ChargeItemsRepository repo;
    private final MedicationService medicationService;
    private final ServiceService serviceService;

    public ChargeItemService() {
        this.repo = new ChargeItemsRepository();
        this.medicationService = new MedicationService();
        this.serviceService = new ServiceService();
    }

    public ChargeItemService(ChargeItemsRepository repo, MedicationService medicationService,ServiceService serviceService) {
        this.repo = repo;
        this.medicationService = medicationService;
        this.serviceService = serviceService;
    }

    public List<ChargeItems> chargeItemsList() {
        return repo.findAll();
    }

    public List<ChargeItems> chargeItemsList(String appointmentId) {
        List<ChargeItems> out = new ArrayList<>();
        for (ChargeItems c : repo.findAll()) {
            if (Objects.equals(appointmentId, c.getAppointmentId())) {
                out.add(c);
            }
        }
        return out;
    }

    public void add(ChargeItems item) {
        List<ChargeItems> all = new ArrayList<>(repo.findAll());
        all.add(item);
        repo.saveAll(all);
    }

    public void addAll(List<ChargeItems> items) {
        List<ChargeItems> all = new ArrayList<>(repo.findAll());
        all.addAll(items);
        repo.saveAll(all);
    }

    public ChargeItems createAndAdd(String appointmentId, String serviceId,String medicationId, int quantity, double unitPrice, double totalAmount, String description) {
        List<ChargeItems> all = new ArrayList<>(repo.findAll());
        String id = Utils.idIncrement(all, "I");

        ChargeItems row = new ChargeItems(
                id,
                appointmentId,
                serviceId,
                medicationId,
                quantity,
                unitPrice,
                totalAmount,
                description
        );

        all.add(row);
        repo.saveAll(all);
        return row;
    }

    public boolean delete(String chargeItemId) {
        List<ChargeItems> all = new ArrayList<>(repo.findAll());
        boolean removed = all.removeIf(ci -> Objects.equals(chargeItemId, ci.getChargeItemId()));
        if (removed) repo.saveAll(all);
        return removed;
    }

    //convert the id in chargeitem txt into name
    public String getNameByChargeItemId(String chargeItemId) {
        for (ChargeItems c : repo.findAll()) {
            if (Objects.equals(chargeItemId, c.getChargeItemId())) {
                if (c.getMedicationId() != null) {
                    Medication m = medicationService.loadMedication(c.getMedicationId());
                    return (m != null) ? m.getName() : "null";
                } else if (c.getServiceId() != null) {
                    Service s = serviceService.loadService(c.getServiceId());
                    return (s != null) ? s.getName() : "null";
                }
                break;
            }
        }
        return "null";
    }

}