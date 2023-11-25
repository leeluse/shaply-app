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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyListsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PlaylistAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists);

        initializeViews();
        setupRecyclerView();
        fetchUserLists();
    }


    // ----- 액티비티의 필요한 뷰 초기화 -----
    private void initializeViews() {
        // FooterFragment 추가
        FooterFragment footerFragment = new FooterFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.footer_container, footerFragment)
                .commit();
        // RecyclerView를 리니어와 함꼐 초기화
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // -----  RecyclerView 설정 -----
    private void setupRecyclerView() {
        // PlaylistAdaper 초기화 및 RecyclerView에 연결
        adapter = new PlaylistAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    // ----- 유저 정보 리스트 설정 -----
    private void fetchUserLists() {
        // 사용자가 존재하면
        if (currentUser != null) {
            // 현재 유저의 UID get
            String userUid = currentUser.getUid();
            // FirebaseDatabase의 lists 자식 가져오기
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("lists");

            // userUID가 currentUser와 같은 데이터에 대해서 처리
            databaseReference.orderByChild("userUid").equalTo(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // 데이터가 변할 때
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // dataList 초기화
                    List<ListItem> dataList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // 데이터 가져오기 및 리스트 구성
                        String listName = snapshot.child("listName").getValue(String.class);
                        String selectedText = snapshot.child("selectedText").getValue(String.class);

                        List<String> tags = new ArrayList<>();
                        DataSnapshot tagsSnapshot = snapshot.child("tags");
                        // for문을 이용해 태그 객체 데이터 하나씩 처리
                        for (DataSnapshot tagSnapshot : tagsSnapshot.getChildren()) {
                            String tag = tagSnapshot.getValue(String.class);
                            tags.add(tag);
                        }

                        String uid = snapshot.child("userUid").getValue(String.class);

                        ListItem listItem = new ListItem(listName, selectedText, tags, uid);
                        dataList.add(listItem);
                    }

                    // adapter에 DataList를 setData
                    adapter.setData(dataList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 에러 처리
                    Toast.makeText(MyListsActivity.this, "Failed to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    // 플레이리스트 추가 버튼 onClick 이벤트 핸들러
    public void gotoAddList(View v) {
        Intent intent = new Intent(this, AddListActivity.class);
        startActivity(intent);
        finish(); // MyListsActivity 종료
    }

    // ----- FOOTER -----
    // 커뮤니티 버튼 onClick 이벤트 핸들러
    public void gotoCommunity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // MyListsActivity 종료
    }

    // 로그아웃 버튼 onClick 이벤트 핸들러
    public void googleLogout(View view) {
        signOut(); // 로그아웃 메서드
    }

    // 내 리스트 버튼 onClick 이벤트 핸들러
    public void gotoMylists(View view) {
        Intent intent = new Intent(this, MyListsActivity.class);
        startActivity(intent);
        finish(); // MyListsActivity 종료
    }

    // 로그아웃 메서드
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        // 로그아웃 후 추가 작업이 있다면 여기에 구현
        // 로그아웃 후 LoginActivity로 이동
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // MyListsActivity 종료
    }


}