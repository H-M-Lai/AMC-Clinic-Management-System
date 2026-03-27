/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.User;

import javaproj.Methods.IdGenerator.IdGenerator;
import javaproj.Methods.IdGenerator.FileIdGenerator;
import java.util.*;
import javaproj.Model.Role.Customer;
import javaproj.Model.Role.User;
import javaproj.Methods.Parser.CustomerParser;
import javaproj.Repository.UserRepository;

/**
 *
 * @author NICK
 */
public class CustomerService extends AbstractUserService<Customer> {

    private final IdGenerator generator = new FileIdGenerator();

    public CustomerService() {
        super(new UserRepository("data/Customer.txt", new CustomerParser()));
    }

    @SuppressWarnings("unchecked")
    public List<Customer> customers() {
        return (List<Customer>) (List<?>) super.list();
    }

    public boolean checkDuplicates(String ic, String email) {
        for (Customer c : customers()) {
            if (c.getIdentityNumber().equals(ic)) return false;
            if (c.getEmail().equalsIgnoreCase(email)) return false;
        }
        return true;
    }

    public String validateForm(String name, String ic, String address, String phone, String email) {
        return validateCommonFields(name, ic, address, phone, email);
    }

    //used in manager
    public Customer create(String name, String ic, String address, String phone, String email) {
        List<String> ids = userIdList();
        String id = generator.nextId(ids, "C"); 
        Customer c = new Customer(id, name, ic, address, phone, email, "123");
        save(c);
        return c;
    }

    //used in signup account, additional add password
    public Customer signup(String name, String ic, String address, String phone, String email, String password) {
        List<String> ids = userIdList();
        String id = generator.nextId(ids, "C");
        Customer c = new Customer(id, name, ic, address, phone, email, password);
        save(c);
        return c;
    }

    //update profile for staff and manager use
    public boolean update(String id, String name, String ic, String address, String phone, String email) {
        List<? extends User> all = customers();
        List<User> copy = new ArrayList<>(all);

        for (int i = 0; i < copy.size(); i++) {
            Customer old = (Customer) copy.get(i);
            if (old.getSystemId().equals(id)) {
                String preservedPwd = old.getPassword();

                Customer updated = new Customer(id, name, ic, address, phone, email, preservedPwd);

                copy.set(i, updated);
                repo.saveAll(copy);
                return true;
            }
        }
        return false;
    }

    public boolean deleteCustomer(String id) {
        List<? extends User> all = customers();
        List<User> copy = new ArrayList<>(all);
        boolean changed = copy.removeIf(u -> id.equals(u.getSystemId()));
        if (changed) repo.saveAll(copy);
        return changed;
    }
    
    public Map<String, Customer> mapById() {
    List<Customer> customers = customers(); 
    Map<String, Customer> map = new LinkedHashMap<>();
    for (Customer c : customers) {
        map.put(c.getSystemId(), c);
    }
    return map;
}
}

