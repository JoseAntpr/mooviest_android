package com.mooviest.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jesus on 15/10/16.
 */

public class Collection implements Parcelable{

    private int id;
    private String type_movie;
    private int user;
    private int movie;

    protected Collection(Parcel in) {
        id = in.readInt();
        type_movie = in.readString();
        user = in.readInt();
        movie = in.readInt();
    }

    public static final Creator<Collection> CREATOR = new Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel in) {
            return new Collection(in);
        }

        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
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
     * The type_movie
     */
    public String getType_movie() {
        return type_movie;
    }

    /**
     *
     * @param type_movie
     * The type_movie
     */
    public void setType_movie(String type_movie) {
        this.type_movie = type_movie;
    }

    /**
     *
     * @return
     * The user
     */
    public int getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(int user) {
        this.user = user;
    }

    /**
     *
     * @return
     * The movie
     */
    public int getMovie() {
        return movie;
    }

    /**
     *
     * @param movie
     * The movie
     */
    public void setMovie(int movie) {
        this.movie = movie;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(type_movie);
        parcel.writeInt(user);
        parcel.writeInt(movie);
    }
}
