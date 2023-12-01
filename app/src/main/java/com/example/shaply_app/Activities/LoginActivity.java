package com.example.shaply_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shaply_app.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase 초기화
        FirebaseApp.initializeApp(this);

        // Firebase 인증 객체 초기화
        mAuth = FirebaseAuth.getInstance();

        // Google 로그인 버튼 찾기
        ImageButton signInButton = findViewById(R.id.btn_google_sign_in);

        // Google 로그인 옵션 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Google 로그인 클라이언트 설정
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Activity Result 처리를 위한 런처 등록
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        handleSignInResult(data);
                    }
                });

        // Google 로그인 버튼 클릭 리스너 설정
        signInButton.setOnClickListener(view -> signIn());
    }

    // Google 로그인 메서드
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        someActivityResultLauncher.launch(signInIntent);
    }

    // Google 로그인 결과 처리
    private void handleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google 로그인 성공 시 Firebase로 인증
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // Google 로그인 실패 시 UI 업데이트
            Log.w("GoogleActivity", "Google sign in failed", e);
            Toast.makeText(LoginActivity.this, "Google 로그인 실패", Toast.LENGTH_SHORT).show();
        }
    }

    // Google 인증으로 Firebase에 인증
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 로그인 성공 시 UI 업데이트
                        Log.d("GoogleActivity", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // 로그인 실패 시 메시지 표시
                        Log.w("GoogleActivity", "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "인증 실패", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });

    }

    // FirebaseUser를 이용한 UI 업데이트
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // MainActivity로 이동하여 로그인 성공
            Intent intent = new Intent(this, CommunityActivity.class);
            startActivity(intent);
            finish(); // 현재 LoginActivity 종료
        }
    }

    }

