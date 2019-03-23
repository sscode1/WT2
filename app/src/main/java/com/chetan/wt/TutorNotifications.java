package com.chetan.wt;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TutorNotifications extends AppCompatActivity {
    TutorNotification notification = new TutorNotification();
    DatabaseReference reff;
    String Tid;
    //private FirebaseAuth
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_notifications);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Tid = user.getUid();
        final ListView listView = findViewById(R.id.ListView);
        final ArrayList<String> myArrayList = new ArrayList<>();
        reff = FirebaseDatabase.getInstance().getReference("Tutor Notifications").child(Tid);

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myArrayList);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myArrayList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    notification = ds.getValue(TutorNotification.class);
                    myArrayList.add("\n" + notification.getStudent_name() + notification.getCourse_name() +  notification.getDate() + "\n");
                }
                listView.setAdapter(myArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button clear_notification = findViewById(R.id.clear_notifications);
        clear_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TutorNotifications.this);

                builder.setMessage("Are you sure you want to clear the notifictaions?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference del = FirebaseDatabase.getInstance().getReference("Tutor Notifications").child(Tid);
                        del.setValue(null);
                        Toast.makeText(getApplicationContext(),"Cleared Succesfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).setNegativeButton("Cancel",null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
