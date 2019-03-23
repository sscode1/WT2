package com.chetan.wt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class Profile extends AppCompatActivity {
    TextView nameview,emailview,qfview,cityview;
    FloatingActionButton fab,fab2;
    String s1,s2;
    Bitmap bitmap;
    Button eb;
    private FirebaseAuth fa;
    private DatabaseReference dbr;
    private Context context;
    Button changeAuth;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    de.hdodenhof.circleimageview.CircleImageView pro;
    /* @GlideModule
     public class MyAppGlideModule extends AppGlideModule {

         @Override
         public void registerComponents(Context context, Glide glide, Registry registry) {
             // Register FirebaseImageLoader to handle StorageReference
             registry.append(StorageReference.class, InputStream.class,
                     new FirebaseImageLoader.Factory());
         }
     }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameview= findViewById(R.id.namedisplay);
        emailview= findViewById(R.id.emaildisplay);
        qfview= findViewById(R.id.degreedisplay);
        cityview= findViewById(R.id.locationdisplay);
        fab= findViewById(R.id.floatingActionButton);
        changeAuth = findViewById(R.id.changeAuth);
        //fab2=(FloatingActionButton)findViewById(R.id.floatingActionButton2);
        // eb=(Button)findViewById(R.id.emailbutton);
        pro= findViewById(R.id.profilephoto);
        fa=FirebaseAuth.getInstance();
        FirebaseUser cuser = fa.getCurrentUser();
        dbr= FirebaseDatabase.getInstance().getReference("users");
        final String id=cuser.getUid();
        Intent int1=getIntent();
        //s1=(String)int1.getSerializableExtra("Username");
        //s2=(String)int1.getSerializableExtra("Email");
        final StorageReference mountainsRef = storageRef.child(id+".jpg");
        dbr.child(id).addValueEventListener(new ValueEventListener() {
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
        //StorageReference mountainsRef = storageRef.child(id+".jpg");


        //nameview.setText(dbr.getDatabase().);
        //emailview.setText(s2);
        //qfview.setText((String)int1.getSerializableExtra("degree"));
        //cityview.setText((String)int1.getSerializableExtra("city"));
        //bitmap=int1.getParcelableExtra("BitmapImage");
        //pro.setImageBitmap(bitmap);
        /*eb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",emailview.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int2=new Intent(Profile.this,EditProfile.class);
                int2.putExtra("Username",nameview.getText().toString());
                int2.putExtra("qf",qfview.getText().toString());
                int2.putExtra("city",cityview.getText().toString());
                int2.putExtra("bitmap",bitmap);
                int2.putExtra("em",emailview.getText().toString());
                startActivity(int2);
            }
        });

        changeAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this,ChangeAuthInfoTutor.class);
                startActivity(i);
                dbr= FirebaseDatabase.getInstance().getReference("users");
                dbr.child(id).addValueEventListener(new ValueEventListener() {
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

            }
        });



     /*   fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}