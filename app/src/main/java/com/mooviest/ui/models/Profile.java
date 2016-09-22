package com.mooviest.ui.models;

/**
 * Created by jesus on 15/9/16.
 */
public class Profile {
    private Lang lang;
    private String avatar;
    private String city;
    private String born;
    private String gender;
    private String postalCode;

    /**
     *
     * @return
     * The lang
     */
    public Lang getLang() {
        return lang;
    }

    /**
     *
     * @param lang
     * The lang
     */
    public void setLang(Lang lang) {
        this.lang = lang;
    }

    /**
     *
     * @return
     * The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     *
     * @param avatar
     * The avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     *
     * @return
     * The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     * The born
     */
    public String getBorn() {
        return born;
    }

    /**
     *
     * @param born
     * The born
     */
    public void setBorn(String born) {
        this.born = born;
    }

    /**
     *
     * @return
     * The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     * The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     * The city
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     *
     * @param postalCode
     * The postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

}
