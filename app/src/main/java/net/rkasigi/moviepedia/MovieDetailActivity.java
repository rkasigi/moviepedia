package net.rkasigi.moviepedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        MovieEntity movie = (MovieEntity) getIntent().getSerializableExtra("MovieEntity");

        String releaseYear = "n/a";
        String synopsis =
                (movie.getSynopsis() == null || movie.getSynopsis().isEmpty()) ?
                "No Overview found" :
                movie.getSynopsis();

        String[] datePart = movie.getReleaseDate().split("-");
        if(datePart.length > 0 && !datePart[0].isEmpty()) {
            releaseYear = datePart[0];

        }


        TextView tvTitle = (TextView) findViewById(R.id.md_tv_title);
        TextView tvReleaseDate = (TextView) findViewById(R.id.md_tv_release_date);
        TextView tvRating = (TextView) findViewById(R.id.md_tv_rating);
        TextView tvSynopsis = (TextView) findViewById(R.id.md_tv_synopsis);
        ImageView ivPoster = (ImageView) findViewById(R.id.md_iv_poster);

        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(releaseYear);
        tvRating.setText(String.format("%s/10", movie.getRating()));
        tvSynopsis.setText(synopsis);

        Picasso
                .with(this)
                .load(movie.getImage())
                .error(R.drawable.imagenotfound_poster)
                .into(ivPoster);


    }
}
