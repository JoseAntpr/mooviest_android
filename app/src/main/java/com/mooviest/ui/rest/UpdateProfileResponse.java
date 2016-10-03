package com.mooviest.ui.rest;

import com.mooviest.ui.models.User;

/**
 * Created by jesus on 3/10/16.
 */

public class UpdateProfileResponse {

    private Errors errors;
    private User user;
    private int status;

    /**
     *
     * @return
     * The errors
     */
    public Errors getErrors() {
        return errors;
    }

    /**
     *
     * @param errors
     * The errors
     */
    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    /**
     *
     * @return
     * The user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     * The status
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateProfileResponse{" +
                "errors=" + errors.toString() +
                ", user=" + user.toString() +
                ", status=" + status +
                '}';
    }
}
