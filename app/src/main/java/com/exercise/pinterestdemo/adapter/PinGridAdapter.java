package com.exercise.pinterestdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.exercise.pinterestdemo.Data.PinData;
import com.exercise.pinterestdemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * @author mausam
 *         Custom Adapter class for loading image in Pin Grid
 */
public class PinGridAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<PinData> pinList = null;


    public PinGridAdapter(Context context, ArrayList<PinData> list) {

        this.context = context;
        this.pinList = list;

    }

    @Override
    public int getCount() {
        return pinList.size();
    }

    @Override
    public Object getItem(int position) {
        return pinList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_grid_image, parent, false);
        }

        //find the imageview to display image data.
        ImageView imgPin = (ImageView) convertView.findViewById(R.id.imgPin);

        // Get the data item for this position.
        PinData data = pinList.get(position);

        // Download and display user profile image using Picasso library.
        Picasso.with(context).load(data.getImgURL())
                .error(R.drawable.img_default)
                .placeholder(R.drawable.img_default)
                .into(imgPin);


        return convertView;
    }

}
