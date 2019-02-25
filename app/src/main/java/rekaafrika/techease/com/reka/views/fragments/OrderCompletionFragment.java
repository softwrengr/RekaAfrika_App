package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.adapters.PlaceOrderAdapter;
import rekaafrika.techease.com.reka.dateModels.OrderItemModel;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.activities.MainActivity;

public class OrderCompletionFragment extends Fragment {
    AlertDialog alertDialog;
    View view;
    @BindView(R.id.et_car_number)
    EditText etCardNumber;
    @BindView(R.id.et_ccv)
    EditText etCCV;
    @BindView(R.id.et_exp_day)
    EditText etExpDay;
    @BindView(R.id.et_exp_year)
    EditText etExpYear;
    @BindView(R.id.btn_pay)
    Button btnPay;

    String strCardNumber, strCCV, strPaymentType, strQuantity, strExpiryDate,
            strExpiryYear, strAmount, strCustomerID, strCardType, strOrderID;
    Boolean valid = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_completion, container, false);
        customActionBar();
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        strCustomerID = GeneralUtils.getUserID(getActivity());
        strPaymentType = GeneralUtils.getPaymentType(getActivity());
        strQuantity = GeneralUtils.getQuantity(getActivity());
        strOrderID = GeneralUtils.getOrderID(getActivity());
        strAmount = GeneralUtils.getSubTotalPrice(getActivity());

        String[] separated = strAmount.split("\\.");
        strAmount = separated[0];

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    apiCallPaymentCompletion();
                }
            }
        });

    }


    private void apiCallPaymentCompletion() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PAYMENT
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();

                if (response.contains("200")) {
                    Toast.makeText(getActivity(), "Order Confirmed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Could not complete your request this time", Toast.LENGTH_SHORT).show();
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
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("payment_type", strPaymentType);
                params.put("quantity", strQuantity);
                params.put("ccnumber", "4111111111111111");
                params.put("expday", strExpiryDate);
                params.put("expyear", strExpiryYear);
                params.put("cvv", strCCV);
                params.put("amount", strAmount);
                params.put("customer_id", strCustomerID);
                params.put("card_type", "Visa");
                params.put("order_id", strOrderID);
                params.put("currency",GeneralUtils.getCurrency(getActivity()));
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    private boolean validate() {
        valid = true;
        strCardNumber = etCardNumber.getText().toString().trim();
        strCCV = etCCV.getText().toString().trim();
        strExpiryDate = etExpDay.getText().toString().trim();
        strExpiryYear = etExpYear.getText().toString().trim();

        if (strCardNumber.isEmpty()) {
            etCardNumber.setError("please enter your valid card number");
            valid = false;
        } else {
            etCardNumber.setError(null);
        }

        if (strCCV.isEmpty()) {
            etCCV.setError("please enter your valid card ccv");
            valid = false;
        } else {
            etCCV.setError(null);
        }

        if (strExpiryDate.isEmpty()) {
            etExpDay.setError("enter expiry date of your card");
            valid = false;
        } else {
            etExpDay.setError(null);
        }

        if (strExpiryYear.isEmpty()) {
            etExpYear.setError("enter expiry year of your card");
            valid = false;
        } else {
            etExpYear.setError(null);
        }
        return valid;
    }

    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        tvTitle.setText("Payment");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();

    }
}
