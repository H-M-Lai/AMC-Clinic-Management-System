package javaproj.Model.Role;

public class User {
    private String systemId;
    private String name;
    private String identityNumber;
    private String address;
    private String phone;
    private String email;
    private String password;

    public User(String systemId, String name, String identityNumber, String address, String phone, String email, String password) {
        this.systemId = systemId;
        this.name = name;
        this.identityNumber = identityNumber;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getName() {
        return name;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getAddress() {
        if (address != null && address.startsWith("\"") && address.endsWith("\"")) {
            return address.substring(1, address.length() - 1);
        }
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
}
