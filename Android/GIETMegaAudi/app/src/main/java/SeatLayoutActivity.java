package com.mno.gietmegaaudi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SeatLayoutActivity extends AppCompatActivity {

    TextView logindet;
    int action = 0;
    String name,id;
    Button b[] = new Button[120],book;
    int i=0;
    int seat_status[] = new int[120];
    int selected=-2;
    char alpha[]={
            'A','B','C','D','E','F','G','H','I','J','K','L'
    };
    ProgressDialog p;
    int err = 0;
    String seat;

    String seatArray[] = new String[120];
    int ind = 0;

    String json_array;
    JSONObject jsonObject;
    JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_seat_layout);

        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("roll");

        id = id.toUpperCase();
        name = name.toUpperCase();

        logindet = (TextView) findViewById(R.id.logindet);
        logindet.setText(name+" - "+id);

        book = (Button) findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected!=-2) {
                    int temp = selected + 1;
                    int digit = temp % 10;
                    temp = temp / 10;
                    if (digit == 0) {
                        digit = 10;
                        temp--;
                    }
                    seat = alpha[temp] +""+ digit;
                    action = 1;
                    updateSeatStatus();
                }
                else{
                    Toast t = Toast.makeText(SeatLayoutActivity.this, "Please select any seat to book", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER,0,0);
                    t.show();
                }
            }
        });
        updateSeatStatus();
    }



    public void updateSeatStatus(){
        err = 0;
        ind = 0;
        FetchSeatDetails fsd = new FetchSeatDetails();
        fsd.execute();

    }

    public void setupSeatArray(){
        for(int i = 0;i<=119;i++)
        {
            seat_status[i] = Integer.parseInt(seatArray[i]);
        }
        i = 0;
        initButtons();
        if(action == 1){
            if(seat_status[selected]==1){
                Toast t = Toast.makeText(SeatLayoutActivity.this, "Sorry, This seat is already booked. Select another seat", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
            }
            else{
                Intent i = new Intent(SeatLayoutActivity.this, ConfirmationActivity.class);
                i.putExtra("seatno", seat);
                i.putExtra("id",id);
                startActivityForResult(i,33);
            }
        }
    }

    public void initButtons(){


        b[i] = (Button) findViewById(R.id.a1);i++;
        b[i] = (Button) findViewById(R.id.a2);i++;
        b[i] = (Button) findViewById(R.id.a3);i++;
        b[i] = (Button) findViewById(R.id.a4);i++;
        b[i] = (Button) findViewById(R.id.a5);i++;
        b[i] = (Button) findViewById(R.id.a6);i++;
        b[i] = (Button) findViewById(R.id.a7);i++;
        b[i] = (Button) findViewById(R.id.a8);i++;
        b[i] = (Button) findViewById(R.id.a9);i++;
        b[i] = (Button) findViewById(R.id.a10);i++;
        b[i] = (Button) findViewById(R.id.b1);i++;
        b[i] = (Button) findViewById(R.id.b2);i++;
        b[i] = (Button) findViewById(R.id.b3);i++;
        b[i] = (Button) findViewById(R.id.b4);i++;
        b[i] = (Button) findViewById(R.id.b5);i++;
        b[i] = (Button) findViewById(R.id.b6);i++;
        b[i] = (Button) findViewById(R.id.b7);i++;
        b[i] = (Button) findViewById(R.id.b8);i++;
        b[i] = (Button) findViewById(R.id.b9);i++;
        b[i] = (Button) findViewById(R.id.b10);i++;
        b[i] = (Button) findViewById(R.id.c1);i++;
        b[i] = (Button) findViewById(R.id.c2);i++;
        b[i] = (Button) findViewById(R.id.c3);i++;
        b[i] = (Button) findViewById(R.id.c4);i++;
        b[i] = (Button) findViewById(R.id.c5);i++;
        b[i] = (Button) findViewById(R.id.c6);i++;
        b[i] = (Button) findViewById(R.id.c7);i++;
        b[i] = (Button) findViewById(R.id.c8);i++;
        b[i] = (Button) findViewById(R.id.c9);i++;
        b[i] = (Button) findViewById(R.id.c10);i++;
        b[i] = (Button) findViewById(R.id.d1);i++;
        b[i] = (Button) findViewById(R.id.d2);i++;
        b[i] = (Button) findViewById(R.id.d3);i++;
        b[i] = (Button) findViewById(R.id.d4);i++;
        b[i] = (Button) findViewById(R.id.d5);i++;
        b[i] = (Button) findViewById(R.id.d6);i++;
        b[i] = (Button) findViewById(R.id.d7);i++;
        b[i] = (Button) findViewById(R.id.d8);i++;
        b[i] = (Button) findViewById(R.id.d9);i++;
        b[i] = (Button) findViewById(R.id.d10);i++;
        b[i] = (Button) findViewById(R.id.e1);i++;
        b[i] = (Button) findViewById(R.id.e2);i++;
        b[i] = (Button) findViewById(R.id.e3);i++;
        b[i] = (Button) findViewById(R.id.e4);i++;
        b[i] = (Button) findViewById(R.id.e5);i++;
        b[i] = (Button) findViewById(R.id.e6);i++;
        b[i] = (Button) findViewById(R.id.e7);i++;
        b[i] = (Button) findViewById(R.id.e8);i++;
        b[i] = (Button) findViewById(R.id.e9);i++;
        b[i] = (Button) findViewById(R.id.e10);i++;
        b[i] = (Button) findViewById(R.id.f1);i++;
        b[i] = (Button) findViewById(R.id.f2);i++;
        b[i] = (Button) findViewById(R.id.f3);i++;
        b[i] = (Button) findViewById(R.id.f4);i++;
        b[i] = (Button) findViewById(R.id.f5);i++;
        b[i] = (Button) findViewById(R.id.f6);i++;
        b[i] = (Button) findViewById(R.id.f7);i++;
        b[i] = (Button) findViewById(R.id.f8);i++;
        b[i] = (Button) findViewById(R.id.f9);i++;
        b[i] = (Button) findViewById(R.id.f10);i++;
        b[i] = (Button) findViewById(R.id.g1);i++;
        b[i] = (Button) findViewById(R.id.g2);i++;
        b[i] = (Button) findViewById(R.id.g3);i++;
        b[i] = (Button) findViewById(R.id.g4);i++;
        b[i] = (Button) findViewById(R.id.g5);i++;
        b[i] = (Button) findViewById(R.id.g6);i++;
        b[i] = (Button) findViewById(R.id.g7);i++;
        b[i] = (Button) findViewById(R.id.g8);i++;
        b[i] = (Button) findViewById(R.id.g9);i++;
        b[i] = (Button) findViewById(R.id.g10);i++;
        b[i] = (Button) findViewById(R.id.h1);i++;
        b[i] = (Button) findViewById(R.id.h2);i++;
        b[i] = (Button) findViewById(R.id.h3);i++;
        b[i] = (Button) findViewById(R.id.h4);i++;
        b[i] = (Button) findViewById(R.id.h5);i++;
        b[i] = (Button) findViewById(R.id.h6);i++;
        b[i] = (Button) findViewById(R.id.h7);i++;
        b[i] = (Button) findViewById(R.id.h8);i++;
        b[i] = (Button) findViewById(R.id.h9);i++;
        b[i] = (Button) findViewById(R.id.h10);i++;
        b[i] = (Button) findViewById(R.id.i1);i++;
        b[i] = (Button) findViewById(R.id.i2);i++;
        b[i] = (Button) findViewById(R.id.i3);i++;
        b[i] = (Button) findViewById(R.id.i4);i++;
        b[i] = (Button) findViewById(R.id.i5);i++;
        b[i] = (Button) findViewById(R.id.i6);i++;
        b[i] = (Button) findViewById(R.id.i7);i++;
        b[i] = (Button) findViewById(R.id.i8);i++;
        b[i] = (Button) findViewById(R.id.i9);i++;
        b[i] = (Button) findViewById(R.id.i10);i++;
        b[i] = (Button) findViewById(R.id.j1);i++;
        b[i] = (Button) findViewById(R.id.j2);i++;
        b[i] = (Button) findViewById(R.id.j3);i++;
        b[i] = (Button) findViewById(R.id.j4);i++;
        b[i] = (Button) findViewById(R.id.j5);i++;
        b[i] = (Button) findViewById(R.id.j6);i++;
        b[i] = (Button) findViewById(R.id.j7);i++;
        b[i] = (Button) findViewById(R.id.j8);i++;
        b[i] = (Button) findViewById(R.id.j9);i++;
        b[i] = (Button) findViewById(R.id.j10);i++;
        b[i] = (Button) findViewById(R.id.k1);i++;
        b[i] = (Button) findViewById(R.id.k2);i++;
        b[i] = (Button) findViewById(R.id.k3);i++;
        b[i] = (Button) findViewById(R.id.k4);i++;
        b[i] = (Button) findViewById(R.id.k5);i++;
        b[i] = (Button) findViewById(R.id.k6);i++;
        b[i] = (Button) findViewById(R.id.k7);i++;
        b[i] = (Button) findViewById(R.id.k8);i++;
        b[i] = (Button) findViewById(R.id.k9);i++;
        b[i] = (Button) findViewById(R.id.k10);i++;
        b[i] = (Button) findViewById(R.id.l1);i++;
        b[i] = (Button) findViewById(R.id.l2);i++;
        b[i] = (Button) findViewById(R.id.l3);i++;
        b[i] = (Button) findViewById(R.id.l4);i++;
        b[i] = (Button) findViewById(R.id.l5);i++;
        b[i] = (Button) findViewById(R.id.l6);i++;
        b[i] = (Button) findViewById(R.id.l7);i++;
        b[i] = (Button) findViewById(R.id.l8);i++;
        b[i] = (Button) findViewById(R.id.l9);i++;
        b[i] = (Button) findViewById(R.id.l10);


        for(int j=0;j<=119;j++){
            if(seat_status[j]==1){
                b[j].setBackground(getResources().getDrawable(R.drawable.seat_button_booked));
                b[j].setTextColor(getResources().getColor(R.color.fore));
                b[j].setTypeface(null,Typeface.NORMAL);
            }
            if(seat_status[j]==0){
                b[j].setBackground(getResources().getDrawable(R.drawable.seat_button_normal));
                b[j].setTextColor(getResources().getColor(R.color.back));
                b[j].setTypeface(null,Typeface.NORMAL);
            }

        }
    }


    public void onClick(View v){
        int id = v.getId();
        Button bt = (Button) findViewById(id);

        if(bt.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.seat_button_booked).getConstantState())){
            Toast t = Toast.makeText(SeatLayoutActivity.this, "This seat is already booked. Please select any other seat", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }
        else if(bt.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.seat_button_normal).getConstantState())){
            i=0;
            initButtons();
            bt.setBackground(getResources().getDrawable(R.drawable.seat_button_selected));
            bt.setTextColor(getResources().getColor(R.color.fore));
            bt.setTypeface(null, Typeface.BOLD);
            for(int k=0;k<=119;k++){
                if(b[k].getId()==bt.getId()){
                    selected=k;
                    break;
                }
            }
        }
        else{
            bt.setBackground(getResources().getDrawable(R.drawable.seat_button_normal));
            bt.setTextColor(getResources().getColor(R.color.back));
            bt.setTypeface(null, Typeface.NORMAL);
            selected=-2;
        }
    }



    class FetchSeatDetails extends AsyncTask<Void , Void , String> {

        String json_url;
        String JSON_STRING="";

        @Override
        protected void onPreExecute() {
            p  = new ProgressDialog(SeatLayoutActivity.this);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setMessage("Loading...Please wait");
            p.setIndeterminate(true);
            p.setCancelable(false);
            p.show();
            json_url = "https://gietmegaaudi.000webhostapp.com/ReadSeatStatus.php";
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
                    }
                });
                err = 1;
                Intent i = new Intent(SeatLayoutActivity.this,NoNetworkActivityTwo.class);
                startActivityForResult(i,130);
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
                    seatArray[ind] = jo.getString("status_seat");
                    ind++;
                    count++;
                }
                p.cancel();
                p.hide();
                setupSeatArray();
            } catch (JSONException e) {
                if(err == 0) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            p.cancel();
                            p.hide();
                        }
                    });
                    if(err == 0) {
                        Intent i = new Intent(SeatLayoutActivity.this, NoNetworkActivityTwo.class);
                        startActivityForResult(i, 130);
                    }
                }
            }


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 130) {
            if (resultCode == RESULT_OK) {
                updateSeatStatus();
            }
        }
        if (requestCode == 33) {
            if (resultCode == RESULT_CANCELED) {
                updateSeatStatus();
            }
        }

    }


}
