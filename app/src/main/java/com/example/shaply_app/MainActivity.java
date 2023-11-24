package com.example.shaply_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TextView userInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Footer Fragment 추가
        FooterFragment footerFragment = new FooterFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.footer_container, footerFragment)
                .commit();

        // 사용자 데이터 표시할 요소 갖고 오기
        userInfoTextView = findViewById(R.id.iv_profiles);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            // 데이터 변경 감지 및 표시
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userEmail = dataSnapshot.child("email").getValue(String.class);
                        String userName = dataSnapshot.child("name").getValue(String.class);

                        // 데이터를 가져와서 화면에 표시
                        String userInfo = "사용자 이메일: " + userEmail + "\n사용자 이름: " + userName + "\n사용자 UID " + userId;
                        userInfoTextView.setText(userInfo);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // 데이터 불러오기 실패 시 처리
                    String userInfo = "데이터 불러오기 실패";
                    userInfoTextView.setText(userInfo);
                }
            });
        } else {
            // No user is signed in
        }


    }


    // < FOOTER >
    // 커뮤니티 버튼 onClick 이벤트 핸들러
    public void gotoCommunity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // MainActivity 종료
    }

    // 로그아웃 버튼 onClick 이벤트 핸들러
    public void googleLogout(View view) {
        signOut(); // 로그아웃 메서드
    }

    // 내 리스트 버튼 onClick 이벤트 핸들러
    public void gotoMylists(View view) {
        Intent intent = new Intent(this, MyListsActivity.class);
        startActivity(intent);
        finish(); // MainActivity 종료
    }

    // 로그아웃 메서드
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        // 로그아웃 후 추가 작업이 있다면 여기에 구현
        // 로그아웃 후 LoginActivity로 이동
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // MainActivity 종료
    }


}
