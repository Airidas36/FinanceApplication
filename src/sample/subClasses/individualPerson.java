package sample.subClasses;

import sample.Classes.User;

public class individualPerson extends User {

    private final String phoneNum;
    private final String position;

    public individualPerson(String username, String password, String permissions, String email, String phoneNum, String position, boolean legalPerson) {
        super(username, password, permissions, email, legalPerson);
        this.phoneNum = phoneNum;
        this.position = position;
    }

    @Override
    public String toString() {
        return super.toString() + " Job position: " + position + " " + phoneNum;
    }
}
