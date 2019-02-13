package rekaafrika.techease.com.reka.views.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.adapters.CartAdapter;
import rekaafrika.techease.com.reka.dateModels.CartModel;
import rekaafrika.techease.com.reka.helpers.ShopCrud;
import rekaafrika.techease.com.reka.interfaces.LoginInterface;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;


public class AddCartFragment extends Fragment implements LoginInterface {
    android.support.v7.app.AlertDialog alertDialog;
    View view;
    @BindView(R.id.rv_cart)
    RecyclerView rvCart;
    @BindView(R.id.checkout)
    TextView tvOrder;
    @BindView(R.id.recycler_layout)
    RelativeLayout layoutRecycler;
    @BindView(R.id.empty)
    RelativeLayout layoutEmpty;

    public static TextView tvSubTotalPrice;
    public static TextView tvSubTotalItemsCount;
    ArrayList<CartModel> productsModelArrayList;
    ShopCrud shopCrud;
    HashMap<String, String> hmProductIDs, hmQuantities;

    StringBuilder builderIDs, builderQuantities;
    String strProductIDS, strProdutctQuantities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_cart, container, false);

        tvSubTotalPrice = view.findViewById(R.id.sub_total_price);
        tvSubTotalItemsCount = view.findViewById(R.id.sub_total_items_count);
        getActivity().setTitle("My Bag");
        shopCrud = new ShopCrud(getActivity());
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        hmProductIDs = new HashMap<>();
        hmQuantities = new HashMap<>();
        RecyclerView.LayoutManager mLayoutManagerReviews = new LinearLayoutManager(getActivity());
        rvCart.setLayoutManager(mLayoutManagerReviews);
        productsModelArrayList = new ArrayList<>();
        showProducts();

        if(productsModelArrayList==null || productsModelArrayList.size()==0){
            layoutRecycler.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        }
        else {
        }


        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GeneralUtils.isLogin(getActivity())) {
                    showPopUp();
                } else {
                    showDialog();
                }


            }
        });

    }


    public void showProducts() {

        Cursor cursor = shopCrud.getProducts();
        while (cursor.moveToNext()) {
            String strProductID = cursor.getString(1);
            String strProductName = cursor.getString(2);
            String strProductImage = cursor.getString(3);
            String strProductPrice = cursor.getString(4);
            String strProductQuantity = cursor.getString(5);

            readAllProducts(strProductID, strProductName, strProductImage, strProductPrice, strProductQuantity);

            hmProductIDs.put(strProductID, strProductID);
            hmQuantities.put(strProductID, strProductQuantity);

            builderIDs = new StringBuilder();
            for (String name : hmProductIDs.values()) {
                builderIDs.append(name + ",");
            }
            builderQuantities = new StringBuilder();
            for (String name : hmProductIDs.values()) {
                builderQuantities.append(name + ",");
            }

        }
    }

    private void readAllProducts(String strProductID, String strProductName, String strProductImage, String strProductPrice, String strQuantity) {

        CartModel cartModel = new CartModel();
        cartModel.setId(strProductID);
        cartModel.setItem_name(strProductName);
        cartModel.setItem_image(strProductImage);
        cartModel.setItem_price(strProductPrice);
        cartModel.setItem_quantity(strQuantity);

        productsModelArrayList.add(cartModel);
        Set<CartModel> set = new HashSet<>(productsModelArrayList);
        productsModelArrayList.clear();
        productsModelArrayList.addAll(set);

        CartAdapter adapter = new CartAdapter(getActivity(), productsModelArrayList);
        rvCart.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Login").setMessage("you must be logged in first to continue.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GeneralUtils.connectDrawerFragmentWithBack(getActivity(), new LoginFragment());
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
    }

    private void apiCallOrder() {
        strProductIDS = builderIDs.toString().trim();
        strProdutctQuantities = builderQuantities.toString().trim();
        final String replaceIDs = strProductIDS.substring(0, strProductIDS.length() - 1) + "";
        final String replaceQuantities = strProdutctQuantities.substring(0, strProdutctQuantities.length() - 1) + "";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CREATE_ORDER
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
               if(response.contains("true")){
                   Toast.makeText(getActivity(), "Order Created", Toast.LENGTH_SHORT).show();
                   shopCrud.clearData();
                   GeneralUtils.connectDrawerFragmentWithBack(getActivity(),new CustomerOrderFragment());
               }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //alertDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("note", "this is an example");
                params.put("userid", GeneralUtils.getUserID(getActivity()));
                params.put("product_ids", replaceIDs);
                params.put("quantity", replaceQuantities);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    @Override
    public void onLoginClicked(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        switch (message) {
            case "":
                break;
        }
    }

    private void showPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Order").setMessage("Are you sure to place order ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog = AlertUtils.createProgressDialog(getActivity());
                alertDialog.show();
                apiCallOrder();
            }
        });
        builder.show();
    }
}
