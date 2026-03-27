package javaproj.Methods.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javaproj.Model.Role.Manager;
import javaproj.Model.Role.Supermanager;
import javaproj.Model.Role.User;
import javaproj.Methods.Parser.SupermanagerParser;
import javaproj.Repository.UserRepository;

public class SuperManagerService extends AbstractUserService<Supermanager> {

    private final ManagerService managerService  = new ManagerService();
    private final CustomerService customerService = new CustomerService();
    private final DoctorService doctorService   = new DoctorService();
    private final StaffService staffService    = new StaffService();

    public SuperManagerService() {
        super(new UserRepository("data/supermanager.txt", new SupermanagerParser()));
    }

    private List<User> allUsersForUniqueness() {
        List<User> all = new ArrayList<>();
        all.addAll(customerService.list());   
        all.addAll(doctorService.list());    
        all.addAll(staffService.list());     
        all.addAll(managerService.list());    
        all.addAll(this.list());             
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

    public boolean updateManager(String id, String name, String ic, String address, String phone, String email) {
        return managerService.updateSelfRole(id, name, ic, address, phone, email);
    }

    public Manager createManager(String name, String ic, String address, String phone, String email) {
        return managerService.create(name, ic, address, phone, email); 
    }

    public boolean deleteManager(String id) {
        return managerService.deleteManager(id);
    }
}