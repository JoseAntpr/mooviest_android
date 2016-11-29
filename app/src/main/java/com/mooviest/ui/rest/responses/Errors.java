package com.mooviest.ui.rest.responses;

/**
 * Created by jesus on 12/9/16.
 */
import java.util.ArrayList;
import java.util.List;

public class Errors {

    private List<String> username = new ArrayList<String>();
    private List<String> email = new ArrayList<String>();
    private List<String> password = new ArrayList<String>();

    /**
     *
     * @return
     * The username
     */
    public List<String> getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(List<String> username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The email
     */
    public List<String> getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(List<String> email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The password
     */
    public List<String> getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(List<String> password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Username: " + getUsername().toString() + " Email: " + getEmail().toString() + " Password: "+ getPassword().toString();
    }

}