package edu.uci.ics.fabflixmobile.ui.singlemovie;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.databinding.ActivitySinglemovieBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleMovieActivity extends AppCompatActivity{
    private String host;
    private String port;
    private String domain;
    private TextView Titletextbox;
    private TextView Yeartextbox;
    private TextView Directortextbox;
    private TextView Genretextbox;
    private TextView Startextbox;



    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySinglemovieBinding binding = ActivitySinglemovieBinding.inflate(getLayoutInflater());
        // upon creation, inflate and initialize the layout
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        host = intent.getStringExtra("host");
        port = intent.getStringExtra("port");
        domain = intent.getStringExtra("domain");

        baseUrl = "https://"+host+":"+port+"/"+domain+"/api/single-movie?id=";

        String TheMovieId = intent.getStringExtra("movieid");

        Titletextbox = binding.MovieTitle;
        Yeartextbox = binding.Year;
        Directortextbox = binding.Director;
        Genretextbox = binding.Genre;
        Startextbox = binding.Star;
//

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        String mainUrl = baseUrl + TheMovieId;
        StringRequest singleMovieRequest = new StringRequest(
                Request.Method.GET,
                mainUrl,
                response -> {
                    try {
                        if(response != null) {
                            JSONObject mainJsonObj = new JSONObject(response);

                            Titletextbox.setText("Title: " + mainJsonObj.getString("title"));
                            Yeartextbox.setText("Year: " + mainJsonObj.getString("year"));
                            Directortextbox.setText("Director: " + mainJsonObj.getString("director"));

                            JSONArray genrejsonarray = mainJsonObj.getJSONArray("genre");

                            String genrestr = "";

                            for(int i=0;i<genrejsonarray.length();i++){


                                if(i == genrejsonarray.length()-1){
                                    genrestr += genrejsonarray.get(i);
                                }
                                else{
                                    genrestr += genrejsonarray.get(i) + ", ";
                                }
                            }

                            Genretextbox.setText("Genres: " + genrestr);

                            JSONArray starjsonarray = mainJsonObj.getJSONArray("star");

                            String starstr = "";

                            for(int i=0;i<starjsonarray.length();i++){
                                JSONObject astarobj = starjsonarray.getJSONObject(i);

                                if(i == starjsonarray.length()-1){
                                    starstr += astarobj.getString("name");
                                }
                                else{
                                    starstr += astarobj.getString("name") + ", ";
                                }


                            }

                            Startextbox.setText("Stars: " + starstr);






                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    System.out.println("Failed in requestQueue");
                    Log.d("movieList.error", error.toString());
                });
        queue.add(singleMovieRequest);


//        ActivitySingleMovieBinding binding =
//                ActivitySingleMovieBinding.inflate(getLayoutInflater());
//        // upon creation, inflate and initialize the layout
//        setContentView(binding.getRoot());
//
//        querytextbox = binding.query;
////        password = binding.password;
//        message = binding.message;
//        final Button loginButton = binding.querybutton;

        //assign a listener to call a function to handle the user request when clicking a button
        //loginButton.setOnClickListener(view -> queryaction());
    }
}
