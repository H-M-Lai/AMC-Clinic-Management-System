/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
