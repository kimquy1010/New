package com.example.quynh.company.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quynh.company.Adapter.MagazineAdapter;
import com.example.quynh.company.Constants.AppConstants;
import com.example.quynh.company.Objects.Flag.OpenGLFlagSurfaceView;
import com.example.quynh.company.Objects.MagazineDetails;
import com.example.quynh.company.Objects.ProductionDetails;
import com.example.quynh.company.R;
import com.example.quynh.company.Utils.PermissionUtils;
import com.example.quynh.company.Utils.Utils;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Quynh on 12/29/2017.
 */


public class ReviewProductionActitity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener,
        View.OnClickListener {
    @BindView(R.id.image_production)
    ImageView mImage;
    @BindView(R.id.description_production)
    TextView mDescription;
    @BindView(R.id.title_production)
    TextView mTitle;
    @BindView(R.id.price_production)
    TextView mPrice;
    @BindView(R.id.decrement)
    Button mDescrement;
    @BindView(R.id.increment)
    Button mIncrement;
    @BindView(R.id.amount)
    EditText mAmount;
    @BindView(R.id.order)
    ImageView mOrder;
    @BindView(R.id.flag_production)
    OpenGLFlagSurfaceView mFlagProduction;
    @BindView(R.id.stock_state)
    TextView mStockState;
    @BindView(R.id.button_stock_magazine)
    Button btnStockMagazine;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "My location";
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private ProductionDetails mProduction;
    private String mProductionName;
    private String mProductionDescription;
    private long mProductionPrice;
    private File mProductionImage;
    private int mProductionId;
    private final List<Marker> mListMarkerMagazine = new ArrayList<>();
    private final List<int[]> mPositionMagazine = new ArrayList<>();
    BitmapDescriptor mIconMagazine;
    private int mLengthAddressMagazine;
    private List<MagazineDetails> mListMagazine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_production_activity);
        ButterKnife.bind(this);
        getDataFromActivity(getIntent());
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getDataFromActivity(Intent intent) {
        mProduction = (ProductionDetails) intent.getSerializableExtra(AppConstants.REVIEW_PRODUCTION);
        mProductionId = mProduction.getProductionId();
        mProductionName = mProduction.getProductionName();
        mProductionDescription = mProduction.getProductionDescription();
        mProductionImage = mProduction.getProductionImageFile();
        mProductionPrice = mProduction.getProductionPrice();
        Utils.setImageViewFromFile(mProductionImage, mImage);
        mDescription.setText(mProductionDescription);
        mTitle.setText(mProductionName);
        mPrice.setText(getString(R.string.price) + ": " + mProductionPrice);
        mDescrement.setOnClickListener(this);
        mIncrement.setOnClickListener(this);
        mOrder.setOnClickListener(this);
        mIconMagazine = BitmapDescriptorFactory.fromResource(R.drawable.ic_order);
        Log.d(TAG, "Test ID production: " + mProductionId);
    }

    /**
     * Get magazine's address
     */
    private void getMagazineAddress(int productionId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConstants.LINK_MAGAZINE_ADDRESS + String.format("?page=%d&production_id=%d", 1, productionId),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        if (response != null) {
                            Log.d(TAG, "Have result: Total address: " + response.length() + ", response: " + response.toString());
                            mLengthAddressMagazine = response.length();
                            if (mLengthAddressMagazine > 0) {
                                mStockState.setText(getString(R.string.stock) + mLengthAddressMagazine);
                                mListMagazine = new ArrayList<>();
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        //When downloaded all items, set image for adapter
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        final int idMagazine = jsonObject.getInt(getString(R.string.id_magazine));
                                        final String nameMagazine = jsonObject.getString(getString(R.string.name_magazine));
                                        final String imageMagazine = jsonObject.getString(getString(R.string.image_magazine));
                                        final String addressMagazine = jsonObject.getString(getString(R.string.address_magazine));
                                        addMarkersToMap(addressMagazine, nameMagazine, mMap);
                                        mListMagazine.add(new MagazineDetails(idMagazine, nameMagazine, addressMagazine, null));
                                    }
                                    btnStockMagazine.setVisibility(View.VISIBLE);
                                    btnStockMagazine.setOnClickListener(ReviewProductionActitity.this);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                mStockState.setText(R.string.null_stock);
                                btnStockMagazine.setVisibility(View.GONE);
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!Utils.isNetworkConnected(ReviewProductionActitity.this)) {
                    Toast.makeText(ReviewProductionActitity.this, getString(R.string.toast_error_get_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.decrement:
                if (mAmount.getText().toString().equals("1")) {
                    mAmount.setText("99");
                } else
                    mAmount.setText(Integer.toString(Integer.parseInt(mAmount.getText().toString()) - 1));
                break;
            case R.id.increment:
                if (mAmount.getText().toString().equals("99")) {
                    mAmount.setText("1");
                } else
                    mAmount.setText(Integer.toString(Integer.parseInt(mAmount.getText().toString()) + 1));
                break;
            case R.id.order:
                Utils.gotoActivity(ReviewProductionActitity.this, OrderActivity.class, mProductionId);
                break;
            case R.id.button_stock_magazine:
                Log.d(TAG,"Button magazine clicked");
                showDialogMagazine();
                break;

        }
    }

    private void showDialogMagazine() {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View attachView = getLayoutInflater().inflate(R.layout.dialog_magazine_address, null);
            builder.setView(attachView);
            Log.d(TAG,"Done set data for dialog");
            final AlertDialog dialog = builder.create();
            final ImageView ivClose = attachView.findViewById(R.id.iv_close);
            final ListView listView = attachView.findViewById(R.id.list_magazine);
            MagazineAdapter magazineAdapter = new MagazineAdapter(this, R.layout.adapter_magazine, mListMagazine);
            listView.setAdapter(magazineAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    TextView magazineAddress = view.findViewById(R.id.magazine_address);
                    String address = magazineAddress.getText().toString();
                    TextView magazineName = view.findViewById(R.id.magazine_name);
                    String name = magazineName.getText().toString();
                    moveCameraToAddress(address,name,mMap);
                    dialog.dismiss();
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Map ready");
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        getMagazineAddress(mProductionId);
        mMap.setOnMarkerClickListener(this);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Don't have permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            //Granted permission
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                CameraPosition lastLocation = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(15.5f)
                                        .bearing(0)
                                        .tilt(25)
                                        .build();
                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(lastLocation));
                                // Logic to handle location object
                                Log.d(TAG, "My last location: " + location);
                            }
                        }
                    });
            createLocationRequest();
            checkLocationSetting();

            // Get current location
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                        // Update UI with location data
                        // ...
                        Log.d(TAG, "My location now: " + location);
                        float zoom = mMap.getCameraPosition().zoom;
                        CameraPosition currentLocation = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude()))
                                .zoom(15)
                                .bearing(0)
                                .tilt(25)
                                .build();
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentLocation));
                        mFusedLocationClient.removeLocationUpdates(this);
                    }
                }
            };
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
    }

    private void checkLocationSetting() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                Log.d(TAG, "Test location setting response: " + locationSettingsResponse.getLocationSettingsStates().isLocationUsable());
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(ReviewProductionActitity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }

    private void addMarkersToMap(String address, String name, GoogleMap map) {
        LatLng locationFromAddress = getLocationFromAddress(this, address);
        map.addMarker(new MarkerOptions().position(locationFromAddress).title(name));
    }

    private void moveCameraToAddress(String address, String name, GoogleMap map){
        LatLng locationFromAddress = getLocationFromAddress(this, address);
        CameraPosition newLocation = new CameraPosition.Builder().target(locationFromAddress).zoom(15.5f)
                .bearing(0)
                .tilt(25)
                .build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(newLocation));

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        Toast.makeText(this, marker.getTitle() + " z-index set to " + zIndex,
                Toast.LENGTH_SHORT).show();

        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        enableMyLocation();
        Log.d(TAG, "Click to button my location");
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Enable the my location layer if the permission has been granted.
                enableMyLocation();
            } else {
                Log.d(TAG, "denied permission");
                // Display the missing permission error dialog when the fragments resume.
                mPermissionDenied = true;
            }
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog or show it again???.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}
