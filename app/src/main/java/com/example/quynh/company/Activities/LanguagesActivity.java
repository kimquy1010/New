package com.example.quynh.company.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;


import com.example.quynh.company.Constants.AppConstants;
import com.example.quynh.company.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


import butterknife.BindView;
import butterknife.ButterKnife;


public class LanguagesActivity extends AppCompatActivity {
    private static final String TAG = "Language";
    @BindView(R.id.language_list)
    ListView mListView;

    private int mSelectedLanguage = 0;
    private List<String> mLanguageCodesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        setupActionBar();

        mLanguageCodesList = Arrays.asList(getResources().getStringArray(R.array.language_codes));
        String currentCode = getIntent().getStringExtra(AppConstants.KEY_EXTRA_LANGUAGE);
        for (int i = 0; i < mLanguageCodesList.size(); i++) {
            if (mLanguageCodesList.get(i).equals(currentCode)) {
                mSelectedLanguage = i;
                break;
            }
        }
        Log.d(TAG,"Test list view: "+ mListView);
        mListView.setAdapter(new LanguageArrayAdapter(this, R.layout.adapter_language));
        mListView.setNestedScrollingEnabled(true);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position != mSelectedLanguage) {
                    selectLanguage(position);
                    finish();
                }
            }
        });
        mListView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mListView.setSelection(mSelectedLanguage);
                mListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.language);
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

    private void selectLanguage(int pos) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.pref_display_language), mLanguageCodesList.get(pos));
        Intent intent = getIntent();
        intent.putExtra(AppConstants.KEY_EXTRA_LANGUAGE, mLanguageCodesList.get(pos));
        setResult(RESULT_OK, intent);
    }

    private final class LanguageArrayAdapter extends ArrayAdapter<String> {
        private List<String> mListLanguageName;
        private List<String> mListTranslators;

        private Integer[] mFlags = {R.drawable.ic_device_language,
                R.drawable.ic_flag_vietnam};

        LanguageArrayAdapter(Context context, int resource) {
            super(context, resource, mLanguageCodesList);
            mListTranslators = Arrays.asList(getResources().getStringArray(R.array.translator));
            mListTranslators.set(0, Locale.getDefault().getDisplayLanguage());
            initListLanguageName();
        }

        void initListLanguageName() {
            mListLanguageName = new ArrayList<>();
            mListLanguageName.add(getString(R.string.device_language));
            for (int i = 1; i < mLanguageCodesList.size(); i++) {
                final Locale locale;
                String code = mLanguageCodesList.get(i);
                if (code.contains("_")) {
                    String[] codes = code.split("_");
                    locale = new Locale(codes[0], codes[1]);
                } else {
                    locale = new Locale(code);
                }

                String language = locale.getDisplayName(locale);
                mListLanguageName.add(language.substring(0, 1).toUpperCase(locale)
                        + language.substring(1));
            }
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.adapter_language, null);
            }

            TextView tvLanguage = ButterKnife.findById(v, R.id.tv_language);
            TextView tvTranslator = ButterKnife.findById(v, R.id.tv_translator);
            ImageView ivFlag = ButterKnife.findById(v, R.id.iv_flag);

            RadioButton radioButton = ButterKnife.findById(v, R.id.rb_select);
            radioButton.setTag(position);
            radioButton.setChecked(position == mSelectedLanguage);
            tvLanguage.setText(mListLanguageName.get(position));
            tvTranslator.setText(mListTranslators.get(position));
            ivFlag.setImageResource(mFlags[position]);

            radioButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    if (position != mSelectedLanguage) {
                        selectLanguage(position);
                    }
                    finish();
                }
            });
            return v;
        }
    }

}
