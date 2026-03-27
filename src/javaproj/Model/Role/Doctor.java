/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Model.Role;

/**
 *
 * @author NICK
 */
public class Doctor extends User{

    String specialty;
    public Doctor(String systemId, 
            String name, 
            String identityNumber, 
            String address, 
            String phone, 
            String email,
            String specialty,
            String password) {
        super(systemId, name, identityNumber, address, phone, email, password);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    
    
    @Override
    public String toString() {
        return getSystemId() + "," + getName() + "," + getIdentityNumber() +  ",\"" + getAddress() + "\"," + getPhone() + "," + getEmail() + "," + specialty + "," + getPassword();
    }
}
