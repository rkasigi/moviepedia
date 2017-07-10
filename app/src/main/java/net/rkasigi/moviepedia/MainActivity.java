/*
 * This project was born to complete one of the terms of graduation from
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 *
 * July 6, 2017
 */
package net.rkasigi.moviepedia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity
 *
 * @author Rendi Kasigi
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener {

    private final MovieApiService movieApiService = new MovieApiService();


    private RecyclerView rvMovieList;
    public TextView tvMovieMessage;
    private List<MovieEntity> moviesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        rvMovieList = (RecyclerView) findViewById(R.id.rv_movie_list);
        tvMovieMessage = (TextView) findViewById(R.id.tv_movie_message);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvMovieList.setLayoutManager(gridLayoutManager);
        rvMovieList.setHasFixedSize(true);

        movieApiService.setNetworkErrorListener(new MovieApiService.NetworkErrorListener(this){

            @Override
            public void invoke(Context context, String errorMessage) {

                final String errorText = errorMessage;
                MainActivity activity = (MainActivity) context;

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMovieMessage.setText(errorText);
                        tvMovieMessage.setVisibility(View.VISIBLE);

                    }
                });


            }

        });

        loadMovies(MovieApiService.SortBy.POPULAR);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sortby:
                DialogFragment sortByDialog = new SortByDialog();
                sortByDialog.show(getFragmentManager(), "sortByDialog");

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovies(MovieApiService.SortBy sortBy) {
        new PullMovieTask().execute(sortBy);
    }

    private void loadMovies() {
        MoviesAdapter mAdapter = new MoviesAdapter(moviesList, this);
        rvMovieList.setAdapter(mAdapter);

    }

    @Override
    public void onMovieItemClick(int clickedItemIndex) {

        MovieEntity movie = moviesList.get(clickedItemIndex);
        Intent i = new Intent(this, MovieDetailActivity.class);
        i.putExtra("MovieEntity", movie);
        startActivity(i);

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
            if(moviesList != null) {
                tvMovieMessage.setVisibility(View.INVISIBLE);

            }
        }

        private void showProgress() {
            if (!(mProgressDialog != null && mProgressDialog.isShowing())) {
                mProgressDialog = ProgressDialog.show(MainActivity.this, "Wait...", "loading data ...", true);
                mProgressDialog.setCancelable(false);
            }

        }

        private void hideProgress() {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public static class SortByDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.sort_by)
                    .setItems(R.array.sort_by, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            MainActivity activity = (MainActivity) getActivity();

                            switch (which) {
                                case 0:
                                    activity.loadMovies(MovieApiService.SortBy.POPULAR);
                                    break;

                                case 1:
                                    activity.loadMovies(MovieApiService.SortBy.RATED);
                                    break;



                            }

                        }
                    });


            return builder.create();
        }
    }
}
