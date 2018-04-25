package com.example.quynh.company.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quynh.company.Objects.ProductionDetails;
import com.example.quynh.company.R;
import com.example.quynh.company.Utils.Utils;

import java.util.List;

/**
 * Created by Quynh on 10/9/2017.
 */

public class ProductionAdapter extends ArrayAdapter {
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private final List<ProductionDetails> productions;
    private static class ViewHolder {
        private TextView productionName;
        private ImageView productionImage;
        private TextView productionDescription;
    }

    public ProductionAdapter(Context ctx, int resourceId, List<ProductionDetails> objects) {
        super( ctx, resourceId, objects );
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
        productions = objects;
//        DebugLogUtils.d("Test adapter: ","bat dau tao adapter ");
    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
        View rowView = convertView;
        /* create a new view of my layout and inflate it in the row */
        if (rowView==null) {
            rowView = (LinearLayout) inflater.inflate(resource, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.productionName = (TextView) rowView.findViewById(R.id.production_title);
            viewHolder.productionDescription = (TextView) rowView.findViewById(R.id.production_description);
            viewHolder.productionImage = (ImageView) rowView.findViewById(R.id.production_icon);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.productionName.setText(productions.get(position).getProductionName());
        holder.productionDescription.setText(productions.get(position).getProductionDescription());
        Utils.setImageViewFromFile(productions.get(position).getProductionImageFile(),holder.productionImage);
        return rowView;
    }

    public void updateListItems(List<ProductionDetails> newlist){
        productions.clear();
        productions.addAll(newlist);
        this.notifyDataSetChanged();
    }
}
