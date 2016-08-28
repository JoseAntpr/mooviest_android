
package com.mooviest.ui.models;

import java.util.ArrayList;
import java.util.List;

public class MooviestApiResult {

    private int count;
    private String next;
    private String previous;
    private ArrayList<Movie> results = new ArrayList<Movie>();

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
    public ArrayList<Movie> getMovies() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setMovies(ArrayList<Movie> results) {
        this.results = results;
    }


}
