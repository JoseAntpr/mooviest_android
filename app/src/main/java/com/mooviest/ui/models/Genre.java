
package com.mooviest.ui.models;

import java.util.ArrayList;
import java.util.List;

public class Genre {

    private List<Lang> langs = new ArrayList<Lang>();

    /**
     * 
     * @return
     *     The langs
     */
    public List<Lang> getLangs() {
        return langs;
    }

    /**
     * 
     * @param langs
     *     The langs
     */
    public void setLangs(List<Lang> langs) {
        this.langs = langs;
    }


}
