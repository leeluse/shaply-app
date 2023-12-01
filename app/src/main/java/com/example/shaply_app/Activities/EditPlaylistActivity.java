package com.example.shaply_app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shaply_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditPlaylistActivity extends AppCompatActivity {

    EditText etListName;
    EditText etTag;
    DatabaseReference listsReference;
    RadioGroup radioGroup;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String playlistId; // 플레이리스트 ID를 저장할 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_playlist);

        // ----- 초기화 -----
        etListName = findViewById(R.id.et_editlistName);
        etTag = findViewById(R.id.et_edittag);
        radioGroup = findViewById(R.id.editRadioGroup);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        listsReference = database.getReference().child("lists");


        // ----- 플레이리스트 ID의 정보 가져오기 -----
        Intent intent = getIntent();
        if (intent != null) {
            // Intent로부터 플레이리스트 ID 받아오기
            playlistId = intent.getStringExtra("playlistId");

            // Firebase에서 playlistId의 플레이리스트 정보를 불러오기
            listsReference.child(playlistId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // 데이터가 변경될 때 호출
            public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                String listName = dataSnapshot.child("listName").getValue(String.class);
                String selectedText = dataSnapshot.child("selectedText").getValue(String.class);

                // Firebase에서 가져온 정보를 EditText에 설정
                etListName.setText(listName);


                //  Firebase에서 가져온 RadioButton의 선택 상태 설정
                int radioButtonCount = radioGroup.getChildCount();
                for (int i = 0; i < radioButtonCount; i++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if (radioButton.getText().toString().equals(selectedText)) {
                        radioButton.setChecked(true);
                        break;
                    }
                }

                // Firebase의 플레이리스트 태그 정보를 가져와서 각 태그를 tags 문자열에 추가
                String tags = "";
                for (DataSnapshot tagSnapshot : dataSnapshot.child("tags").getChildren()) {
                    tags += tagSnapshot.getValue(String.class) + ",";
                }
                if (!tags.isEmpty()) {
                    tags = tags.substring(0, tags.length() - 1); // 마지막 쉼표 제거
                }
                etTag.setText(tags);
            }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(EditPlaylistActivity.this, "데이터를 가져오는 중 에러 발생", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // ----- 플레이리스트 수정 클릭 이벤트 -----
    public void saveChanges(View view) {
        // EditText에서 값을 가져오기
        String listName = etListName.getText().toString();
        // 선택된 라디오버튼 값 가져오기
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

        String selectedText = selectedRadioButton.getText().toString();
        // 태그 값 가져오기
        String inputText = etTag.getText().toString();
        String[] tags = inputText.split(",");


        // Firebase 데이터베이스에 데이터 업데이트하기
        if (playlistId != null) {
            // 수정된 데이터를 Firebase에 업데이트하기 위한 Map을 생성
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("listName", listName);
            updatedData.put("selectedText", selectedText);
            updatedData.put("tags", Arrays.asList(tags));

            // Firebase의 데이터베이스에서 해당 플레이리스트를 찾아서 updatedData에 있는 정보로 업데이트
            listsReference.child(playlistId).updateChildren(updatedData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditPlaylistActivity.this, "플레이리스트가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditPlaylistActivity.this, MyListsActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EditPlaylistActivity.this, "플레이리스트 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    // ----- 이전 버튼 클릭 이벤트 -----
    public void gotoMylists(View view) {
        Intent intent = new Intent(this, MyListsActivity.class);
        startActivity(intent);
        finish(); // MyListsActivity 종료
    }
}