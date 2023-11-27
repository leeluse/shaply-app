package com.example.shaply_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvOption;
    TextView tvTags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        // Intent에서 데이터 받아오기
        Intent intent = getIntent();
        if (intent != null) {
            String listName = intent.getStringExtra("listName");
            String selectedText = intent.getStringExtra("selectedText");
            List<String> tags = intent.getStringArrayListExtra("tags");
            // 플레이리스트 태그 설정
            StringBuilder tagsTextBuilder = new StringBuilder();
            for (String tag : tags) {
                tagsTextBuilder.append("#").append(tag).append(" ");
            }
            String tagsText = tagsTextBuilder.toString();

            // 받아온 데이터를 사용하여 UI 업데이트 등 작업 수행
            // 예시: TextView에 받아온 데이터 설정
            tvTitle = findViewById(R.id.tv_listName);
            tvOption = findViewById(R.id.tv_Option);
            tvTags = findViewById(R.id.tv_Tag);

            tvTitle.setText(listName);
            tvOption.setText(selectedText);
            tvTags.setText(tagsText);
            // ... 다른 데이터들을 적절한 뷰에 설정
        }
    }

    // 내 리스트 버튼 onClick 이벤트 핸들러
    public void gotoMylists(View view) {
        Intent intent = new Intent(this, MyListsActivity.class);
        startActivity(intent);
        finish(); // MyListsActivity 종료
    }

    public void addMusic(View view) {
        Intent intent = new Intent(this, AddMusicActivity.class);
        startActivity(intent);
        finish(); // MyListsActivity 종료
    }
}
