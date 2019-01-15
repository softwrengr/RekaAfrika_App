package rekaafrika.techease.com.reka.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.adapters.ProductAdapter;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;


public class HomeFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    View view;
    GridView gvProducts;
    ArrayList<AllProductsModel> productsModelArrayList;
    ProductAdapter productAdapter;
    public static ArrayList<AllProductsModel> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Products");
        gvProducts = view.findViewById(R.id.gridView);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        productsModelArrayList = new ArrayList<>();
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        getLiveRates();

    }


    private void getLiveRates() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.ALL_PRODUCTS
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    alertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject itemObject = jsonArray.getJSONObject(i);
                        AllProductsModel model = new AllProductsModel();

                        String strProductID = itemObject.getString("product_id");
                        String strTitle = itemObject.getString("title");
                        String strPrice = itemObject.getString("price");
                        String strImage = itemObject.getString("image");

                        JSONArray array = itemObject.getJSONArray("category");
                        for(int j=0;j<array.length();j++){
                            JSONObject object = array.getJSONObject(j);
                            String categoryName = object.getString("name");
                            arrayList.add(model);
                            model.setArrayList(arrayList);

                        }

                        model.setProduct_id(strProductID);
                        model.setTitle(strTitle);
                        model.setPrice(strPrice);
                        model.setImage(strImage);



                        productsModelArrayList.add(model);
                        ProductAdapter productAdapter;
                        productAdapter = new ProductAdapter(getActivity(), productsModelArrayList);
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


