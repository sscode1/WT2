package com.chetan.wt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TutorProfileToStudent extends AppCompatActivity {

    TextView nameed,cityed,qfed,emailed;
    String names,citys,qfs,emails;

    private FirebaseAuth fa;
    private DatabaseReference dbr;
    private Context context;
    Button eb;

    TextView nameview,emailview,qfview,cityview;
    FloatingActionButton fab;
    String s1,s2;
    Bitmap bitmap;
    de.hdodenhof.circleimageview.CircleImageView pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile_to_student);
        context = this;

        nameview= findViewById(R.id.namedisplay);
        emailview= findViewById(R.id.emaildisplay);
        qfview= findViewById(R.id.degreedisplay);
        cityview= findViewById(R.id.locationdisplay);
        fab= findViewById(R.id.floatingActionButton);
        pro= findViewById(R.id.profilephoto);
        eb= findViewById(R.id.emailbutton);


        String id = getIntent().getStringExtra("TId");
        dbr= FirebaseDatabase.getInstance().getReference("users");

        dbr.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameview.setText(dataSnapshot.getValue(user.class).getName());
                emailview.setText(dataSnapshot.getValue(user.class).getEmail());
                qfview.setText(dataSnapshot.getValue(user.class).getDegree());
                cityview.setText(dataSnapshot.getValue(user.class).getCity());
                String imageUri = dataSnapshot.getValue(user.class).getDurl();
                //Toast.makeText(getApplicationContext(),imageUri,Toast.LENGTH_LONG).show();
                //ImageView ivBasicImage = (ImageView) findViewById(R.id.ivBasicImage);
                if(imageUri!="")
                    Picasso.get().load(imageUri).fit().centerCrop().into(pro);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        eb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",emailview.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });


    }
}
