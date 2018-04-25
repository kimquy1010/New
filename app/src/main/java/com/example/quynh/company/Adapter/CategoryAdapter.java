package com.example.quynh.company.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.company.Activities.ProductionActivity;
import com.example.quynh.company.Objects.CategoryDetails;
import com.example.quynh.company.R;
import com.example.quynh.company.Utils.Utils;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Quynh on 4/1/2018.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<CategoryDetails> mDataList = new ArrayList<>();

    public CategoryAdapter(Context context, ArrayList<CategoryDetails> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.category_view_row;
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        v.getLayoutParams().height = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 1.0f / 2);
        return new CategoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.mView;
        final CategoryDetails categoryDetails = mDataList.get(position);
        View rootView = view.findViewById(R.id.root_view);
        ImageView ivThumb = ButterKnife.findById(view, R.id.iv_thumb);
        Utils.setImageViewFromFile(categoryDetails.getCategoryImage(), ivThumb);
        TextView tvName = ButterKnife.findById(view, R.id.tv_name);
        tvName.setText(categoryDetails.getCategoryName());
        final int id = categoryDetails.getCategoryId();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoActivity(mContext, ProductionActivity.class, id);
            }
        });
        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO Make animation here
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View mView;

        ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

}
