package com.mooviest.ui.rest;

import com.mooviest.ui.models.User;

/**
 * Created by jesus on 18/9/16.
 */
public class UserProfileResponse {
    private int status;
    private User user;

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
}
