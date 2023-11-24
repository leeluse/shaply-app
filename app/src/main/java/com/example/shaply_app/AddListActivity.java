package com.example.shaply_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        // Footer Fragment 추가
        FooterFragment footerFragment = new FooterFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.footer_container, footerFragment)
                .commit();
    }
}