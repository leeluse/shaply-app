package com.example.shaply_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupRecyclerView();
        fetchPublicLists();
    }

    // ----- 액티비티의 필요한 뷰 초기화 -----
    private void initializeViews() {
        // Footer Fragment 추가
        FooterFragment footerFragment = new FooterFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.footer_container, footerFragment)
                .commit();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // -----  RecyclerView 설정 -----
    private void setupRecyclerView() {
        adapter = new PlaylistAdapter(new ArrayList<>(), this::onItemClick);
        recyclerView.setAdapter(adapter);
    }

    // ----- 유저 정보 리스트 설정 -----
    private void fetchPublicLists() {
        // FirebaseDatabase의 lists 자식 가져오기
        databaseReference = FirebaseDatabase.getInstance().getReference().child("lists");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // dataList 초기화
                List<ListItem> dataList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 데이터 가져오기 및 리스트 구성
                    String listName = snapshot.child("listName").getValue(String.class);
                    String selectedText = snapshot.child("selectedText").getValue(String.class);
                    List<String> tags = (List<String>) snapshot.child("tags").getValue();
                    String userUid = snapshot.child("userUid").getValue(String.class);

                    // 옵션 설정이 Public인 경우만 데이터 처리
                    if (selectedText != null && selectedText.equals("Public")) {
                        ListItem listItem = new ListItem(listName, selectedText, tags, userUid);
                        dataList.add(listItem);
                    }
                }
                // adapter에 DataList를 setData
                adapter.setData(dataList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 에러 처리
                Toast.makeText(MainActivity.this, "Failed to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 플레이리스트 버튼 onClick 이벤트 핸들러
    private void onItemClick(ListItem item) {
        String listItemData = item.getListName();
        Intent intent = new Intent(MainActivity.this, MusicActivity.class);
        intent.putExtra("ListItemData", listItemData);
        startActivity(intent);
    }

    // ----- FOOTER -----
    // 커뮤니티 버튼 onClick 이벤트 핸들러
    public void gotoCommunity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    // 로그아웃 버튼 onClick 이벤트 핸들러
    public void googleLogout(View view) {
        signOut();
    }
    // 내 리스트 버튼 onClick 이벤트 핸들러
    public void gotoMylists(View view) {
        Intent intent = new Intent(this, MyListsActivity.class);
        startActivity(intent);
        finish();
    }

    // 로그아웃 메서드
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
