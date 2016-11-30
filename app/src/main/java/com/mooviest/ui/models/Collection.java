package com.mooviest.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jesus on 15/10/16.
 */

public class Collection implements Parcelable{

    private int id;
    private String typeMovie;
    private int user;
    private int movie;

    protected Collection(Parcel in) {
        id = in.readInt();
        typeMovie = in.readString();
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
     * The typeMovie
     */
    public String getTypeMovie() {
        return typeMovie;
    }

    /**
     *
     * @param type_movie
     * The typeMovie
     */
    public void setTypeMovie(String type_movie) {
        this.typeMovie = type_movie;
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
        parcel.writeString(typeMovie);
        parcel.writeInt(user);
        parcel.writeInt(movie);
    }
}
