package com.example.anonymus.synapp;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymus.synapp.data.Synonym;
import com.example.anonymus.synapp.logic.DBManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    private DBManager db = null;
    private Synonym synonym = null;

    /*
    *   GUI elements
    */
    private SearchView searchView = null;

    private TextView tvAlt1 = null;
    private TextView tvAlt2 = null;
    private TextView tvAlt3 = null;

    private TextView tvAuthor = null;
    private TextView tvCapital = null;
    private TextView tvDescription = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        db = new DBManager(this);

        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvCapital = (TextView) findViewById(R.id.tvCapital);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        tvAlt1 = (TextView) findViewById(R.id.tvAlt1);
        tvAlt2 = (TextView) findViewById(R.id.tvAlt2);
        tvAlt3 = (TextView) findViewById(R.id.tvAlt3);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String searchString) {

                Synonym syn = db.getSynonym(searchString);

                tvCapital.setText(syn.getName());
                tvAlt1.setText(syn.getAlt1());
                tvAlt2.setText(syn.getAlt2());
                tvAlt3.setText(syn.getAlt3());
                tvDescription.setText(syn.getDesc());
                return false;
            }

            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }
}
