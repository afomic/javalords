package com.afomic.javalords.activity;

import android.app.Application;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.afomic.javalords.ApplicationController;
import com.afomic.javalords.R;
import com.afomic.javalords.adapter.ListAdapter;
import com.afomic.javalords.data.Constants;
import com.afomic.javalords.models.Developer;
import com.afomic.javalords.util.SearchUtil;
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
    RelativeLayout emptyLayout,progressLayout;
    ArrayList<Developer> developers;
    ListAdapter mAdapter;
    Button refresh;
    SearchView developerSearch;
    SearchUtil util;
    MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainList=(ListView) findViewById(R.id.main_list);
        emptyLayout=(RelativeLayout) findViewById(R.id.empty_layout);
        mainList.setEmptyView(emptyLayout);
        progressLayout=(RelativeLayout) findViewById(R.id.progress_layout);


        refresh=(Button) findViewById(R.id.refresh_button);
        if(refresh!=null){
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchData();
                    progressLayout.setVisibility(View.VISIBLE);
                }
            });
        }

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
        developers=new ArrayList<>();
        mAdapter=new ListAdapter(MainActivity.this,developers);
        mainList.setAdapter(mAdapter);
        fetchData();

    }

    public void fetchData(){
        JsonObjectRequest req=new JsonObjectRequest(Request.Method.GET,Constants.API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(Constants.TAG, response.toString());
                developers.clear();
                progressLayout.setVisibility(View.GONE);
                try{
                    JSONArray array=response.getJSONArray("items");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Developer developer=new Developer(object);
                        developers.add(developer);
                    }
                    util=SearchUtil.getInstance(developers);
                }catch (JSONException e){
                    Log.e(Constants.TAG, "Json parsing error: " + e.getMessage());
                }
                mAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.TAG, "Error: " + error.getMessage());
                progressLayout.setVisibility(View.GONE);
            }
        });
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        item =menu.findItem(R.id.menu_con_search);
        developerSearch=(SearchView) MenuItemCompat.getActionView(item);
        developerSearch.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
        developerSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(developers.size()==0) return true;
                ArrayList<Developer> result=util.search(newText);
                mAdapter.setArray(result);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
