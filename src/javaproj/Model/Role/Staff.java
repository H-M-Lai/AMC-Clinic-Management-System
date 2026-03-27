package javaproj.Model.Role;


public class Staff extends User{

    public Staff(String systemId, 
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
        return getSystemId() + "," + getName() + "," + getIdentityNumber() + ",\"" + getAddress() + "\","  + getPhone() + "," + getEmail() + "," + getPassword();
    }
}