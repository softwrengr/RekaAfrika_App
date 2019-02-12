package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import rekaafrika.techease.com.reka.adapters.CustomerOrderAdapter;
import rekaafrika.techease.com.reka.adapters.PlaceOrderAdapter;
import rekaafrika.techease.com.reka.dateModels.CustomerOrderModel;
import rekaafrika.techease.com.reka.dateModels.OrderItemModel;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.activities.PaypalPaymentActivity;


public class PaymentMethodFragment extends Fragment {
    AlertDialog alertDialog;
    View view;
    @BindView(R.id.rv_order_items)
    RecyclerView rvItems;
    @BindView(R.id.tv_paypal)
    TextView tvPaypal;
    @BindView(R.id.sub_total)
    TextView tvSubTotal;
    @BindView(R.id.total)
    TextView tvTotal;
    ArrayList<OrderItemModel> orderItemModelArrayList;
    PlaceOrderAdapter placeOrderAdapter;
    float totalPrice = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment_method, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager mLayoutManagerReviews = new LinearLayoutManager(getActivity());
        rvItems.setLayoutManager(mLayoutManagerReviews);
        orderItemModelArrayList = new ArrayList<>();
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        apiCallOrders();

        tvPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaypalPaymentActivity.class));
            }
        });
    }

    private void apiCallOrders() {
        final int positon = Integer.parseInt(GeneralUtils.getPosition(getActivity()));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_ORDERS
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    try {
                        alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject object = jsonArray.getJSONObject(positon);
                        JSONArray array = object.getJSONArray("line_items");

                        for (int i = 0; i <= array.length(); i++) {
                            JSONObject itemObject = array.getJSONObject(i);
                            String strName = itemObject.getString("name");
                            String strPrice = itemObject.getString("price");

                            OrderItemModel model = new OrderItemModel();
                            model.setItemName(strName);
                            model.setItemPrice(strPrice);

                            orderItemModelArrayList.add(model);
                            placeOrderAdapter = new PlaceOrderAdapter(getActivity(), orderItemModelArrayList);
                            rvItems.setAdapter(placeOrderAdapter);

                            sumPrices(strPrice);

                        }
                        placeOrderAdapter.notifyDataSetChanged();


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

    private void sumPrices(String prices){
        float subTotal = Float.parseFloat(prices);
        totalPrice += subTotal;
        tvSubTotal.setText(String.valueOf(totalPrice));
        tvTotal.setText(String.valueOf(totalPrice));
        GeneralUtils.putStringValueInEditor(getActivity(),"sub_total",String.valueOf(totalPrice));
    }

}
