package com.example.lakshaysharma.firebasetutorial;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddToDatabase extends AppCompatActivity {

    private static final String TAG = "AddToDatabase";

    private EditText phone, name, email;
    private Button add;
    private String userID;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_database);

        name = findViewById(R.id.add_new_name);
        email = findViewById(R.id.add_new_email);
        phone = findViewById(R.id.add_new_phone);
        add = findViewById(R.id.btnAddToDB);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                userID = mAuth.getCurrentUser().getUid();

                if (mAuth.getCurrentUser() != null){
                    Log.d(TAG, "onAuthStateChanged: UserID- " + userID);
                }
            }
        };

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textName = name.getText().toString().trim();
                String textEmail = email.getText().toString().trim();
                String textPhone = phone.getText().toString().trim();

                mDatabaseRef.child(userID).child("Name").setValue(textName);
                mDatabaseRef.child(userID).child("Email").setValue(textEmail);
                mDatabaseRef.child(userID).child("Phone").setValue(textPhone);

                name.setText("");
                email.setText("");
                phone.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
