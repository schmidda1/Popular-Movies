package com.example.android.popularMovies.utilities;

import java.io.Serializable;

public class movieModel implements Serializable {
    private String originalTitle;
    private String moviePoster;
    private String plotSynopsis;
    private float vote_average;
    private String releaseDate;
    private String posterUrl;
    public movieModel(){

    }
    public movieModel(String originalTitle, String moviePoster, String plotSynopsis, float vote_average,
                      String releaseDate, String posterUrl){
        this.originalTitle = originalTitle;
        this.moviePoster = moviePoster;
        this.plotSynopsis = plotSynopsis;
        this.vote_average = vote_average;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
    }
    //get methods
    public String getOriginalTitle(){
        return originalTitle;
    }
    public String getMoviePoster(){
        return moviePoster;
    }
    public String getPlotSynopsis(){
        return plotSynopsis;
    }
    public float getVote_average(){
        return vote_average;
    }
    public String getReleaseDate(){
        return releaseDate;
    }
    public String getPosterUrl(){
        return posterUrl;
    }
    //set methods
    public void setOriginalTitle(String originalTitle){
        this.originalTitle = originalTitle;
    }
    public void setMoviePoster(String moviePoster){
        this.moviePoster = moviePoster;
    }
    public void setPlotSynopsis(String plotSynopsis){
        this.plotSynopsis = plotSynopsis;
    }
    public void setVote_average(float vote_average){
        this.vote_average = vote_average;
    }
    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }
    public void setPosterUrl(String posterUrl){
        this.posterUrl = posterUrl;
    }


}
