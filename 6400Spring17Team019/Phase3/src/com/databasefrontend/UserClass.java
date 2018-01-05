package com.databasefrontend;

/**
 * Created by adityasahai on 11/03/17.
 */
public class UserClass {

    private String username;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String siteId;

    public UserClass(String username, String email, String password, String firstname, String lastname, String siteId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.siteId = siteId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getSiteId() {
        return siteId;
    }
}
