package javaproj.Methods.Items;

import java.util.List;
import java.util.ArrayList;

import javaproj.Model.Medication;
import javaproj.Repository.ItemRepository;
import javaproj.Methods.Parser.MedicationsParser;
import javaproj.Utils.ReturnList;
import javaproj.Utils.Utils;

public class MedicationService {
    private final ItemRepository<Medication> repo;

    public MedicationService() {
        this.repo = new ItemRepository<>("data/medications.txt", new MedicationsParser());
    }

    public List<Medication> medicationList() {
        return repo.findAll();
    }

    public List<String> medicationNameList() {
        List<String> list = new ArrayList<>();
        for (Medication m : medicationList()) {
            list.add(m.getId() + " - " + m.getName());
        }
        return list;
    }

    public Medication loadMedication(String id) {
        for (Medication m : medicationList()) {
            if (m.getId().equals(id)) return m;
        }
        return null;
    }

    public void addMedication(String name, String description, double unitPrice, String category) {
        List<Medication> medications = medicationList();
        String id = Utils.idIncrement(medications, "X");
        Medication medication = new Medication(id, name, description, unitPrice, category);
        medications.add(medication);
        repo.saveAll(medications);
    }
    
    public boolean deleteMedication(String id) {
        List<Medication> all = new ArrayList<>(medicationList());
        boolean removed = all.removeIf(s -> id != null && id.equals(s.getId()));
        if (removed) repo.saveAll(all);
        return removed;
    }
    
    public boolean updateMedication(String id, String name, String desc, double price, String cat) {
        List<Medication> all = new ArrayList<>(medicationList());
        for (int i = 0; i < all.size(); i++) {
            if (id != null && id.equals(all.get(i).getId())) {
                all.set(i, new Medication(id, name, desc, price, cat));
                repo.saveAll(all);
                return true;
            }
        }
        return false;
}
    public List<String> ids() {
        List<Medication> all = medicationList();
        List<String> out = new ArrayList<>(all.size());
        for (Medication m : all) out.add(m.getId());
        return out;
    }

    public List<String> categories() {
        return ReturnList.get("therapeuticclass");
    }
}
