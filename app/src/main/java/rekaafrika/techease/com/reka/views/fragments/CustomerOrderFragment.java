package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.adapters.CategoryAdapter;
import rekaafrika.techease.com.reka.adapters.CustomerOrderAdapter;
import rekaafrika.techease.com.reka.dateModels.CartModel;
import rekaafrika.techease.com.reka.dateModels.CategoryDataModel;
import rekaafrika.techease.com.reka.dateModels.CustomerOrderModel;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;


public class CustomerOrderFragment extends Fragment {
    AlertDialog alertDialog;
    View view;
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;
    ArrayList<CustomerOrderModel> orderModelArrayList;
    CustomerOrderAdapter customerOrderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer_order, container, false);
        customActionBar();
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager mLayoutManagerReviews = new LinearLayoutManager(getActivity());
        rvOrder.setLayoutManager(mLayoutManagerReviews);
        orderModelArrayList = new ArrayList<>();
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        showCustomerOrder();
    }

    private void showCustomerOrder() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_ORDERS
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("true")) {
                    try {
                        alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String orderID = object.getString("id");
                            String orderNumber = object.getString("order_number");
                            String orderStatus = object.getString("status");
                            String orderTotal = object.getString("total");

                            CustomerOrderModel model = new CustomerOrderModel();
                            model.setOrderID(orderID);
                            model.setOrderNumber(orderNumber);
                            model.setOrderStatus(orderStatus);
                            model.setOrderTotal(orderTotal);

                            orderModelArrayList.add(model);
                            customerOrderAdapter = new CustomerOrderAdapter(getActivity(), orderModelArrayList);
                            rvOrder.setAdapter(customerOrderAdapter);
                        }
                        customerOrderAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
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
                params.put("userid", GeneralUtils.getUserID(getActivity()));
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        tvTitle.setText("Customer Orders");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();

    }

}
