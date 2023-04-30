package com.mno.gietmegaaudi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ConfirmationActivity extends AppCompatActivity {

    TextView msg;
    Button book,cancel;
    String seat;
    String id;
    ProgressDialog p;
    String json_array;
    JSONObject jsonObject;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_confirmation);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width =  dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.2));

        seat = getIntent().getStringExtra("seatno");
        id = getIntent().getStringExtra("id");
        msg = (TextView) findViewById(R.id.msg);
        msg.setText("SELECTED SEAT : "+seat);

        book = (Button) findViewById(R.id.book);
        cancel = (Button) findViewById(R.id.cancel);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSeatStatus uss = new UpdateSeatStatus();
                uss.execute();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void seatBooked(){
        Intent i = new Intent(ConfirmationActivity.this,BookedMessageActivity.class);
        i.putExtra("seatno",seat);
        startActivity(i);
    }

    public void seatBookFailed(){
        Toast t = Toast.makeText(ConfirmationActivity.this, "This seat is already booked....Select any other seat", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
        Intent i = new Intent();
        setResult(RESULT_CANCELED,i);
        finish();
    }


    class UpdateSeatStatus extends AsyncTask<Void , Void , String> {

        String insert_url;
        String json_url;
        String JSON_STRING="";

        @Override
        protected void onPreExecute() {
            p  = new ProgressDialog(ConfirmationActivity.this);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setMessage("Loading...Please wait");
            p.setIndeterminate(true);
            p.setCancelable(false);
            p.show();
            insert_url = "https://gietmegaaudi.000webhostapp.com/UpdateSeatStatus.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(insert_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"+
                        URLEncoder.encode("seatno","UTF-8")+"="+URLEncoder.encode(seat,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }

                inputStream.close();

                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p.cancel();
                        p.hide();
                    }
                });
                Intent i = new Intent(ConfirmationActivity.this,NoNetworkActivityTwo.class);
                startActivityForResult(i,130);
            }

            return null;
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
                int count = 0;
                String res;
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    res = jo.getString("bookstatus");
                    count++;
                    p.cancel();
                    p.hide();
                    int resint = Integer.parseInt(res);
                    if(resint == 1) {
                        seatBooked();
                    }
                    else{
                        seatBookFailed();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p.cancel();
                        p.hide();
                    }
                });
                Intent i = new Intent(ConfirmationActivity.this, NoNetworkActivityTwo.class);
                startActivityForResult(i, 130);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 130) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }

    }

}


