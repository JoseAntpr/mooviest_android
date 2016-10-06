package com.mooviest.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jesus on 15/9/16.
 */
public class Lang implements Parcelable{

    private String code;

    protected Lang(Parcel in) {
        code = in.readString();
    }

    public static final Creator<Lang> CREATOR = new Creator<Lang>() {
        @Override
        public Lang createFromParcel(Parcel in) {
            return new Lang(in);
        }

        @Override
        public Lang[] newArray(int size) {
            return new Lang[size];
        }
    };

    /**
     *
     * @return
     * The code
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
    }
}