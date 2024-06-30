package com.example.gr.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gr.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends BaseActivity{
    private static final String TAG = "EmailPassword";

    private ActivitySignUpBinding mActivitySignUpBinding;
    private FirebaseAuth mAuth;
    private String email;
    private String confirmPassword;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignUpBinding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mActivitySignUpBinding.btnSignUp.setOnClickListener(v->createAccount());
    }
    private boolean validateEmailAndPassword(){
        email = mActivitySignUpBinding.signUpEditEmail.getText().toString().trim();
        confirmPassword = mActivitySignUpBinding.signUpEditConfirmPassword.getText().toString().trim();
        password = mActivitySignUpBinding.signUpEditPassword.getText().toString().trim();
        if(email.isEmpty()){
            showAlertDialog("Vui lòng nhập email !");
            return false;
        }else if(password.isEmpty()){
            showAlertDialog("Vui lòng nhập mật khẩu !");
            return false;
        }else if(confirmPassword.isEmpty()){
            showAlertDialog("Vui lòng nhập xác nhận mật khẩu  !");
            return false;
        }
        if(!password.equals(confirmPassword)){
            showAlertDialog("Xác nhận mật khẩu chưa chính xác  !");
            return false;
        }
        return true;
    }
    private void createAccount() {
        // [START create_user_with_email]
        if(validateEmailAndPassword()){
            showProgressDialog(true);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                showProgressDialog(false);
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        // [END create_user_with_email]
    }
}
