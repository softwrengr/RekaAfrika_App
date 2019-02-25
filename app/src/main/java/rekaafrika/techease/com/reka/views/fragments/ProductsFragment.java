package rekaafrika.techease.com.reka.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import rekaafrika.techease.com.reka.utilities.GeneralUtils;


public class ProductsFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    View view;
    GridView gvProducts;
    ArrayList<AllProductsModel> productsModelArrayList;
    ProductAdapter productAdapter;
    public static ArrayList<String> arrayList = new ArrayList<>();
    String strCurrency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        customActionBar();
        gvProducts = view.findViewById(R.id.gridView);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        // GeneralUtils.putStringValueInEditor(getActivity(), "currency", "rand");
        productsModelArrayList = new ArrayList<>();
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        apiCallGetProducts();

    }


    private void apiCallGetProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CATEGORY_PRODUCT
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

                        model.setProduct_id(strProductID);
                        model.setTitle(strTitle);
                        model.setPrice(strPrice);
                        model.setImage(strImage);

                        productsModelArrayList.add(model);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category", GeneralUtils.getSlug(getActivity()));
                return params;
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
        final ImageView ivFilter = mCustomView.findViewById(R.id.iv_filter);
        ivFilter.setVisibility(View.VISIBLE);

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropDownMenu(ivFilter);
            }
        });
        tvTitle.setText("Products");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();

    }

    private void showDropDownMenu(ImageView filter) {

        PopupMenu popup = new PopupMenu(getActivity(), filter);
        popup.getMenuInflater()
                .inflate(R.menu.currencies, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dollar:
                        convertCurrency("ZAR_USD", "usd");
                        break;
                    case R.id.rand:
                        convertCurrency("ZAR_ZAR", "zar");
                        break;
                    case R.id.pound:
                        convertCurrency("ZAR_GBP", "pound");
                        break;
                    case R.id.euro:
                        convertCurrency("ZAR_EUR", "euro");
                        break;
                    case R.id.pula:
                        convertCurrency("ZAR_BWP", "pula");
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    private void convertCurrency(String currency, String name) {
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        GeneralUtils.putStringValueInEditor(getActivity(), "currency", name);
        apiCallCurrency(currency);
    }

    private void apiCallCurrency(final String currency) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.CurrencyConverterUrl + currency + "&compact=y&apiKey=" + Config.CurrencyToken
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    alertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject object = jsonObject.getJSONObject(currency);
                    strCurrency = object.getString("val");
                    GeneralUtils.putStringValueInEditor(getActivity(), "converted_currency", strCurrency);
                    productAdapter.notifyDataSetChanged();

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


