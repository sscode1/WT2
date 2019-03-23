package com.chetan.wt;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
    }

    public void login_tutor(View view){
        Intent intent = new Intent(this, loginTutor.class);
        startActivity(intent);
    }

    public void login_student(View view){
        Intent intent = new Intent(this, loginStudent.class);
        startActivity(intent);
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

}
