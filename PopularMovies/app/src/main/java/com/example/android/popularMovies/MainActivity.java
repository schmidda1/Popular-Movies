/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularMovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularMovies.MovieAdapter.MovieAdapterOnClickHandler;
import com.example.android.popularMovies.utilities.NetworkUtils;
import com.example.android.popularMovies.utilities.OpenMovieJsonUtils;
import com.example.android.popularMovies.utilities.movieModel;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler, Serializable {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private MenuItem mPopular;
    private MenuItem mTopRated;
    private int mSortType;
    private ArrayList<movieModel> mMovieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mSortType = 0;



        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        // COMPLETED (11) Pass in 'this' as the MovieAdapterOnClickHandler
        /*
         * The MovieAdapter is responsible for linking our movie data with the Views that
         * will end up displaying our movie data.
         */
        mMovieAdapter = new MovieAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMovieAdapter);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the movie data. */
        getMoviedbData();
        setTitle(getString(R.string.popular_movies_title));
        //loadMovieData();//for UI testing
    }


    public int getSortType(){return mSortType;}
    private void loadMovieData() {
        showMovieDataView();
        String[] movieFiles = getResources().getStringArray(R.array.movie_files);
        mMovieAdapter.setMovieFiles(movieFiles);
    }

    private void getMoviedbData(){

        new FetchMovieTask().execute();
        mMovieAdapter.setMovieData(mMovieData);
    }

    @Override
    public void onClick(movieModel movie, int position) {
        Context context = this;
        launchDetailActivity(movie, position);
    }
    private void launchDetailActivity(movieModel movie,int position){
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            NetworkUtils.sortPref = mSortType;
        }

        @Override
        protected Integer doInBackground(Void...params) {

            URL movieRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                mMovieData = OpenMovieJsonUtils
                        .getMovieListFromJson(MainActivity.this, jsonMovieResponse);
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return 1;
            }

        }

        @Override
        protected void onPostExecute(Integer status) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (status == 0) {
                showMovieDataView();
                mMovieAdapter.setMovieData(mMovieData);

            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        mPopular = menu.findItem(R.id.popular);
        mTopRated = menu.findItem(R.id.top_rated);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular && mSortType == 1) {
            mSortType = 0;
            getMoviedbData();
            setTitle(getString(R.string.popular_movies_title));
            mPopular.setChecked(true);
            mTopRated.setChecked(false);
            return true;
        }
        else if (id == R.id.top_rated && mSortType == 0){
            mSortType = 1;
            getMoviedbData();
            setTitle(getString(R.string.top_rated_title));
            mTopRated.setChecked(true);
            mPopular.setChecked(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}