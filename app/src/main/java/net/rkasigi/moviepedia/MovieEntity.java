/*
 * This project was born to complete one of the terms of graduation from
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 *
 * July 6, 2017
 */
package net.rkasigi.moviepedia;

import java.io.Serializable;

/**
 * MovieEntity
 *
 * @author Rendi Kasigi
 * @version 1.0
 */
public class MovieEntity implements Serializable {

    private String title;
    private String image;
    private String rating;

    private String synopsis;
    private String releaseDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "MovieEntity{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", rating='" + rating + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
