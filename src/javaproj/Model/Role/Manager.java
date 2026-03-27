package javaproj.Model.Role;

import java.util.ArrayList;
import java.util.List;
//import javaproj.Services.UserService;

public class Manager extends User{
    //UserService userService = new UserService();
    public Manager(String systemId, 
            String name, 
            String identityNumber, 
            String address, 
            String phone, 
            String email, 
            String password) {
        super(systemId, name, identityNumber, address, phone, email, password);
    }
    
    
    @Override
    public String toString() {
        return getSystemId() + "," + getName() + "," + getIdentityNumber() + ",\"" + getAddress() + "\"," + getPhone() + "," +getEmail() + "," + getPassword();
    }
//    public List<User> getUserList() {
//        // Manager cannot manage other managers
//        List<User> users = new ArrayList<>();
//        try {
//            String[] roles = {"Staff", "Doctor", "Customer"};
//            for (String role : roles) {
//                for (String id : userService.userIdList(role)) {
//                    users.add(userService.userInformation(role, id));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return users;
//    }
//    
//    // this is only for the validation purpose
//    public List<User> getAllUsers() {
//        List<User> users = new ArrayList<>();
//        try {
//            String[] roles = {"supermanager", "manager", "staff", "doctor", "customer"};
//            for (String role : roles) {
//                for (String id : userService.userIdList(role)) {
//                    users.add(userService.userInformation(role, id));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return users;
//    }
//    
}