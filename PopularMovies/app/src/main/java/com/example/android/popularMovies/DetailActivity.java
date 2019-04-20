package com.example.android.popularMovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularMovies.R;
import com.example.android.popularMovies.utilities.NetworkUtils;
import com.example.android.popularMovies.utilities.movieModel;
import com.squareup.picasso.Picasso;
import org.w3c.dom.Text;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private movieModel movie;
    ImageView mThumbnail;
    TextView mOriginalTitle;
    TextView mPlotSynopsis;
    TextView mVoteAverage;
    TextView mReleaseDate;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        mThumbnail = findViewById(R.id.thumbnail);
        mOriginalTitle = findViewById(R.id.original_title);
        mPlotSynopsis = findViewById(R.id.plot_synopsis);
        mVoteAverage = findViewById(R.id.vote_average);
        mReleaseDate = findViewById(R.id.release_date);

        intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        movie = (movieModel)intent.getSerializableExtra("movie");


        populateUI(movie);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.back_to_main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.back_to_main) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void closeOnError() {
        finish();
            Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    private void populateUI(movieModel movie) {
        int sortPref = NetworkUtils.sortPref;
        Picasso.get()
                .load(movie.getPosterUrl())
                .into(mThumbnail);
        if (sortPref==0)
            setTitle(getString(R.string.popular_movies_title));
        else
            setTitle(getString(R.string.top_rated_title));
        mOriginalTitle.setText(movie.getOriginalTitle());
        mPlotSynopsis.setText(movie.getPlotSynopsis());
        mVoteAverage.setText(String.valueOf(movie.getVote_average()));
        mReleaseDate.setText(movie.getReleaseDate());

    }

}
