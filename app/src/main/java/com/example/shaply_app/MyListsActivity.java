package com.example.shaply_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;

public class MyListsActivity extends AppCompatActivity {

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