package com.mno.gietmegaaudi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LogInActivity extends AppCompatActivity {

    EditText id,otp;
    String sid,sotp,sname;
    int state = 1;

    ProgressDialog p1,p2,p3,p4;
    String json_array;
    JSONObject jsonObject;
    JSONArray jsonArray;
    int err = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_in);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        id = (EditText) findViewById(R.id.id);
        otp = (EditText) findViewById(R.id.pass);

        int width =  dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width),(int)(height*.30));


    }

    public void logInFailed(){
        startActivity(new Intent(LogInActivity.this,InvalidDetailsActivity.class));
        id.setText("");
        otp.setText("");
    }


    public void nameObtained(){
        Intent i = new Intent(LogInActivity.this,SeatLayoutActivity.class);
        i.putExtra("name",sname);
        i.putExtra("roll",sid);
        startActivity(i);
        finish();
    }

    public void studentOTPFound(){

        BackgroundTaskReadStudent btrs = new BackgroundTaskReadStudent();
        btrs.execute();

    }

    public void studentOTPnotfound(){

        CheckOtpFaculty cof = new CheckOtpFaculty();
        cof.execute();

    }

    public void facultyOTPFound(){
        BackgroundTaskReadFaculty btrf = new BackgroundTaskReadFaculty();
        btrf.execute();
    }


    public void logIn(View view){
        err = 0;
        if(id.getText().toString().length()!=0 || otp.getText().toString().length()!=0) {
            sid = id.getText().toString();
            sotp = otp.getText().toString();

            sid = sid.toLowerCase();
            CheckOtpStudent bt = new CheckOtpStudent();
            bt.execute();
        }
        else{
            Toast t = Toast.makeText(LogInActivity.this, "Enter id and OTP to Log in", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }
    }

    class CheckOtpStudent extends AsyncTask<Void , Void , String> {

        String json_url;
        String JSON_STRING="";

        @Override
        protected void onPreExecute() {
            p1  = new ProgressDialog(LogInActivity.this);
            p1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p1.setMessage("Logging in.....Please wait");
            p1.setIndeterminate(true);
            p1.setCancelable(false);
            p1.show();
            json_url = "https://gietmegaaudi.000webhostapp.com/CheckStudentOTP.php";
        }

        @Override
        protected String doInBackground(Void... voids) {



            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(sid,"UTF-8")+"&"+
                        URLEncoder.encode("otp","UTF-8")+"="+URLEncoder.encode(sotp,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p1.cancel();
                        p1.hide();
                        err = 1;
                        startActivity(new Intent(LogInActivity.this,NoNetworkActivityTwo.class));
                    }
                });

            }

            return "Failed";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            json_array = result;
            try {
                jsonObject = new JSONObject(json_array);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count =0;
                String response= "";
                while(count<jsonArray.length()){
                    JSONObject jo = jsonArray.getJSONObject(count);
                    response = jo.getString("otpresponse");
                    count++;
                }
                final String s = response;
                int val = Integer.parseInt(s);
                if(val == 0){
                    p1.cancel();
                    p1.hide();
                    studentOTPnotfound();
                }
                else{
                    p1.cancel();
                    p1.hide();
                    studentOTPFound();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p1.cancel();
                        p1.hide();
                        if(err == 0)
                            startActivity(new Intent(LogInActivity.this,NoNetworkActivityTwo.class));
                    }
                });
            }


        }
    }






    class CheckOtpFaculty extends AsyncTask<Void , Void , String> {

        String json_url;
        String JSON_STRING="";

        @Override
        protected void onPreExecute() {
            p2  = new ProgressDialog(LogInActivity.this);
            p2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p2.setMessage("Logging in.....Please wait");
            p2.setIndeterminate(true);
            p2.setCancelable(false);
            p2.show();
            json_url = "https://gietmegaaudi.000webhostapp.com/CheckFacultyOTP.php";
        }

        @Override
        protected String doInBackground(Void... voids) {



            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(sid,"UTF-8")+"&"+
                        URLEncoder.encode("otp","UTF-8")+"="+URLEncoder.encode(sotp,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p2.cancel();
                        p2.hide();
                        err = 1;
                            startActivity(new Intent(LogInActivity.this,NoNetworkActivityTwo.class));
                    }
                });

            }

            return "Failed";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            json_array = result;
            try {
                jsonObject = new JSONObject(json_array);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count =0;
                String response= "";
                while(count<jsonArray.length()){
                    JSONObject jo = jsonArray.getJSONObject(count);
                    response = jo.getString("otpresponse");
                    count++;
                }
                final String ss;
                ss = response;
                int var = Integer.parseInt(ss);
                if(var == 0){
                    logInFailed();
                    p2.cancel();
                    p2.hide();
                }
                else{
                    p2.cancel();
                    p2.hide();
                    facultyOTPFound();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p2.cancel();
                        p2.hide();
                        if(err == 0)
                            startActivity(new Intent(LogInActivity.this,NoNetworkActivityTwo.class));
                    }
                });
            }



        }
    }








    class BackgroundTaskReadStudent extends AsyncTask<Void , Void , String> {

        String json_url;
        String JSON_STRING="";

        @Override
        protected void onPreExecute() {

            p3  = new ProgressDialog(LogInActivity.this);
            p3.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p3.setMessage("Logging in.....Please wait");
            p3.setIndeterminate(true);
            p3.setCancelable(false);
            p3.show();
            json_url = "https://gietmegaaudi.000webhostapp.com/ReadStudentDetails.php";

        }

        @Override
        protected String doInBackground(Void... voids) {



            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("roll","UTF-8")+"="+URLEncoder.encode(sid,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p3.cancel();
                        p3.hide();
                        err = 1;
                        startActivity(new Intent(LogInActivity.this,NoNetworkActivityTwo.class));
                    }
                });

            }

            return "Failed";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            json_array = result;
            try {
                jsonObject = new JSONObject(json_array);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count =0;
                String nameresponse= "";
                while(count<jsonArray.length()){
                    JSONObject jo = jsonArray.getJSONObject(count);
                    nameresponse = jo.getString("name");
                    count++;
                }
                    p3.cancel();
                    p3.hide();
                    sname = nameresponse;
                    nameObtained();

            } catch (JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p3.cancel();
                        p3.hide();
                        if(err == 0)
                            startActivity(new Intent(LogInActivity.this,NoNetworkActivityTwo.class));
                    }
                });
            }


        }
    }




    class BackgroundTaskReadFaculty extends AsyncTask<Void , Void , String> {

        String json_url;
        String JSON_STRING="";

        @Override
        protected void onPreExecute() {
            p4  = new ProgressDialog(LogInActivity.this);
            p4.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p4.setMessage("Logging in.....Please wait");
            p4.setIndeterminate(true);
            p4.setCancelable(false);
            p4.show();
            json_url = "https://gietmegaaudi.000webhostapp.com/ReadFacultyDetails.php";

        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("roll","UTF-8")+"="+URLEncoder.encode(sid,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p4.cancel();
                        p4.hide();
                        err = 1;
                        startActivity(new Intent(LogInActivity.this,NoNetworkActivityTwo.class));
                    }
                });

            }

            return "Failed";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            json_array = result;
            try {
                jsonObject = new JSONObject(json_array);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count =0;
                String nameresponse= "";
                while(count<jsonArray.length()){
                    JSONObject jo = jsonArray.getJSONObject(count);
                    nameresponse = jo.getString("name");
                    count++;
                }
                sname = nameresponse;
                nameObtained();
            } catch (JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p4.cancel();
                        p4.hide();
                        if(err == 0)
                            startActivity(new Intent(LogInActivity.this,NoNetworkActivityTwo.class));
                    }
                });
            }


        }
    }



}
