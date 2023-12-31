package com.example.shaply_app.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaply_app.R;
import com.example.shaply_app.Lists.Track;
import com.example.shaply_app.Adapters.TrackAdapter;

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
    private String playlistId; // playlistId 선언


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);

        recyclerView = findViewById(R.id.recycler_view);
        editText = findViewById(R.id.edit_text);

        trackList = new ArrayList<>();
        // Intent로 전달받은 playlistId 값을 저장
        Intent intent = getIntent();
        if (intent != null) {
            playlistId = intent.getStringExtra("playlistId");
            Log.d("playlistId", "Clicked playlistId: " + playlistId);
            // TrackAdapter 초기화 시에 playlistId 전달
            trackAdapter = new TrackAdapter(trackList, playlistId);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(trackAdapter);
    }

    public void gotoAddMusic(View v) {
        // 여기에 클릭 이벤트 처리 코드를 넣어줍니다.
        // 새로운 액티비티에 데이터 전달을 위한 Intent 생성
        Intent intent = new Intent(v.getContext(), MusicActivity.class);

        intent.putExtra("playlistId", playlistId);
        // 다른 액티비티 시작
        v.getContext().startActivity(intent);
    }


    public void searchMusic(View v) {
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
