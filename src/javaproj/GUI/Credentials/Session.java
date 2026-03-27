package javaproj.GUI.Credentials;

import javaproj.Model.Role.User;

public class Session {
    private static User currentUser;
    
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }

    public static String getUserRole() {
        if (currentUser == null) return "Guest";
        return currentUser.getClass().getSimpleName(); 
    }
    
    public static String getUserRole(User user) {
        return user.getClass().getSimpleName(); 
    }
    
}
