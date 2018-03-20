package com.rv.prog3tentmars;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DataRequestAPI extends AsyncTask<String, Void, String> {
    private Context mcontext;
    private SQLiteDatabase mDb;
    private Cursor cursor;
    private DataAvailable listener = null;
    private static final String TAG = DataRequestAPI.class.getSimpleName();

    public DataRequestAPI(DataAvailable listener, Context context) {
        Log.i(TAG, "DataRequestAPI Constructor Called");
        this.listener = listener;
        this.mcontext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream inputStream = null;
        int responsCode = -1;
        String itemUrl = params[0];
        String response = "";
        Log.i(TAG, "doInBackground - " + itemUrl);

        try {
            URL url = new URL(itemUrl);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                Log.i(TAG, "urlConntion is already instance of HttpURLConnection");
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("TAG", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }
        return response;
    }

    protected void onPostExecute(String response) {
        Log.i(TAG, "onPostExecute " + response);
        if (response == null || response == "") {
            Log.e(TAG, "onPostExecute got an empty response");
            return;
        }

        DictionaryOpenhelper dictionaryOpenhelper = new DictionaryOpenhelper(mcontext);
        mDb = dictionaryOpenhelper.getWritableDatabase();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);

            JSONArray items = jsonObject.getJSONArray("photos");
            for (int idx = 0; idx < items.length(); idx++) {
                JSONObject item = items.getJSONObject(idx);
                int id = item.getInt("id");
                JSONObject name = item.getJSONObject("camera");
                String fullname = name.getString("full_name");
                String imageUrl = item.getString("img_src");

                ContentValues RoverRecord = new ContentValues();
                RoverRecord.put("camId", id);
                RoverRecord.put("fullName", fullname);
                RoverRecord.put("URL", imageUrl);
                mDb.insert("rover_db", null, RoverRecord);
            }
        } catch (JSONException ex) {
            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
        }

        String query = "SELECT * FROM rover_db";
        cursor = mDb.rawQuery(query, null);
        listener.onDataAvailable(cursor);
    }

    private static String getStringFromInputStream(InputStream is) {
        Log.i(TAG, "DataRequestAPI getStringFromInputStream Called");
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public interface DataAvailable {
        void onDataAvailable(Cursor cursor);
    }
}


