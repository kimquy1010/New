package com.example.quynh.company.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quynh.company.Objects.DrawerItem;
import com.example.quynh.company.R;
import com.example.quynh.company.Utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerAdapter extends ArrayAdapter<DrawerItem> {
    private static final String TAG = "DrawerAdapter";
    private List<DrawerItem> mListItems;
    private Activity mActivity;

    public DrawerAdapter(Activity activity, List<DrawerItem> objects) {
        super(activity, 0, objects);
        mListItems = objects;
        mActivity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ItemHolder itemHolder;
        if (view == null) {
            view = mActivity.getLayoutInflater().inflate(R.layout.navigation_row, parent, false);
            itemHolder = new ItemHolder(view);
            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        DrawerItem drawerItem = mListItems.get(position);
        switch (drawerItem.getItemStatus()) {
            case DrawerItem.ITEM_HEADER:
                itemHolder.headerLayout.setVisibility(View.VISIBLE);
                itemHolder.normalLayout.setVisibility(View.INVISIBLE);
                itemHolder.mainLayout.setVisibility(View.INVISIBLE);
                itemHolder.tvTitle.setText(drawerItem.getHeaderTitle());
                break;
            case DrawerItem.ITEM_NORMAL:
                itemHolder.headerLayout.setVisibility(View.INVISIBLE);
                itemHolder.normalLayout.setVisibility(View.VISIBLE);
                itemHolder.mainLayout.setVisibility(View.INVISIBLE);
                itemHolder.tvName.setText(drawerItem.getItemName());
                if (drawerItem.getIconDrawableId() != 0) {
                    itemHolder.ivIcon.setImageResource(drawerItem.getIconDrawableId());
                    Log.d(TAG, "Using drawable: " + drawerItem.getIconDrawableId());
                } else if (drawerItem.getIconBitmap() != null) {
                    itemHolder.ivIcon.setImageBitmap(drawerItem.getIconBitmap());
                    Log.d(TAG, "Using bitmap: " + drawerItem.getIconBitmap());
                } else if (drawerItem.getIconFile() != null) {
                    Utils.setImageViewFromFile(drawerItem.getIconFile(), itemHolder.ivIcon);
                    Log.d(TAG, "Using file");
                }
                break;
            case DrawerItem.ITEM_IMAGE:
                itemHolder.headerLayout.setVisibility(View.INVISIBLE);
                itemHolder.normalLayout.setVisibility(View.INVISIBLE);
                itemHolder.mainLayout.setVisibility(View.VISIBLE);
                itemHolder.ivMainImage.setImageResource(drawerItem.getMainImageId());
                break;
        }
        return view;
    }

    class ItemHolder {
        @BindView(R.id.header_layout)
        LinearLayout headerLayout;
        @BindView(R.id.normal_layout)
        LinearLayout normalLayout;
        @BindView(R.id.main_layout)
        LinearLayout mainLayout;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.iv_main_image)
        ImageView ivMainImage;

        ItemHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


