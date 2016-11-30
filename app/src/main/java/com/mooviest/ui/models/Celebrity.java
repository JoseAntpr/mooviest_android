
package com.mooviest.ui.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Celebrity implements Parcelable{

    private int id;
    private String name;
    private String born;
    private String image;
    private String twitterAccount;
    private String address;

    protected Celebrity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        born = in.readString();
        image = in.readString();
        twitterAccount = in.readString();
        address = in.readString();
    }

    public static final Creator<Celebrity> CREATOR = new Creator<Celebrity>() {
        @Override
        public Celebrity createFromParcel(Parcel in) {
            return new Celebrity(in);
        }

        @Override
        public Celebrity[] newArray(int size) {
            return new Celebrity[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(born);
        parcel.writeString(image);
        parcel.writeString(twitterAccount);
        parcel.writeString(address);
    }
}
