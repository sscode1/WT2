package com.chetan.wt;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CourseDetails extends AppCompatActivity {
    TextView name, agenda, date, time, duration, venue, tname;
    Button delete;
    String cid, tid;
    Intent in;
    String sid;

    DatabaseReference reff, notify, temp;
    TutorNotification notification = new TutorNotification();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);



        name = findViewById(R.id.Cname);
        tname = findViewById(R.id.Tname);
        agenda = findViewById(R.id.Cagenda);
        date = findViewById(R.id.Cdate);
        time = findViewById(R.id.Ctime);
        duration = findViewById(R.id.Cduration);
        venue = findViewById(R.id.Cvenue);
        cid = getIntent().getStringExtra("CourseID");
        //Toast.makeText(this,ID,Toast.LENGTH_SHORT).show();


        final int[] total_std = new int[1];

        final int[] count = new int[1];

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        sid = user.getUid();

        temp = FirebaseDatabase.getInstance().getReference("Students");
        temp.child(sid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").getValue()!=null)
                notification.setStudent_name(dataSnapshot.child("name").getValue().toString()+" unregistered for ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference tu_ref = FirebaseDatabase.getInstance().getReference("Tutor Courses").child(cid);

        reff = FirebaseDatabase.getInstance().getReference("Student Courses").child(sid).child(cid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0) {
                    name.setText(dataSnapshot.child("name").getValue().toString());
                    notification.setCourse_name(dataSnapshot.child("name").getValue().toString()+ " which is on ");
                    agenda.setText(dataSnapshot.child("agenda").getValue().toString());
                    date.setText(dataSnapshot.child("date").getValue().toString());
                    notification.setDate(dataSnapshot.child("date").getValue().toString());
                    tname.setText(dataSnapshot.child("tname").getValue().toString());
                    time.setText(dataSnapshot.child("start").getValue().toString());
                    duration.setText(dataSnapshot.child("duration").getValue().toString());
                    venue.setText(dataSnapshot.child("venue").getValue().toString());
                    tid = dataSnapshot.child("tid").getValue().toString();
                  //  count[0] = Integer.parseInt(dataSnapshot.child("no_of_students").getValue().toString());

                }
                else
                    finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        delete = findViewById(R.id.delete);
        tu_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count[0] = Integer.parseInt(dataSnapshot.child("no_of_students").getValue().toString());
               // Toast.makeText(getApplicationContext(),String.valueOf(count[0]),Toast.LENGTH_LONG).show();
                count[0]--;
                //tut.child("no_of_students").setValue(count[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference tut = FirebaseDatabase.getInstance().getReference("Tutor Courses").child(cid);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetails.this);

                builder.setMessage("Are you sure you want to Delete this Course?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notify = FirebaseDatabase.getInstance().getReference("Tutor Notifications").child(tid);
                        notify.push().setValue(notification);
                        DatabaseReference del = FirebaseDatabase.getInstance().getReference("Student Courses").child(sid).child(cid);
                     //  count[0]--;
                        tut.child("no_of_students").setValue(count[0]);
                        del.setValue(null);
                        Toast.makeText(getApplicationContext(),"Deleted Succesfully",Toast.LENGTH_SHORT).show();
                        Intent in =new Intent(getApplicationContext(),CourseList.class);
                        startActivity(in);
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
