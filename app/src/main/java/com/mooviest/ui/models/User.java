package com.mooviest.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jesus on 12/9/16.
 */
public class User implements Parcelable{

    private int id;
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private String password;
    private Profile profile;

    protected User(Parcel in) {
        id = in.readInt();
        first_name = in.readString();
        last_name = in.readString();
        username = in.readString();
        email = in.readString();
        profile = in.readParcelable(getClass().getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The username
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The first_name
     */
    public String getFirstName() {
        return first_name;
    }

    /**
     *
     * @param first_name
     * The first_name
     */
    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    /**
     *
     * @return
     * The username
     */
    public String getLastName() {
        return last_name;
    }

    /**
     *
     * @param last_name
     * The last_name
     */
    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     *
     * @param profile
     * The profile
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "Username: " + getUsername() + " Email: " + getEmail();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeParcelable(profile, i);
    }
}