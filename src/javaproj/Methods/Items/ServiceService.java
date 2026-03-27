package javaproj.Methods.Items;

import java.util.*;
import javaproj.Model.Service;
import javaproj.Methods.Parser.ServiceParser;
import javaproj.Repository.ItemRepository;
import javaproj.Utils.ReturnList;
import javaproj.Utils.Utils;

public class ServiceService {
    private final ItemRepository<Service> repo;

    public ServiceService() {
        this.repo = new ItemRepository<>("data/services.txt", new ServiceParser());
    }

    public List<Service> serviceList() {
        return repo.findAll();
    }

    public List<String> serviceNameList() {
        List<String> list = new ArrayList<>();
        for (Service s : serviceList()) {
            list.add(s.getId() + " - " + s.getName());
        }
        return list;
    }

    public Service loadService(String id) {
        for (Service s : serviceList()) {
            if (id != null && id.equals(s.getId())) return s;
        }
        return null;
    }

    public void addService(String name, String description, double unitPrice, String category) {
        List<Service> services = new ArrayList<>(serviceList());
        String id = Utils.idIncrement(services, "V");
        services.add(new Service(id, name, description, unitPrice, category));
        repo.saveAll(services);
    }

    public boolean updateService(String id, String name, String desc, double price, String cat) {
        List<Service> all = new ArrayList<>(serviceList());
        for (int i = 0; i < all.size(); i++) {
            if (id != null && id.equals(all.get(i).getId())) {
                all.set(i, new Service(id, name, desc, price, cat));
                repo.saveAll(all);
                return true;
            }
        }
        return false;
    }

    public boolean deleteService(String id) {
        List<Service> all = new ArrayList<>(serviceList());
        boolean removed = all.removeIf(s -> id != null && id.equals(s.getId()));
        if (removed) repo.saveAll(all);
        return removed;
    }
    
    public List<String> ids() {
        List<Service> all = serviceList();          
        List<String> out = new ArrayList<>(all.size());
        for (Service s : all) out.add(s.getId());
        return out;
    }

    public List<String> categories() {
        return ReturnList.get("servicetype");
    }
}
