
package com.mooviest.ui.models;


public class Celebrity {

    private int id;
    private String name;
    private String born;
    private String image;
    private String twitterAccount;
    private String address;

    /**
     * 
     * @return
     *     The id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The born
     */
    public String getBorn() {
        return born;
    }

    /**
     * 
     * @param born
     *     The born
     */
    public void setBorn(String born) {
        this.born = born;
    }

    /**
     * 
     * @return
     *     The image
     */
    public String getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The twitterAccount
     */
    public String getTwitterAccount() {
        return twitterAccount;
    }

    /**
     * 
     * @param twitterAccount
     *     The twitter_account
     */
    public void setTwitterAccount(String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(String address) {
        this.address = address;
    }


}
