package com.afomic.javalords.activity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.afomic.javalords.ApplicationController;
import com.afomic.javalords.R;
import com.afomic.javalords.adapter.ListAdapter;
import com.afomic.javalords.data.Constants;
import com.afomic.javalords.models.Developer;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView mainList;
    private ProgressDialog pDialog;
    ArrayList<Developer> developers;
    ListAdapter mAdapter;

    Button refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainList=(ListView) findViewById(R.id.main_list);
        refresh=(Button) findViewById(R.id.refresh_button);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Developer developer=mAdapter.getItem(position);
                Intent intent =new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra(Constants.USERNAME,developer.getUsername());
                intent.putExtra(Constants.PROFILE_URL,developer.getProfileURL());
                intent.putExtra(Constants.PROFILE_PICTURE_URL,developer.getProfileImageURL());
                startActivity(intent);
            }
        });
        pDialog = new ProgressDialog(this);
        developers=new ArrayList<>();
        mAdapter=new ListAdapter(MainActivity.this,developers);
        mainList.setAdapter(mAdapter);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty,null,false);
        mainList.setEmptyView(emptyView);
        fetchData();

    }

    public void fetchData(){
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest req=new JsonObjectRequest(Request.Method.GET,Constants.API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(Constants.TAG, response.toString());
                pDialog.hide();
                developers.clear();
                try{
                    JSONArray array=response.getJSONArray("items");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Developer developer=new Developer(object);
                        developers.add(developer);
                    }
                }catch (JSONException e){
                    Log.e(Constants.TAG, "Json parsing error: " + e.getMessage());
                }
                mAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });
        ApplicationController.getInstance().addToRequestQueue(req);
    }
}
