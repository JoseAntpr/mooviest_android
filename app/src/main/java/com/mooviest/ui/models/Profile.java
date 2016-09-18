package com.mooviest.ui.models;

/**
 * Created by jesus on 15/9/16.
 */
public class Profile {
    private Lang lang;
    private String avatar;

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

}
