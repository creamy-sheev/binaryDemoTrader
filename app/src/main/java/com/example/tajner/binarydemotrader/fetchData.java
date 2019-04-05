package com.example.tajner.binarydemotrader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
/**
 * Created by Tajner on 2018-04-08.
 */

public class fetchData extends AsyncTask<Void,Void,Void> {
    private String data = "";
    private String aJSONString = "";
    private String result = "";
    private Context fetchDataContext;
    private String tableName;
    private String ApiUrl;
    private int type;

    public fetchData (Context contex, String passedTableName, String passedApiUrl, int passedType) {

        fetchDataContext = contex;
        tableName = passedTableName;
        ApiUrl = passedApiUrl;
        type = passedType;  // for 0 = cryptocurrency ; for 1 = stock
    }


    @Override
    protected Void doInBackground(Void... params) {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        dbHelper helper = new dbHelper(fetchDataContext);


        if (!helper.checkIfTableExists()){
            try {
                URL url = new URL(ApiUrl);

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line + "\n";
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (type == 0){
                try {
                    JSONObject jsonObject = new JSONObject(data); //json object z danych pobranych ze strony
                    aJSONString = jsonObject.getString("Time Series (Digital Currency Daily)");  //wycinamy metadata
                    JSONObject jsonObject1 = new JSONObject(aJSONString); // obiekt bez metadata
                    JSONArray jsonArray = jsonObject1.names();  // tworzymy tablicę i zapełniamy ją dzieląc jsonobject na mniejsze obiekty poprzez nazwy tych obiektów

                    for (int i = 0; i< jsonArray.length(); i++){    // pętla for dla każdego obiektu wyciąga nazwę i wybraną przez nas wartość; dodajemy je do ciągu wynikowego
                        String name = jsonArray.getString(i);
                        JSONObject date = jsonObject1.getJSONObject(name);
                        String value_high = date.getString("2a. high (USD)");
                        String value_low = date.getString("3b. low (USD)");
                        String value_open = date.getString("1b. open (USD)");
                        String value_close = date.getString("4b. close (USD)");
                        result = result +  name + ":" + value_high + ":" + value_low + ":" + value_open + ":" + value_close +":";
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    JSONObject jsonObject = new JSONObject(data); //json object z danych pobranych ze strony
                    aJSONString = jsonObject.getString("Time Series (Daily)");  //wycinamy metadata
                    JSONObject jsonObject1 = new JSONObject(aJSONString); // obiekt bez metadata
                    JSONArray jsonArray = jsonObject1.names();  // tworzymy tablicę i zapełniamy ją dzieląc jsonobject na mniejsze obiekty poprzez nazwy tych obiektów

                    for (int i = 0; i< jsonArray.length(); i++){    // pętla for dla każdego obiektu wyciąga nazwę i wybraną przez nas wartość; dodajemy je do ciągu wynikowego
                        String name = jsonArray.getString(i);
                        JSONObject date = jsonObject1.getJSONObject(name);
                        String value_high = date.getString("2. high");
                        String value_low = date.getString("3. low");
                        String value_open = date.getString("1. open");
                        String value_close = date.getString("4. close");
                        result = result +  name + ":" + value_high + ":" + value_low + ":" + value_open + ":" + value_close +":";
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }





            String[] array = result.split(":");
            double value_high, value_low, value_open, value_close;
            for (int i = 0; i< array.length; i = i+5) {

                value_high = Double.parseDouble(array[i+1]);
                value_low = Double.parseDouble(array[i+2]);
                value_open = Double.parseDouble(array[i+3]);
                value_close = Double.parseDouble(array[i+4]);
                helper.insertData(array[i], value_high, value_low, value_open, value_close,tableName);
            }

        }
        else {
            Log.i("db", "Data exists, skipping insert");
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}

