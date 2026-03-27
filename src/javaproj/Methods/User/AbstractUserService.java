package javaproj.Methods.User;

import java.util.*;
import java.util.stream.Collectors;
import javaproj.Model.Role.User;
import javaproj.Repository.UserRepository;

public abstract class AbstractUserService<T extends User> {
    protected final UserRepository repo;

    protected AbstractUserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<? extends User> list() {
        return new ArrayList<>(repo.findAll());
    }

    @SuppressWarnings("unchecked")
    public T getById(String id) {
        for (User u : list()) {
            if (Objects.equals(u.getSystemId(), id)) return (T) u;
        }
        return null;
    }

    public void save(User user) {
        List<User> all = new ArrayList<>(repo.findAll());
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (Objects.equals(all.get(i).getSystemId(), user.getSystemId())) {
                all.set(i, user);
                updated = true;
                break;
            }
        }
        if (!updated) {
            all.add(user);
        }
        repo.saveAll(all);
    }

    public boolean delete(String id) {
        List<User> all = new ArrayList<>(repo.findAll());
        boolean changed = all.removeIf(u -> Objects.equals(u.getSystemId(), id));
        if (changed) repo.saveAll(all);
        return changed;
    }

    public Map<String, String> nameMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (User u : list()) {
            map.put(u.getSystemId(), u.getName());
        }
        return map;
    }


    public List<User> search(String query) {
        return search(list(), query);
    }

    public List<User> search(List<? extends User> all, String query) {
        String q = (query == null ? "" : query.trim().toLowerCase());
        List<User> out = new ArrayList<>();
        for (User u : all) {
            boolean match =
                q.isEmpty()
                || (u.getName() != null            && u.getName().toLowerCase().contains(q))
                || (u.getIdentityNumber() != null  && u.getIdentityNumber().toLowerCase().contains(q))
                || (u.getEmail() != null           && u.getEmail().toLowerCase().contains(q))
                || (u.getPhone() != null           && u.getPhone().toLowerCase().contains(q))
                || (u.getAddress() != null         && u.getAddress().toLowerCase().contains(q));
            if (match) out.add(u);
        }
        return out;
    }

    public List<String> userIdList() {
        return list().stream()
                .map(User::getSystemId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    //validate user info
    protected String validateCommonFields(String name, String ic, String address, String phone, String email) {
        if (name == null || name.trim().isEmpty()) return "Name is required";
        if (ic == null || !ic.matches("\\d{12}")) return "IC must be 12 digits";
        if (address == null || address.trim().isEmpty()) return "Address is required";
        if (phone == null || !phone.matches("\\d{10,11}")) return "Phone must be 10–11 digits";
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) return "Invalid email format";
        return null;
    }
    
    // for edit profile in common panel to use
    public String validateBasicFields(String name, String ic, String address, String phone, String email) {
        return validateCommonFields(name, ic, address, phone, email);
    }
    
    public boolean changePassword(User currentUser, String currentPw, String newPw, String reconfirmPw) {
        
        String cur = currentPw == null ? "" : currentPw.trim(); //current password from user input
        String db  = Optional.ofNullable(currentUser.getPassword()).orElse("").trim();  //current password stored in object
        String nw  = newPw == null ? "" : newPw.trim(); //new password
        String rc  = reconfirmPw == null ? "" : reconfirmPw.trim(); //authenticate password again

        if (!Objects.equals(cur, db)) return false; //compare both current password
        if (!Objects.equals(nw, rc))  return false; //compare both new password

        //update user
        currentUser.setPassword(nw);
        //generate the list
        List<? extends User> all = list();
        List<User> copy = new ArrayList<>(all);
        //update the list by using index
        for (int i = 0; i < copy.size(); i++) {
            if (Objects.equals(copy.get(i).getSystemId(), currentUser.getSystemId())) {
                copy.set(i, currentUser);
                repo.saveAll(copy);
                return true;
            }
        }
        return false;
    }
    
    public boolean resetPassword(User targetUser, String newPassword) {
        List<? extends User> all = list();
        List<User> copy = new ArrayList<>(all);
        for (int i = 0; i < copy.size(); i++) {
            if (Objects.equals(copy.get(i).getSystemId(), targetUser.getSystemId())) {
                copy.get(i).setPassword(newPassword == null ? "" : newPassword);
                repo.saveAll(copy);
                return true;
            }
        }
        return false;
    }

    public String nameFromId(String id) {
        User u = getById(id);
        return u == null ? null : u.getName();
    }

    public String idFromIc(String ic) {
        for (User u : list()) {
            if (Objects.equals(u.getIdentityNumber(), ic)) return u.getSystemId();
        }
        return null;
    }
}