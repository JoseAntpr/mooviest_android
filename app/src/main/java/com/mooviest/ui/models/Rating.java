
package com.mooviest.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Rating implements Parcelable{

    private String name;
    private int rating;
    private int count;
    private String dateUpdate;

    protected Rating(Parcel in) {
        name = in.readString();
        rating = in.readInt();
        count = in.readInt();
        dateUpdate = in.readString();
    }

    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };

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
     *     The rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * 
     * @param rating
     *     The rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * 
     * @return
     *     The count
     */
    public int getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 
     * @return
     *     The dateUpdate
     */
    public String getDateUpdate() {
        return dateUpdate;
    }

    /**
     * 
     * @param dateUpdate
     *     The date_update
     */
    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(rating);
        parcel.writeInt(count);
        parcel.writeString(dateUpdate);
    }
}
