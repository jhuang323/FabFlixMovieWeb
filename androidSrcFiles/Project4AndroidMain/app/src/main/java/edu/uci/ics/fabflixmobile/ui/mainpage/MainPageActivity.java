package edu.uci.ics.fabflixmobile.ui.mainpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import edu.uci.ics.fabflixmobile.databinding.ActivityMainpageBinding;
import edu.uci.ics.fabflixmobile.ui.movielist.MovieListActivity;

public class MainPageActivity extends AppCompatActivity {
    private String host;
    private String port;
    private String domain;
    private EditText querytextbox;
    private TextView message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainpageBinding binding = ActivityMainpageBinding.inflate(getLayoutInflater());
        // upon creation, inflate and initialize the layout
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        host = intent.getStringExtra("host");
        port = intent.getStringExtra("port");
        domain = intent.getStringExtra("domain");

        querytextbox = binding.query;
//        password = binding.password;
        message = binding.message;
        final Button loginButton = binding.querybutton;

        //assign a listener to call a function to handle the user request when clicking a button
        loginButton.setOnClickListener(view -> queryaction());
    }

    @SuppressLint("SetTextI18n")
    public void queryaction() {

//        message.setText("trying to query");

        // initialize the activity(page)/destination
        Intent MovieListPage = new Intent(MainPageActivity.this, MovieListActivity.class);

        MovieListPage.putExtra("querystr",  querytextbox.getText().toString());
        MovieListPage.putExtra("host",host);
        MovieListPage.putExtra("port",port);
        MovieListPage.putExtra("domain",domain);

        if(!querytextbox.getText().toString().isEmpty()){
            // activate the list page.
            startActivity(MovieListPage);
        }




    }


}
