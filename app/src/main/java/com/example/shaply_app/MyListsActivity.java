package com.example.shaply_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class MyListsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    PlaylistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists);

        // FooterFragment를 생성하고 추가
        FooterFragment footerFragment = new FooterFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.footer_container, footerFragment)
                .commit();


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PlaylistAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("lists");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<ListItem> dataList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String listName = snapshot.child("listName").getValue(String.class);
                        String selectedText = snapshot.child("selectedText").getValue(String.class);
                        List<String> tags = (List<String>) snapshot.child("tags").getValue();



                        ListItem listItem = new ListItem(listName, selectedText, tags);
                        dataList.add(listItem);

                    }
                    adapter.setData(dataList);
                    adapter.notifyDataSetChanged(); // 어댑터에 데이터 변경을 알림
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MyListsActivity.this, "Failed to get data.", Toast.LENGTH_SHORT).show();
                }
            });

        }
    // 플레이리스트 추가 버튼 onClick 이벤트 핸들러
    public void gotoAddList(View view) {
        Intent intent = new Intent(this, AddListActivity.class);
        startActivity(intent);
        finish(); // MyListsActivity 종료
    }


    // < FOOTER >
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