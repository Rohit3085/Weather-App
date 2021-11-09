package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView cloudTextView;// = findViewById(R.id.cloudTextView);
    TextView tempTextview;// = findViewById(R.id.temptextView);
    TextView cityname;

    public class weatherInfoDownload extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try{
                URL url;
                url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!=-1){
                    char current = (char) data;
                    result+=current;
                    data = reader.read();
                }
                return result;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                String tempInfo = jsonObject.getString("main");
                JSONObject mainInfo = new JSONObject(tempInfo);
                String avgTemp = mainInfo.getString("temp");
                tempTextview.setText( "Temperature :: " + avgTemp);



                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);

                                for(int i = 0;i<arr.length();i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    cloudTextView.setText("CLoud:: "+jsonPart.getString("main"));


                }




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void checkWeather (View view){
        String cityNameInString = cityname.getText().toString();
        weatherInfoDownload infoLondon = new weatherInfoDownload();

        if(cityname != null) {

            infoLondon.execute("https://api.openweathermap.org/data/2.5/weather?q=" + cityNameInString + "&appid=f2eca11dfd8a788ffacc395949bf1fa7");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         cityname = findViewById(R.id.cityNameTextView);
         cloudTextView = findViewById(R.id.cloudTextView);
        tempTextview = findViewById(R.id.temptextView);


    }
}