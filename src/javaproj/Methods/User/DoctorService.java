/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.User;

import javaproj.Methods.IdGenerator.IdGenerator;
import javaproj.Methods.IdGenerator.FileIdGenerator;
import java.util.*;
import javaproj.Model.Role.Doctor;
import javaproj.Model.Role.User;
import javaproj.Methods.Parser.DoctorParser;
import javaproj.Repository.UserRepository;

/**
 *
 * @author NICK
 */
public class DoctorService extends AbstractUserService<Doctor> {

    private final IdGenerator generator = new FileIdGenerator();

    public DoctorService() {
        super(new UserRepository("data/doctor.txt", new DoctorParser()));
    }

    @SuppressWarnings("unchecked")
    public List<Doctor> doctors() {
        return (List<Doctor>) (List<?>) super.list();
    }

    public String validateForm(String name, String ic, String address, String phone, String email, String specialty) {
        // check common fields
        String commonError = validateCommonFields(name, ic, address, phone, email);
        if (commonError != null) return commonError;

        // doctor-specific check
        if (specialty == null || specialty.trim().isEmpty()) {
            return "Specialty is required for doctor";
        }

        return null; // means OK
    }

    public Doctor create(String name, String ic, String address, String phone, String email, String specialty) {
        List<String> ids = userIdList();
        String id = generator.nextId(ids, "D");
        Doctor d = new Doctor(id, name, ic, address, phone, email, specialty, "123");
        save(d);
        return d;
    }

    public boolean update(String id, String name, String ic, String address, String phone, String email, String specialty) {
        List<? extends User> all = doctors();
        List<User> copy = new ArrayList<>(all);

        for (int i = 0; i < copy.size(); i++) {
            Doctor old = (Doctor) copy.get(i);
            if (old.getSystemId().equals(id)) {
                String preservedPwd = old.getPassword();
                Doctor updated = new Doctor(id, name, ic, address, phone, email, specialty, preservedPwd);
                copy.set(i, updated);
                repo.saveAll(copy);
                return true;
            }
        }
        return false;
    }

    public boolean deleteDoctor(String id) {
        List<? extends User> all = doctors();
        List<User> copy = new ArrayList<>(all);
        boolean changed = copy.removeIf(u -> id.equals(u.getSystemId()));
        if (changed) repo.saveAll(copy);
        return changed;
    }

    public Map<String, Doctor> mapById() {
        Map<String, Doctor> m = new LinkedHashMap<>();
        for (Doctor d : doctors()) m.put(d.getSystemId(), d);
        return m;
    }
}
