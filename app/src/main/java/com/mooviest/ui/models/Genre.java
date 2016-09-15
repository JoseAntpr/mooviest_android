
package com.mooviest.ui.models;

import java.util.ArrayList;
import java.util.List;

public class Genre {

    private List<GenreLang> langs = new ArrayList<GenreLang>();

    /**
     * 
     * @return
     *     The genreLangs
     */
    public List<GenreLang> getGenreLangs() {
        return langs;
    }

    /**
     * 
     * @param genreLangs
     *     The genreLangs
     */
    public void setGenreLangs(List<GenreLang> genreLangs) {
        this.langs = langs;
    }


}
