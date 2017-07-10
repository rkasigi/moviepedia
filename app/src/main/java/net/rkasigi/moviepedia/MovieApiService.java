/*
 * This project was born to complete one of the terms of graduation from
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 *
 * July 6, 2017
 */
package net.rkasigi.moviepedia;

import android.content.Context;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * MovieApiService
 *
 * @author Rendi Kasigi
 * @version 1.0
 */
public class MovieApiService {
    private final String TAG = MovieApiService.class.getCanonicalName();

    private NetworkErrorListener networkErrorListener;


    public void setNetworkErrorListener(NetworkErrorListener networkErrorListener) {
        this.networkErrorListener = networkErrorListener;
    }

    public List<MovieEntity> getMovies(SortBy sortBy) {

        URL apiUrl = buildUrl(sortBy);
        List<MovieEntity> movies = null;

        try {
            String jsonResponse = getResponseFromHttpUrl(apiUrl);
            movies = jsonToMovieEntity(jsonResponse);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            triggerNetworkErrorListener(e.getMessage());
        }

        return movies;

    }

    private List<MovieEntity> jsonToMovieEntity(String jsonString) throws Exception {

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


            } else if (jsonObject.has("status_code") && jsonObject.has("status_message")) {
                throw new Exception(jsonObject.getString("status_message"));

            } else {
                Log.e(TAG, "return json can not identify: " + jsonString);
                throw new Exception("failed to identify data");
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            triggerNetworkErrorListener(e.getMessage());
        }

        return movies;

    }

    /**
     *
     * @param sortBy get result for sorting by
     * @return URL
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
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            triggerNetworkErrorListener(e.getMessage());
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

            InputStream in;
            if(urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                in = urlConnection.getInputStream();
            } else {
                in = urlConnection.getErrorStream();
            }

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

    private void triggerNetworkErrorListener(String errorMessage) {
        if(networkErrorListener != null) {
            networkErrorListener.invoke(networkErrorListener.getContext(), errorMessage);
        }

    }

    private String moviedbError(int responseHttpCode) {
        Map<Integer, String> errorMap = new HashMap<Integer, String>()
        {{
            put(200,	"Success.");
            put(501,	"Invalid service: this service does not exist.");
            put(401,	"Authentication failed: You do not have permissions to access the service.");
            put(405,	"Invalid format: This service doesn't exist in that format.");
            put(422,	"Invalid parameters: Your request parameters are incorrect.");
            put(404,	"Invalid id: The pre-requisite id is invalid or not found.");
            put(401,	"Invalid API key: You must be granted a valid key.");
            put(403,	"Duplicate entry: The data you tried to submit already exists.");
            put(503,	"Service offline: This service is temporarily offline, try again later.");
            put(401,	"Suspended API key: Access to your account has been suspended, contact TMDb.");
            put(500,	"Internal error: Something went wrong, contact TMDb.");
            put(201,	"The item/record was updated successfully.");
            put(200,	"The item/record was deleted successfully.");
            put(401,	"Authentication failed.");
            put(500,	"Failed.");
            put(401,	"Device denied.");
            put(401,	"Session denied.");
            put(400,	"Validation failed.");
            put(406,	"Invalid accept header.");
            put(422,	"Invalid date range: Should be a range no longer than 14 days.");
            put(200,	"Entry not found: The item you are trying to edit cannot be found.");
            put(400,	"Invalid page: Pages start at 1 and max at 1000. They are expected to be an integer.");
            put(400,	"Invalid date: Format needs to be YYYY-MM-DD.");
            put(504,	"Your request to the backend server timed out. Try again.");
            put(429,	"Your request count (#) is over the allowed limit of (40).");
            put(400,	"You must provide a username and password.");
            put(400,	"Too many append to response objects: The maximum number of remote calls is 20.");
            put(400,	"Invalid timezone: Please consult the documentation for a valid timezone.");
            put(400,	"You must confirm this action: Please provide a confirm=true parameter.");
            put(401,	"Invalid username and/or password: You did not provide a valid login.");
            put(401,	"Account disabled: Your account is no longer active. Contact TMDb if this is an error.");
            put(401,	"Email not verified: Your email address has not been verified.");
            put(401,	"Invalid request token: The request token is either expired or invalid.");
            put(401,	"The resource you requested could not be found.");
        }};

        return errorMap.get(responseHttpCode);
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
        SortBy(final String text) {
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

    public static abstract class NetworkErrorListener {

        Context context;

        NetworkErrorListener(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        abstract void invoke(Context context, String errorMessage);
    }
}
