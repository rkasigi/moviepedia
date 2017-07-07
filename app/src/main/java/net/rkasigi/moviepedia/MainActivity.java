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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity
 *
 * @author Rendi Kasigi
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener {

    private MovieApiService movieApiService = new MovieApiService();

    private MoviesAdapter mAdapter;
    private RecyclerView rvMovieList;
    private List<MovieEntity> moviesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMovies(MovieApiService.SortBy.POPULAR);

        rvMovieList = (RecyclerView) findViewById(R.id.rv_movie_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvMovieList.setLayoutManager(gridLayoutManager);
        rvMovieList.setHasFixedSize(true);

    }

    private void loadMovies(MovieApiService.SortBy sortBy) {
        new PullMovieTask().execute(sortBy);
    }

    private void loadMovies() {
        mAdapter = new MoviesAdapter(moviesList, this);
        rvMovieList.setAdapter(mAdapter);

    }

    @Override
    public void onMovieItemClick(int clickedItemIndex) {

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
            moviesList = movies;
            loadMovies();
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
