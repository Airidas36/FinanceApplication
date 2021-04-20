package sample.subClasses;

import sample.Classes.User;

public class legalPerson extends User {

    private String companyName;
    private String website = "";

    public legalPerson(String username, String password, String permissions, String companyName, String email, String website, boolean legalPerson) {
        super(username, password, permissions, email, legalPerson);
        this.companyName = companyName;
        this.website = website;
    }

    @Override
    public String toString() {
        if(website != null)
            return super.toString() + " Company name: " + companyName + " Website: " + website;
        else return super.toString() + " " + companyName;
    }
}
