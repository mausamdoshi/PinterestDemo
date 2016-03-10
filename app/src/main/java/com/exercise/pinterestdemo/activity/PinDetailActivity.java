package com.exercise.pinterestdemo.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.exercise.pinterestdemo.Data.PinData;
import com.exercise.pinterestdemo.Data.UserData;
import com.exercise.pinterestdemo.R;
import com.squareup.picasso.Picasso;

/**
 * @author mausam
 *This class displays Pin details
 * Pin image displayed in a same size coming from the Pinterest API
 */
public class PinDetailActivity extends BaseActivity {

    private UserData userData=null;
    private PinData pinData =null;
    private ImageView imgPinLarge=null;
    private TextView txtDesc =null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pin_detail);
        userData=(UserData) getIntent().getSerializableExtra("userData");
        pinData=(PinData)getIntent().getSerializableExtra("pinData");

        imgPinLarge=(ImageView)findViewById(R.id.imgPinLarge);
        txtDesc =(TextView)findViewById(R.id.txtDescription);

        //Loading custom action bar
        initActionBar();

        // populate the description in the textview.
        if(pinData.getDescription()!=null && !pinData.getDescription().equals(""))
            txtDesc.setText(pinData.getDescription());


        // Download and display large pin image using Picasso library
        Picasso.with(PinDetailActivity.this).load(pinData.getImgURL())
                .error(R.drawable.img_default)
                .placeholder(R.drawable.img_default)
                .into(imgPinLarge);

    }

    //Customized Action bar to display user name and profile image
    private void initActionBar() {


        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(getString());
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.view_actionbar);
        View view = actionBar.getCustomView();

        ImageView imgUser = (ImageView) view.findViewById(R.id.imgUser);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(userData.getUsername());

        if(userData.getProfileImageURL()!=null && !userData.getProfileImageURL().equals("")){

            // Download and display user profile image using Picasso library
            Picasso.with(PinDetailActivity.this).load(userData.getProfileImageURL())
                    .error(R.drawable.img_default)
                    .placeholder(R.drawable.img_default)
                    .into(imgUser);
        }


    }
}
