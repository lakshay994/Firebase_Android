package com.example.lakshaysharma.firebasetutorial;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDatabase extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";
    private ListView listView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = findViewById(R.id.listview);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                userID = user.getUid();
                if (user != null){
                    Log.d(TAG, "onAuthStateChanged: UID" + user.getUid());
                    Log.d(TAG, "onAuthStateChanged: Email" + user.getEmail());
                }
            }
        };

        Query query = mDatabaseRef.child("Users").equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                displayData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayData(DataSnapshot dataSnapshot) {

        String name = dataSnapshot.getValue(String.class);
        Log.d(TAG, "displayData: " + name);

        /*for (DataSnapshot data: dataSnapshot.getChildren()){
            DatabaseExtraction extraction = new DatabaseExtraction();
            extraction.setEmail(data.child(userID).getValue(DatabaseExtraction.class).getEmail());
            extraction.setName(data.child(userID).getValue(DatabaseExtraction.class).getName());
            extraction.setPhone(data.child(userID).getValue(DatabaseExtraction.class).getPhone());

            Log.d(TAG, "showData: name: " + extraction.getName());
            Log.d(TAG, "showData: email: " + extraction.getEmail());
            Log.d(TAG, "showData: phone_num: " + extraction.getPhone());

            *//*ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(extraction.getName());
            arrayList.add(extraction.getEmail());
            arrayList.add(extraction.getPhone());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(adapter);*//*
        }*/
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
