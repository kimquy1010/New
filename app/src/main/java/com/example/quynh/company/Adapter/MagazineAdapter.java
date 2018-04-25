package com.example.quynh.company.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quynh.company.Objects.MagazineDetails;
import com.example.quynh.company.Objects.ProductionDetails;
import com.example.quynh.company.R;
import com.example.quynh.company.Utils.Utils;

import java.util.List;

/**
 * Created by Quynh on 1/8/2018.
 */

public class MagazineAdapter extends ArrayAdapter {
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private final List<MagazineDetails> magazines;
    private static class ViewHolder {
        private TextView magazineName;
        private ImageView magazineImage;
        private TextView magazineAddress;
    }

    public MagazineAdapter(Context ctx, int resourceId, List<MagazineDetails> objects) {
        super( ctx, resourceId, objects );
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
        magazines = objects;
//        DebugLogUtils.d("Test adapter: ","bat dau tao adapter ");
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent ) {
        View rowView = convertView;
        /* create a new view of my layout and inflate it in the row */
        if (rowView==null) {
            rowView = (LinearLayout) inflater.inflate(resource, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.magazineName = (TextView) rowView.findViewById(R.id.magazine_name);
            viewHolder.magazineAddress = (TextView) rowView.findViewById(R.id.magazine_address);
            viewHolder.magazineImage = (ImageView) rowView.findViewById(R.id.magazine_image);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.magazineName.setText(magazines.get(position).getMagazineName());
        holder.magazineAddress.setText(magazines.get(position).getMagazineAddress());
//        Utils.setImageViewFromFile(productions.get(position).getMagazineImage(),holder.productionImage);
        return rowView;
    }

    public void updateListItems(List<MagazineDetails> newlist){
        magazines.clear();
        magazines.addAll(newlist);
        this.notifyDataSetChanged();
    }
}

