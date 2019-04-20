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
package com.example.android.popularMovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;


public final class OpenMovieJsonUtils {



    public static ArrayList<movieModel> getMovieListFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String posterUrlBase = "http://image.tmdb.org/t/p/w185/";


        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray("results");

        ArrayList<movieModel> moviesList = new ArrayList<>(movieArray.length());

        for (int i = 0; i < movieArray.length(); i++) {
            String originalTitle;
            String moviePoster;
            String plotSynopsis;
            float vote_average = -1.0f;
            String releaseDate;
            String posterUrl;

            JSONObject movieItemObj = movieArray.getJSONObject(i);
            originalTitle = movieItemObj.getString("original_title");
            if (originalTitle == "")originalTitle = "No_information";
            moviePoster = movieItemObj.getString("poster_path");
            if(moviePoster == "")moviePoster = "No_information";
            plotSynopsis = movieItemObj.getString("overview");
            if(plotSynopsis == "")plotSynopsis = "No_information";
            vote_average = (float)movieItemObj.getDouble("vote_average");
            releaseDate = movieItemObj.getString("release_date");
            if(releaseDate == "")releaseDate = "No_information";
            posterUrl = posterUrlBase + moviePoster;
            movieModel movie_model = new movieModel(originalTitle, moviePoster, plotSynopsis, vote_average, releaseDate, posterUrl);
            moviesList.add(movie_model);
        }

        return moviesList;
    }
}