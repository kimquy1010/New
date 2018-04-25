package com.example.quynh.company.Adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.quynh.company.Constants.AppConstants;
import com.example.quynh.company.R;
import com.example.quynh.company.Utils.Utils;

import java.io.File;

import butterknife.ButterKnife;

/**
 * Created by Quynh on 12/8/2017.
 */

public class NewProductionPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "NewProduction";

    public NewProductionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = ProductionFragment.newInstance(position);
        Log.d(TAG, "Getting item for pager adapter " + position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    public static class ProductionFragment extends Fragment {
        private static final String PRODUCTION_POSITION_KEY = "position";
        private int mPosition;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static ProductionFragment newInstance(int position) {
            ProductionFragment productionFragment = new ProductionFragment();
            Bundle args = new Bundle();
            // Supply num input as an argument.
            args.putInt(PRODUCTION_POSITION_KEY, position);
            productionFragment.setArguments(args);
            return productionFragment;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPosition = getArguments() != null ? getArguments().getInt(PRODUCTION_POSITION_KEY) : 1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.collection, container, false);
            ButterKnife.bind(this, rootView);
            final ImageView imageView = rootView.findViewById(R.id.collection);
//            Animation animationRightIn = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_in);
//            imageView.setAnimation(animationRightIn);
//            imageView.startAnimation(animationRightIn);
            String fileLocalName = AppConstants.FILE_NEW_PRODUCTION + "_" + mPosition + ".png";
            Utils.setImageViewFromFile(new File(AppConstants.FULL_CACHE_DIR, fileLocalName), imageView);

            return rootView;
        }
    }
}
