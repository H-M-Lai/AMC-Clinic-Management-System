package javaproj.Methods.User;

import javaproj.Methods.IdGenerator.IdGenerator;
import javaproj.Methods.IdGenerator.FileIdGenerator;
import java.util.*;
import javaproj.Model.Role.Staff;
import javaproj.Model.Role.User;
import javaproj.Methods.Parser.StaffParser;
import javaproj.Repository.UserRepository;

public class StaffService extends AbstractUserService<Staff> {
    private final IdGenerator generator = new FileIdGenerator();

    public StaffService() {
        super(new UserRepository("data/staff.txt", new StaffParser()));
    }

    public String validateForm(String name, String ic, String address, String phone, String email) {
        return validateCommonFields(name, ic, address, phone, email);
    }

    public Staff create(String name, String ic, String address, String phone, String email) {
        List<String> ids = userIdList();
        String id = generator.nextId(ids, "S");
        Staff s = new Staff(id, name, ic, address, phone, email, "123");
        save(s);
        return s;
    }

    public boolean update(String id, String name, String ic, String address, String phone, String email) {
        List<? extends User> all = list();
        List<User> copy = new ArrayList<>(all);
        for (int i = 0; i < copy.size(); i++) {
            Staff old = (Staff) copy.get(i);
            if (old.getSystemId().equals(id)) {
                String preservedPwd = old.getPassword();
                Staff updated = new Staff(id, name, ic, address, phone, email, preservedPwd);
                copy.set(i, updated);
                repo.saveAll(copy);
                return true;
            }
        }
        return false;
    }

    public boolean delete(String id) {
        List<? extends User> all = list();
        List<User> copy = new ArrayList<>(all);
        boolean changed = copy.removeIf(u -> id.equals(u.getSystemId()));
        if (changed) repo.saveAll(copy);
        return changed;
    }
}