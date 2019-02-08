package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import rekaafrika.techease.com.reka.helpers.ShopCrud;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;

public class ProductDetailsFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;
    @BindView(R.id.product_image)
    ImageView ivProduct;
    @BindView(R.id.product_name)
    TextView tvProductName;
    @BindView(R.id.tv_product_price)
    TextView tvProductPrice;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.product_descp)
    TextView tvProductDescp;
    @BindView(R.id.btnAddCart)
    Button btnAddCart;
    @BindView(R.id.iv_add_quantity)
    ImageView ivAddQuantity;
    @BindView(R.id.iv_remove_quantity)
    ImageView ivRemoveQuantity;
    View view;

    ShopCrud shopCrud;
    String strProductID, strProductName, strProductImage, strProductPrice;
    int singleQuantity;
    float  productPrice, totalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_details, container, false);
        customActionBar();
        shopCrud = new ShopCrud(getActivity());
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

                if(totalPrice==0){
                   totalPrice = productPrice;
                }

                shopCrud.insertSingleProduct(strProductID, strProductName, strProductImage, String.valueOf(totalPrice),String.valueOf(singleQuantity));
                GeneralUtils.connectDrawerFragmentWithBack(getActivity(), new AddCartFragment());
            }
        });

        singleQuantity = Integer.parseInt(tvQuantity.getText().toString());
        ivAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(singleQuantity>=0){
                    singleQuantity++;
                    tvQuantity.setText(String.valueOf(singleQuantity));
                    totalPrice = productPrice * singleQuantity;
                    tvTotalPrice.setText(String.valueOf(totalPrice));
                }
                else if(singleQuantity<0){
                    singleQuantity=0;
                    singleQuantity++;
                    tvQuantity.setText(String.valueOf(singleQuantity));
                    totalPrice = productPrice * singleQuantity;
                    tvTotalPrice.setText(String.valueOf(totalPrice));
                }

            }
        });

        ivRemoveQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleQuantity--;
                if(singleQuantity==1){
                    tvQuantity.setText("1");
                    tvTotalPrice.setText(String.valueOf(productPrice));
                }
                else if(singleQuantity>=1) {
                    tvQuantity.setText(String.valueOf(singleQuantity));
                    totalPrice = productPrice * singleQuantity;
                    tvTotalPrice.setText(String.valueOf(totalPrice));
                }

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

                    strProductID = itemObject.getString("product_id");
                    strProductImage = itemObject.getString("image");
                    strProductName = itemObject.getString("title");
                    strProductPrice = itemObject.getString("price");

                    Picasso.get().load(strProductImage).into(ivProduct);
                    tvProductName.setText(strProductName);
                    tvProductPrice.setText(strProductPrice);
                    tvTotalPrice.setText(strProductPrice);
                    tvProductDescp.setText(itemObject.getString("description"));

                    productPrice = Float.parseFloat(strProductPrice);


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

    //cutomAction Bar
    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        tvTitle.setText("Product Detail");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();

    }
}
