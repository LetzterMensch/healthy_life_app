package com.example.gr.controller.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.gr.ControllerApplication;
import com.example.gr.databinding.ActivityLogInBinding;
import com.example.gr.model.Diary;
import com.example.gr.model.FoodLog;
import com.example.gr.model.Workout;
import com.example.gr.model.database.LocalDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LogInActivity extends BaseActivity{

    private ActivityLogInBinding mActivityLogInBinding;
    private FirebaseAuth mAuth;
    private String email;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLogInBinding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(mActivityLogInBinding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mActivityLogInBinding.btnLogIn.setOnClickListener(v->signIn());
        mActivityLogInBinding.layoutBtnSignUp.setOnClickListener(v->goToSignUpActivity());
    }
    private boolean validateEmailAndPassword(){
        email = mActivityLogInBinding.logInEditEmail.getText().toString().trim();
        password = mActivityLogInBinding.logInEditPassword.getText().toString().trim();
        System.out.println(email);
        System.out.println(password);
        if(email.isEmpty()){
            showAlertDialog("Vui lòng nhập email !");
            return false;
        }else if(password.isEmpty()){
            showAlertDialog("Vui lòng nhập mật khẩu !");
            return false;
        }
        return true;
    }
    private void goToSignUpActivity(){
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void signIn() {
        if(validateEmailAndPassword()){
            showProgressDialog(true);

            // [START sign_in_with_email]
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user != null){
                                    ControllerApplication.getApp().getUserDatabaseReference().child(user.getUid()).child("diary").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            List<Diary> diaryList = new ArrayList<>();
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                diaryList.add(dataSnapshot.getValue(Diary.class));
                                            }
                                            LocalDatabase.getInstance(getApplicationContext()).diaryDAO().insertAll(diaryList);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    ControllerApplication.getApp().getUserDatabaseReference().child(user.getUid()).child("foodlog").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            List<FoodLog> foodLogList = new ArrayList<>();
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                foodLogList.add(dataSnapshot.getValue(FoodLog.class));
                                            }
                                            LocalDatabase.getInstance(getApplicationContext()).foodLogDAO().insertAll(foodLogList);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    ControllerApplication.getApp().getUserDatabaseReference().child("workout").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            List<Workout> workoutList = new ArrayList<>();
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                workoutList.add(dataSnapshot.getValue(Workout.class));
                                            }
                                            LocalDatabase.getInstance(getApplicationContext()).workoutDAO().insertAll(workoutList);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                showProgressDialog(false);
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                showAlertDialog("Tài khoản hoặc mật khẩu không đúng !");
                            }
                        }
                    });
            // [END sign_in_with_email]
        }
    }


}
