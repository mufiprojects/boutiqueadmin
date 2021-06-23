package com.example.acer.boutiqueadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class user_state extends AppCompatActivity {

    TextView userNameTextView;

    MaterialButton activateBtn,deActivateBtn;

    String userName,userId;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseActiveUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_state);

        databaseActiveUsers = database.getReference("activeUsers");
        userNameTextView=findViewById(R.id.userName);
        activateBtn=findViewById(R.id.activate_btn);
        deActivateBtn=findViewById(R.id.deactivate_btn);
        userNameTextView.setText("hello");

        Intent intent=getIntent();
        userName=intent.getStringExtra("userName");
        userNameTextView.setText(userName);

        userId=intent.getStringExtra("userId");

        activateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setState(true);
            }
        });
        deActivateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setState(false);
            }
        });
    }
    private void setState(Boolean setState){
        databaseActiveUsers.child(userId).setValue(setState);
    }
}