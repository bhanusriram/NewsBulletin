package com.example.cherr.inclass06;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cherr on 19-02-2018.
 */

/*
Homework 3
Trivia Application
Bhanu Teja Sriram
Tejaswini Naredla
*/



public class getImageAsync extends AsyncTask<String,Void,Bitmap> {
    ProgressDialog dialog;
    ImageView iv;
    Context con;

    public getImageAsync(ImageView iv, Context context) {
        this.iv = iv;
        con = context;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Loading Image");
        this.dialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        HttpURLConnection con=null;
        Bitmap image = null;
        try {
            URL url=new URL(urls[0]);
            con= (HttpURLConnection) url.openConnection();
            con.connect();
            if(con.getResponseCode()==HttpURLConnection.HTTP_OK) {
                image = BitmapFactory.decodeStream(con.getInputStream());
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return image;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        dialog.dismiss();
        iv.setImageBitmap(bitmap);

    }
}
