package javaproj.Model.Role;

import java.util.*;
//import javaproj.Services.UserService;

public class Supermanager extends Manager{
    //UserService userService = new UserService();
    public Supermanager(String systemId, String name, String identityNumber, String address, String phone, String email, String password) {
        super(systemId, name, identityNumber, address, phone, email, password);
    }
    
//    @Override
//    public List<User> getUserList() {
//        List<User> users = new ArrayList<>();
//        try {
//            String[] roles = {"Manager", "Staff", "Doctor", "Customer"};
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
}
