package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;
import edu.uci.ics.fabflixmobile.databinding.ActivityMainpageBinding;
import edu.uci.ics.fabflixmobile.databinding.ActivityMovielistBinding;
import edu.uci.ics.fabflixmobile.ui.login.LoginActivity;
import edu.uci.ics.fabflixmobile.ui.mainpage.MainPageActivity;
import edu.uci.ics.fabflixmobile.ui.singlemovie.SingleMovieActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
public class MovieListActivity extends AppCompatActivity {

    String thequery;
    int pageCounter = 1;

    private String host;
    private String port;
    private String domain;

    private String baseUrl;
    private ArrayList<Movie> movies;
    private RequestQueue queue;
    private MovieListViewAdapter adapter;
    private  TextView pageNumText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        // TODO: this should be retrieved from the backend server
        Intent intent = getIntent();
        thequery = intent.getStringExtra("querystr");
        host = intent.getStringExtra("host");
        port = intent.getStringExtra("port");
        domain = intent.getStringExtra("domain");

        baseUrl = "https://"+host+":"+port+"/"+domain+"/api/movie-list?" +
                "fulltext=true&sortfirst=title&sorttype1=a&sorttype2=a&numlimit=10";

        ActivityMovielistBinding binding = ActivityMovielistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pageNumText = binding.pageNum;
        final Button backButton = binding.back;
        backButton.setOnClickListener(view -> back());
        final Button frontButton = binding.front;
        frontButton.setOnClickListener(view -> front());
        this.movies = new ArrayList<Movie>();

//        MovieListViewAdapter adapter = new MovieListViewAdapter(this, movies);
//        ListView listView = findViewById(R.id.list);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            Movie movie = movies.get(position);
//            singleMovieCall(movie);
//        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        String mainUrl = baseUrl + "&page=" + Integer.toString(pageCounter) + "&title=" + thequery;
        adapter = new MovieListViewAdapter(this, movies);
        queue = NetworkManager.sharedManager(this).queue;
        StringRequest movieListRequest = new StringRequest(
                Request.Method.GET,
                mainUrl,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        buildMovieList(jsonArray);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    System.out.println("Failed in requestQueue");
                    Log.d("movieList.error", error.toString());
                });
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Movie movie = movies.get(position);
            singleMovieCall(movie);
        });
        queue.add(movieListRequest);
        pageNumText.setText(String.valueOf(pageCounter));
    }
    private void back(){
        if(pageCounter != 1){
            pageCounter -= 1;
            StringRequest movieListRequest = callAPI();
            queue.add(movieListRequest);
        }
    }
    private void front(){
        pageCounter += 1;
        StringRequest movieListRequest = callAPI();
        queue.add(movieListRequest);
    }
    private void singleMovieCall(Movie movie){
        movie.getId();
        // initialize the activity(page)/destination
        Intent SingleMoviePageintent = new Intent(MovieListActivity.this, SingleMovieActivity.class);

        SingleMoviePageintent.putExtra("movieid", movie.getId());
        SingleMoviePageintent.putExtra("host",host);
        SingleMoviePageintent.putExtra("port",port);
        SingleMoviePageintent.putExtra("domain",domain);
        startActivity(SingleMoviePageintent);

    }
    private StringRequest callAPI(){
        String mainUrl = baseUrl + "&page=" + Integer.toString(pageCounter) + "&title=" + thequery;
        StringRequest movieListRequest = new StringRequest(
                Request.Method.GET,
                mainUrl,
                response -> {
                    try {
                        if(response != null) {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() != 0) {
                                buildMovieList(jsonArray);
                                adapter.notifyDataSetChanged();
                            }
                            else{
                                pageCounter -= 1;
                            }
                            pageNumText.setText(String.valueOf(pageCounter));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    System.out.println("Failed in requestQueue");
                    Log.d("movieList.error", error.toString());
                });
        return movieListRequest;
    }
    private void buildMovieList(JSONArray jsonArray){
        try {
            movies.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                String titleAndYear = jsonArray.getJSONObject(i).getString("title")
                        + "(" + jsonArray.getJSONObject(i).getString("year") + ")";
                String id = jsonArray.getJSONObject(i).getString("id");
                String director = jsonArray.getJSONObject(i).getString("director");
                String genres = "Genres: ";
                for (int j = 0; j < jsonArray.getJSONObject(i).getJSONArray("genre").length(); j++) {
                    genres += jsonArray.getJSONObject(i).getJSONArray("genre").get(j) +
                            " ";
                }
                String stars = "Stars: ";
                for (int j = 0; j < jsonArray.getJSONObject(i).getJSONArray("star").length(); j++) {
                    stars += jsonArray.getJSONObject(i).getJSONArray("star")
                            .getJSONObject(j).getString("name") + " ";
                }
                String rating = jsonArray.getJSONObject(i).getString("rating");
                this.movies.add(new Movie(titleAndYear, id, director, genres, stars,
                        rating));
            }
        }catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}