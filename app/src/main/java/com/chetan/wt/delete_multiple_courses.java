package com.chetan.wt;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
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

public class delete_multiple_courses extends AppCompatActivity {

    DatabaseReference reff, notify, temp;
    TutorNotification notification = new TutorNotification();
    Course course;
    ArrayList<String> key, tutorId, course_name, course_date;
    String StudentID,cid, tid;
    private FirebaseAuth SID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_multiple_courses);

        key = new ArrayList<String>();
        tutorId = new ArrayList<String>();
        course_name = new ArrayList<String>();
        course_date = new ArrayList<String>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StudentID = user.getUid();

        temp = FirebaseDatabase.getInstance().getReference("Students");
        temp.child(StudentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").getValue()!=null)
                notification.setStudent_name(dataSnapshot.child("name").getValue().toString()+" unregistered for ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final ListView listView = findViewById(R.id.ListView);
        final ArrayList<String> myArrayList = new ArrayList<>();
        reff = FirebaseDatabase.getInstance().getReference("Student Courses").child(StudentID);

        final ArrayList<Integer> total_stu = new ArrayList<>();
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, myArrayList);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myArrayList.clear();
                key.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    course = ds.getValue(Course.class);
                    key.add(ds.getKey());
                    course_name.add(course.getName());
                    course_date.add(course.getDate());
                    tutorId.add(course.getTId());
                    myArrayList.add("\n" + course.getName() + "\n");
                 //   total_stu.add(course.getNo_of_students());

                }
                listView.setAdapter(myArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        final DatabaseReference tu_ref = FirebaseDatabase.getInstance().getReference("Tutor Courses");

        tu_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(int i=0;i<key.size();i++){
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        if(ds.getKey().equals(key.get(i))){
                            total_stu.add(Integer.parseInt(ds.child("no_of_students").getValue().toString()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        View.OnClickListener listenerDel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(delete_multiple_courses.this);
                builder.setMessage("Are you sure you want to Delete this Course?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int x) {
                        /** Getting the checked items from the listview */
                        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
                        int itemCount = listView.getCount();

                        for(int i=itemCount-1; i >= 0; i--){
                            if(checkedItemPositions.get(i)){
                                myArrayAdapter.remove(myArrayList.get(i));
                                cid = key.get(i);
                                tid = tutorId.get(i);
                                notification.setCourse_name(course_name.get(i) + " which is on ");
                                notification.setDate(course_date.get(i));
                                final DatabaseReference tut = FirebaseDatabase.getInstance().getReference("Tutor Courses").child(cid);
                                notify = FirebaseDatabase.getInstance().getReference("Tutor Notifications").child(tid);
                                notify.push().setValue(notification);
                                DatabaseReference del = FirebaseDatabase.getInstance().getReference("Student Courses").child(StudentID).child(cid);
                                del.setValue(null);
                                int n = total_stu.get(i)-1;
                                tut.child("no_of_students").setValue(n);


                            }
                        }
                        Toast.makeText(getApplicationContext(),"Deleted Succesfully",Toast.LENGTH_SHORT).show();
                        checkedItemPositions.clear();
                        myArrayAdapter.notifyDataSetChanged();
                        finish();
                    }
                }).setNegativeButton("Cancel",null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        };

        Button delmultiple = findViewById(R.id.delete_multiple);
        delmultiple.setOnClickListener(listenerDel);

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
