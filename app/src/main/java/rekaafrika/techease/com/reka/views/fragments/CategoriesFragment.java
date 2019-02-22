package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.adapters.CategoryAdapter;
import rekaafrika.techease.com.reka.adapters.CategoryItemAdapter;
import rekaafrika.techease.com.reka.adapters.ProductAdapter;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;
import rekaafrika.techease.com.reka.dateModels.CategoryDataModel;
import rekaafrika.techease.com.reka.dateModels.CategoryItemModel;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;


public class CategoriesFragment extends Fragment {
    AlertDialog alertDialog;
    View view;
    ArrayList<CategoryDataModel> categoryDataModels;
    CategoryAdapter categoryAdapter;
    @BindView(R.id.gv_categories)
    GridView gvCategories;
    Typeface typeface;
    String image1, image2, image3, image4, image5;
    SliderLayout sliderLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_categories, container, false);
        sliderLayout = view.findViewById(R.id.imageSlider);
        sliderLayout.setScrollTimeInSec(3);

        customActionBar();
        typeface = Typeface.createFromAsset(getActivity().getAssets(),
                "bold.otf");
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        categoryDataModels = new ArrayList<>();
        apiCallSliderImages();
        getCategoryItem();
    }

    private void getCategoryItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.CATEGORIES
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    alertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i <= jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("id");
                        String strName = object.getString("name");
                        String strImage = object.getString("image");

                        CategoryDataModel model = new CategoryDataModel();
                        model.setId(strID);
                        model.setName(strName);
                        model.setImage(strImage);


                        categoryDataModels.add(model);
                        categoryAdapter = new CategoryAdapter(getActivity(), categoryDataModels);
                        gvCategories.setAdapter(categoryAdapter);
                        categoryAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    private void apiCallSliderImages() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.SLIDER_IMAGES
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject imageObject = jsonObject.getJSONObject("data");
                    image1 = imageObject.getString("image1");
                    image2 = imageObject.getString("image2");
                    image3 = imageObject.getString("image3");
                    image4 = imageObject.getString("image4");
                    image5 = imageObject.getString("image5");
                    setSliderViews();

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    //cutomAction Bar
    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        tvTitle.setText("Categories");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();

    }

    private void setSliderViews() {

        HashMap<String, String> url_images = new HashMap<String, String>();
        url_images.put("1", image1);
        url_images.put("2", image2);
        url_images.put("3", image3);
        url_images.put("4", image4);
        url_images.put("5", image5);


        for (String name : url_images.keySet()) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            sliderView.setImageUrl(url_images.get(name));
            sliderLayout.addSliderView(sliderView);
        }
    }
}
