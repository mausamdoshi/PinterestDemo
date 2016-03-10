package com.exercise.pinterestdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exercise.pinterestdemo.Data.PinData;
import com.exercise.pinterestdemo.Data.UserData;
import com.exercise.pinterestdemo.Utils.ConnectivityTools;
import com.exercise.pinterestdemo.adapter.PinGridAdapter;
import com.exercise.pinterestdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * @author mausam
 *   This class loads Pins of user using Pinterest public api
 *   Pin image displayed in a same size coming from the Pinterest API.
 *   Pinterest Public API : https://widgets.pinterest.com/v3/pidgets/users/<username>/pins
 *   <username> Pinterest username of a person whose shared pins are loaded.(the person's username
 *   may differ with their actual name. To work with thid demo,correct username should be required.
 *   (Person's Pinterest username can be fetch from their profile url.)
 *   (Note: Pinterest this public API provides at most 50 Pins of the given username.
 *   This demo displays actual number of user's shared pins but users with more than 50 Pins would
 *    have only 50 pins displayed in a grid.)
 */
public class PinGridActivity extends BaseActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    public static final String TAG = "PinLoad";
    private EditText edtUserName = null;
    private Button btnLoadPin = null;
    private LinearLayout lnrPin = null;
    private GridView pinGrid = null;
    private TextView txtUser = null, txtNoPin = null;
    private ArrayList<PinData> pinList = null;
    private JsonObjectRequest jsonRequest = null;
    private RequestQueue mRequestQueue = null;
    private String URL = null;
    private UserData userData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingrid);


        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(this);

        //Find views to populate data.
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        btnLoadPin = (Button) findViewById(R.id.btnLoadPin);
        txtUser = (TextView) findViewById(R.id.txtUser);
        pinGrid = (GridView) findViewById(R.id.gridView);
        txtNoPin = (TextView) findViewById(R.id.txtNoPin);
        lnrPin = (LinearLayout) findViewById(R.id.lnrPin);
        lnrPin.setVisibility(View.GONE);



        //Handles click of Load Pin button and start making API call.
        btnLoadPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtUserName.getText() == null || edtUserName.getText().toString().equals("")) {
                    Toast.makeText(PinGridActivity.this, getString(R.string.error_enter_username), Toast.LENGTH_LONG).show();
                } else {

                    //Checks internet connection availability, if it is available, make API request to server to load pins
                    if (ConnectivityTools.isNetworkAvailable(PinGridActivity.this)) {
                        initLoading(edtUserName.getText().toString().trim());
                    } else {
                        showMessage(getString(R.string.no_internet));

                    }


                }
            }
        });


        //Handles click of Grid item and navigates to pin detail screen.
        pinGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PinGridActivity.this, PinDetailActivity.class);
                intent.putExtra("userData", userData);
                intent.putExtra("pinData", pinList.get(position));
                startActivity(intent);
            }
        });


    }


    //Initiate Data loading process.
    private void initLoading(String username) {

        //prepare the url with given username
        URL = getString(R.string.url).replace("@username", username);

        // Request a json response from the provided URL using volley library
        jsonRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, this, this);

        // Set the tag on the request.
        jsonRequest.setTag(TAG);
        pinList = new ArrayList<PinData>();
        pinGrid.setAdapter(new PinGridAdapter(this, pinList));
        txtNoPin.setVisibility(View.GONE);
        lnrPin.setVisibility(View.GONE);
        showProgressDialog();
        //Add the request to the Volley RequestQueue.
        mRequestQueue.add(jsonRequest);


    }

    //parse json data and set grid adapter.
    private void loadPins(JSONObject jsonObj) throws Exception {

        try {

            //parses the json data into pin data objects and creates a list of Pins.
            JSONObject userObj = jsonObj.getJSONObject("user");
            JSONArray pinArray = jsonObj.getJSONArray("pins");
            userData = new UserData();
            userData.setUserId(userObj.getString("id"));
            userData.setUsername(userObj.getString("full_name"));
            userData.setPinCount(userObj.getInt("pin_count"));
            userData.setProfileImageURL(userObj.getString("image_small_url"));

            if (userObj.getInt("pin_count") <= 0) {
                txtNoPin.setVisibility(View.VISIBLE);
                lnrPin.setVisibility(View.GONE);
            } else {
                txtUser.setText(userData.getUsername() + " (" + userData.getPinCount() + " Pins)");
                txtNoPin.setVisibility(View.GONE);
                lnrPin.setVisibility(View.VISIBLE);
                for (int i = 0; i < pinArray.length(); i++) {

                    JSONObject pinObj = pinArray.getJSONObject(i);
                    PinData pindata = new PinData();
                    pindata.setDescription(pinObj.getString("description"));
                    pindata.setPinId(pinObj.getString("id"));
                    JSONObject imgObj = pinObj.getJSONObject("images").getJSONObject("237x");
                    pindata.setImgURL(imgObj.getString("url"));
                    pindata.setHeight(imgObj.getInt("height"));
                    pindata.setWidth(imgObj.getInt("width"));

                    pinList.add(pindata);
                }

                //set the pins data loaded adapter to Grid.
                pinGrid.setAdapter(new PinGridAdapter(this, pinList));
            }

        } catch (JSONException e) {
            throw new Exception(getString(R.string.json_parsing_error));

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }


    }


    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            //Cancel all the requests that have this tag.
            mRequestQueue.cancelAll(TAG);
        }
    }


    //Callback method of Volley triggered when request is proceed and get the response.
    @Override
    public void onResponse(JSONObject response) {

        try {
            if (response.getInt("code") != 0) {
                showMessage(response.getString("message"));
            } else {
                loadPins(response.getJSONObject("data"));
            }

            dismissProgressDialog();
        } catch (JSONException e) {
            dismissProgressDialog();
            showMessage(getString(R.string.json_parsing_error));

        } catch (Exception e) {
            dismissProgressDialog();
            showMessage(e.getMessage());
        }

    }

    //Callback method of Volley triggered when Error occurred in response from the API request.
    @Override
    public void onErrorResponse(VolleyError error) {

        dismissProgressDialog();

        String message = "";
        final int status = error.networkResponse.statusCode;

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            message = getString(R.string.error_network_timeout);
            showMessage(message);
        } else if (error instanceof NetworkError) {
            message = getString(R.string.network_error);
            showMessage(message);
        } else if (status == 404) {
            message = getString(R.string.user_not_found);
            showMessage(message);
        } else if (status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER) {
            final String location = error.networkResponse.headers.get("Location");

            //Make the request with new address to fetch data.
            jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, location, null, this, this);

            mRequestQueue.add(jsonRequest);
        } else {
            message = getString(R.string.server_error);
            showMessage(message);
        }


    }

}
