/*
InClass_06
Bhanu Teja Sriram
Tejaswini Naredla

*/
package com.example.cherr.inclass06;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GetJsonParsingAsync.iData{
    TextView sc,title,time,desc,page;
    ImageView prev,next,urlimage;
    ArrayList<String> Categories=new ArrayList<>();
    String selectedKeyword;
    RequestParams params;
    String jsonurl;
    int current_index=0;
    ArrayList<Articles> result = new ArrayList<>();
    Button go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Categories.add("Business");
        Categories.add("Entertainment");
        Categories.add("General");
        Categories.add("Health");
        Categories.add("Science");
        sc=findViewById(R.id.textViewShowCategories);
        title=findViewById(R.id.textViewTitle);
        time=findViewById(R.id.textViewTime);
        desc=findViewById(R.id.textViewDescription);
        page=findViewById(R.id.textViewPage);
        prev=findViewById(R.id.imageViewPrev);
        next=findViewById(R.id.imageViewNext);
        urlimage=findViewById(R.id.imageViewUrlImage);
        go=findViewById(R.id.buttonGo);

        prev.setEnabled(false);
        next.setEnabled(false);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder keywords = new AlertDialog.Builder(MainActivity.this);
                keywords.setCancelable(false);
                keywords.setTitle("Choose Category");
                final ArrayAdapter<String> passwordsAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, Categories);
                               keywords.setAdapter(passwordsAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedKeyword = Categories.get(which);
                        sc.setText(selectedKeyword);

                        params=new RequestParams();
                        params.addParameter("country","us");
                        params.addParameter("category",selectedKeyword);
                        params.addParameter("apikey","e579f38f1af04525a02493e7ff999035");

                        if(isConnected()) {
                            jsonurl=params.getEncodeUrl("https://newsapi.org/v2/top-headlines");

                            new GetJsonParsingAsync(MainActivity.this,MainActivity.this).execute(jsonurl);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"There is no Internet Connection",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                keywords.show();
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cm.getActiveNetworkInfo();
        if (nf == null || !nf.isConnected()) {
            return false;
        }
        return true;
    }

    public void processFinish(ArrayList<Articles> output){
        result = output;
        if(result.size()==0){
            title.setText(null);
            time.setText(null);
            desc.setText(null);
            urlimage.setImageBitmap(null);
            page.setText(null);
            Toast.makeText(MainActivity.this,"No News Found",Toast.LENGTH_SHORT).show();
        }
        else {

            title.setText(result.get(current_index).title);
            time.setText(result.get(current_index).publishedAt);
            desc.setText(result.get(current_index).description);
            //new getImageAsync(urlimage,MainActivity.this).execute(result.get(0).urlToImage);
            if(isConnected()) {
                Picasso.with(MainActivity.this).load(result.get(current_index).urlToImage).into(urlimage);
            }
            else{
                Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                urlimage.setImageBitmap(null);
            }
            page.setText("1 out of " + result.size());

            if (result.size() > 1) {
                prev.setEnabled(true);
                next.setEnabled(true);

            }
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current_index == result.size()) {
                        current_index = -1;
                    }
                    current_index++;
                    if (current_index < result.size()) {
                        title.setText(result.get(current_index).title);
                        time.setText(result.get(current_index).publishedAt);
                        desc.setText(result.get(current_index).description);
                        if(isConnected()) {
                            Picasso.with(MainActivity.this).load(result.get(current_index).urlToImage).into(urlimage);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                            urlimage.setImageBitmap(null);
                        }


                        page.setText(String.valueOf(current_index + 1) + " out of " + result.size());
                    }


                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current_index == 0) {
                        current_index = result.size();
                    }
                    current_index--;
                    title.setText(result.get(current_index).title);
                    time.setText(result.get(current_index).publishedAt);
                    desc.setText(result.get(current_index).description);if(isConnected()) {
                        Picasso.with(MainActivity.this).load(result.get(current_index).urlToImage).into(urlimage);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                        urlimage.setImageBitmap(null);
                    }
                    page.setText(String.valueOf(current_index + 1) + " out of " + result.size());

                }
            });
        }

    }




}
