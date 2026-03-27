/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.User;

import javaproj.Methods.IdGenerator.IdGenerator;
import javaproj.Methods.IdGenerator.FileIdGenerator;
import java.util.*;
import javaproj.Model.Role.Manager;
import javaproj.Model.Role.User;
import javaproj.Methods.Parser.ManagerParser;
import javaproj.Repository.UserRepository;

/**
 *
 * @author NICK
 */
public class ManagerService extends AbstractUserService<Manager> {

    private final IdGenerator generator = new FileIdGenerator();

    // Services used for cross-role uniqueness checks
    private final CustomerService customerService = new CustomerService();
    private final DoctorService doctorService = new DoctorService();
    private final StaffService staffService = new StaffService();

    public ManagerService() {
        super(new UserRepository("data/manager.txt", new ManagerParser()));
    }

    private List<User> allUsersForUniqueness() {
        List<User> all = new ArrayList<>();
        all.addAll(customerService.list()); // customers
        all.addAll(doctorService.list()); // doctors
        all.addAll(staffService.list()); // staff
        all.addAll(this.list()); // managers
        return all;
    }

    public String validateForm(Manager loggedInManager, String currentUserId,
                                String selectedRole, String name, String ic,
                                String address, String phone, String email, String specialty) {
        
        String commonError = validateCommonFields(name, ic, address, phone, email);
        if (commonError != null) return commonError;

        if ("doctor".equalsIgnoreCase(selectedRole)
                && (specialty == null || specialty.trim().isEmpty())) {
            return "Specialty is required for doctor.";
        }

        for (User other : allUsersForUniqueness()) {
            if (Objects.equals(other.getSystemId(), currentUserId)) continue; // skip self on update
            if (ic != null && ic.trim().equals(other.getIdentityNumber())) {
                return "IC already exists.";
            }
            String otherEmail = other.getEmail();
            if (email != null && otherEmail != null &&
                otherEmail.trim().equalsIgnoreCase(email.trim())) {
                return "Email already exists.";
            }
        }
        return null;
    }

    public Manager create(String name, String ic, String address, String phone, String email) {
        List<String> ids = userIdList();
        String id = generator.nextId(ids, "M");   
        Manager m = new Manager(id, name, ic, address, phone, email, "123");
        save(m);
        return m;
    }

    public boolean updateSelfRole(String id, String name, String ic, String address, String phone, String email) {
        List<? extends User> all = list();
        List<User> copy = new ArrayList<>(all);

        for (int i = 0; i < copy.size(); i++) {
            Manager old = (Manager) copy.get(i);
            if (Objects.equals(old.getSystemId(), id)) {
                String preservedPwd = old.getPassword();
                Manager updated = new Manager(id, name, ic, address, phone, email, preservedPwd);
                copy.set(i, updated);
                repo.saveAll(copy);
                return true;
            }
        }
        return false;
    }

    public boolean deleteManager(String id) {
        List<? extends User> all = list();
        List<User> copy = new ArrayList<>(all);
        boolean changed = copy.removeIf(u -> Objects.equals(u.getSystemId(), id));
        if (changed) repo.saveAll(copy);
        return changed;
    }
}
