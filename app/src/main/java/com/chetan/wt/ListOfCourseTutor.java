package com.chetan.wt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfCourseTutor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<String> key;
    Course mCourse;
    String TId;
    String Tid;
    int flag = 0;
    ArrayList<Course> course_list;
    CardView cd;
    DatabaseReference refff;

    ProgressBar pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_course_tutor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Intent intent = getIntent();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mCourse = new Course();
        Button newcourse = findViewById(R.id.newcourse);
        final ListView courselist = findViewById(R.id.courselist);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tutor Courses");
        pg = findViewById(R.id.progressBar);
        cd = findViewById(R.id.card_view);

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);


        key = new ArrayList<>();
        course_list = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                list.clear();
                course_list.clear();
                key.clear();

                pg.setVisibility(View.VISIBLE);
                for(DataSnapshot ds:dataSnapshot.getChildren()) {



                    if(ds.getValue(Course.class).getTId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                        key.add(ds.getKey());
                        mCourse = ds.getValue(Course.class);
                       // list.add("\nCourse Name:    " + mCourse.getName() + "\nVenue:             " + mCourse.getVenue() + "\nDate:              " + mCourse.getDate() + "\nTime:              " + mCourse.getStart() + "\nTotal no. of Students enrolled:" + mCourse.getNo_of_students());
                        course_list.add(mCourse);
                    }


                }
                pg.setVisibility(View.GONE);

                CustomAdapter customAdapter = new CustomAdapter();
                courselist.setAdapter(customAdapter);

                courselist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ListOfCourseTutor.this, CourseViewOnly.class);
                        mCourse = course_list.get(i);
                        intent.putExtra("CourseID",key.get(i));
                        // Toast.makeText(MainActivity.this,Integer.toString(course_list.size()),Toast.LENGTH_SHORT).show();
                        intent.putExtra("Course_name", mCourse.getName());
                        intent.putExtra("Tutor_name", mCourse.getTname());
                        intent.putExtra("Venue", mCourse.getVenue());
                        intent.putExtra("Time", mCourse.getStart());
                        intent.putExtra("Duration", mCourse.getDuration());
                        intent.putExtra("agenda", mCourse.getAgenda());
                        intent.putExtra("date", mCourse.getDate());
                        intent.putExtra("TId",mCourse.getTId());
                        //intent.putExtra("id", mCourse.getI());
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        newcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListOfCourseTutor.this,CourseDetailsTutor.class);
                startActivity(i);
            }
        });
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return key.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customview,null);

            TextView cours_name = convertView.findViewById(R.id.cou_name);
            TextView course_date = convertView.findViewById(R.id.cou_date);
            TextView no_enrolled = convertView.findViewById(R.id.no_of_en);

            cours_name.setText(course_list.get(position).getName());
            course_date.setText(course_list.get(position).getDate());
            no_enrolled.setText(String.valueOf(course_list.get(position).getNo_of_students()));


            return convertView;
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(flag==1){
                finishAffinity();
            }
            else {
                Toast.makeText(this,"Press once again to exit",Toast.LENGTH_SHORT).show();
                flag++;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_of_course_tutor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nmy_profile) {
            // Handle the camera action

            Intent it=new Intent(ListOfCourseTutor.this,Profile.class);
            startActivity(it);

        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(ListOfCourseTutor.this,TutorNotifications.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.Logout) {
            FirebaseAuth fbu=FirebaseAuth.getInstance();
            fbu.signOut();
            Toast.makeText(getApplicationContext(),"logout successful",Toast.LENGTH_SHORT).show();
            Intent it=new Intent(getApplicationContext(),Welcome.class);
            startActivity(it);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_developers) {
            Intent it=new Intent(ListOfCourseTutor.this,developers.class);
            startActivity(it);

        } else if (id == R.id.nav_feedback)
        {
            Intent it=new Intent(ListOfCourseTutor.this,feedback_activity.class);
            startActivity(it);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
