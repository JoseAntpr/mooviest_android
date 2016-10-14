
package com.mooviest.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {

    private int id;
    private int movie_lang_id;
    private String title;
    private String image;
    private List<Genre> genres = new ArrayList<Genre>();
    private List<Participation> participations = new ArrayList<Participation>();
    private MovieLang langs;
    private List<Object> emotions = new ArrayList<Object>();
    private List<Rating> ratings = new ArrayList<Rating>();
    private String originalTitle;
    private int runtime;
    private int released;
    private String movieProducer;
    private String synopsis;
    private int sagaOrder;
    private String average;


    protected Movie(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        title = in.readString();
        image = in.readString();
        runtime = in.readInt();
        released = in.readInt();
        movieProducer = in.readString();
        sagaOrder = in.readInt();
        average = in.readString();
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
     *     The langs
     */
    public MovieLang getLangs() {
        return langs;
    }

    /**
     * 
     * @param langs
     *     The langs
     */
    public void setLangs(MovieLang langs) {
        this.langs = langs;
    }

    /**
     * 
     * @return
     *     The emotions
     */
    public List<Object> getEmotions() {
        return emotions;
    }

    /**
     * 
     * @param emotions
     *     The emotions
     */
    public void setEmotions(List<Object> emotions) {
        this.emotions = emotions;
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
     *     The originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * 
     * @param originalTitle
     *     The original_title
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
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
     *     The movieProducer
     */
    public String getMovieProducer() {
        return movieProducer;
    }

    /**
     * 
     * @param movieProducer
     *     The movie_producer
     */
    public void setMovieProducer(String movieProducer) {
        this.movieProducer = movieProducer;
    }

    /**
     * 
     * @return
     *     The sagaOrder
     */
    public int getSagaOrder() {
        return sagaOrder;
    }

    /**
     * 
     * @param sagaOrder
     *     The saga_order
     */
    public void setSagaOrder(int sagaOrder) {
        this.sagaOrder = sagaOrder;
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
        parcel.writeString(originalTitle);
        parcel.writeString(title);
        parcel.writeInt(runtime);
        parcel.writeInt(released);
        parcel.writeString(image);
        parcel.writeString(movieProducer);
        parcel.writeInt(sagaOrder);
        parcel.writeString(average);
    }
}
