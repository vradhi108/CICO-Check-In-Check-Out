// login upload photo
package com.internshala.cico;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity5 extends AppCompatActivity {
    // intialize variable
    ImageView imageView;
    Button btOpen;
    String un;
    TextView textView;
    String st;
    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        un = getIntent().getExtras().getString("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        imageView = findViewById(R.id.image_view);
        btOpen = findViewById(R.id.bt_open);
        textView = findViewById(R.id.textView2);
        time = new SimpleDateFormat("yyyy-MM-dd/HH-mm-ss").format(Calendar.getInstance().getTime());

        // request for camera permission
        if (ContextCompat.checkSelfPermission(MainActivity5.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity5.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
        }

        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, "abc.jpg");
                startActivityForResult(intent, 100);
//                Toast.makeText(MainActivity2.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // get capture image
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            // set capture image to imageview
            imageView.setImageBitmap(captureImage);
            BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = draw.getBitmap();
            FileOutputStream outStream = null;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(MainActivity5.this.getFilesDir().getAbsolutePath()+"/");
//            dir.mkdirs();
            String fileName = "abc.jpg";
            Toast.makeText(MainActivity5.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

            File outFile = new File(dir, fileName);
            try {
                outStream = new FileOutputStream(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            try {
                outStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        UploadFileAsync upld = new UploadFileAsync("abc.jpg");
        upld.execute();
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
                String sourceFileUri = MainActivity5.this.getFilesDir().getAbsolutePath() + "/" + this.filename;

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "***";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);
                String folderName = "test";
                if (sourceFile.isFile()) {

                    try {
                        // String upLoadServerUri = "http://14.99.36.147:8000/soft/upld.php";
                        String upLoadServerUri = "http://192.168.124.200/upld1.php?path=" + "/" + folderName + "&fn=" + un;
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
                            textView.setText("Attendance Marked!!");

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
            Verify v = new Verify(un);
            v.execute(this);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


    }

    @SuppressLint("StaticFieldLeak")
    public class Verify extends AsyncTask<Object, Void, String> {
        String uname = "";



        //MainActivity activity;
        Verify(String u) {
            uname = u;

        }

        @Override
        protected String doInBackground(Object... params) {
            // activity = (MainActivity)params[0];
            Log.d("test", "Vradhi1");
            try {
                StringBuilder sb = new StringBuilder();
                //java.net.URL url = new URL("http://192.168.43.105/ffmpegtest/dbread.php?a="+uname+"&b="+pass);
                java.net.URL url = new URL("http://192.168.124.200/runpy2.php?photo=" + uname + ".jpg");
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
            textView.setText(st);
            Verify1 v1 = new Verify1(un, time);
            v1.execute(this);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Verify1 extends AsyncTask<Object, Void, String> {
        String uname = "";
        String tim = "";



        //MainActivity activity;
        Verify1(String u, String t) {
            uname = u;
            tim = t;

        }

        @Override
        protected String doInBackground(Object... params) {
            // activity = (MainActivity)params[0];
            Log.d("test", "Vradhi1");
            try {
                StringBuilder sb = new StringBuilder();
                //java.net.URL url = new URL("http://192.168.43.105/ffmpegtest/dbread.php?a="+uname+"&b="+pass);
                java.net.URL url = new URL("http://192.168.124.200/attendance.php?a=" + uname + "&b=" + tim);
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

            Toast.makeText(MainActivity5.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
        }
    }
}