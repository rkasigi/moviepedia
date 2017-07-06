/*
 * This project was born to complete one of the terms of graduation from
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 *
 * July 6, 2016
 */
package net.rkasigi.moviepedia;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

/**
 * MainActivity
 *
 * @author Rendi Kasigi
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private MovieApiService movieApiService = new MovieApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadMovies(MovieApiService.SortBy.POPULAR);
    }

    private void loadMovies(MovieApiService.SortBy sortBy) {
        new PullMovieTask().execute(sortBy);
    }

    private class PullMovieTask extends AsyncTask<MovieApiService.SortBy, Void, List<MovieEntity>> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            showProgress();
            super.onPreExecute();
        }

        @Override
        protected List<MovieEntity> doInBackground(MovieApiService.SortBy... params) {
            return movieApiService.getMovies(params[0]);
        }

        @Override
        protected void onPostExecute(List<MovieEntity> movies) {
            hideProgress();
            //super.onPostExecute(movies);
            for(MovieEntity m: movies) {
                Log.d(Config.APP_TAG, m.toString());
            }
        }

        private void showProgress() {
            if (!(mProgressDialog != null && mProgressDialog.isShowing())) {
                mProgressDialog = ProgressDialog.show(MainActivity.this, "Wait...", "sending data ...", true);
                mProgressDialog.setCancelable(false);
            }

        }

        private void hideProgress() {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }
}
