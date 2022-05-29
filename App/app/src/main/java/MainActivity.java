// registration form
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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText username;
    Double Lati;
    Double Longi;
    Double Lati1;
    Double Longi1;
    private static int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditText name = findViewById(R.id.name);
        EditText passwordcheck = findViewById(R.id.passwordcheck);
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        TextView alracc = findViewById(R.id.alreadyacc);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        // admin and admin
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (password.getText().toString().equals(passwordcheck.getText().toString()) && !TextUtils.isEmpty(username.getText().toString()) && !TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(passwordcheck.getText().toString())) {
                    try {
                        
                        Verify v = new Verify(username.getText().toString(), name.getText().toString(), password.getText().toString(), Lati.toString(), Longi.toString());
                        v.execute(this);
                        Toast.makeText(MainActivity.this, "Account creation successful", Toast.LENGTH_SHORT).show();
                        openCaptureImage();
//                        UploadFileAsync upld = new UploadFileAsync("abc.jpg");
//                        upld.execute();

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                    }
                } else if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(passwordcheck.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Empty field(s) not followed!!!", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().equals(passwordcheck.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alracc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
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

    public void openCaptureImage() {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);

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

                Toast.makeText(this, "UserEmail: " + personEmail, Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(MainActivity.this, MainActivity2.class));
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


// insert in database

    @SuppressLint("StaticFieldLeak")
    public class Verify extends AsyncTask<Object, Void, String> {
        String uname = "";
        String nam = "";
        String pass = "";
        String la = "";
        String lo = "";

        public String st;

        //MainActivity activity;
        Verify(String u, String n, String p, String lati, String longi) {
            uname = u;
            nam = n;
            pass = p;
            la = lati;
            lo = longi;

        }

        @Override
        protected String doInBackground(Object... params) {
            // activity = (MainActivity)params[0];

            try {
                StringBuilder sb = new StringBuilder();
                //java.net.URL url = new URL("http://192.168.43.105/ffmpegtest/dbread.php?a="+uname+"&b="+pass);
                java.net.URL url = new URL("http://192.168.124.200/save1.php?a=" + uname + "&b=" + nam + "&c=" + pass + "&d=" + la + "&e=" + lo);

                BufferedReader in;
                in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    sb.append(inputLine);
                st = sb.toString();
                in.close();

                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            //Do something with result string
            // WebView webView = activity.findViewById(R.id.web_view);
            // webView.loadData(str, "text/html; charset=UTF-8", null);
        }

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
                String sourceFileUri = MainActivity.this.getFilesDir().getAbsolutePath() + "/" + this.filename;

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
                        String upLoadServerUri = "http://192.168.124.200/upld.php?path=" + "/" + folderName;
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
    // getting latitude and longitude of the user location

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

                        Geocoder geocoder = new Geocoder(MainActivity.this,
                                Locale.getDefault());

                        // Initialize address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        // set latitude on textview
                        Lati = addresses.get(0).getLatitude();
                        Longi = addresses.get(0).getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getLocationLogin() {
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

                        Geocoder geocoder = new Geocoder(MainActivity.this,
                                Locale.getDefault());

                        // Initialize address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        // set latitude on textview
                        Lati1 = addresses.get(0).getLatitude();
                        Longi1 = addresses.get(0).getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}