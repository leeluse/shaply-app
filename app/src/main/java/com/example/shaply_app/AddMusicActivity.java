package com.example.shaply_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddMusicActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private List<Track> trackList;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);

        recyclerView = findViewById(R.id.recycler_view);
        editText = findViewById(R.id.edit_text);

        trackList = new ArrayList<>();
        trackAdapter = new TrackAdapter(trackList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(trackAdapter);
    }

    public void mOnClick(View v) {
        // EditText에서 사용자가 입력한 값을 가져옴
        String searchText = editText.getText().toString();

        // API 호출을 위한 AsyncTask 실행
        new APITask().execute(searchText);
    }

    private class APITask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder response = new StringBuilder();
            try {
                String searchText = strings[0];
                String apiUrl = "https://ws.audioscrobbler.com/2.0/?method=track.search&track=" + searchText + "&api_key=1c07ffd42e607412a36b69be83c9470f&format=json";
                URL url = new URL(apiUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject results = jsonObject.getJSONObject("results");
                JSONObject trackMatches = results.getJSONObject("trackmatches");
                JSONArray tracks = trackMatches.getJSONArray("track");

                trackList.clear(); // 기존 목록 초기화
                for (int i = 0; i < tracks.length(); i++) {
                    JSONObject trackObj = tracks.getJSONObject(i);
                    String trackName = trackObj.getString("name");
                    String artistName = trackObj.getString("artist");

                    Track track = new Track(trackName, artistName);
                    trackList.add(track);
                }

                trackAdapter.notifyDataSetChanged(); // 데이터 변경 알림
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
