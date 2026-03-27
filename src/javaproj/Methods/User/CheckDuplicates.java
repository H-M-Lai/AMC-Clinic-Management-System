/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javaproj.Model.Role.User;

/**
 *
 * @author NICK
 */
public final class CheckDuplicates {
    private final CustomerService customerService;
    private final DoctorService   doctorService;
    private final StaffService    staffService;
    private final ManagerService  managerService;

    // Public constructor: you can create it once and reuse it
    public CheckDuplicates() {
        this.customerService = new CustomerService();
        this.doctorService   = new DoctorService();
        this.staffService    = new StaffService();
        this.managerService  = new ManagerService();
    }

    private List<User> allUsers() {
        List<User> all = new ArrayList<>();
        all.addAll(customerService.list());
        all.addAll(doctorService.list());
        all.addAll(staffService.list());
        all.addAll(managerService.list());
        return all;
    }

    public String checkDuplicates(String currentUserId, String ic, String email) {
        for (User other : allUsers()) {
            if (Objects.equals(other.getSystemId(), currentUserId)) continue; // skip self
            if (ic != null && ic.trim().equals(other.getIdentityNumber())) {
                return "This IC is already registered by other user.";
            }
            String otherEmail = other.getEmail();
            if (email != null && otherEmail != null &&
                otherEmail.trim().equalsIgnoreCase(email.trim())) {
                return "This Email is already registered by other user.";
            }
        }
        return null;
    }
    
}
