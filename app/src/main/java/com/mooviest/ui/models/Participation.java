
package com.mooviest.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Participation implements Parcelable{

    private Celebrity celebrity;
    private String role;
    private String character;
    private String award;

    protected Participation(Parcel in) {
        celebrity = in.readParcelable(getClass().getClassLoader());
        role = in.readString();
        character = in.readString();
        award = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(celebrity,flags);
        dest.writeString(role);
        dest.writeString(character);
        dest.writeString(award);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Participation> CREATOR = new Creator<Participation>() {
        @Override
        public Participation createFromParcel(Parcel in) {
            return new Participation(in);
        }

        @Override
        public Participation[] newArray(int size) {
            return new Participation[size];
        }
    };

    /**
     * 
     * @return
     *     The celebrity
     */
    public Celebrity getCelebrity() {
        return celebrity;
    }

    /**
     * 
     * @param celebrity
     *     The celebrity
     */
    public void setCelebrity(Celebrity celebrity) {
        this.celebrity = celebrity;
    }

    /**
     * 
     * @return
     *     The role
     */
    public String getRole() {
        return role;
    }

    /**
     * 
     * @param role
     *     The role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 
     * @return
     *     The character
     */
    public String getCharacter() {
        return character;
    }

    /**
     * 
     * @param character
     *     The character
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     * 
     * @return
     *     The award
     */
    public String getAward() {
        return award;
    }

    /**
     * 
     * @param award
     *     The award
     */
    public void setAward(String award) {
        this.award = award;
    }


}
