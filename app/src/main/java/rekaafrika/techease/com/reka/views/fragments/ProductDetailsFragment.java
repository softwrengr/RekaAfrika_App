package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.adapters.ProductAdapter;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;

public class ProductDetailsFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    @BindView(R.id.product_image)
    ImageView ivProduct;
    @BindView(R.id.product_name)
    TextView tvProductName;
    @BindView(R.id.product_price)
    TextView tvProductPrice;
    @BindView(R.id.product_descp)
    TextView tvProductDescp;
    @BindView(R.id.btnAddCart)
    Button btnAddCart;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_details, container, false);
        getActivity().setTitle("Product Detail");
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        productDetail();

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectDrawerFragmentWithBack(getActivity(),new AddCartFragment());
            }
        });

    }

    private void productDetail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PRODUCT_DETAILS
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    alertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject itemObject = jsonArray.getJSONObject(0);


                        Picasso.get().load(itemObject.getString("image")).into(ivProduct);
                        tvProductName.setText(itemObject.getString("title"));
                        tvProductPrice.setText("PRICE "+itemObject.getString("price"));
                        tvProductDescp.setText(itemObject.getString("description"));



                } catch (JSONException e)

            {
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
                params.put("id", GeneralUtils.getID(getActivity()));
                return params;
            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }
}
