
package com.mooviest.ui.models;

public class Participation {

    private Celebrity celebrity;
    private int role;
    private String character;
    private String award;

    /**
     * 
     * @return
     *     The celebrity
     */
    public Celebrity getCelebrity() {
        return celebrity;
    }

    /**
     * 
     * @param celebrity
     *     The celebrity
     */
    public void setCelebrity(Celebrity celebrity) {
        this.celebrity = celebrity;
    }

    /**
     * 
     * @return
     *     The role
     */
    public int getRole() {
        return role;
    }

    /**
     * 
     * @param role
     *     The role
     */
    public void setRole(int role) {
        this.role = role;
    }

    /**
     * 
     * @return
     *     The character
     */
    public String getCharacter() {
        return character;
    }

    /**
     * 
     * @param character
     *     The character
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     * 
     * @return
     *     The award
     */
    public String getAward() {
        return award;
    }

    /**
     * 
     * @param award
     *     The award
     */
    public void setAward(String award) {
        this.award = award;
    }


}
