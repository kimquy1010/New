package com.example.quynh.company.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quynh.company.Adapter.ProductionAdapter;
import com.example.quynh.company.AsyncTasks.SaveCacheFile;
import com.example.quynh.company.Constants.AppConstants;
import com.example.quynh.company.Objects.ProductionDetails;
import com.example.quynh.company.R;
import com.example.quynh.company.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quynh on 12/9/2017.
 */

public class ProductionActivity extends AppCompatActivity {

    private static final String TAG = "ProductionActivity";
    private List<ProductionDetails> listProductions;
    private ListView listView;
    private ProductionAdapter mProductionAdapter;
    private String mCategoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        String categoryId = getIntent().getAction();
        Log.d(TAG, "Test categoryId: " + categoryId);
        listProductions = new ArrayList();
        getDataFromServer(categoryId);
        setupListView();
    }

    /**
     * Get data from server  by category ID to set production inside this activity
     */
    public void getDataFromServer(String categoryId) {
        getCategoryData(categoryId);
        getProductionData(categoryId);

    }

    /**
     * Method get category data from server
     */
    public void getCategoryData(final String categoryId) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConstants.LINK_CATEGORY, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                if (response != null) {
                    try {
                        Log.d(TAG, "Setting up action bar for production activity");
                        mCategoryName = response.getJSONObject(Integer.parseInt(categoryId) - 1).getString(getString(R.string.name_production));
                        Log.d(TAG, "Test response: " + mCategoryName);
                        setupActionBar(mCategoryName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!Utils.isNetworkConnected(ProductionActivity.this)) {
                    Toast.makeText(ProductionActivity.this, getString(R.string.toast_error_get_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Method get production data from server
     */
    private void getProductionData(String categoryId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConstants.LINK_PRODUCTION + String.format("?page=%d&category_id=%s", 1, categoryId),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        if (response != null) {
                            Log.d(TAG, "Have result: Total category: " + response.length() + ", response: " + response.toString());
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    //When downloaded all items, set image for adapter
                                    if (i == response.length() - 1) {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        final int idProduction = jsonObject.getInt(getString(R.string.id_production));
                                        final String nameProduction = jsonObject.getString(getString(R.string.name_production));
                                        final String imageProductionUrl = jsonObject.getString(getString(R.string.image_production));
                                        final long priceProduction = jsonObject.getLong(getString(R.string.price_production));
                                        final String descriptionProduction = jsonObject.getString(getString(R.string.description_production));
                                        new SaveCacheFile(AppConstants.FILE_PRODUCTION, idProduction) {
                                            @Override
                                            protected void onPostExecute(File file) {
                                                Log.d(TAG, "Done get category's data");
                                                if (file != null) {
                                                    Log.d(TAG, "Test file cache: " + file);
                                                    listProductions.add(new ProductionDetails(idProduction, nameProduction, descriptionProduction, file, priceProduction));
                                                    //Start setupUI when got all data we need
                                                    setupListView();
                                                }
                                            }
                                        }.execute(imageProductionUrl);
                                    } else {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        final int idProduction = jsonObject.getInt(getString(R.string.id_production));
                                        final String nameProduction = jsonObject.getString(getString(R.string.name_production));
                                        final String imageProductionUrl = jsonObject.getString(getString(R.string.image_production));
                                        final long priceProduction = jsonObject.getLong(getString(R.string.price_production));
                                        final String descriptionProduction = jsonObject.getString(getString(R.string.description_production));
                                        new SaveCacheFile(AppConstants.FILE_PRODUCTION, idProduction) {
                                            @Override
                                            protected void onPostExecute(File file) {
                                                Log.d(TAG, "Done get category's data");
                                                if (file != null) {
                                                    Log.d(TAG, "Test file cache: " + file);
                                                    listProductions.add(new ProductionDetails(idProduction, nameProduction, descriptionProduction, file, priceProduction));

                                                }
                                            }
                                        }.execute(imageProductionUrl);

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!Utils.isNetworkConnected(ProductionActivity.this)) {
                    Toast.makeText(ProductionActivity.this, getString(R.string.toast_error_get_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void setupActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListView() {
        listView = (ListView) findViewById(R.id.production_list);
        mProductionAdapter = new ProductionAdapter(
                this, R.layout.adapter_production, listProductions);
        listView.setAdapter(mProductionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentReviewProduction = new Intent(ProductionActivity.this, ReviewProductionActitity.class);
                intentReviewProduction.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ProductionDetails productionDetails = (ProductionDetails) parent.getItemAtPosition(position);
                intentReviewProduction.putExtra(AppConstants.REVIEW_PRODUCTION, productionDetails);
                startActivity(intentReviewProduction);
            }
        });
    }

}
