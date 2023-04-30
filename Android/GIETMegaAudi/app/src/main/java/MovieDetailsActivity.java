package com.mno.gietmegaaudi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieDetailsActivity extends AppCompatActivity {


    String json_array;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ProgressDialog p;
    RelativeLayout rel;
    String slottime[] = new String[10],slotid[] = new String[10];
    int ind=0;
    int err ;

    ImageView poster;
    Button book;
    TextView movname,slot1t1,slot1t2,slot2t1,slot2t2,slot3t1,slot3t2,slot4t1,slot4t2,slot5t1,slot5t2,head1,head2;
    String moviename,slot1t1s,slot1t2s,slot2t1s,slot2t2s,slot3t1s,slot3t2s,slot4t1s,slot4t2s,slot5t1s,slot5t2s,head1t,head2t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_movie_details);

        rel = (RelativeLayout) findViewById(R.id.rel);

        book = (Button) findViewById(R.id.book);
        poster = (ImageView) findViewById(R.id.poster);
        movname = (TextView) findViewById(R.id.movname);
        slot1t1 = (TextView) findViewById(R.id.slot1t1);
        slot1t2 = (TextView) findViewById(R.id.slot1t2);
        slot2t1 = (TextView) findViewById(R.id.slot2t1);
        slot2t2 = (TextView) findViewById(R.id.slot2t2);
        slot3t1 = (TextView) findViewById(R.id.slot3t1);
        slot3t2 = (TextView) findViewById(R.id.slot3t2);
        slot4t1 = (TextView) findViewById(R.id.slot4t1);
        slot4t2 = (TextView) findViewById(R.id.slot4t2);
        slot5t1 = (TextView) findViewById(R.id.slot5t1);
        slot5t2 = (TextView) findViewById(R.id.slot5t2);
        head1 = (TextView) findViewById(R.id.head1);
        head2 = (TextView) findViewById(R.id.head2);

        //setSlotDetails();

        poster.setImageDrawable(getResources().getDrawable(R.drawable.poster));

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MovieDetailsActivity.this,LogInActivity.class);
                startActivity(i);
            }
        });

        initialize();
    }

    public void initialize(){
        err = 0;
        if(!isNetworkAvailable()){
            rel.setVisibility(View.VISIBLE);
            Intent i = new Intent(MovieDetailsActivity.this,NoNetworkActivity.class);
            startActivityForResult(i,170);
        }
        else{
            BackgroundTask backgroundTask = new BackgroundTask();
            backgroundTask.execute();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void setSlotDetails(){


        moviename = slottime[5];
        head1t = "Saturday "+slottime[6];
        head2t = "Sunday "+slottime[7];

        slot1t1s = slottime[0];
        slot1t2s = "All Girls,Ladies faculties,Faculty club family members";

        slot2t1s = slottime[1];
        slot2t2s = "All 2nd year students ( Boys )";

        slot3t1s = slottime[2];
        slot3t2s = "All 1st students of B.Tech, BBA, MBA, M.Sc, B.Sc ( Boys )";

        slot4t1s = slottime[3];
        slot4t2s = "All 3rd year students ( Boys )";

        slot5t1s = slottime[4];
        slot5t2s = "All 4th year students ( Boys ), Bachelor Teaching and non-teaching staff";

        movname.setText(moviename);

        head1.setText(head1t);
        head2.setText(head2t);

        slot1t1.setText(slot1t1s);
        slot1t2.setText(slot1t2s);

        slot2t1.setText(slot2t1s);
        slot2t2.setText(slot2t2s);

        slot3t1.setText(slot3t1s);
        slot3t2.setText(slot3t2s);

        slot4t1.setText(slot4t1s);
        slot4t2.setText(slot4t2s);

        slot5t1.setText(slot5t1s);
        slot5t2.setText(slot5t2s);
    }

    class BackgroundTask extends AsyncTask<Void , Void , String> {

        String json_url;
        String JSON_STRING="";

        @Override
        protected void onPreExecute() {
            p  = new ProgressDialog(MovieDetailsActivity.this);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setMessage("Loading...Please wait");
            p.setIndeterminate(true);
            p.setCancelable(false);
            p.show();
            json_url = "https://gietmegaaudi.000webhostapp.com/ReadSlotDetails.php";
        }

        @Override
        protected String doInBackground(Void... voids) {



            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

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
                err = 1;
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p.cancel();
                        p.hide();
                        rel.setVisibility(View.VISIBLE);
                    }
                });
                Intent i = new Intent(MovieDetailsActivity.this,NoNetworkActivity.class);
                startActivityForResult(i,170);
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
                while(count<jsonArray.length()){
                    JSONObject jo = jsonArray.getJSONObject(count);
                    slottime[ind] = jo.getString("slottime");
                    slotid[ind] = jo.getString("slotid");
                    ind++;
                    count++;
                }
                p.cancel();
                p.hide();
                setSlotDetails();
                rel.setVisibility(View.GONE);
            } catch (JSONException e) {
                if(err == 0) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            p.cancel();
                            p.hide();
                            rel.setVisibility(View.VISIBLE);
                        }
                    });
                    Intent i = new Intent(MovieDetailsActivity.this, NoNetworkActivity.class);
                    startActivityForResult(i, 170);
                }
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 170) {
            if (resultCode == RESULT_OK) {
                initialize();
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
        if (requestCode == 22) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }

    }

    public void showInfo(View v){
        startActivity(new Intent(MovieDetailsActivity.this,InfoActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivityForResult(new Intent(MovieDetailsActivity.this,ConfirmExitActivity.class),22);
    }
}
