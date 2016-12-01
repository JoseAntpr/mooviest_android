package com.mooviest.ui.rest.responses;

import com.mooviest.ui.models.User;

import java.util.ArrayList;

/**
 * Created by jesus on 1/12/16.
 */

public class SearchUsersResponse {

    private int count;
    private String next;
    private String previous;
    private ArrayList<User> results = new ArrayList<User>();

    /**
     *
     * @return
     *     The count
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @param count
     *     The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     *
     * @return
     *     The next
     */
    public String getNext() {
        return next;
    }

    /**
     *
     * @param next
     *     The next
     */
    public void setNext(String next) {
        this.next = next;
    }

    /**
     *
     * @return
     *     The previous
     */
    public String getPrevious() {
        return previous;
    }

    /**
     *
     * @param previous
     *     The previous
     */
    public void setPrevious(String previous) {
        this.previous = previous;
    }

    /**
     *
     * @return
     *     The results
     */
    public ArrayList<User> getMovies() {
        return results;
    }

    /**
     *
     * @param results
     *     The results
     */
    public void setMovies(ArrayList<User> results) {
        this.results = results;
    }
}
