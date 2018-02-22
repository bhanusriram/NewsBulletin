/*
InClass_06
Bhanu Teja Sriram
Tejaswini Naredla

*/

package com.example.cherr.inclass06;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by cherr on 19-02-2018.
 */

public class GetJsonParsingAsync extends AsyncTask<String,Void,ArrayList<Articles>> {
    iData idata;
    ProgressDialog dialog;
    Context con;

    public GetJsonParsingAsync(iData idata,Context iContext) {
        this.idata = idata;
        con=iContext;
        dialog=new ProgressDialog(con);

    }

    @Override
    protected void onPreExecute() {

        dialog.setTitle("Loading News");
        dialog.show();

    }

    @Override

    protected ArrayList<Articles> doInBackground(String[] params) {

        Log.d("demo", "json "+params[0]);
        HttpURLConnection con = null;
        ArrayList<Articles> result = new ArrayList<>();
        BufferedReader br=null;
        StringBuilder sb=new StringBuilder();
        try {
            URL url=new URL(params[0]);
            con= (HttpURLConnection) url.openConnection();
            InputStream is=con.getInputStream();
            br=new BufferedReader(new InputStreamReader(is));
            String line="";
            while ((line=br.readLine())!=null){
                sb.append(line);
            }
            String json= sb.toString();

            JSONObject root = new JSONObject(json);
            JSONArray articles = root.getJSONArray("articles");
            Log.d("demo", String.valueOf(articles.length()));
            for (int i=0;i<articles.length();i++) {

                JSONObject articleJson = articles.getJSONObject(i);
                Articles article = new Articles();
                article.title = articleJson.getString("title");
                article.description = articleJson.getString("description");
                article.urlToImage=articleJson.getString("urlToImage");
                //article.totalArticles=articleJson.getString("totalArticles");
                article.publishedAt=articleJson.getString("publishedAt");
                result.add(article);

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();

        } finally {
            if(con!=null){
                con.disconnect();
            }
        }
        //Log.d("demo", "doInBackground: "+result.get(0).toString());
        return result;
    } public interface iData{
        void processFinish(ArrayList<Articles> output);
    }

    @Override
    protected void onPostExecute(ArrayList<Articles> arrayList) {

        idata.processFinish(arrayList);

        dialog.dismiss();


    }
}
