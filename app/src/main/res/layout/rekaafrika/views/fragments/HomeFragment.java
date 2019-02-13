package com.techease.rekaafrika.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.rekaafrika.dateModels.AllProductsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;


public class HomeFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    View view;
    GridView gvProducts;
    ArrayList<AllProductsModel> productsModelArrayList;
    com.techease.rekaafrika.adapters.ProductAdapter productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        gvProducts = view.findViewById(R.id.gridView);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        productsModelArrayList = new ArrayList<>();
        alertDialog = com.techease.rekaafrika.utilities.AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        getCategories();

    }


    private void getCategories() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, com.techease.rekaafrika.utilities.Config.ALL_PRODUCTS
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    alertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject itemObject = jsonArray.getJSONObject(i);

                        String strProductID = itemObject.getString("product_id");
                        String strTitle = itemObject.getString("title");
                        String strPrice = itemObject.getString("price");
                        String strImage = itemObject.getString("image");

                        AllProductsModel model = new AllProductsModel();
                        model.setProduct_id(strProductID);
                        model.setTitle(strTitle);
                        model.setPrice(strPrice);
                        model.setImage(strImage);

                        productsModelArrayList.add(model);
                        com.techease.rekaafrika.adapters.ProductAdapter productAdapter;
                        productAdapter = new com.techease.rekaafrika.adapters.ProductAdapter(getActivity(), productsModelArrayList);
                        gvProducts.setAdapter(productAdapter);
                        productAdapter.notifyDataSetChanged();
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
}


