package com.chetan.wt;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerStudent extends AppCompatActivity {

    private int flag=0, flag_img = 0;
    int logic = 1;
    private String id;
    private static int RESULT_LOAD_IMAGE = 1;
    Button fetch;

    private FirebaseAuth mAuth;
    private FirebaseAuth fa;
    private DatabaseReference dataBase;
    private StorageReference storageReference;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    double[] latarray = new double[5];
    double[] lonarray = new double[5];
    String[] places = new String[5];
    String x="Mangalore";
    Double lat, lon;
    TextView user_location;

    public void Places() {
        places[0] = "mangalore";
        places[1] = "udupi";
        places[2] = "bangalore";
        places[3] = "kasaragod";
        places[4] = "goa";
    }

    public void Latarray() {
        latarray[0] = 12.917;
        latarray[1] = 13.3323;
        latarray[2] = 12.9716;
        latarray[3] = 12.5033;
        latarray[4] = 15.2993;

    }

    public void lonarray() {
        lonarray[0] = 74.85603;
        lonarray[1] = 74.746;
        lonarray[2] = 77.5946;
        lonarray[3] = 74.9896;
        lonarray[4] = 74.124;
    }


    String downloadUrl=new String("https://i.imgur.com/tGbaZCY.jpg");

    ImageButton profiledp;
    Button register;
    Bitmap bitmap;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private ProgressDialog pb;
    students student_user;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== RESULT_LOAD_IMAGE && resultCode== RESULT_OK && null!=data){
            bitmap =(Bitmap)data.getExtras().get("data");
            profiledp.setImageBitmap(bitmap);
            flag_img=1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance().getReference().child("Students");
        storageReference = FirebaseStorage.getInstance().getReference().child("Students");

        register = findViewById(R.id.registerbutton);
        profiledp = findViewById(R.id.stuDp);

        pb=new ProgressDialog(this);
        pb.setMessage("Registering...");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                final Intent Newpage = new Intent(registerStudent.this, loginStudent.class);

                final TextView name = findViewById(R.id.name);
                final TextView mail = findViewById(R.id.email);
                final TextView pass = findViewById(R.id.password);
                final TextView pass_cpy = findViewById(R.id.confirmpassword);


                fetch = findViewById(R.id.cityview);
                user_location= findViewById(R.id.city);
                final TextView qualification = findViewById(R.id.qualification);


                final String Name = name.getText().toString();
                final String Mail = mail.getText().toString();
                final String Pass = pass.getText().toString();
                final String Cpy_Pass = pass_cpy.getText().toString();
                final String Quali = qualification.getText().toString();

                if (TextUtils.isEmpty(Name)) {
                    name.setError("Enter name!!");
                    flag = 1;
                }
                if (TextUtils.isEmpty(Mail)) {
                    mail.setError("Enter email!!");
                    flag = 1;
                }
                if (TextUtils.isEmpty(Pass)) {
                    pass.setError("Enter Password!!");
                    flag = 1;
                }
                if (TextUtils.isEmpty(Cpy_Pass)) {
                    pass.setError("Confirm Password!!");
                    flag = 1;
                }

                if (TextUtils.isEmpty(Quali)) {
                    pass.setError("Enter Qualification!!");
                    flag = 1;
                }


                String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                final Matcher mail_matcher = pattern.matcher(Mail);

                Pattern name_pattern = Pattern.compile("[A-Za-z ]+");
                final Matcher name_matcher = name_pattern.matcher(Name);
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(registerStudent.this);
                fetch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        fetchLocation();


                    }
                });

                if (name_matcher.matches() && mail_matcher.matches() && Pass.length() >= 8 && Pass.equals(Cpy_Pass) && flag == 0) {
                    //Toast.makeText(getApplicationContext(), "HI", Toast.LENGTH_LONG).show();
                    pb.show();
                    mAuth.createUserWithEmailAndPassword(Mail, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser cuser = fa.getCurrentUser();
                                id = cuser.getUid();

                                try {
                                    TimeUnit.SECONDS.sleep(0);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                logic = 0;
                                Intent Newpage = new Intent(getApplicationContext(), CourseList.class);

                                if (flag_img == 1) {
                                    final StorageReference students_img = storageReference.child(id + ".jpg");
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    UploadTask uploadTask = students_img.putBytes(data);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            students_img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    downloadUrl = uri.toString();
                                                    student_user = new students(id, Name, Mail, Quali, x, downloadUrl);
                                                    dataBase.child(id).setValue(student_user);
                                                }
                                            });
                                        }
                                    });
                                }
                                else if (flag_img == 0) {
                                    student_user = new students(id, Name, Mail, Quali, x);
                                }
                                else {
                                    student_user = new students(id, Name, Mail, Quali, x, downloadUrl);
                                }
                                dataBase.child(id).setValue(student_user);

                                pb.dismiss();
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                                startActivity(Newpage);
                            }
                            else
                            {
                                pb.dismiss();
                                Toast.makeText(getApplicationContext(),"Account already exists\nPlease Login",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(registerStudent.this,loginStudent.class);
                                startActivity(intent);
                            }
                        }

                    });
                }
                else {
                    if (!name_matcher.matches()) {
                        name.setError("Invalid Name!!");
                        flag++;
                    }
                    if (!mail_matcher.matches()) {
                        mail.setError("Invalid E-mail");
                        flag++;
                    }
                    if (Pass.length() < 8) {
                        pass.setError("Password Not Long Enough");
                        flag++;
                    }
                    if (!Pass.equals(Cpy_Pass)) {
                        pass_cpy.setError("Password Mismatch");
                        flag++;
                    }
                    Toast.makeText(getApplicationContext(), "Registration could not be completed!\nPlease Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profiledp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intobj2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intobj2, RESULT_LOAD_IMAGE);
            }
        });

    }
    private void fetchLocation() {


        if (ContextCompat.checkSelfPermission(registerStudent.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(registerStudent.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {


                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(registerStudent.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {

                ActivityCompat.requestPermissions(registerStudent.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);


            }
        } else {

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {

                                Double lat = location.getLatitude();
                                Double lon = location.getLongitude();
                                int index = 0, d;
                                double min = 1000000, dis;
                                for (d = 0; d < 5; d++) {

                                    dis = ((lat - latarray[d]) * (lat - latarray[d])) + ((lon - lonarray[d]) * (lon - lonarray[d]));
                                    if (dis < min) {
                                        index = d;
                                        min = dis;
                                    }
                                }
                                user_location.setText(places[index]);
                                x = places[index];

                            }
                        }
                    });

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else{

            }
        }
    }
}
