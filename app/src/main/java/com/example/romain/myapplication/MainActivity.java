package com.example.romain.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity {
    private Context context;
    private static String url1 = "https://sheltered-headland-2927.herokuapp.com/listes.json";
    private static String url2 = "https://sheltered-headland-2927.herokuapp.com/elements.json";

    private static final String NAME = "name";

    ListView list;
    JSONArray JArray = null;
    ArrayList<HashMap<String, String>> jlist = new ArrayList<HashMap<String, String>>();
    JSONParser Hello = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.listView);
        HashMap<String, String> element;

        ListAdapter adapter = new SimpleAdapter(this,jlist,android.R.layout.simple_list_item_2,new String[] {"text1", "text2"},new int[] {android.R.id.text1, android.R.id.text2 });
        //Pour finir, on donne à la ListView le SimpleAdapter
        list.setAdapter(adapter);

        new DLTask(MainActivity.this).execute(url1,url2);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {
                String[] st = jlist.get((int)id).get("text2").split(" ");
                Random randomGenerator = new Random();
                int randomInt = randomGenerator.nextInt(st.length)+1;
                Toast.makeText(MainActivity.this, st[randomInt], Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class DLTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;
        private Context context;
        private Activity activity;

        public DLTask(Activity activity) {
            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected String doInBackground(String...urls){
            try{
                getJSON(urls[0],2);
                getJSON(urls[1],1);
                return downloadUrl(urls[0]);
            } catch (IOException e){
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Loading Database");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(String result){
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Hello.Input(result);
            JSONArray json = Hello.GetList();
            JSONArray json1 = Hello.GetElements();

            for(int i = 0; i<json.length(); i++)
            try {
                JSONObject c = json.getJSONObject(i);
                HashMap<String, String> element = new HashMap<String, String>();
                element.put("text1", c.getString("nom"));
                String elements = "";
                for(int j=0; j<json1.length();j++) {
                    JSONObject o = json1.getJSONObject(j);
                    if(o.getString("id_liste")==c.getString("id_liste"))
                        elements = elements +" " + o.getString("name");
                }
                element.put("text2", elements);

                jlist.add(element);
            }
            catch (JSONException e) {
                Toast.makeText(getApplication().getApplicationContext(),"Bug",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            ListAdapter adapter = new SimpleAdapter(context,jlist,android.R.layout.simple_list_item_2,new String[] {"text1", "text2"},new int[] {android.R.id.text1, android.R.id.text2 });
        //    Toast.makeText(getApplication().getApplicationContext(),"Loading Achieved",Toast.LENGTH_LONG).show();
            list.setAdapter(adapter);
        }
    }

    private String downloadUrl(String my_url) throws  IOException{
        InputStream is = null;

        try{
            URL url = new URL(my_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = readIt(is, 50);
            return contentAsString;
        } finally {
            if(is != null){
                is.close();
            }
        }
    }

    public void getJSON(String nameurl,int i) throws IOException {

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

        if(i==1)
            Hello.InputElements(Array);
        if(i==2)
            Hello.InputList(Array);
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
