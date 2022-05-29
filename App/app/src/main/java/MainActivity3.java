// log in activiy
package com.internshala.cico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.internal.location.zzz;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    public String st;
    public String st1;
    EditText username;
    EditText password;
    String query;
    Double lat;
    Double lon;
    Double lati;
    Double longi;
    static int i = 0;
    private static int RC_SIGN_IN = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        Button loginbtn =  findViewById(R.id.loginbtnfive);
        Log.d("test", "Vradhi");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        // admin and admin
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Verify v = new Verify(username.getText().toString(), password.getText().toString());
                v.execute(this);

            }
        });
        // gmail login

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Toast.makeText(this, "UserEmail: "+personEmail, Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(MainActivity3.this, MainActivity2.class));
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Message", e.toString());

        }
    }

    public void openLogin() {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public class Verify extends AsyncTask<Object, Void, String> {
        String uname = "";
        String pass = "";



        //MainActivity activity;
        Verify(String u, String p) {
            uname = u;
            pass = p;

        }

        @Override
        protected String doInBackground(Object... params) {
            // activity = (MainActivity)params[0];
            Log.d("test", "Vradhi1");
            try {
                StringBuilder sb = new StringBuilder();
                //java.net.URL url = new URL("http://192.168.43.105/ffmpegtest/dbread.php?a="+uname+"&b="+pass);
                java.net.URL url = new URL("http://192.168.124.200/search.php?a=" + uname + "&b=" + pass);
                query = url.getQuery();
                BufferedReader in;
                Log.d("test", "Vradhi2");
                in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));
                Log.d("test", "Vradhi3");
                String inputLine;
                while ((inputLine = in.readLine()) != null){
                    sb.append(inputLine);
                }
                Log.d("test", "Vradhi4");
                st = sb.toString();
                Log.d("Loginvalue", st);
                in.close();

                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
//            processingstatus.setText("Video Uploaded");
//            sendsms.setEnabled(true);
//            sendwhatsapp.setEnabled(true);
            Verify1 v1 = new Verify1(username.getText().toString(), password.getText().toString());
            v1.execute(this);
//            float[] results = new float[1];
//            Log.d("lati", String.valueOf(lati));
//            Location.distanceBetween(lati, longi, lat, lon, results);
//            float distanceInMeters = results[0];
//            boolean isWithin10km = distanceInMeters <= 100;
//            if(st.equals("Login Successful")){
//                Toast.makeText(MainActivity3.this, "Login successful", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity3.this, MainActivity5.class);
//                intent.putExtra("username", username.getText().toString());
//                startActivity(intent);
//
////                MainActivity3.UploadFileAsync upld = new MainActivity3.UploadFileAsync("abc.jpg");
////                upld.execute();
//            }
//            else if (!isWithin10km) Toast.makeText(MainActivity3.this, "You are not in the estimated range", Toast.LENGTH_SHORT).show();
//            else{
//                Toast.makeText(MainActivity3.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
//            }
        }
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // initilaize location
                Location location = task.getResult();
                if (location != null) {
                    try {
                        // initilaize geocoder

                        Geocoder geocoder = new Geocoder(MainActivity3.this,
                                Locale.getDefault());

                        // Initialize address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        // set latitude on textview
                        lat = addresses.get(0).getLatitude();
                        lon = addresses.get(0).getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // to upload photo

    @SuppressLint("StaticFieldLeak")
    private class UploadFileAsync extends AsyncTask<String, Void, String> {
        int serverResponseCode;
        String filename;

        UploadFileAsync(String filename) {
            this.filename = filename;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String sourceFileUri = MainActivity3.this.getFilesDir().getAbsolutePath() + "/" + this.filename;

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "***";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);
                String folderName = "train";
                if (sourceFile.isFile()) {

                    try {
                        // String upLoadServerUri = "http://14.99.36.147:8000/soft/upld.php";
                        String upLoadServerUri = "http://192.168.124.200/upld.php?path=" + "/" + folderName + "&fn=";
                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE",
                                "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("bill", sourceFileUri);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                                + sourceFileUri + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);

                        }

                        // send multipart form data necesssary after file
                        // data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);

                        // Responses from the server (code and message)
                        serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn
                                .getResponseMessage();

                        if (serverResponseCode == 200) {

                            // messageText.setText(msg);
                            //Toast.makeText(ctx, "File Upload Complete.",
                            //      Toast.LENGTH_SHORT).show();

                            // recursiveDelete(mDirectory1);

                        }

                        // close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (Exception e) {

                        // dialog.dismiss();
                        e.printStackTrace();

                    }
                    // dialog.dismiss();

                } // End else block


            } catch (Exception ex) {
                // dialog.dismiss();

                ex.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
//            processingstatus.setText("Video Uploaded");
//            sendsms.setEnabled(true);
//            sendwhatsapp.setEnabled(true);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


    }

    @SuppressLint("StaticFieldLeak")
    public class Verify1 extends AsyncTask<Object, Void, String> {
        String uname = "";
        String pass = "";



        //MainActivity activity;
        Verify1(String u, String p) {
            uname = u;
            pass = p;

        }

        @Override
        protected String doInBackground(Object... params) {
            // activity = (MainActivity)params[0];
            Log.d("test", "Vradhi1");
            try {
                StringBuilder sb = new StringBuilder();
                //java.net.URL url = new URL("http://192.168.43.105/ffmpegtest/dbread.php?a="+uname+"&b="+pass);
                java.net.URL url = new URL("http://192.168.124.200/latlongfetch.php?a=" + uname + "&b=" + pass);
                query = url.getQuery();
                BufferedReader in;
                Log.d("test", "Vradhi2");
                in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));
                Log.d("test", "Vradhi3");
                String inputLine;
                while ((inputLine = in.readLine()) != null){
                    sb.append(inputLine);
                }
                Log.d("test", "Vradhi4");
                st1 = sb.toString();
                Log.d("Loginvalue", st1);
                in.close();

                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
//            processingstatus.setText("Video Uploaded");
//            sendsms.setEnabled(true);
//            sendwhatsapp.setEnabled(true);
            String[] words=st1.split("\\s");
            lati = Double.parseDouble(words[0]);
            Log.d("lati1", String.valueOf(lati));
            longi = Double.parseDouble(words[1]);
            float[] results = new float[1];
            Log.d("lati", String.valueOf(lati));
            Location.distanceBetween(lati, longi, lat, lon, results);
            float distanceInMeters = results[0];
            boolean isWithin10km = distanceInMeters <= 1000;
            Log.d("distance", String.valueOf(distanceInMeters));
            if(st.equals("Login Successful") && isWithin10km){
                Toast.makeText(MainActivity3.this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity3.this, MainActivity5.class);
                intent.putExtra("username", username.getText().toString());
                startActivity(intent);

//                MainActivity3.UploadFileAsync upld = new MainActivity3.UploadFileAsync("abc.jpg");
//                upld.execute();
            }
            else if (!isWithin10km) Toast.makeText(MainActivity3.this, "You are not in the estimated range", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(MainActivity3.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }
}