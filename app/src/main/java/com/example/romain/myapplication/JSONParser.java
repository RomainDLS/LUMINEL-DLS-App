package com.example.romain.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by romain on 06/01/15.
 */
public class JSONParser {

    private String stg = "tmp";

    private JSONArray List = null;
    private JSONArray Elements = null;

    public JSONParser(){

    }

    public void Input(String st){
        stg = st;
    }

    public String GetString(){
        return stg;
    }

    public void InputList(JSONArray Array){
        List = Array;
    }

    public JSONArray GetList(){ return List; }

    public void InputElements(JSONArray Array){
        Elements = Array;
    }

    public JSONArray GetElements(){ return Elements; }

   /* public JSONArray getJSON(String nameurl) throws IOException {

        InputStream is = null;
        JSONArray Array = null;
        StringBuilder builder = new StringBuilder();
        String line;

        try{
            URL url = new URL(nameurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try{
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Array = new JSONArray( builder.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
        } finally {
            if(is != null){
                is.close();
            }
        }

        return Array;
    }

    JSONArray json = Hello.GetList();
        //    JSONArray json1 = Hello.GetElements();

            for(int i = 0; i<json.length(); i++)
            try {
                JSONObject c = json.getJSONObject(i);
                HashMap<String, String> element = new HashMap<String, String>();
                element.put("text1", c.getString("nom"));
                String elements = "";
                for(int j=0; j<json.length();j++) {
                    JSONObject o = json.getJSONObject(j);
                    if(o.getString("id_liste")==c.getString("id_liste"))
                        elements.concat(o.getString("name")+" ");
                }
                element.put("text2", elements);

                jlist.add(element);
            }*/

}
