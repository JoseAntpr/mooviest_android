package com.mooviest.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jesus on 15/9/16.
 */
public class Profile implements Parcelable{
    private Lang lang;
    private String avatar;
    private String city;
    private Date born;
    private String gender;
    private String postalCode;

    protected Profile(Parcel in) {
        avatar = in.readString();
        city = in.readString();
        gender = in.readString();
        postalCode = in.readString();
        lang = in.readParcelable(getClass().getClassLoader());
        born = new Date(in.readLong());
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

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
    public Date getBorn() {
        return born;
    }

    /**
     *
     * @param born
     * The born
     */
    public void setBorn(Date born) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(avatar);
        parcel.writeString(city);
        parcel.writeString(gender);
        parcel.writeString(postalCode);
        parcel.writeParcelable(lang, i);
        parcel.writeLong(born.getTime());
    }
}
