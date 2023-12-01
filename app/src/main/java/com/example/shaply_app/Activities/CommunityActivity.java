package com.example.shaply_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shaply_app.Fragments.FooterFragment;
import com.example.shaply_app.Lists.ListItem;
import com.example.shaply_app.Adapters.PlaylistAdapter;
import com.example.shaply_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private DatabaseReference databaseReference;
    private List<ListItem> dataList = new ArrayList<>(); // dataList 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        initializeViews();
        setupRecyclerView();
        fetchPublicLists();

        // EditText 초기화
        EditText editText = findViewById(R.id.edit_text);

        // EditText에 TextWatcher 추가
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 텍스트 변경 전에 수행할 작업
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 텍스트가 변경되었을 때 수행할 작업
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 입력이 변경될 때마다 수행될 작업 (검색 기능 구현)
                String searchText = editable.toString().trim().toLowerCase();

                // 태그 검색
                List<ListItem> filteredList = new ArrayList<>();
                for (ListItem listItem : dataList) {
                    for (String tag : listItem.getTags()) {
                        if (tag.toLowerCase().contains(searchText)) {
                            filteredList.add(listItem);
                            break;
                        }
                    }
                }

                // 필터링된 리스트를 어댑터에 설정하여 RecyclerView 갱신
                adapter.setData(filteredList);
                adapter.notifyDataSetChanged();
            }
        });
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
        adapter = new PlaylistAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    // ----- 유저 정보 리스트 설정 -----
    private void fetchPublicLists() {
        // FirebaseDatabase의 lists 자식 가져오기
        databaseReference = FirebaseDatabase.getInstance().getReference().child("lists");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear(); // 기존 데이터를 삭제하고 새로운 데이터로 채움

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 데이터 가져오기 및 리스트 구성
                    String listId = snapshot.getKey(); // 플레이리스트의 ID 가져오기
                    String listName = snapshot.child("listName").getValue(String.class);
                    String selectedText = snapshot.child("selectedText").getValue(String.class);
                    List<String> tags = (List<String>) snapshot.child("tags").getValue();
                    String userUid = snapshot.child("userUid").getValue(String.class);

                    if (selectedText != null && selectedText.equals("Public")) {
                        ListItem listItem = new ListItem(listId, listName, selectedText, tags, userUid);
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
                Toast.makeText(CommunityActivity.this, "Failed to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ----- FOOTER -----
    // 커뮤니티 버튼 onClick 이벤트 핸들러
    public void gotoCommunity(View view) {
        Intent intent = new Intent(this, CommunityActivity.class); // 변경된 클래스로 수정
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
