
package com.mooviest.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {

    private int id;
    private int movie_lang_id;
    private int runtime;
    private int released;

    private String average;
    private String title;
    private String original_title;
    private String backdrop;
    private String image;
    private String synopsis;
    private String movie_producer;

    private Collection collection;

    private List<Genre> genres = new ArrayList<Genre>();
    private List<Participation> participations = new ArrayList<Participation>();
    private List<Rating> ratings = new ArrayList<Rating>();


    protected Movie(Parcel in) {
        id = in.readInt();
        movie_lang_id = in.readInt();
        runtime = in.readInt();
        released = in.readInt();


        average = in.readString();
        title = in.readString();
        original_title = in.readString();
        backdrop = in.readString();
        image = in.readString();
        synopsis = in.readString();
        movie_producer = in.readString();

        collection = in.readParcelable(getClass().getClassLoader());

        genres = new ArrayList<Genre>();
        in.readTypedList(genres, Genre.CREATOR);

        participations = new ArrayList<Participation>();
        in.readTypedList(participations, Participation.CREATOR);


        ratings = new ArrayList<Rating>();
        in.readTypedList(ratings, Rating.CREATOR);

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
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
     *     The movie_lang_id
     */
    public int getMovie_lang_id() {
        return movie_lang_id;
    }

    /**
     *
     * @param movie_lang_id
     *     The movie_lang_id
     */
    public void setMovie_lang_id(int movie_lang_id) {
        this.movie_lang_id = movie_lang_id;
    }

    /**
     *
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     *     The synopsis
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     *
     * @param synopsis
     *     The synopsis
     */
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
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
     *     The backdrop
     */
    public String getBackdrop() {
        return backdrop;
    }

    /**
     *
     * @param backdrop
     *     The backdrop
     */
    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    /**
     * 
     * @return
     *     The genres
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * 
     * @param genres
     *     The genres
     */
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    /**
     * 
     * @return
     *     The participations
     */
    public List<Participation> getParticipations() {
        return participations;
    }

    /**
     * 
     * @param participations
     *     The participations
     */
    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    /**
     * 
     * @return
     *     The ratings
     */
    public List<Rating> getRatings() {
        return ratings;
    }

    /**
     * 
     * @param ratings
     *     The ratings
     */
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    /**
     * 
     * @return
     *     The original_title
     */
    public String getOriginalTitle() {
        return original_title;
    }

    /**
     * 
     * @param original_title
     *     The original_title
     */
    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    /**
     * 
     * @return
     *     The runtime
     */
    public int getRuntime() {
        return runtime;
    }

    /**
     * 
     * @param runtime
     *     The runtime
     */
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    /**
     * 
     * @return
     *     The released
     */
    public int getReleased() {
        return released;
    }

    /**
     * 
     * @param released
     *     The released
     */
    public void setReleased(int released) {
        this.released = released;
    }

    /**
     * 
     * @return
     *     The movie_producer
     */
    public String getMovieProducer() {
        return movie_producer;
    }

    /**
     * 
     * @param movie_producer
     *     The movie_producer
     */
    public void setMovieProducer(String movie_producer) {
        this.movie_producer = movie_producer;
    }

    /**
     *
     * @return
     *     The collection
     */
    public Collection getCollection() {
        return collection;
    }

    /**
     *
     * @param collection
     *     The collection
     */
    public void setCollection(Collection collection) {
        this.collection = collection;
    }


    /**
     * 
     * @return
     *     The average
     */
    public String getAverage() {
        return average;
    }

    /**
     * 
     * @param average
     *     The average
     */
    public void setAverage(String average) {
        this.average = average;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(movie_lang_id);
        parcel.writeInt(runtime);
        parcel.writeInt(released);

        parcel.writeString(average);
        parcel.writeString(title);
        parcel.writeString(original_title);
        parcel.writeString(backdrop);
        parcel.writeString(image);
        parcel.writeString(synopsis);
        parcel.writeString(movie_producer);

        parcel.writeParcelable(collection, i);

        parcel.writeTypedList(genres);
        parcel.writeTypedList(participations);
        parcel.writeTypedList(ratings);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return id == movie.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
