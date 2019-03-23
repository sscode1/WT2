package com.chetan.wt;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBuffer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SelectCourse extends AppCompatActivity {

    DatabaseReference db, reg, temp, notify,ref;
    String sid,cid,url;
    DatabaseReference reff;
    Course course1 = new Course();
    TutorNotification notification = new TutorNotification();
    Intent in;
    String TutorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectcourse);

        final TextView tutor = findViewById(R.id.TNAME);
        final TextView course = findViewById(R.id.CNAME);
        final TextView agenda = findViewById(R.id.AGENDA);
        final TextView date = findViewById(R.id.DATE);
        final TextView duration = findViewById(R.id.DURATION);
        final TextView venue = findViewById(R.id.VENUE);
        final ImageView iv = findViewById(R.id.imageView2);
        final TextView start = findViewById(R.id.START);
        cid = getIntent().getStringExtra("CourseID");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        sid = user.getUid();
        final int[] n = new int[1];
        final ArrayList<String> list = new ArrayList<>();



        temp = FirebaseDatabase.getInstance().getReference("Students");
        temp.child(sid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").getValue()!=null)
                notification.setStudent_name(dataSnapshot.child("name").getValue().toString()+" registered for ");
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        db = FirebaseDatabase.getInstance().getReference("Tutor Courses").child(cid);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("tname").getValue()!=null){
                    tutor.setText(dataSnapshot.child("tname").getValue().toString());
                    course.setText(dataSnapshot.child("name").getValue().toString());
                    venue.setText(dataSnapshot.child("venue").getValue().toString());
                    duration.setText(dataSnapshot.child("duration").getValue().toString());
                    agenda.setText(dataSnapshot.child("agenda").getValue().toString());
                    date.setText(dataSnapshot.child("date").getValue().toString());
                    start.setText(dataSnapshot.child("start").getValue().toString());
                    course1.setTname(dataSnapshot.child("tname").getValue().toString());
                    course1.setName(dataSnapshot.child("name").getValue().toString());
                    notification.setCourse_name(dataSnapshot.child("name").getValue().toString()+" which is on ");
                    course1.setVenue(dataSnapshot.child("venue").getValue().toString());
                    course1.setDuration(dataSnapshot.child("duration").getValue().toString());
                    course1.setAgenda(dataSnapshot.child("agenda").getValue().toString());
                    course1.setDate(dataSnapshot.child("date").getValue().toString());
                    notification.setDate(dataSnapshot.child("date").getValue().toString());
                    course1.setStart(dataSnapshot.child("start").getValue().toString());
                    course1.setTId(dataSnapshot.child("tid").getValue().toString());
                    n[0] = Integer.parseInt(dataSnapshot.child("no_of_students").getValue().toString());
                    TutorId = dataSnapshot.child("tid").getValue().toString();
                    ref = FirebaseDatabase.getInstance().getReference("users").child(dataSnapshot.child("tid").getValue().toString()).child("durl");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            url = dataSnapshot.getValue(String.class);
                            Picasso.get().load(url).error(R.drawable.noimage).into(iv);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            iv.setImageResource(R.drawable.noimage);
                        }
                    });
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageView imageView = findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.tutor2);


        Button button = findViewById(R.id.button5);
        final DatabaseReference ch = FirebaseDatabase.getInstance().getReference("Student Courses");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int[] flag = new int[1];
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectCourse.this);

                DatabaseReference dbstu = FirebaseDatabase.getInstance().getReference("Student Courses").child(sid);
                dbstu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        flag[0] = 0;
                        //          key.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (cid.equals(ds.getKey())) {
                                flag[0] = 1;
                                Log.i("REFERENCE", "1");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Log.i("Inside Builder", "Problem");

                builder.setMessage("Are you sure you want to Register?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (flag[0] == 1) {
                            Toast.makeText(getApplicationContext(), "Already Registered for this course", Toast.LENGTH_LONG).show();
                        }
                        else {
                            reg = FirebaseDatabase.getInstance().getReference("Student Courses").child(sid).child(cid);
                            n[0]++;
                            notify = FirebaseDatabase.getInstance().getReference("Tutor Notifications").child(course1.getTId());
                            notify.push().setValue(notification);
                            reg.setValue(course1);
                            if(course1.getTId()!=null){
                                System.out.println("HELo" + course1.getTId());
                                reg.child("no_of_students").setValue(n[0]);
                                db.child("no_of_students").setValue(n[0]);
                            }
                            DatabaseReference st_ref = FirebaseDatabase.getInstance().getReference("Student Courses");
                            st_ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                                        for (DataSnapshot ds1:ds.getChildren()){
                                            if (ds1.getKey().equals(cid)){
                                                list.add(ds.getKey());
                                           //     Toast.makeText(getApplication(),ds.getKey(),Toast.LENGTH_LONG).show();

                                                break;
                                            //    Toast.makeText(getApplication(),ds.getKey(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Toast.makeText(getApplicationContext(), "Registered Succesfully", Toast.LENGTH_SHORT).show();
                            in = new Intent(getApplicationContext(),StudentCourses.class);

                            for (int j=0;j<list.size();j++){
                                ch.child(list.get(j)).child(cid).setValue(n[0]);
                             //   Toast.makeText(getApplicationContext(),String.valueOf(n[0]),Toast.LENGTH_LONG).show();
                                System.out.println(list.get(j) + "   kkk");
                            }

                            startActivity(in);

                        }

                    }
                }).setNegativeButton("Cancel",null);
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SelectCourse.this, TutorProfileToStudent.class);
                i.putExtra("TId", TutorId);

                startActivity(i);

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
