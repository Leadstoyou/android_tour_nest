package com.example.tour_nest.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tour_nest.activity.home.HomeActivity;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.databinding.ActivityLoginBinding;
import com.example.tour_nest.model.User;
import com.example.tour_nest.service.UserService;
import com.example.tour_nest.util.SharedPrefHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.example.tour_nest.R;

import java.util.Objects;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 9001;
    private final FirebaseCallback<User> callbackResult = new FirebaseCallback<User>() {
        @Override
        public void onSuccess(User result) {
            if (result != null) {
                Toast.makeText(getBaseContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                if(binding.chkRememberMe.isChecked()){
                    SharedPrefHelper.saveUser(getBaseContext(), result);
                }
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Exception e) {
            Toast.makeText(getBaseContext(), "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
            Log.e("LoginError", "Lỗi khi đăng nhập: ", e);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(v -> handleLocalSignIn());
        binding.btnGoogle.setOnClickListener(v -> signInWithGoogle());
        binding.btnFacebook.setOnClickListener(v -> {
            Toast.makeText(this, "Developing", Toast.LENGTH_SHORT).show();
        });
        binding.tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleLocalSignIn() {
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString();
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
        UserService.login(new User(email, password))
                .onResult(callbackResult);
    }

    private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            UserService.handleGoogleSignIn(firebaseUser).onResult(callbackResult);
                        }
                    } else {
                        Toast.makeText(this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
