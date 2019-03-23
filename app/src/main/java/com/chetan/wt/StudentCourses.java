package com.chetan.wt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentCourses extends AppCompatActivity {

    DatabaseReference reff;
    Course course;
    ArrayList<String> key;
    String StudentID,cid;
    private FirebaseAuth SID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_courses);
        key = new ArrayList<String>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StudentID = user.getUid();
        final ListView listView = findViewById(R.id.ListView);
        final ArrayList<String> myArrayList = new ArrayList<>();
        reff = FirebaseDatabase.getInstance().getReference("Student Courses").child(StudentID);

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myArrayList);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myArrayList.clear();
                key.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    course = ds.getValue(Course.class);
                    key.add(ds.getKey());
                    myArrayList.add("\n" + course.getName() + "\n" + course.getDate() + "     " + course.getStart() + "\n");
                }

                //else
                listView.setAdapter(myArrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(StudentCourses.this, CourseDetails.class);
                        i.putExtra("CourseID", key.get(position));
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


        Button delmultiple = findViewById(R.id.delete_multiple);
        delmultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myArrayList.size()!=0) {
                    Intent i = new Intent(v.getContext(), delete_multiple_courses.class);
                    startActivity(i);
                }
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

