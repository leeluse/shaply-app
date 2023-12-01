package com.example.shaply_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaply_app.Lists.MusicList;
import com.example.shaply_app.Adapters.MusicListAdapter;
import com.example.shaply_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity {

    private DatabaseReference playlistRef; // Firebase 데이터베이스 참조
    private String playlistId; // playlistId를 클래스 레벨의 멤버 변수로 이동
    private RecyclerView recyclerView; // RecyclerView 변수 추가
    private MusicListAdapter musicListAdapter; // MusicListAdapter 변수 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        // 리사이클러뷰 설정
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Intent로부터 데이터 받아오기
        Intent intent = getIntent();

        if (intent != null) {
            // getStringExtra로 데이터 받아 초기화
            playlistId = intent.getStringExtra("playlistId");
            playlistRef = FirebaseDatabase.getInstance().getReference().child("lists").child(playlistId);
            playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // 데이터가 바뀔 때마다 설정값
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String listName = dataSnapshot.child("listName").getValue(String.class);
                    String selectedText = dataSnapshot.child("selectedText").getValue(String.class);

                    TextView textViewListName = findViewById(R.id.tv_listName);
                    TextView textViewSelectedText = findViewById(R.id.tv_Option);
                    TextView textViewTag = findViewById(R.id.tv_Tag);

                    textViewListName.setText(listName);
                    textViewSelectedText.setText(selectedText);

                    // 태그 정보 가져오기
                    DataSnapshot tagsSnapshot = dataSnapshot.child("tags");
                    List<String> tags = new ArrayList<>();
                    for (DataSnapshot tagSnapshot : tagsSnapshot.getChildren()) {
                        String tag = tagSnapshot.getValue(String.class);
                        tags.add(tag);
                    }
                    // 플레이리스트 태그 설정
                    StringBuilder tagsTextBuilder = new StringBuilder();
                    for (String tag : tags) {
                        tagsTextBuilder.append("#").append(tag); // 각 태그 사이에 #을 추가합니다.
                    }
                    String tagsText = tagsTextBuilder.toString().replaceAll("\\s+", "").replaceAll("#+", " #"); // #과 태그 사이의 공백 제거 및 각 태그 사이에 공백 추가
                    tagsText = tagsText.trim(); // 문자열 양 끝의 공백 제거
                    textViewTag.setText(tagsText); // 텍스트 설정

                    //------ 수정/삭제/추가 버튼 UI 설정 -----
                    LinearLayout layoutButtons = findViewById(R.id.layout_buttons);

                    // 사용자의 UID 가져오기
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        String userUid = currentUser.getUid();

                        // 데이터 스냅샷에서 'userUid' 값을 가져오기
                        String playlistUid = dataSnapshot.child("userUid").getValue(String.class);
                        Log.d("Playlist UID", "Playlist UID: " + playlistUid); // 플레이리스트 UID 로그

                        // 사용자의 UID와 플레이리스트의 UID 비교하여 addMusic 버튼 처리
                        if (playlistUid != null && playlistUid.equals(userUid)) {
                            // UID가 일치할 경우 버튼을 보여 주기
                            layoutButtons.setVisibility(View.VISIBLE);

                        } else {
                            // UID가 일치하지 않을 경우 버튼 숨기기
                            layoutButtons.setVisibility(View.GONE);

                        }
                    }

                    // 이 부분에서 "Track" 아래의 데이터를 가져오도록 수정
                    DataSnapshot trackSnapshot = dataSnapshot.child("Track");

                    List<MusicList> musicList = new ArrayList<>();

                    for (DataSnapshot track : trackSnapshot.getChildren()) {
                        String trackName = track.child("trackName").getValue(String.class);
                        String artistName = track.child("artistName").getValue(String.class);

                        MusicList music = new MusicList(trackName, artistName);
                        musicList.add(music);
                    }

                    // 리사이클러뷰에 musicListAdapter 어댑터 설정하기
                    musicListAdapter = new MusicListAdapter(musicList);
                    recyclerView.setAdapter(musicListAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MusicActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // ------ 플레이리스트 추가 이벤트 -----
    public void addMusic(View view) {
        // 새로운 액티비티에 데이터 전달을 위한 Intent 생성
        Intent intent = new Intent(this, AddMusicActivity.class);

        intent.putExtra("playlistId", playlistId); // playlistId를 Intent에 추가하여 전달
        startActivity(intent);
        finish(); // MusicActivity 종료
    }

    // ------ 플레이리스트 삭제 이벤트 -----
    public void deletePlaylist(View view) {
        // Firebase에서 플레이리스트를 삭제하는 코드
        playlistRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MusicActivity.this, "플레이리스트가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MusicActivity.this, MyListsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MusicActivity.this, "플레이리스트 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // ------ 플레이리스트로 수정 -----
    public void fixPlaylist(View view) {
        // 새로운 액티비티에 데이터 전달을 위한 Intent 생성
        Intent intent = new Intent(this, EditPlaylistActivity.class);

        intent.putExtra("playlistId", playlistId); // playlistId를 Intent에 추가하여 전달
        startActivity(intent);
        finish(); // MusicActivity 종료
    }



    // ------ 플레이리스트로 이동(뒤로 가기) -----
    public void gotoMylists(View view) {
        Intent intent = new Intent(this, MyListsActivity.class);
        startActivity(intent);
        finish(); // MusicActivity 종료
    }


}
