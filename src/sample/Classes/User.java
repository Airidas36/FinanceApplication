package sample.Classes;

import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private String password;
    private String permissions;
    private final String email;
    private final boolean legalPerson;

    public User(String username, String password, String permissions, String email, boolean legalPerson) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
        this.legalPerson = legalPerson;
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPermissions() {
        return permissions;
    }

    public boolean isLegalPerson() {
        return legalPerson;
    }

    public void setPass(String newPass){
        this.password = newPass;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return username + " [" + permissions + "] " + "Contact info: " + email + "   " + legalPerson;
    }
}
