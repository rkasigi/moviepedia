/*
 * This project was born to complete one of the terms of graduation from
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 *
 * July 6, 2017
 */
package net.rkasigi.moviepedia;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * MovieApiService
 *
 * @author Rendi Kasigi
 * @version 1.0
 */
public class MovieApiService {


    public List<MovieEntity> getMovies(SortBy sortBy) {

        URL apiUrl = buildUrl(sortBy);
        List<MovieEntity> movies = null;

        try {
            String jsonResponse = getResponseFromHttpUrl(apiUrl);
            movies = jsonToMovieEntity(jsonResponse);

        } catch (Exception e) {
            Log.e(Config.APP_TAG, e.getMessage());
            e.printStackTrace();
        }

        return movies;

    }

    private List<MovieEntity> jsonToMovieEntity(String jsonString) {

        List<MovieEntity> movies = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has("results")) {

                JSONArray moviesJson = jsonObject.getJSONArray("results");

                if(moviesJson.length() > 0) {

                    movies = new ArrayList<>();
                    for (int i = 0; i < moviesJson.length(); i++) {
                        JSONObject movieJson = moviesJson.getJSONObject(i);

                        String imageUrl = "http://image.tmdb.org/t/p/w185" + movieJson.getString("poster_path");

                        MovieEntity movie =  new MovieEntity();
                        movie.setTitle(movieJson.getString("original_title"));
                        movie.setImage(imageUrl);
                        movie.setRating(movieJson.getString("vote_average"));
                        movie.setReleaseDate(movieJson.getString("release_date"));
                        movie.setSynopsis(movieJson.getString("overview"));

                        movies.add(movie);
                    }

                }


            }

        } catch (JSONException e) {
            Log.e(Config.APP_TAG, e.getMessage());
            e.printStackTrace();
        }

        return movies;

    }

    /**
     *
     * @param sortBy get result for sorting by
     * @return
     */
    private URL buildUrl(SortBy sortBy) {
        Uri builtUri = Uri.parse(Config.MOVIEDB_BASE_URL).buildUpon()
                .appendQueryParameter("api_key", Config.MOVIEDB_API_KEY)
                .appendQueryParameter("sort_by", sortBy.toString())
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(Config.APP_TAG, e.getMessage());
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public enum SortBy {
        POPULAR("popularity.desc"),
        RATED("vote_average.desc")
        ;

        private final String text;

        /**
         *
         * @param text
         */
        private SortBy(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }
}
