
package com.mooviest.ui.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    private int id;
    private List<Genre> genres = new ArrayList<Genre>();
    private List<Participation> participations = new ArrayList<Participation>();
    private List<Lang_> langs = new ArrayList<Lang_>();
    private List<Object> emotions = new ArrayList<Object>();
    private List<Rating> ratings = new ArrayList<Rating>();
    private String originalTitle;
    private int runtime;
    private int released;
    private String movieProducer;
    private int sagaOrder;
    private String average;

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
    public List<Lang_> getLangs() {
        return langs;
    }

    /**
     * 
     * @param langs
     *     The langs
     */
    public void setLangs(List<Lang_> langs) {
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


}
