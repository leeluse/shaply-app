package com.example.shaply_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddListActivity extends AppCompatActivity {

    EditText etListName;
    EditText etTag;
    RadioGroup radioGroup;
    DatabaseReference listsReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        // EditText들을 초기화합니다.
        etListName = findViewById(R.id.et_listName);
        etTag = findViewById(R.id.et_tag);
        radioGroup = findViewById(R.id.radioGroup);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        listsReference = database.getReference().child("lists");

    }

    // 다음 단계로 넘어가기
    public void gotoNextStep(View view) {

        // 선택된 라디오버튼의 ID 가져오기
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        // 선택된 라디오버튼을 찾아 레퍼런스를 얻습니다.
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

        // EditText에서 값을 가져옵니다.
        String listName = etListName.getText().toString();
        String selectedText = selectedRadioButton.getText().toString();
        String inputText = etTag.getText().toString();
        String[] tags = inputText.split(",");


        // Firebase 데이터베이스에 데이터 추가하기
        String key = listsReference.push().getKey(); // 새로운 키 생성

        Map<String, Object> listData = new HashMap<>();
        listData.put("listName", listName);
        listData.put("selectedText", selectedText);
        listData.put("tags", Arrays.asList(tags));

        listsReference.child(key).setValue(listData);
        Intent intent = new Intent(this, MyListsActivity.class);
        startActivity(intent);
        finish(); // MyListsActivity 종료
    }


    // Mylist로 돌아가기
    public void gotoMylists(View view) {
        Intent intent = new Intent(this, MyListsActivity.class);
        startActivity(intent);
        finish(); // MyListsActivity 종료
    }
}