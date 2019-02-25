package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    @BindView(R.id.tv_stripe)
    TextView tvStripe;
    public static TextView tvTotal,tvSubTotal;
    @BindView(R.id.btn_order_checkout)
    Button btnOrderCheckout;
    @BindView(R.id.paypal_checked)
    ImageView ivPaypalCheck;
    @BindView(R.id.stripe_checked)
    ImageView ivStripeCheck;
    ArrayList<OrderItemModel> orderItemModelArrayList;
    PlaceOrderAdapter placeOrderAdapter;
    public static float totalPrice = 0;
    private String strCurrency;

    public static ImageView ivTotalSymbol,ivSubTotalSymbol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment_method, container, false);
        customActionBar();
        tvSubTotal = view.findViewById(R.id.sub_total);
        tvTotal = view.findViewById(R.id.total);
        ivSubTotalSymbol = view.findViewById(R.id.iv_subtotal_symbol);
        ivTotalSymbol = view.findViewById(R.id.iv_total_symbol);
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
                GeneralUtils.putStringValueInEditor(getActivity(), "payment_type", "paypal");
                ivPaypalCheck.setVisibility(View.VISIBLE);
            }
        });

        tvStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.putStringValueInEditor(getActivity(), "payment_type", "stripe");
                ivStripeCheck.setVisibility(View.VISIBLE);
                ivPaypalCheck.setVisibility(View.GONE);
            }
        });

        btnOrderCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckOutPopUp();
            }
        });
    }

    private void apiCallOrders() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SPECIFIC_ORDER
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("true")) {
                    try {
                        alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject object = jsonObject.getJSONObject("data");

                        String totalAmount = object.getString("total");
                        String totalQuantity = object.getString("total_line_items_quantity");

                        GeneralUtils.putStringValueInEditor(getActivity(), "subtotal", totalAmount);
                        GeneralUtils.putStringValueInEditor(getActivity(), "quantity", totalQuantity);

                        JSONArray array = object.getJSONArray("line_items");
                        for (int i = 0; i <= array.length(); i++) {
                            JSONObject itemObject = array.getJSONObject(i);
                            String strName = itemObject.getString("name");
                            String strPrice = itemObject.getString("price");
                            String strQuantity = itemObject.getString("quantity");

                            OrderItemModel model = new OrderItemModel();
                            model.setItemName(strName);
                            model.setItemPrice(strPrice);
                            model.setItemQuantity(strQuantity);

                            orderItemModelArrayList.add(model);
                            placeOrderAdapter = new PlaceOrderAdapter(getActivity(), orderItemModelArrayList);
                            rvItems.setAdapter(placeOrderAdapter);

                           // sumPrices(strPrice);

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
                params.put("orderid", GeneralUtils.getOrderID(getActivity()));
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    private void sumPrices(String prices) {
        float subTotal = Float.parseFloat(prices);
        totalPrice += subTotal;
        tvSubTotal.setText(String.valueOf(totalPrice));
        tvTotal.setText(String.valueOf(totalPrice));
        GeneralUtils.putStringValueInEditor(getActivity(), "sub_total", String.valueOf(totalPrice));
    }

    private void showCheckOutPopUp() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Rekaafrika").setMessage("Are you sure to confirm your order ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GeneralUtils.connectDrawerFragmentWithBack(getActivity(), new OrderCompletionFragment());
            }
        });
        builder.show();
    }

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
        tvTitle.setText("Customer Order");
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
                        totalPrice = 0;
                        break;
                    case R.id.rand:
                        convertCurrency("ZAR_ZAR", "zar");
                        totalPrice = 0;
                        break;
                    case R.id.pound:
                        convertCurrency("ZAR_GBP", "pound");
                        totalPrice = 0;
                        break;
                    case R.id.euro:
                        convertCurrency("ZAR_EUR", "euro");
                        totalPrice = 0;
                        break;
                    case R.id.pula:
                        convertCurrency("ZAR_BWP", "pula");
                        totalPrice = 0;
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
                    placeOrderAdapter.notifyDataSetChanged();

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
