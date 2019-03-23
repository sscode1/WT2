package com.chetan.wt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileStudent extends AppCompatActivity {
    TextView name, email, address, contact;
    String StudentID="1";
    DatabaseReference reff;
    private FirebaseAuth fa;
    String id;
    Button mbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_student);

        mbutton = findViewById(R.id.edit);
        name = findViewById(R.id.editSname);
        contact = findViewById(R.id.qualification);
        address = findViewById(R.id.location);
        email = findViewById(R.id.email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        reff = FirebaseDatabase.getInstance().getReference("Students");
        StudentID = user.getUid();

        reff.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                email.setText(dataSnapshot.child("mail").getValue().toString());
                contact.setText(dataSnapshot.child("qualification").getValue().toString());
                address.setText(dataSnapshot.child("city").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),EditStudentProfile.class);
                i.putExtra("StudentID",StudentID);
                startActivity(i);
            }
        });



        Button auth = findViewById(R.id.changeAuth);
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ChangeAuthInfo.class);
                startActivity(i);
            }
        });


    }
}

