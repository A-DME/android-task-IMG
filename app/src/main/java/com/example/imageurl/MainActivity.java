package com.example.imageurl;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText iurl;
    Button down;
    ImageView img;
    String url;

    ProgressDialog p;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iurl=findViewById(R.id.eturl);
        down=findViewById(R.id.dbtn);
        img=findViewById(R.id.img);
        p=new ProgressDialog(MainActivity.this);
        p.setIndeterminate(false);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setTitle("Connecting");

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url=iurl.getText().toString();
                if(url.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please insert a URL!",Toast.LENGTH_SHORT).show();
                }
                else {
                    Downloadimg d = new Downloadimg(img);
                    try {
                        img.setImageBitmap(d.execute(url).get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public class Downloadimg extends AsyncTask<String,Void, Bitmap> {

        ImageView imgVu;
        public Downloadimg(ImageView i){
            imgVu=i;

        }

        @Override
        protected void onPreExecute() {
            p.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String theURL=strings[0];
            Bitmap bm=null;
            URL ofimg;
            HttpURLConnection connection;
            InputStream strm;
            Log.i("Bos","just before try");
            try {
                Log.i("Bos","try entered");
                ofimg=new URL(theURL);
                connection=(HttpURLConnection) ofimg.openConnection();
                strm=connection.getInputStream();
                Log.i("Bos","Stream opened");
                bm= BitmapFactory.decodeStream(strm);
                Log.i("Bos","image decoded");
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("Bos","returning bitmap");

            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.i("Bos","image sent back");

            imgVu.setImageBitmap(bitmap);
            p.cancel();
        }
    }
}

