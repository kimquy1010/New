package com.example.quynh.company.AsyncTasks;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quynh.company.Constants.AppConstants;
import com.example.quynh.company.R;
import com.example.quynh.company.Utils.Utils;

import org.json.JSONArray;

/**
 * Created by Quynh on 1/3/2018.
 */

public class Production {
    private static final String TAG = "Production";
    private static JSONArray result;
    private static boolean isFinishGetData;

    public static JSONArray getNewProduction(Context context) {
        Log.d(TAG, "Start get new production");
        isFinishGetData = false;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConstants.LINK_CATEGORY, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                Log.d(TAG, "Have result");
                result = response;
                isFinishGetData = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());

            }
        });
        requestQueue.add(jsonArrayRequest);
        while (!isFinishGetData) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Done get data");
        return result;
    }
}
