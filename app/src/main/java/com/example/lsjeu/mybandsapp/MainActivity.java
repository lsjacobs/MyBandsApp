package com.example.lsjeu.mybandsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // TAG to Debug
    private String TAG = MainActivity.class.getSimpleName();

    // Main list that contains the bands gotten from the Json
    ArrayList<String> bands = new ArrayList<>();

    // To be used to populate ListView
    private ListView lv;

    // ProgressDialog to be used in the AsyncTask
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new getJSONdata().execute(); // Starts Async Task

        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            JSONArray ids = jsonObject.getJSONArray("bands");

            for( int i=0; i<ids.length(); i++){
                JSONObject jsonObj = ids.getJSONObject(i);
                bands.add(jsonObj.getString("name"));
                Log.e(TAG, "Json: " + bands);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        lv = (ListView)findViewById(R.id.bandList);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                bands);

        lv.setAdapter(arrayAdapter);

        // When a place is selected, send the place obj to another activity in the intent
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, BandActivity.class);
                String idSelectedFromList = (lv.getItemAtPosition(position).toString());

                String bandSelected="";
                for (String band : bands){
                    //If the option selected matches the band from the list, starts another activity
                    if (band.matches(idSelectedFromList)){
                        bandSelected = band;
                    }
                }
                intent.putExtra("band", bandSelected);
                startActivity(intent);
            }
        });
    }

    private class getJSONdata extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting data from file...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            loadJSONFromAsset();
            return null;
        }

        protected void onPostExecute(Void v) {
            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("bands.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
