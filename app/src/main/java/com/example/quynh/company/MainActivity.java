package com.example.quynh.company;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quynh.company.Activities.FAQActivity;
import com.example.quynh.company.Activities.LanguagesActivity;
import com.example.quynh.company.Activities.ProductionActivity;
import com.example.quynh.company.Activities.ShowLocationActivity;
import com.example.quynh.company.Adapter.CategoryAdapter;
import com.example.quynh.company.Adapter.DrawerAdapter;
import com.example.quynh.company.Adapter.NewProductionPagerAdapter;
import com.example.quynh.company.AsyncTasks.SaveCacheFile;
import com.example.quynh.company.Constants.AppConstants;
import com.example.quynh.company.Helper.CheckAppVersion;
import com.example.quynh.company.Objects.CategoryDetails;
import com.example.quynh.company.Objects.DrawerItem;
import com.example.quynh.company.Utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MainActivity";
    private static final String YES = "YES";

    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager_gallery)
    ViewPager mViewPager;
    @BindView(R.id.left_drawer)
    ListView mDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.button_get_token)
    Button btnGetToken;

    private ActionBarDrawerToggle mDrawerToggle;
    private CategoryAdapter mAdapter;
    private ArrayList<CategoryDetails> mCategoryArray = new ArrayList<>();
    private Handler mHandler = new Handler();
    private Handler mHandLerInitTabView = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Test item adapter: " + mViewPager.getCurrentItem());
            if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1) {
                mViewPager.setCurrentItem(0);
            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
            mHandler.postDelayed(this, 10000);
        }
    };

    private String currentVersion, marketVersion;
    private static int mDrawerLength;
    private int mTotalCategory = 0, mTotalProduction = 0;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initNotification();
        initCategory();
        getCategoryData();
        getNewProductionData();
        setupActionBar();
        btnGetToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = FirebaseInstanceId.getInstance().getToken();
                String msg = getString(R.string.msg_token_fmt,token);
                Log.d(TAG,"Test token for notification: "+msg);
            }
        });
    }

    private void initNotification(){
        String channelId  = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                channelName, NotificationManager.IMPORTANCE_LOW));
    }

    private void checkForUpdate() {
        currentVersion = BuildConfig.VERSION_NAME;
        try {
            CheckAppVersion checkAppVersion = new CheckAppVersion();
            marketVersion = checkAppVersion.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (currentVersion.equals(marketVersion)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View attachView = getLayoutInflater().inflate(R.layout.dialog_update_app, null);
            builder.setView(attachView).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    finish();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
            setFinishOnTouchOutside(true);
            attachView.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.APP_MARKET_ADDRESS)));
                }
            });
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = ButterKnife.findById(this, R.id.tool_bar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(getString(R.string.title));
        }
    }

    private void setDrawerLayout() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        mDrawer.getLayoutParams().width = widthPixels * 2 / 3;
        final List<DrawerItem> drawerItems = new ArrayList<>();
        //Header
        drawerItems.add(new DrawerItem(R.drawable.ic_sub_facebook));

        //Category
        for (int i = 0; i < mCategoryArray.size(); i++) {
            Log.d(TAG, "adding category to drawerLayout");
            drawerItems.add(new DrawerItem(mCategoryArray.get(i).getCategoryImage(),
                    mCategoryArray.get(i).getCategoryName()));
        }

        //Other feature of app
        drawerItems.add(new DrawerItem(R.drawable.ic_location,
                getString(R.string.location)));
        drawerItems.add(new DrawerItem(R.drawable.ic_share_white_24dp,
                getString(R.string.share_app)));
        drawerItems.add(new DrawerItem(R.drawable.ic_translate,
                getString(R.string.language)));
        drawerItems.add(new DrawerItem(R.drawable.ic_question,
                getString(R.string.faq)));
        drawerItems.add(new DrawerItem(R.drawable.ic_feedback,
                getString(R.string.send_feedback)));
        drawerItems.add(new DrawerItem(R.drawable.ic_quit,
                getString(R.string.quit)));
        mDrawer.setAdapter(new DrawerAdapter(this, drawerItems));
        mDrawer.setOnItemClickListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerLength = drawerItems.size();
    }

    private void initViewPager() {
        NewProductionPagerAdapter newProductionPagerAdapter = new NewProductionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(newProductionPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        Log.d(TAG, "Test padding tab: " + mTabLayout.getPaddingLeft());
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.translution));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setMinimumWidth(20);
        mTabLayout.setMinimumHeight(20);

        int tabCount = mTabLayout.getTabCount();
        mTabLayout.getTabAt(0).setCustomView(R.layout.tab_view_not_choose);
        mTabLayout.getTabAt(0).getCustomView().findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.ic_choose);
        Log.d(TAG, "Test total tab: " + tabCount);
        for (int i = 1; i < tabCount; i++) {
            mTabLayout.getTabAt(i).setCustomView(R.layout.tab_view_not_choose);
        }
        mHandLerInitTabView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tab.getCustomView().findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.ic_choose);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        tab.getCustomView().findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.ic_not_choose);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        }, 500);
        mHandler.postDelayed(runnable, 10000);

    }

    private void initCategory() {
        mAdapter = new CategoryAdapter(this, mCategoryArray);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null
                && mDrawerToggle.onOptionsItemSelected(item)) {
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (position > 0 & position <= mDrawerLength) {
            mDrawerLayout.closeDrawers();
            Utils.gotoActivity(this, ProductionActivity.class, position);
        } else
            switch (position) {
                case 0: //Subscribe
                    mDrawerLayout.closeDrawers();
                    Utils.openFacebook(this, AppConstants.PAGE_ID);
                    break;
                case 3: //Magazine address
                    mDrawerLayout.closeDrawers();
                    Intent locationIntent = new Intent(this, ShowLocationActivity.class);
                    startActivity(locationIntent);
                    break;

                case 4: //Share app
                    mDrawerLayout.closeDrawers();
                    Utils.shareLink(this, AppConstants.APP_LINK);
                    break;

                case 5: //Language
                    mDrawerLayout.closeDrawers();
                    Intent languageIntent = new Intent(this, LanguagesActivity.class);
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                    String currentLanguage = sp.getString(getString(R.string.pref_display_language)
                            , AppConstants.DEVICE_LANGUAGE);
                    languageIntent.putExtra(AppConstants.KEY_EXTRA_LANGUAGE, currentLanguage);
                    startActivityForResult(languageIntent, AppConstants.REQUEST_CODE_SELECT_LANGUAGE);
                    break;

                case 6: //FAQ
                    mDrawerLayout.closeDrawers();
                    Utils.gotoActivity(this, FAQActivity.class, 0);
                    break;

                case 7: //Send mail feedback
                    mDrawerLayout.closeDrawers();
                    Utils.sendMail(this, AppConstants.EXTRA_MAIL_SUBJECT_REPORT,
                            AppConstants.EXTRA_MAIL_MSG_REPORT);
                    break;
                case 8: //Finish
                    mDrawerLayout.closeDrawers();
                    finish();
                    break;
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_CODE_SELECT_LANGUAGE
                && resultCode == RESULT_OK && data != null) {
            String selectedLanguage = data.getStringExtra(AppConstants.KEY_EXTRA_LANGUAGE);
            final Locale locale;
            if (selectedLanguage.equals(AppConstants.DEVICE_LANGUAGE)) {
                locale = Locale.getDefault();
            } else if (selectedLanguage.contains("_")) {
                String[] codes = selectedLanguage.split("_");
                locale = new Locale(codes[0], codes[1]);
            } else {
                locale = new Locale(selectedLanguage);
            }

            Resources res = getApplicationContext().getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;
            conf.setLayoutDirection(locale);
            res.updateConfiguration(conf, dm);
            finish();
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }

    private void getCategoryData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConstants.LINK_CATEGORY, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                if (response != null) {
                    mTotalCategory = response.length();
                    Log.d(TAG, "Have result: Total category: " + mTotalCategory + ", response: " + response.toString());
                    try {
                        for (int i = 0; i < mTotalCategory; i++) {
                            //When downloaded all items, set image for adapter
                            if (i == response.length() - 1) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                final int idCategory = jsonObject.getInt(getString(R.string.id_category));
                                final String nameCategory = jsonObject.getString(getString(R.string.name_category));
                                final String imageCategoryUrl = jsonObject.getString(getString(R.string.image_category));
                                new SaveCacheFile(AppConstants.FILE_CATEGORY, idCategory) {
                                    @Override
                                    protected void onPostExecute(File file) {
                                        Log.d(TAG, "Done get category's data");
                                        if (file != null) {
                                            Log.d(TAG, "Test file cache: " + file);
                                            mCategoryArray.add(new CategoryDetails(idCategory, nameCategory, file));
                                            //Start setupUI when got all data we need
                                            initCategory();
                                            setDrawerLayout();
                                        }
                                    }
                                }.execute(imageCategoryUrl);
                            } else {
                                JSONObject jsonObject = response.getJSONObject(i);
                                final int idCategory = jsonObject.getInt(getString(R.string.id_category));
                                final String nameCategory = jsonObject.getString(getString(R.string.name_category));
                                final String imageCategoryUrl = jsonObject.getString(getString(R.string.image_category));
                                new SaveCacheFile(AppConstants.FILE_CATEGORY, idCategory) {
                                    @Override
                                    protected void onPostExecute(File file) {
                                        Log.d(TAG, "Done get category's data");
                                        if (file != null) {
                                            Log.d(TAG, "Test file cache: " + file);
                                            mCategoryArray.add(new CategoryDetails(idCategory, nameCategory, file));
                                        }
                                    }
                                }.execute(imageCategoryUrl);

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
                if (!Utils.isNetworkConnected(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, getString(R.string.toast_error_get_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getNewProductionData() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConstants.LINK_NEW_PRODUCTION, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                Log.d(TAG, "Have result: Total production: " + mTotalProduction + ", response: " + response.toString());
                mTotalProduction = response.length();
                try {
                    for (int i = 0; i < mTotalProduction; i++) {
                        if (i == mTotalProduction - 1) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            final int idNewProduction = jsonObject.getInt(getString(R.string.id_production));
                            final String nameNewProduction = jsonObject.getString(getString(R.string.name_production));
                            final String imageNewProductionUrl = jsonObject.getString(getString(R.string.image_production));
                            new SaveCacheFile(AppConstants.FILE_NEW_PRODUCTION, i) {
                                @Override
                                protected void onPostExecute(File file) {
                                    Log.d(TAG, "Done get new production's data");
                                    if (file != null) {
                                        Log.d(TAG, "Test file cache: " + file);
                                        //Start setupUI when got all data we need
                                        initViewPager();
                                    }
                                }
                            }.execute(imageNewProductionUrl);
                        } else {
                            JSONObject jsonObject = response.getJSONObject(i);
                            final int idNewProduction = jsonObject.getInt(getString(R.string.id_production));
                            final String nameNewProduction = jsonObject.getString(getString(R.string.name_production));
                            final String imageNewProductionUrl = jsonObject.getString(getString(R.string.image_production));
                            new SaveCacheFile(AppConstants.FILE_NEW_PRODUCTION, i) {
                                @Override
                                protected void onPostExecute(File file) {
                                    Log.d(TAG, "Done get new production's data");
                                    if (file != null) {
                                        Log.d(TAG, "Test file cache: " + file);
                                        //Start setupUI when got all data we need
                                    }
                                }
                            }.execute(imageNewProductionUrl);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Responese error: " + error.getMessage());

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}
