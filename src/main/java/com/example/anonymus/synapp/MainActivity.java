package com.example.anonymus.synapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    //private Synonym synonym = null;

    /*
    *   GUI elements
    */
    private SearchView searchView = null;


    private ListView listView = null;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter = null;

    private String TAG = "SYNAPP";

    private String search  = null;
    private String synonym = null;

    /*
    important strings for communication with web service
     */
    private static final String SOAP_ACTION = "urn:synapp#getSynonym";
    private static final String METHOD_NAME = "getSynonym";
    private static final String NAMESPACE = "urn:synapp";
    private static final String URL = "http://app.synapp.bplaced.net/soap-server.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listviewTimeInterval);

        // Create ArrayAdapter using the planet list.
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simplerow, listItems);

        // Set the ArrayAdapter as the ListView's adapter.
        listView.setAdapter(adapter);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String searchString) {

                search = searchString;

                //Create instance for AsyncCallWS
                AsyncCallWS task = new AsyncCallWS();
                //Call execute
                task.execute();

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

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            getSynonym(search);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

           adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            //tv.setText("Calculating...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }

    public void getSynonym(String searchString) {
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Create and define input parameters
        PropertyInfo input = new PropertyInfo();
        input.setName("Synonym");
        input.setValue(searchString);
        input.setType(String.class);

        request.addProperty(input);

        //Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;

        try {
            //Invole web service
            androidHttpTransport.call(SOAP_ACTION, envelope);
            //Get the response
            Object response = (Object) envelope.getResponse();
            //SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            //SoapObject response = (SoapObject) envelope.getResponse();

            //clear list items for new synonym
            listItems.clear();

            Log.e(TAG, response.toString());

            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                listItems.add(jsonArray.getString(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
