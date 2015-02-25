package com.example.bestbuy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity 
{

	private static final String SERVICE_URL = "http://api.remix.bestbuy.com/v1/categories?format=json&apiKey=tdrrtdw3mm8vx5ven8ds4tbw&show=id";
	private static final String TAG = "BEST BUY";
	private static final String TAG_VER = "ID";
	private static final String TAG_NAME = "Name";
	ListView lv;
	//ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv=(ListView)findViewById(R.id.listView1);
		//oslist = new ArrayList<HashMap<String, String>>();
		String sampleURL = SERVICE_URL + "tdrrtdw3mm8vx5ven8ds4tbw&show=id";		 
        WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK, this, "GETting data...");        
        wst.execute(new String[] { sampleURL });
		
	}
	public void handleResponse(String response)
	{
		try 
        {
             
            JSONObject jso = new JSONObject(response);
             
            String id = jso.getString("from");
            //String name=jso.getString("Name");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TAG_VER, id);
            //map.put(TAG_NAME, name);
            //oslist.add(map);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.activity_main,new String[] { TAG_VER });
            lv.setAdapter(adapter);                     
        } catch (Exception e) 
        {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
class WebServiceTask extends AsyncTask<String, Integer, String>
{
	private static final int CONN_TIMEOUT = 3000;  
	private static final int SOCKET_TIMEOUT = 5000;
	private int taskType = GET_TASK;
	public static final int GET_TASK = 2;
	public static final int POST_TASK = 1;
	private static final String TAG = null;
	private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

	public WebServiceTask(int getTask, MainActivity mainActivity, String string) {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String doInBackground(String... urls) 
	{
		// TODO Auto-generated method stub
		String url = urls[0];
        String result = "";

        HttpResponse response = doResponse(url);
        if (response == null) {
            return result;
        } else 
        {

            try 
            {

                result = inputStreamToString(response.getEntity().getContent());
                Log.d("result string",""+result);
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);

            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

        }
		return result;
		
	
	}

	private String inputStreamToString(InputStream content) {
		// TODO Auto-generated method stub
		
            String line = "";
            StringBuilder total = new StringBuilder();
            // Wrap a BufferedReader around the InputStream
            BufferedReader rd = new BufferedReader(new InputStreamReader(content));
            try 
            {
                // Read response until the end
                while ((line = rd.readLine()) != null) 
                {
                    total.append(line+"\n");
                }
            } catch (IOException e) 
            {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
            // Return full string
            return total.toString();
        
	}

	private HttpResponse doResponse(String url) 
	{
		// TODO Auto-generated method stub
		HttpClient httpclient = new DefaultHttpClient(getHttpParams());
		HttpResponse response = null;
		 
        try 
        {
            switch (taskType) 
            {

            case POST_TASK:
                HttpPost httppost = new HttpPost(url);
                // Add parameters
                httppost.setEntity(new UrlEncodedFormEntity(params));

                response = httpclient.execute(httppost);
                Log.d("response in HTTPResponse()", ""+response);
                break;
            case GET_TASK:
                HttpGet httpget = new HttpGet(url);
                response = httpclient.execute(httpget);
                Log.d("response in HTTPResponse() GET Task", ""+response);
                break;
            }
        }
        catch (Exception e) 
        {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return response;
		
	}

	private HttpParams getHttpParams() {
		// TODO Auto-generated method stub
		HttpParams htpp = new BasicHttpParams();
        
        HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
         
        return htpp;
	}
	
}
